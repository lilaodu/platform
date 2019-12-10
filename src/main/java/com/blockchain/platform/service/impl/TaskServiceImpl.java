package com.blockchain.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.constant.UpgradeConst;
import com.blockchain.platform.mapper.*;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.GradeDTO;
import com.blockchain.platform.pojo.dto.KlineDTO;
import com.blockchain.platform.pojo.dto.TaskDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.ITaskService;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 定时任务服务
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-26 10:13 AM
 **/
@Service
public class TaskServiceImpl implements ITaskService {

    /**
     * 定时任务数据接口
     */
    @Resource
    private TaskMapper mapper;

    /**
     * 用户数据接口
     */
    @Resource
    private UserMapper userMapper;

    /**
     * 用户钱包数据接口
     */
    @Resource
    private UserWalletMapper walletMapper;

    @Resource
    private RedisPlugin redisPlugin;


    @Override
    public List<UserEntity> findUserByCondition(TaskDTO dto) {
        //认证用户，秒合约交易用户
        return mapper.findUserByCondition( dto);
    }


    @Override
    public UserUpgradeFlowEntity findLastUpgrade(BaseDTO dto){
        return mapper.findLastUpgrade( dto);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean addLockProfitEntity(UserLockProfitEntity entity){
        // 插入记录
        int k = mapper.addLockProfitEntity( entity);
        if ( k > 0) {
            // 获取钱包
            UserWalletEntity walletEntity = walletMapper.findByCondition( BaseDTO.builder().build());

            if ( ObjectUtil.isNotEmpty( walletEntity)) {
                UserWalletEntity db = new UserWalletEntity();
                db.setUserId( entity.getUserId());
                db.setSymbol( entity.getSymbol());
                db.setBalance( BigDecimalUtils.add( walletEntity.getBalance(), entity.getAmount()));
                k = walletMapper.updateById( db);
            } else {
                walletEntity = new UserWalletEntity();
                walletEntity.setBalance( entity.getAmount());
                walletEntity.setSymbol( entity.getSymbol());
                walletEntity.setUserId( entity.getUserId());
                k = walletMapper.insert( walletEntity);
            }
        }
        return k > 0;
    }


    @Override
    public List<UnLockProfitVO> findUnLockProfitData(TaskDTO dto) {
        return  mapper.findUnLockProfitData( dto);
    }


    @Override
    public BigDecimal teamTradeVolume(TaskDTO dto) {
        return mapper.teamTradeVolume(dto);
    }


    @Override
    @Transactional
    public Boolean addUserUpgradeFlow(UserUpgradeFlowEntity entity){

        int k = mapper.addUserUpgradeFlow( entity);

        if ( k > 0) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId( entity.getUserId());
            userEntity.setRank( entity.getRankLevel());

            k  = userMapper.update( userEntity);
        }
        return k > 0;
    }


    @Override
    public Map<Integer, UpgradeDirectVO> findDirectHold(TaskDTO dto) {

        List<UserWalletEntity> wallets = mapper.findDirectHold( dto);


        Map<String, CoinEntity> tokens = redisPlugin.get(RedisConst.PLATFORM_COIN_CONFIG);

        Map<Integer, UpgradeDirectVO> map = MapUtil.newHashMap();

        if ( CollUtil.isNotEmpty( wallets)) {

            for (UserWalletEntity entity : wallets) {

                UpgradeDirectVO vo = null;

                if ( map.containsKey( entity.getUserId())) {
                    vo = map.get( entity.getUserId());
                } else {
                    vo = new UpgradeDirectVO();
                }
                vo.setUserId( entity.getUserId());
                // 换算USDT
                if ( StrUtil.equals( entity.getSymbol(), BizConst.USDT_TOKEN)) {
                    vo.setAmount( BigDecimalUtils.add( vo.getAmount(), entity.getBalanceT()));
                } else {
                    // 获取汇率
                    if ( tokens.containsKey( entity.getSymbol())) {
                        BigDecimal price = BigDecimal.ZERO;

                        if ( !BigDecimalUtils.isZero( tokens.get( entity.getSymbol()).getMarketPrice())) {
                            price = tokens.get( entity.getSymbol()).getMarketPrice();
                        }
                        // 计算实际价值
                        BigDecimal amount = BigDecimalUtils.multi( price, entity.getBalanceT());

                        vo.setAmount( BigDecimalUtils.add( amount, vo.getAmount()));
                    }
                }
                map.put( entity.getUserId(), vo);
            }
        }
        return map;
    }

    @Override
    public List<ContractProfitVO> findContractProfitData(TaskDTO dto) {
        return mapper.findContractProfitData( dto);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean addTradeProfitEntity(UserTradeProfitEntity entity) {
        int k = mapper.addTradeProfitEntity( entity);
        if ( k > 0 ){
            UserWalletEntity walletEntity = walletMapper.findByCondition( BaseDTO.builder()
                                                            .userId( entity.getUserId())
                                                            .symbol( entity.getSymbol()).build());
            if ( ObjectUtil.isNotEmpty( walletEntity)) {
                UserWalletEntity db = new UserWalletEntity();
                db.setId( walletEntity.getId());
                db.setBalance( BigDecimalUtils.add( walletEntity.getBalance(), entity.getAmount()));
                db.setUserId( entity.getUserId());
                db.setSymbol( entity.getSymbol());
                k = walletMapper.updateById( db);
            } else {
                UserWalletEntity uwe = new UserWalletEntity();

                uwe.setBalance( entity.getAmount());
                uwe.setUserId( entity.getUserId());
                uwe.setSymbol( entity.getSymbol());
                k = walletMapper.insert( uwe);
            }
        }
        return k > 0;
    }
}
