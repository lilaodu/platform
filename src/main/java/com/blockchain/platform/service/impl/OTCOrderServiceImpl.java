package com.blockchain.platform.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.mapper.OTCAdvertMapper;
import com.blockchain.platform.mapper.OTCOrderMapper;
import com.blockchain.platform.mapper.UserWalletMapper;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserWalletDTO;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.OTCOrderVO;
import com.blockchain.platform.service.IOTCAdvertService;
import com.blockchain.platform.service.IOTCOrderService;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * otc订单服务实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-21 8:38 PM
 **/
@Service
public class OTCOrderServiceImpl extends ServiceImpl<OTCOrderMapper, OTCOrderEntity> implements IOTCOrderService {

    /**
     * otc 订单数据库借口
     */
    @Resource
    private OTCOrderMapper mapper;

    /**
     * 用户钱包数据库接口
     */
    @Resource
    private UserWalletMapper walletMapper;

    /**
     * 广告数据接口
     */
    @Resource
    private OTCAdvertMapper advertMapper;

    /**
     * redis 工具类
     */
    @Resource
    private RedisPlugin redisPlugin;

    @Override
    public List<OTCOrderVO> query(PageDTO dto) {
        return mapper.query(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean buy(OTCOrderEntity entity, OTCAdvertEntity advert) {
        //用户剩余的预库存
        BigDecimal num = redisPlugin.get(StrUtil.concat(Boolean.TRUE, RedisConst.PLATFORM_OTC_SELL, advert.getAdvertNumber()));
        //新增买入订单
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());

        Integer flag = mapper.insert(entity);
        if (flag > 0) {
            //广告详情
            OTCAdvertEntity otcAdvertEntity = advertMapper.detail(BaseDTO.builder().id(advert.getId()).build());
            //更新广告表的剩余货币数量
            OTCAdvertEntity advertEntity = OTCAdvertEntity.builder()
                    .id(entity.getAdvertId())
                    .surplus(BigDecimalUtils.subtr(num, entity.getNum()))
                    .version(otcAdvertEntity.getVersion()).build();
            flag = advertMapper.update(advertEntity);
            //判断剩余的可购买数量是否低于下限，若低于，直接下架广告
            BigDecimal down = BigDecimalUtils.divi(advert.getLimitDown(), advert.getPrice());
            if (BigDecimalUtils.compareTo(down, BigDecimalUtils.subtr(num, entity.getNum()))) {
                //修改广告状态，将剩余数量返回给商户钱包
                otcAdvertEntity = advertMapper.detail(BaseDTO.builder().id(advert.getId()).build());
                advertEntity.setVersion(otcAdvertEntity.getVersion());
                advertEntity.setState(BizConst.AdvertConst.STATE_INVALID); //下架状态
                flag = advertMapper.update(advertEntity);
                //更新用户钱包
                flag = walletMapper.updateOTCAssets(UserWalletDTO.builder()
                        .userId(advert.getUserId())  //商户
                        .symbol(advert.getSymbol())  //代币符号
                        .balance(BigDecimal.ZERO)   //总共
                        .frozenBalance(BigDecimalUtils.subtr(num, entity.getNum()).negate()).build());  //    冻结

            }
            //重新设置预缓存
            redisPlugin.set(StrUtil.concat(Boolean.TRUE, RedisConst.PLATFORM_OTC_SELL, advert.getAdvertNumber()),
                    BigDecimalUtils.subtr(num, entity.getNum()));
        }
        return flag > 0;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean modify(OTCOrderEntity entity) {
        Integer bool = IntUtils.INT_ZERO;
        //判断是新增还是编辑
        if (IntUtils.isZero(entity.getId())) {
            //买入/卖出
            bool = mapper.insert(entity);
             UserWalletDTO wallet = UserWalletDTO.builder()
                    .symbol(entity.getSymbol())  //货币符号
                    .userId(entity.getUserId())  //用户id
                    .balance(BigDecimal.ZERO)  //总量
                    .frozenBalance( entity.getNum()).build();  //冻结的金额
            //更新用户钱包
            bool= walletMapper.updateOTCAssets(wallet) ;
        } else {
            //编辑
            //获取详情
            OTCOrderEntity order = mapper.findByCondition(BaseDTO.builder()
                    .id(entity.getId()).build());
            if (ObjectUtil.isNotEmpty(order)) {
                //设置版本号
                entity.setVersion(order.getVersion());
                //修改
                bool = mapper.update(entity);
            }
        }
        return bool > 0;
    }

    @Override
    @Transactional( rollbackFor = Exception.class)
    public boolean cancelOtcOrder(OTCOrderEntity orderEntity)
    {
        OTCAdvertEntity advert = advertMapper.findByCondition(BaseDTO.builder()
                                                .id(orderEntity.getAdvertId())
                                                 .build());
        Integer bool = IntUtils.INT_ZERO;
        BigDecimal num = orderEntity.getNum();

        //买单退单
        if(orderEntity.getType().equals(BizConst.OTCOrderConst.TYPE_BUY)){
//            UserWalletDTO wallet = UserWalletDTO.builder()
//                    .symbol(orderEntity.getSymbol())  //货币符号
//                    .userId(advert.getUserId())  //用户id
//                    .balance(BigDecimal.ZERO)  //总量
//                    .frozenBalance( num.negate()).build();  //冻结的金额
//            bool=  walletMapper.updateOTCAssets(wallet) ;
            String key = StrUtil.concat(Boolean.TRUE, RedisConst.PLATFORM_OTC_SELL, StrUtil.toString(advert.getAdvertNumber()));
            //预库存重新加上取消订单的购买数量
            redisPlugin.set(key, BigDecimalUtils.add(redisPlugin.get(key), orderEntity.getNum()));

            bool = IntUtils.INT_ONE;
        }else{
            UserWalletDTO wallet = UserWalletDTO.builder()
                            .symbol(orderEntity.getSymbol())  //货币符号
                            .userId(orderEntity.getUserId())  //用户id
                            .balance(BigDecimal.ZERO)  //总量
                            .frozenBalance( num.negate()).build();  //冻结的金额
            bool=  walletMapper.updateOTCAssets(wallet) ;

        }
        if( bool > 0){
            //设置状态
            bool =   mapper.updateCancel( orderEntity);
            //是否修改成功
            if( IntUtils.compare( IntUtils.INT_ZERO, bool)){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }
        return bool > 0;

    }









    @Override
    public OTCOrderEntity findByCondition(BaseDTO dto) {
        return mapper.findByCondition(dto);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class},isolation = Isolation.REPEATABLE_READ)
    public Boolean adopt(OTCOrderEntity entity) {
        //修改用户订单状态
        OTCOrderEntity order = findByCondition( BaseDTO.builder()
                .id( entity.getId()).build());


        if(order.getState().equals(BizConst.OTCOrderConst.STATE_CANCEL))  throw ExUtils.error( LocaleKey.OTC_ORDER_CANCEL);
        else if(!order.getState().equals(BizConst.OTCOrderConst.STATE_PAY))  throw ExUtils.error( LocaleKey.OTC_ORDER_NOT_SURE);
         order = OTCOrderEntity.builder()
                .id(entity.getId())  //订单id
                .state(BizConst.OTCOrderConst.STATE_ADOPT).build();  //状态（放币）
        Boolean bool = saveOrUpdate(order);
        if (bool) {
            //对双方货币钱包操作
            //订单用户的钱包
            UserWalletEntity orderWallet = walletMapper.findByCondition(BaseDTO.builder()
                    .userId(entity.getUserId())
                    .symbol(entity.getSymbol()).build());
            //商户对应代币钱包
            //获取商户信息
            OTCAdvertEntity advert = advertMapper.findByCondition(BaseDTO.builder()
                    .id(entity.getAdvertId()).build());
            UserWalletEntity merchantWallet = walletMapper.findByCondition(BaseDTO.builder()
                    .userId(advert.getUserId())
                    .symbol(entity.getSymbol()).build());
            //交易流水记录
            OTCOrderFlowEntity flowEntity = OTCOrderFlowEntity.builder()
                    .userId(entity.getUserId())
                    .merchantId(advert.getUserId())
                    .orderId(entity.getId())
                    .symbol(entity.getSymbol()).build();
            if (StrUtil.equals(entity.getType(), BizConst.OTCOrderConst.TYPE_SELL)) {
                //订单用户 减( 货币数量 + 手续费)
                UserWalletDTO oWallet = UserWalletDTO.builder()
                        .symbol(entity.getSymbol())
                        .userId(entity.getUserId())
                        .balance( BigDecimalUtils.add(entity.getNum(), entity.getFeeRate()))
                        .frozenBalance(BigDecimalUtils.add(entity.getNum(), entity.getFeeRate()).negate()).build();
                //订单用户变更前
                flowEntity.setUserWalletBefore(orderWallet.getBalanceOtc());
                bool = walletMapper.updateOTCAssets(oWallet) > 0;


                //变更后
                flowEntity.setUserWalletAfter(BigDecimalUtils.subtr(orderWallet.getBalanceOtc(),
                        BigDecimalUtils.add(entity.getNum(), entity.getFeeRate())));
                //商户 加（货币数量 - 手续费）
                UserWalletDTO mWallet = UserWalletDTO.builder()
                        .symbol(entity.getSymbol())
                        .userId(advert.getUserId()).build();
                if (ObjectUtil.isEmpty(merchantWallet)) {
                    //商户钱包变更前
                    flowEntity.setMerchantWalletBefore(BigDecimal.ZERO);
                    //创建商户钱包
                    mWallet.setBalance(BigDecimalUtils.subtr(entity.getNum(), entity.getFeeRate()));
                    mWallet.setFrozenBalance(BigDecimal.ZERO);
                    bool = walletMapper.addUserWallet(mWallet) > 0;
                    //变更后
                    flowEntity.setMerchantWalletAfter(BigDecimalUtils.subtr(entity.getNum(), entity.getFeeRate()));
                } else {
                    //变更前
                    flowEntity.setMerchantWalletBefore(merchantWallet.getBalanceOtc());
                    mWallet.setBalance(BigDecimalUtils.subtr(entity.getNum(), entity.getFeeRate()).negate());
                    mWallet.setFrozenBalance(BigDecimal.ZERO);
                    bool = walletMapper.updateOTCAssets(mWallet) > 0;
                    //变更后
                    flowEntity.setMerchantWalletAfter(BigDecimalUtils.add(merchantWallet.getBalanceOtc(),
                            BigDecimalUtils.subtr(entity.getNum(), entity.getFeeRate())));
                }
            } else { //买单
                //订单用户 加(货币数量 - 手续费)
                UserWalletDTO oWallet = UserWalletDTO.builder()
                        .symbol(entity.getSymbol())
                        .userId(entity.getUserId()).build();
                //判断用户是否存在货币钱包
                if (ObjectUtil.isEmpty(orderWallet)) {
                    //变更前
                    flowEntity.setUserWalletBefore(BigDecimal.ZERO);
                    //创建货币钱包
                    oWallet.setBalance(BigDecimalUtils.subtr(entity.getNum(), entity.getFeeRate()));
                    oWallet.setFrozenBalance(BigDecimal.ZERO);
                    bool = walletMapper.addUserWallet(oWallet) > 0;
                    //变更后
                    flowEntity.setUserWalletAfter(BigDecimalUtils.subtr(entity.getNum(), entity.getFeeRate()));
                } else {
                    //变更前
                    flowEntity.setUserWalletBefore(orderWallet.getBalanceOtc());
                    oWallet.setBalance(BigDecimalUtils.subtr(entity.getNum(), entity.getFeeRate()).negate());
                    oWallet.setFrozenBalance(BigDecimal.ZERO);
                    bool = walletMapper.updateOTCAssets(oWallet) > 0;
                    //变更后
                    flowEntity.setMerchantWalletAfter(BigDecimalUtils.add(orderWallet.getBalanceOtc(),
                            BigDecimalUtils.subtr(entity.getNum(), entity.getFeeRate())));
                }
                //商户 减（货币数量 + 手续费）
                UserWalletDTO mWallet = UserWalletDTO.builder()
                        .symbol(entity.getSymbol())
                        .userId(advert.getUserId())
                        .balance(BigDecimalUtils.add(entity.getNum(), entity.getFeeRate()))
                        .frozenBalance(BigDecimalUtils.add(entity.getNum(), entity.getFeeRate()).negate()).build();
                //变更前
                flowEntity.setMerchantWalletBefore(merchantWallet.getBalanceOtc());
                bool = walletMapper.updateOTCAssets(mWallet) > 0;
                //变更后
                flowEntity.setMerchantWalletAfter(BigDecimalUtils.subtr(merchantWallet.getBalanceOtc(),
                        BigDecimalUtils.add(entity.getNum(), entity.getFeeRate())));
            }
            if (bool) {
                //添加订单用户流水
                mapper.addFlow(flowEntity);
            }else{
                throw ExUtils.error(LocaleKey.SYS_PARAM_ERROR);
            }
        }
        return bool;
    }

    @Override
    public Boolean representation(OTCRepresentationEntity entity) {
        return mapper.representation(entity) > 0;
    }

    @Override
    public List<OTCOrderEntity> list(BaseDTO dto) {
        return mapper.list(dto);
    }

    @Override
    public OTCOrderVO details(BaseDTO dto) {
        return mapper.details(dto);
    }
}
