package com.blockchain.platform.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.ChainConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.mapper.ChainMapper;
import com.blockchain.platform.mapper.UserWalletMapper;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserWalletDTO;
import com.blockchain.platform.pojo.entity.ChainWithdrawEntity;
import com.blockchain.platform.pojo.entity.UserWalletEntity;
import com.blockchain.platform.pojo.entity.WalletConfigEntity;
import com.blockchain.platform.pojo.vo.DepositVO;
import com.blockchain.platform.pojo.vo.WithdrawVO;
import com.blockchain.platform.service.IChainService;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 代币流水服务实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-22 2:22 PM
 **/
@Service
public class ChainServiceImpl implements IChainService {

    /**
     * 代币出入账交易记录
     */
    @Resource
    private ChainMapper mapper;

    /**
     * 用户钱包数据库接口
     */
    @Resource
    private UserWalletMapper walletMapper;

    /**
     * 缓存工具
     */
    @Resource
    private RedisPlugin redisPlugin;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String findWalletAddress(BaseDTO dto) {
        //获取用户钱包详情
        UserWalletEntity wallet = walletMapper.findByCondition(dto);
        //判断是否是omni协议,并且代币是usdt
        if (StrUtil.equals(ChainConst.PROTOCOL, dto.getProtocol())
                && StrUtil.equals(ChainConst.DEFAULT_SYMBOL, dto.getSymbol())) {
            //调用omni协议服务

        } else if (ObjectUtil.isNotEmpty(wallet) && StrUtil.isNotEmpty(wallet.getWalletAddress())) {
            return wallet.getWalletAddress();
        }
        //钱包地址
        String walletAddress = StrUtil.EMPTY;
        //获取钱包配置信息
        Map<String, WalletConfigEntity> map = redisPlugin.get(RedisConst.PLATFORM_WALLET_CONFIG);
        //获取对应的大类
        WalletConfigEntity config = map.get(dto.getSymbol());
        //获取用户此大类的钱包地址
        dto.setType(config.getCoinType());
        UserWalletEntity entity = walletMapper.findWalletAddress(dto);
        if (ObjectUtil.isEmpty(entity)) {
            //调用服务接口
            String response = HttpUtil.get(StrUtil.concat(Boolean.FALSE, config.getServiceUrl(), ChainConst.CHAIN_ADDRESS_CREATE,
                    StrUtil.toString(dto.getUserId()), StrUtil.SLASH, config.getCoinType()));
            //解析数据
            Map<String, String> data = JSON.parseObject(response, new TypeReference<Map<String, String>>() {
            });
            walletAddress = data.get(AppConst.MESSAGE);
        } else {
            walletAddress = entity.getWalletAddress();
        }
        //若钱包不存在或钱包地址为空
        UserWalletEntity walletEntity = UserWalletEntity.builder()
                .userId(dto.getUserId())
                .symbol(dto.getSymbol())
                .walletAddress(walletAddress).build();
        if (ObjectUtil.isEmpty(wallet)) {
            //创建钱包
            walletMapper.addWallet(walletEntity);
        } else if (StrUtil.isEmpty(wallet.getWalletAddress())) {
            //更新钱包地址
            walletMapper.updateWallet(walletEntity);
        }
        return walletAddress;
    }

    @Override
    public List<DepositVO> deposit(PageDTO dto) {
        return mapper.deposit(dto);
    }

    @Override
    public List<WithdrawVO> withdraw(PageDTO dto) {
        return mapper.withdraw(dto);
    }

    @Override
    public ChainWithdrawEntity findWithdraw(BaseDTO dto) {
        return mapper.findWithdraw(dto);
    }

    @Override
    @Transactional
    public Boolean modify(ChainWithdrawEntity entity) {
        Integer flag = IntUtils.INT_ZERO;
        //新增流水
        flag = mapper.addWithdrawFlow(entity);

        if (flag > 0) {
            //修改用户钱包，扣除 提币数量 + 手续费
            //如果提币代币与手续费代币相同
            if (StrUtil.equals(entity.getCoinCode(), entity.getFeeCoinCode())) {
                flag = walletMapper.updateAssets(UserWalletDTO.builder()
                        .userId(entity.getUserId())
                        .symbol(entity.getCoinCode())
                        .balance(NumberUtil.add(entity.getNumber(), entity.getRealFee()))
                        .frozenBalance(BigDecimal.ZERO)

                        .build());
            } else {
                //扣除提币代币钱包
                flag = walletMapper.updateAssets(UserWalletDTO.builder()
                        .userId(entity.getUserId())
                        .symbol(entity.getCoinCode())
                        .balance(entity.getNumber())
                        .frozenBalance(BigDecimal.ZERO)
                        .frozenBalance(BigDecimal.ZERO).build());
                if (flag > 0) {
                    //扣除手续费
                    flag = walletMapper.updateAssets(UserWalletDTO.builder()
                            .userId(entity.getUserId())
                            .symbol(entity.getCoinCode())
                            .balance(entity.getRealFee())
                            .build());
                }
            }
        }

        return flag > 0;
    }

    @Override
    public Boolean withdrawal(ChainWithdrawEntity entity) {
        return null;
    }

}
