package com.blockchain.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.constant.WsConst;
import com.blockchain.platform.mapper.DrawMapper;
import com.blockchain.platform.mapper.SecondsContractOrderMapper;
import com.blockchain.platform.mapper.UserMapper;
import com.blockchain.platform.mapper.UserWalletMapper;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.LuckDrawEntity;
import com.blockchain.platform.pojo.entity.LuckDrawLogEntity;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.IDrawService;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.DrawUtils;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 抽奖服务实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-13 6:25 PM
 **/
@Service
public class IDrawServiceImpl implements IDrawService {

    /**
     * 抽奖数据库接口
     */
    @Resource
    private DrawMapper mapper;

    /**
     * 用户数据接口
     */
    @Resource
    private UserMapper userMapper;

    /**
     * 用户钱包
     */
    @Resource
    private UserWalletMapper walletMapper;

    /**
     * 抽奖socket
     */


    /**
     * redis 工具
     */
    @Resource
    private RedisPlugin redisPlugin;

    @Override
    public List<LuckDrawVO> query(BaseDTO dto) {
        return mapper.query( dto);
    }

    @Override
    public List<LuckDrawConfigVO> list(BaseDTO dto) {
        //返回结果
        return mapper.list( dto);
    }

    @Resource
    private SecondsContractOrderMapper secondsContractOrderMapper;


    @Override
    public List<LuckDrawLogVO> log(PageDTO dto) {
        return mapper.log( dto);
    }

    @Override
    public List<LuckDrawLogVO> history(BaseDTO dto) {
        return mapper.history( dto);
    }

    @Override
    public PrizeVO count(BaseDTO dto) {
        //返回对象
        PrizeVO vo = mapper.count( dto);
        int numSum=secondsContractOrderMapper.count(dto.getUserId());
        vo.setContract(numSum);
        //详情
        dto.setState( BizConst.BIZ_STATUS_VALID);
        LuckDrawEntity entity = mapper.findByCondition( dto);
        //返回数据
        return BizUtils.draw( vo, entity);
    }

    @Override
    @Transactional( rollbackFor = Exception.class)
    public PrizeVO prize(BaseDTO dto) throws Exception {
        //抽奖详情
        LuckDrawEntity entity = mapper.findByCondition( BaseDTO.builder()
                                                .id( dto.getId())
                                                .state( BizConst.BIZ_STATUS_VALID).build());

        //获取总的记录条数，计算偏移量
        Integer offset = IntUtils.offset( mapper.countLog( dto));
        //查询数据
        LuckDrawDTO draw = mapper.findDraw( BaseDTO.builder()
                                        .id( dto.getId())
                                        .limit( offset).build());
        //获取所有抽奖活动奖项概率
        Map<String, LuckDrawConfigVO> pro = redisPlugin.hget( RedisConst.DRAW_ACTIVITY, StrUtil.toString( dto.getId()));
        //开始抽奖
        Integer m = DrawUtils.draw( BizUtils.probability( pro), draw);
        //获取对应的奖项
        LuckDrawConfigVO configVO = pro.get( StrUtil.toString( m));

        // 判断中奖金额是否超过单日奖励总额
        if( BigDecimalUtils.compareTo( NumberUtil.add(configVO.getNum(), draw.getNum()), entity.getDayAmount())){
            //不中奖
            m = -1;
        }
        //奖项（几等奖）
        Integer prize = IntUtils.INT_ZERO;
        //奖项id
        Integer configId = IntUtils.INT_ZERO;
        //是否抽中奖
        String isPrize = AppConst.FIELD_N;
        //中奖代币
        String symbol = StrUtil.EMPTY;
        //中奖数量
        BigDecimal num = BigDecimal.ZERO;
        if( !IntUtils.compareTo( IntUtils.INT_ZERO, m)){
            //奖项（几等奖）
            prize = configVO.getPrize();
            //判断是否中奖
            if( !StrUtil.equals( configVO.getWords(), BizConst.DRAW_DEFAULT_PRIZE)) { //不是奖项，谢谢参与
                //中奖
                configId = configVO.getId();
                isPrize = AppConst.FIELD_Y;
                symbol = configVO.getSymbol();
                num = configVO.getNum();
                // 修改用户钱包
                walletMapper.updateAssets( UserWalletDTO.builder()
                                            .symbol( configVO.getSymbol())
                                            .balance( NumberUtil.toBigDecimal( configVO.getNum()).negate())
                                            .frozenBalance( BigDecimal.ZERO)
                                            .userId( dto.getUserId()).build());
            }
        }else{
            //谢谢参与
            for( Map.Entry<String, LuckDrawConfigVO> entry : pro.entrySet()){
                if (StrUtil.equals( entry.getValue().getWords(), BizConst.DRAW_DEFAULT_PRIZE)){
                    symbol = entry.getValue().getSymbol();
                    prize = IntUtils.toInt(entry.getKey());
                    configId = entry.getValue().getId();
                    break;
                }
            }
        }
        //获取用户详情
        UserEntity user = userMapper.findByCondition( UserDTO.builder()
                                            .id( dto.getUserId())
                                            .state( BizConst.BIZ_STATUS_VALID).build());
        //添加抽奖记录
        LuckDrawLogEntity logEntity = LuckDrawLogEntity.builder()
                                        .userId( dto.getUserId())  //用户id
                                        .drawId( dto.getId())    //活动id
                                        .configId( configId)       //奖项id
                                        .isPrize( isPrize)        //是否中奖
                                        .symbol( symbol)       //代币
                                        .num( num)        //数量
                                        .username( StrUtil.isEmpty( user.getMobile()) ? user.getEmail() : user.getMobile()).build(); //用户名
        mapper.addLog( logEntity);
        //修改用户抽奖数据
        PrizeVO vo = BizUtils.draw( mapper.count( dto), entity);
        //是否中奖
        vo.setIsPrize( isPrize);
        //设置中奖奖项
        vo.setPrize( prize);
        //中奖数量
        vo.setPrizeNum( num);
        //中奖代币
        vo.setSymbol( symbol);
        return vo;
    }

    public WinningVO doPrize(DrawDTO dto) {
        WinningVO vo = new WinningVO();

        try {
            PrizeVO prize = prize( BaseDTO.builder().userId( dto.getUserId()).id( dto.getId()).build());
            //类型
            vo.setType(WsConst.MESSAGE_TYPE_DR);
            //是否中奖
            vo.setPrize( prize.getIsPrize());
            // 中奖号码
            vo.setNumber( prize.getPrize());
            // 自己
            vo.setOwn( Boolean.TRUE);
            //抽奖记录
            List< List<Object>> vos = new ArrayList<>();

            //抽奖记录
            LinkedList<Object> bullet = CollUtil.newLinkedList();
            if ( StrUtil.equals( prize.getIsPrize(), AppConst.FIELD_Y)) {
                // 手机号码
                bullet.add(BizUtils.formatName( dto.getName()));
                //中奖数量
                bullet.add( prize.getPrizeNum());
                //中奖代币
                bullet.add( prize.getSymbol());
                //中奖时间
                bullet.add( DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT));

                vos.add( bullet);
            }
            vo.setBullet( vos);
        } catch ( Exception ex){
            ex.printStackTrace();
        }
        return vo;
    }






}
