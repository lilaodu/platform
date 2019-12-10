package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.*;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.blockchain.platform.annotation.DuplicateVerify;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.constant.TypeConst;
import com.blockchain.platform.enums.Method;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.i18n.MessageUtils;
import com.blockchain.platform.mapper.OTCAdvertMapper;
import com.blockchain.platform.mapper.PaymentMapper;
import com.blockchain.platform.mapper.UserMapper;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.OTCOrderVO;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.pojo.vo.UserPaymentVO;
import com.blockchain.platform.service.*;
import com.blockchain.platform.utils.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * OTC订单管理控制器
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-21 8:30 PM
 **/
@RestController
@RequestMapping("/otc/order")
public class OTCOrderController extends BaseController {


    @Resource
    IUserService userService;
    /**
     * otc 订单服务接口
     */
    @Resource
    private IOTCOrderService orderService;

    /**
     * 商户服务接口
     */
    @Resource
    private IOTCMerchantService merchantService;

    /**
     * 广告服务接口
     */
    @Resource
    private IOTCAdvertService advertService;


    @Resource
    OTCAdvertMapper otcAdvertMapper;
    /**
     * 用户钱包服务接口
     */
    @Resource
    private IUserWalletService walletService;

    /**
     * 支付方式服务接口
     */
    @Resource
    private IPaymentService paymentService;

    @Resource
    PaymentMapper paymentMapper;

    @Resource
    IOTCChatService chatService;

    @Resource
    private MessageUtils messageUtils;

    @Resource
    SMSUtils smsUtils;

    ExecutorService pool = Executors.newFixedThreadPool(40);


    /**
     * 订单列表
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/query")
    public ResponseData query(@RequestBody PageDTO dto, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser(request);
            //用户id
            dto.setUserId(user.getId());
            //判断用户是否是商户
            OTCMerchantEntity merchant = merchantService.findByCondition(BaseDTO.builder()
                    .userId(user.getId())  //用户id
                    .state(BizConst.MerchantConst.STATE_COMPLETE).build());  //审核通过
            if (ObjectUtil.isNotEmpty(merchant)) {
                dto.setType(AppConst.FIELD_Y);
            } else {
                dto.setType(AppConst.FIELD_N);
            }
            //开始分页
            Page<OTCOrderVO> page = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
//            page.setOrderBy(" create_time desc ");
            //列表
            List<OTCOrderVO> list = orderService.query(dto);
            //返回数据
            data.setData(PageVO.builder()
                    .total(page.getTotal())
                    .list(list).build());
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 获取广告对应的订单列表
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/list")
    public ResponseData list(@RequestBody PageDTO dto, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //开始分页
            Page<OTCOrderVO> page = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
            //列表
            List<OTCOrderVO> list = orderService.query(dto);
            //返回数据
            data.setData(PageVO.builder()
                    .total(page.getTotal())
                    .list(list).build());
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 卖出
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/sell")
    @DuplicateVerify(method = Method.OTC_SELL)
    public ResponseData sell(@RequestBody @Valid OTCOrderDTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if (valid.hasErrors()) {
                throw ExUtils.error(valid);
            }
            //登录用户
            UserDTO user = getLoginUser(request);
            //获取广告详情
            OTCAdvertEntity advert = advertService.findByCondition(BaseDTO.builder()
                    .id(dto.getAdvertId())
                    .state(BizConst.AdvertConst.STATE_VALID).build());
            //判断是否是自己的广告
            if (IntUtils.equals(user.getId(), advert.getUserId())) {
                throw ExUtils.error(LocaleKey.USER_NOT_OPERATE_OWN_ADVERT);
            }
            //判断用户当前未支付的订单是否超过限制
            List<OTCOrderEntity> payOrder = orderService.list(BaseDTO.builder()
                    .state(BizConst.OTCOrderConst.STATE_APPLY)
                    .userId(user.getId()).build());
            if (IntUtils.compare(payOrder.size(), AppConst.DEFAULT_OTC_ORDER_APPLY)) {
                throw ExUtils.error(LocaleKey.OTC_APPLY_ORDER_TOO_MUCH);
            }
            //判断能否购买
            if (BigDecimalUtils.compareTo(advert.getLimitDown(), dto.getAmount()) ||
                    BigDecimalUtils.compareTo(dto.getAmount(), advert.getLimitUp())) {
                throw ExUtils.error(LocaleKey.OTC_ORDER_NUM_LACK);
            }
            //判断用户是否存在未完成的订单（同一广告）
            List<OTCOrderEntity> orders = orderService.list(BaseDTO.builder()
                    .id(advert.getId())
                    .userId(user.getId())
                    .state(BizConst.OTCOrderConst.STATE_ADOPT).build());
            if (CollUtil.isNotEmpty(orders)) {
                throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_COMPLETE);
            }
            //获取用户支持的支付方式
            List<UserPaymentVO> payList = paymentService.list(BaseDTO.builder()
                    .userId(user.getId())
                    .state(BizConst.BIZ_STATUS_FAILED).build());
            //判断用户支付方式是否满足当前广告的支付方式
            BizUtils.allowPay(payList, CollUtil.toList(advert.getPayType().split(StrUtil.COMMA)));
            //获取用户对应货币钱包余额
            UserWalletEntity wallet = walletService.findByCondition(BaseDTO.builder()
                    .symbol(advert.getSymbol())
                    .userId(user.getId())
                    .state(BizConst.BIZ_STATUS_VALID).build());
            //计算手续费
            String param = redisPlugin.hget(RedisConst.PLATFORM_PARAMS, BizConst.ParamsConst.ADVERT_FEE);
            BigDecimal feeRate = BizUtils.rate(dto.getNum(), advert.getSymbol(), param);
            //判断用户对应货币钱包可用余额是否足够
            BizUtils.verifyWallet(wallet, BigDecimalUtils.add(dto.getNum(), feeRate), BizConst.WalletConst.WALLET_TYPE_OTC);
            //出售
            OTCOrderEntity entity = BeanUtil.toBean(dto, OTCOrderEntity.class);
            //用户id
            entity.setCreateTime(new Date());

            entity.setUserId(user.getId());
            //报价
            entity.setPrice(advert.getPrice());
            //订单状态
            entity.setState(BizConst.OTCOrderConst.STATE_APPLY);
            // 商户ID
            entity.setMerchantId(advert.getUserId());
            //货币简称
            entity.setSymbol(advert.getSymbol());
            //订单编号
            entity.setOrderNumber(StrUtil.toString(System.nanoTime()));
            //备注码(6位)
            entity.setRemarkCode(RandomUtil.randomNumbers(AppConst.DEFAULT_REMARK_CODE));
            //汇率
            entity.setRate(advert.getRate());
            //手续费
            entity.setFeeRate(feeRate);
            //生成订单


            entity.setPayType(dto.getPayType());
            Integer advertUserId = advert.getUserId();
            String address = getUserPayment(dto.getPayType(), user.getId()).getAccount();
            entity.setPayAccount(getUserPayment(dto.getPayType(), advertUserId).getAccount());
            entity.setReceiveAccount(address);
            entity.setReceiveAddress(getUserPayment(dto.getPayType(), user.getId()).getAddress());
            Boolean bool = orderService.modify(entity);


            // 发送一条消息

            if (!bool) {
                throw ExUtils.error(LocaleKey.SYS_OPERATE_FAILED);
            } else {
                OTCChatEntity chatEntity = new OTCChatEntity();

                //用户id
                entity.setUserId( user.getId());
                //广告编号
                chatEntity.setAdvertNumber( StrUtil.isEmpty(advert.getAdvertNumber()) ? StrUtil.EMPTY : advert.getAdvertNumber());
                // 订单编号
                chatEntity.setOrderNumber( entity.getOrderNumber());

                chatEntity.setContent( messageUtils.get( LocaleKey.OTC_CHAT_ORDER, getLang( request)));

                chatEntity.setUserId( user.getId());
                //状态
                chatEntity.setState( BizConst.ChatConst.STATE_MERCHANT_UNREAD);

                chatService.send( chatEntity);
            }
            data.setData(entity.getId());
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }


    @Cached(cacheType = CacheType.LOCAL, expire = 10)
    private UserPaymentVO getUserPayment(String payType, Integer advertUserId) {
        BaseDTO sqlEntity = new BaseDTO();
        sqlEntity.setState(0);
        sqlEntity.setUserId(advertUserId);
        List<UserPaymentVO> userPaymentEntities = paymentMapper.
                list(sqlEntity).stream().
                filter(x -> x.getPayType().
                        equalsIgnoreCase(payType)).collect(Collectors.toList());
        return userPaymentEntities.get(0);
    }


    @Resource
    UserMapper userMapper;
    /**
     * 买入
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/buy")
    @DuplicateVerify(method = Method.OTC_BUY)
    public ResponseData buy(@RequestBody @Valid OTCOrderDTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if (valid.hasErrors()) {
                throw ExUtils.error(valid);
            }
            //登录用户
            UserDTO user = getLoginUser(request);


            UserEntity userEntity=userMapper.selectById(user.getId());
            if(userEntity.getState().equals(2)) throw ExUtils.error(LocaleKey.USER_LOCKED);

            //获取广告详情
            OTCAdvertEntity advert = advertService.findByCondition(BaseDTO.builder()
                    .id(dto.getAdvertId())
                    .state(BizConst.AdvertConst.STATE_VALID).build());
            //判断是否是自己的广告
            if (IntUtils.equals(user.getId(), advert.getUserId())) {
                throw ExUtils.error(LocaleKey.USER_NOT_OPERATE_OWN_ADVERT);
            }
            //判断用户当前未支付的订单是否超过限制
            List<OTCOrderEntity> payOrder = orderService.list(BaseDTO.builder()
                    .state(BizConst.OTCOrderConst.STATE_APPLY)
                    .userId(user.getId()).build());
            if (IntUtils.compare(payOrder.size(), AppConst.DEFAULT_OTC_ORDER_APPLY)) {
                throw ExUtils.error(LocaleKey.OTC_APPLY_ORDER_TOO_MUCH);
            }
            //判断用户是否存在未完成的订单
            List<OTCOrderEntity> list = orderService.list(BaseDTO.builder()
                    .id(dto.getAdvertId())
                    .state(BizConst.OTCOrderConst.STATE_ADOPT)
                    .userId(user.getId()).build());
            if (CollUtil.isNotEmpty(list)) {
                throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_COMPLETE);
            }
            //获取用户支持的支付方式
            List<UserPaymentVO> payList = paymentService.list(BaseDTO.builder()
                    .userId(user.getId())
                    .state(BizConst.BIZ_STATUS_FAILED).build());
            //判断用户支付方式是否满足当前广告的支付方式
            BizUtils.allowPay(payList, CollUtil.toList(advert.getPayType().split(StrUtil.COMMA)));
            //用户剩余的预库存
            BigDecimal num = redisPlugin.get(StrUtil.concat(Boolean.TRUE, RedisConst.PLATFORM_OTC_SELL, advert.getAdvertNumber()));
//            //购买数量 > 剩余数量
//            if (BigDecimalUtils.compareTo(dto.getNum(), num)) {
//                throw ExUtils.error(LocaleKey.OTC_ORDER_NUM_EXCEED);
//            }
            //判断剩余数量是否大于购买下限
            BigDecimal down = BigDecimalUtils.divi(advert.getLimitDown(), advert.getPrice()).subtract(BigDecimal.valueOf(0.1));
            if (down.compareTo(dto.getNum())>0) {
                throw ExUtils.error(LocaleKey.OTC_OTDER_NUM_LESS_LIMIT);
            }


            //购买
            OTCOrderEntity entity = BeanUtil.toBean(dto, OTCOrderEntity.class);
            //用户id
            entity.setCreateTime(new Date());
            entity.setPayType(dto.getPayType());
            entity.setUserId(user.getId());
            //报价
            entity.setPrice(advert.getPrice());
            //订单状态
            entity.setState(BizConst.OTCOrderConst.STATE_APPLY);
            //货币简称
            entity.setSymbol(advert.getSymbol());
            //汇率
            entity.setRate(advert.getRate());
            // 商户ID
            entity.setMerchantId(advert.getUserId());
            //订单编号
            entity.setOrderNumber(StrUtil.toString(System.nanoTime()));
            //备注码(6位)
            entity.setRemarkCode(RandomUtil.randomNumbers(AppConst.DEFAULT_REMARK_CODE));
            //计算手续费
            String param = redisPlugin.hget(RedisConst.PLATFORM_PARAMS, BizConst.ParamsConst.ADVERT_FEE);
            BigDecimal feeRate = BizUtils.rate(dto.getNum(), advert.getSymbol(), param);
            entity.setFeeRate(feeRate);


            Integer advertUserId = advert.getUserId();
            String address = getUserPayment(dto.getPayType(), user.getId()).getAccount();
            entity.setPayAccount(address);
            entity.setReceiveAccount(getUserPayment(dto.getPayType(), advertUserId).getAccount());
            entity.setReceiveAddress(getUserPayment(dto.getPayType(),advertUserId).getAddress());

            //生成订单
            Boolean bool = orderService.buy(entity, advert);
            if (!bool) {
                throw ExUtils.error(LocaleKey.SYS_OPERATE_FAILED);
            } else {
                OTCChatEntity chatEntity = new OTCChatEntity();

                //用户id
                entity.setUserId( user.getId());
                //广告编号
                chatEntity.setAdvertNumber( StrUtil.isEmpty(advert.getAdvertNumber()) ? StrUtil.EMPTY : advert.getAdvertNumber());
                // 订单编号
                chatEntity.setOrderNumber( entity.getOrderNumber());

                chatEntity.setContent( messageUtils.get( LocaleKey.OTC_CHAT_ORDER, getLang( request)));

                chatEntity.setUserId( user.getId());
                //状态
                chatEntity.setState( BizConst.ChatConst.STATE_MERCHANT_UNREAD);

                chatService.send( chatEntity);
            }
            //返回订单id
            data.setData(entity.getId());
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 取消订单
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/cancel/{id}")
    public ResponseData cancel(@PathVariable("id") Integer id, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //登陆用户
            UserDTO user = getLoginUser(request);
            //获取订单详情
            OTCOrderEntity order = orderService.findByCondition(BaseDTO.builder()
                    .state(BizConst.OTCOrderConst.STATE_APPLY)  //订单状态
                    .id(id).build());     //订单id
            if (ObjectUtil.isEmpty(order)) {
                throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_FIND);
            }
            //如果是当前登录用户自己的订单，则不能取消卖单
            if (IntUtils.equals(user.getId(), order.getUserId())) {
                if ((StrUtil.equals(order.getType(), BizConst.OTCOrderConst.TYPE_SELL))) {
                    throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_CANCEL);
                }
            } else {
                //当前登录用户是商户
                //订单对应的广告详情
                OTCAdvertEntity advert = advertService.findByCondition(BaseDTO.builder()
                        .id(order.getAdvertId())
                        .state(BizConst.BIZ_STATUS_VALID)
                        .userId(user.getId()).build());
                if (ObjectUtil.isEmpty(advert) || StrUtil.equals(order.getType(), BizConst.OTCOrderConst.TYPE_BUY)) {
                    throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_CANCEL);
                }
            }
            order.setState( BizConst.OTCOrderConst.STATE_CANCEL);
            //取消

            Boolean bool = orderService.cancelOtcOrder(order);
            if (!bool) {
                throw ExUtils.error(LocaleKey.SYS_OPERATE_FAILED);
            }

            //返回订单id
            data.setData(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 用户点击已放款
     *
     * @param dto
     * @param valid
     * @param request
     * @return
     */
    @RequestMapping("/pay")
    public ResponseData pay(@RequestBody OTCOrderPayDTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if (valid.hasErrors()) {
                throw ExUtils.error(valid);
            }
            //登录用户
            UserDTO user = getLoginUser(request);
            Integer targetUserId;
            //订单详情
            OTCOrderEntity order = orderService.findByCondition(BaseDTO.builder()
                    .state(BizConst.OTCOrderConst.STATE_APPLY)  //订单状态
                    .id(dto.getId()).build());     //订单
            if (ObjectUtil.isEmpty(order)) {
                throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_FIND);
            }
            OTCAdvertEntity advert = advertService.findByCondition(BaseDTO.builder()
                    .id(order.getAdvertId())
                    .state(BizConst.BIZ_STATUS_VALID)
                    .build());
            //订单是当前登录用户的订单
            if (IntUtils.equals(order.getUserId(), user.getId())) {
                // 卖单不能点击放款
                if (StrUtil.equals(order.getType(), BizConst.OTCOrderConst.TYPE_SELL)) {
                    throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_PAY);
                }
                targetUserId=advert.getUserId();
            } else {
                //是商户的订单
                //订单对应的广告详情

                if (ObjectUtil.isEmpty(advert) || StrUtil.equals(order.getType(), BizConst.OTCOrderConst.TYPE_BUY)) {
                    throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_PAY);
                }
                targetUserId=user.getId();

            }
            //修改订单状态
            OTCOrderEntity entity = OTCOrderEntity.builder()
                    .state(BizConst.OTCOrderConst.STATE_PAY)  //状态（放款）
                    .id(order.getId())    //id
                    .payType(dto.getPayType())
                    .payAccount(dto.getPayAccount())
                    .version(order.getVersion()).build();   //版本号
            //放款
            Boolean bool = orderService.modify(entity);
            if (!bool) {
                throw ExUtils.error(LocaleKey.SYS_OPERATE_FAILED);
            }


            pool.submit(()->{
                UserEntity targetUser=userService.getById(targetUserId);

                smsUtils.send(targetUser.getMobile(),"对方已经放款",null,null);

            });
            //返回订单id
            data.setData(dto.getId());
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 放币
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/adopt")
    public ResponseData adopt(@RequestBody BaseDTO dto, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser(request);
            //商户点击放币
            //获取放币对应的订单
            OTCOrderEntity order = orderService.findByCondition(BaseDTO.builder()
                    .id(dto.getId()).build());
            if (ObjectUtil.isEmpty(order)) {
                throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_FIND);
            }
            //判断用户资金密码
            String pwd = SecureUtil.md5( StrUtil.addSuffixIfNot(dto.getCipher(), BizConst.BIZ_SECRET_CODE));
            if( !StrUtil.equals( pwd, user.getCipher())){
                throw ExUtils.error( LocaleKey.USER_CIPHER_PASSWORD_ERROR);
            }
            //订单是当前登录用户的订单
            if (IntUtils.equals(order.getUserId(), user.getId())) {
                // 买单不能点击放币
                if (StrUtil.equals(order.getType(), BizConst.OTCOrderConst.TYPE_BUY)) {
                    throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_ADOPT);
                }
            } else {

            }
            //对双方货币钱包进行操作，扣除手续费
            //对双方货币钱包进行操作
            Boolean bool = orderService.adopt(order);
            if (!bool) {
                throw ExUtils.error(LocaleKey.SYS_OPERATE_FAILED);
            }else{
                if (StrUtil.equals(order.getType(), BizConst.OTCOrderConst.TYPE_BUY)) {

                    UserEntity targetUser=userService.getById(order.getUserId());
                    pool.submit(()->{
                        smsUtils.send(targetUser.getMobile(),"对方已经放币",null,null);

                    });

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }

        //返回订单id
        data.setData( dto.getId());

            return data;
    }

    /**
     * 评论/打分
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/comment")
    public ResponseData comment(@RequestBody @Valid CommentDTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if (valid.hasErrors()) {
                throw ExUtils.error(valid);
            }
            //登录用户
            UserDTO user = getLoginUser(request);
            //获取订单详情
            OTCOrderEntity order = orderService.findByCondition(BaseDTO.builder()
                    .id(dto.getId())
                    .userId(user.getId())
                    .state(BizConst.OTCOrderConst.STATE_ADOPT).build());
            if (ObjectUtil.isEmpty(order)) {
                throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_FIND);
            }
            //修改订单状态
            OTCOrderEntity entity = OTCOrderEntity.builder()
                    .id(dto.getId())   //订单id
                    .state(BizConst.OTCOrderConst.STATE_COMMENT)  // 状态
                    .opinion(dto.getOpinion()).build();  //评论内容
            Boolean bool = orderService.modify(entity);
            //如果评论为 满意
            if (bool && StrUtil.equals(dto.getOpinion(), BizConst.OTCOrderConst.COMMENT_SATISFY)) {
                //修改用户的好评率（满意的订单数量/用户的所有订单）
                //所有订单数量
                Integer all = orderService.query(PageDTO.builder()
                        .userId(user.getId()).build()).size();
                //满意的订单数量
                Integer satisfy = orderService.query(PageDTO.builder()
                        .userId(user.getId())
                        .opinion(BizConst.OTCOrderConst.COMMENT_SATISFY).build()).size();
                //获取广告信息
                OTCAdvertEntity advert = advertService.findByCondition(BaseDTO.builder()
                        .id(order.getAdvertId()).build());
                //修改商户信息
                OTCMerchantEntity merchant = OTCMerchantEntity.builder()
                        .id(advert.getUserId())  //商户id
                        .praise(IntUtils.percent(satisfy, all)).build();  //好评率
                bool = merchantService.updateById(merchant);
                if (!bool) {
                    throw ExUtils.error(LocaleKey.SYS_OPERATE_FAILED);
                }
            }
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 订单申述
     *
     * @param dto
     * @param valid
     * @param request
     * @return
     */
    @RequestMapping("/representation")
    public ResponseData representation(@RequestBody @Valid OTCRepresentationDTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if (valid.hasErrors()) {
                throw ExUtils.error(valid);
            }
            //登录用户
            UserDTO user = getLoginUser(request);
            //判断订单是否存在
            OTCOrderEntity order = orderService.findByCondition(BaseDTO.builder()
                    .orderNumber(dto.getOrderNumber()).build());
            if (ObjectUtil.isEmpty(order)) {
                throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_FIND);
            }
            //copy属性
            OTCRepresentationEntity entity = BeanUtil.toBean(dto, OTCRepresentationEntity.class);
            //申述人
            entity.setUserId(user.getId());
            //提交申述
            Boolean bool = orderService.representation(entity);
            if (!bool) {
                throw ExUtils.error(LocaleKey.SYS_OPERATE_FAILED);
            }
            data.setData(order.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 证明材料上传
     *
     * @param request
     * @return
     */
    @RequestMapping("/material")
    public ResponseData upload(@RequestParam Map<String, Object> param, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //文件上传对象
            UploadDTO ud = BizUtils.getUploadPic(request);
            //登录用户
            UserDTO user = getLoginUser(request);
            // 图片上传对象，备份文件路径
            ud.setUserId(user.getId());
            // 上传路径
            ud.setPathType(TypeConst.TYPE_UPLOAD_C2C);
            // 上传文件
            HttpResponse response = HttpRequest.post(NetUtils.getUploadUrl())
                    .header("content-type", "application/json")
                    .body(JSON.toJSONString(ud))
                    .execute();
            if (!IntUtils.equals(response.getStatus(), HttpStatus.HTTP_OK)) {
                throw ExUtils.error(LocaleKey.FILE_UPLOAD_FAILED);
            }
            data.setData(response.body());
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.FILE_UPLOAD_FAILED);
        }
        return data;
    }

    /**
     * 获取订单详情
     *
     * @param id
     * @return
     */




    @RequestMapping("/details/{id}")
    public ResponseData details(@PathVariable("id") Integer id) {
        ResponseData data = ResponseData.ok();
        try {
            OTCOrderVO vo = orderService.details(BaseDTO.builder()
                    .id(id).build());
            //获取详情
            OTCAdvertEntity advert = otcAdvertMapper.findByNumber(vo.getAdvertNumber());
            OTCOrderEntity otcOrderEntity = orderService.getById(id);

            if (otcOrderEntity.getType().equalsIgnoreCase(BizConst.OTCOrderConst.TYPE_BUY)) {

                setUser(vo, advert.getUserId(), vo.getUserId(),vo.getPayType());
                vo.setBankName(getUserPayment(otcOrderEntity.getPayType(), advert.getUserId()).getBankName());



            } else {

                setUser(vo, vo.getUserId(), advert.getUserId(),vo.getPayType());
                vo.setBankName(getUserPayment(otcOrderEntity.getPayType(), vo.getUserId()).getBankName());


            }

            UserEntity advertUser=userService.getById(advert.getUserId());
            vo.setMerchantImage(advertUser.getHeadImage());

            Date endDate = new Date(otcOrderEntity.getCreateTime().getTime() + 15 * 60 * 1000);

            OTCOrderEntity orderEntity= orderService.getById(id);
            vo.setOrder(orderEntity);
            vo.setEndTime(endDate);
            //判断订单是否存在
            if (ObjectUtil.isEmpty(vo)) {
                throw ExUtils.error(LocaleKey.OTC_ORDER_NOT_FIND);
            }
            //返回数据
            data.setData(vo);
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    private void setUser(OTCOrderVO vo, Integer userId, Integer userId2,String payType) {
        Integer sellUserId = userId;
        Integer buyUserId = userId2;

        UserEntity buyer= userService.getById(buyUserId);
        UserEntity seller= userService.getById(sellUserId);

        String sellUser = getUserPayment(payType,sellUserId).getUsername();
        String buyUser = getUserPayment(payType,buyUserId).getUsername();;
        vo.setSellerName(sellUser);
        vo.setBuyerName(buyUser);
        vo.setBuyerImage(buyer.getHeadImage());
        vo.setSellerImage(seller.getHeadImage());
        vo.setBuyerTel(buyer.getMobile());
        vo.setSellerTel(seller.getMobile());
    }

}
