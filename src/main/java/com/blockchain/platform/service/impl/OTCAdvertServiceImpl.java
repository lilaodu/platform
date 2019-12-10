package com.blockchain.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.mapper.OTCAdvertMapper;
import com.blockchain.platform.mapper.OTCOrderMapper;
import com.blockchain.platform.mapper.UserWalletMapper;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.OTCAdvertEntity;
import com.blockchain.platform.pojo.vo.OTCAdvertVO;
import com.blockchain.platform.service.IOTCAdvertService;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.ExUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * otc广告服务实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 5:30 PM
 **/
@Service
public class OTCAdvertServiceImpl extends ServiceImpl<OTCAdvertMapper, OTCAdvertEntity> implements IOTCAdvertService {

    /**
     * otc广告数据接口
     */
    @Resource
    private OTCAdvertMapper mapper;

    /**
     * 用户钱包数据接口
     */
    @Resource
    private UserWalletMapper walletMapper;

    /**
     * otc 订单数据接口
     */
    @Resource
    private OTCOrderMapper orderMapper;

    @Override
    public List<OTCAdvertVO> query(PageDTO dto) {
        return mapper.query(dto);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean modify(OTCAdvertEntity entity) {
        //新增或编辑
        Boolean bool = saveOrUpdate(entity);
        //为卖的广告
        if (bool && StrUtil.equals(entity.getType(), BizConst.AdvertConst.TYPE_SELL)) {
            //冻结用户钱包OTC金额（手续费 + 发布的数量）
            UserWalletDTO wallet = UserWalletDTO.builder()
                    .symbol(entity.getSymbol())  //货币符号
                    .userId(entity.getUserId())  //用户id
                    .balance(BigDecimal.ZERO)  //总量
                    .frozenBalance(BigDecimalUtils.add(entity.getNum(), entity.getFeeRate())).build();  //冻结的金额
            //更新用户钱包
            bool = walletMapper.updateOTCAssets(wallet) > 0;
        }
        return bool;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)

    public Boolean invalid(OTCAdvertEntity advert) {





        OTCAdvertEntity currentAdvert =         this.getById(advert.getId());


        OTCAdvertEntity entity = OTCAdvertEntity.builder()
                .type(currentAdvert.getType())
                .state(BizConst.AdvertConst.STATE_INVALID)   //状态（下架）
                .id(currentAdvert.getId()).build();//id4


        boolean bool = mapper.update(entity)>0;;
        //更新
        //若为卖的广告
        if (bool && StrUtil.equals(entity.getType(), BizConst.AdvertConst.TYPE_SELL)) {
            //获取广告已完成订单扣除的手续费
//            BigDecimal takeRate = orderMapper.countRate(BaseDTO.builder()
//                    .id(entity.getId())  //广告id
//                    .type(BizConst.OTCOrderConst.TYPE_COMPLETE).build());  //完成的订单
            //将此广告冻结的otc 余额还回去

            UserWalletDTO wallet = UserWalletDTO.builder()
                    .symbol(currentAdvert.getSymbol())  //货币符号
                    .userId(currentAdvert.getUserId())  //用户id
                    .balance(BigDecimal.ZERO)  //总量
                    .frozenBalance((BigDecimalUtils.add(currentAdvert.getSurplus(),BigDecimal.ZERO)).negate()).build();  //冻结的金额
            //更新用户钱包
            bool = walletMapper.updateOTCAssets(wallet) > 0;
            if(!bool)                  throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);



        }
        return bool;
    }


    @Override
    public List<OTCAdvertVO> list(PageDTO dto) {
        return mapper.list(dto);
    }

    @Override
    public OTCAdvertEntity findByCondition(BaseDTO dto) {
        return mapper.findByCondition(dto);
    }

    @Override
    public OTCAdvertEntity detail(BaseDTO dto) {
        return mapper.detail( dto);
    }
}
