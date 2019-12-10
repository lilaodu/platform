package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blockchain.platform.annotation.DuplicateVerify;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.enums.Method;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.OTCAdvertEntity;
import com.blockchain.platform.pojo.entity.OTCMerchantEntity;
import com.blockchain.platform.pojo.entity.OTCOrderEntity;
import com.blockchain.platform.pojo.entity.UserWalletEntity;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.IOTCAdvertService;
import com.blockchain.platform.service.IOTCMerchantService;
import com.blockchain.platform.service.IOTCOrderService;
import com.blockchain.platform.service.IUserWalletService;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * OTC广告控制器
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 5:24 PM
 **/
@RestController
@RequestMapping("/otc/advert")
public class OTCAdvertController extends BaseController {

    /**
     * otc广告服务接口
     */
    @Resource
    private IOTCAdvertService advertService;

    /**
     * otc 商户信息服务接口
     */
    @Resource
    private IOTCMerchantService merchantService;

    /**
     * otc订单 服务接口
     */
    @Resource
    private IOTCOrderService orderService;

    /**
     * 用户钱包服务接口
     */
    @Resource
    private IUserWalletService userWalletService;

    /**
     * 我的广告
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/query")
    public ResponseData query(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录对象
            UserDTO user = getLoginUser( request);
            //判断用户是不是商户
            if(StrUtil.equals(AppConst.FIELD_N, user.getIsMerchant())){
                throw ExUtils.error( LocaleKey.OTC_NOT_MERCHANT);
            }
            //设置用户id
            dto.setUserId( user.getId());
            //开始分页
            Page<OTCAdvertVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            page.setOrderBy(" state asc  , create_time desc");
            //列表查询
            List<OTCAdvertVO> list = advertService.query( dto);
            //返回数据
            data.setData( PageVO.builder()
                                    .total( page.getTotal())
                                    .list( list).build());
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 发布
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/modify")
    @DuplicateVerify(method = Method.OTC_ADVERT)
    public ResponseData modify(@RequestBody @Valid OTCAdvertDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        //广告编号
        String advertNumber = StrUtil.toString( System.nanoTime());
        try {
            if( valid.hasErrors()){
                throw ExUtils.error( valid);
            }
            //登录对象
            UserDTO user = getLoginUser( request);
            //获取商户信息
            OTCMerchantEntity merchant = merchantService.findByCondition( BaseDTO.builder()
                                                                        .userId( user.getId())
                                                                        .state( BizConst.MerchantConst.STATE_COMPLETE).build());
            //判断用户是不是商户
            if(ObjectUtil.isEmpty( merchant)){
                throw ExUtils.error( LocaleKey.OTC_NOT_MERCHANT);
            }
            //copy 属性
            OTCAdvertEntity entity = BeanUtil.toBean( dto, OTCAdvertEntity.class);
            // 广告发布时间
            entity.setCreateTime( new Date());
            //判断广告是否需要缴纳保证金
            //获取广告配置
            String config = redisPlugin.hget( RedisConst.PLATFORM_PARAMS, BizConst.ParamsConst.ADVERT_CONFIG);
            //解析数据
            Map<String,String> map =  JSON.parseObject( config, new TypeReference<Map<String, String>>(){});
            //限制
            BigDecimal limit = NumberUtil.toBigDecimal( map.get(dto.getRate()));
            //广告上限高于限制，需缴纳保证金
            if( BigDecimalUtils.compareTo( dto.getLimitUp(),limit)){

                throw ExUtils.error( "未认证商户最高只能发布上限为"+limit+"的广告");
            }
            if(!IntUtils.isZero( dto.getId())){
                //获取广告详情
                OTCAdvertEntity advert = advertService.findByCondition( BaseDTO.builder()
                        .id( dto.getId()).build());  //广告
                //判断广告是否存在
                if( ObjectUtil.isEmpty( advert)){
                    throw ExUtils.error( LocaleKey.OTC_ADVERT_NOT_FIND);
                }
                //编辑，只有先下架才能编辑广告
                if( IntUtils.equals( advert.getState(), BizConst.AdvertConst.STATE_INVALID)){
                    throw ExUtils.error( LocaleKey.OTC_ADVERT_NEED_INVALID);
                }
            }
            //广告编号(6位随机的数字)
            entity.setAdvertNumber( advertNumber);
            //卖币的广告，计算手续费
            if( StrUtil.equals( dto.getType(), BizConst.OTCOrderConst.TYPE_SELL)){
                //获取用户钱包信息
                UserWalletEntity userWallet = userWalletService.findByCondition( BaseDTO.builder()
                                                                            .symbol( dto.getSymbol())
                                                                            .userId( user.getId()).build());
                //用户可用余额 * 单价 < 上限，不能发布成功
                if( BigDecimalUtils.compareTo( dto.getLimitUp(), NumberUtil.mul( dto.getPrice(),
                        BigDecimalUtils.subtr( userWallet.getBalanceOtc(), userWallet.getFrozenOtc())))){
                    throw ExUtils.error( LocaleKey.WALLET_INSUFFICIENT_FUNDS); //余额不足
                }
                //计算用户币的数量(上限/价格)
                BigDecimal num = BigDecimalUtils.divi(dto.getLimitUp(), dto.getPrice());
                //计算广告手续费
                //获取配置信息
                String params = redisPlugin.hget( RedisConst.PLATFORM_PARAMS, BizConst.ParamsConst.ADVERT_FEE);
                BigDecimal feeRate = BizUtils.rate( num,entity.getSymbol(), params);
                //设置广告手续费
                entity.setFeeRate( feeRate);
                //货币总数量
                entity.setNum( num);
                //判断用户钱包可用余额( 货币的数量 + 手续费)
                BizUtils.verifyWallet( userWallet, BigDecimalUtils.add(num, feeRate), BizConst.WalletConst.WALLET_TYPE_OTC);
                //剩余货币数量
                entity.setSurplus( num);
                //马上更新预库存
                redisPlugin.set(StrUtil.concat(Boolean.TRUE,RedisConst.PLATFORM_OTC_SELL, StrUtil.toString( entity.getAdvertNumber())), num);
            }
            //设置用户id
            entity.setUserId( user.getId());
            //发布
            Boolean bool = advertService.modify( entity);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }catch (Exception ex){
            // 失败后，删除预库存记录
            redisPlugin.del( StrUtil.concat(Boolean.TRUE,RedisConst.PLATFORM_OTC_SELL, advertNumber));
            ex.printStackTrace();
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 下架广告
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/invalid/{id}")
    public ResponseData invalid(@PathVariable("id") Integer id, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //获取广告详情
            OTCAdvertEntity advert = advertService.findByCondition( BaseDTO.builder()
                                                                        .id( id)
                                                                        .userId( user.getId())
                                                                        .state( BizConst.BIZ_STATUS_VALID).build());
            //判断广告是否存在
            if(ObjectUtil.isEmpty( advert)){
                throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
            }
            //判断此广告是否有正在进行中的订单
            PageDTO dto = PageDTO.builder()
                                    .advertId( id).build();

            List<OTCOrderEntity> orders = orderService.list(BaseDTO.builder().id(advert.getId()).build());
            orders =  orders.stream().filter(x-> x.getState().equals(1)|| x.getState().equals(3)).collect(Collectors.toList());
            if(CollUtil.isNotEmpty( orders)){
                throw ExUtils.error( LocaleKey.ADVERT_NOT_TO_BE_INVALID);
            }

            Boolean bool = advertService.invalid( advert);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 获取出售/买入的广告列表
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/list")
    public ResponseData list(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            if( ObjectUtil.isNotEmpty( user)){
                //用户id
                dto.setUserId( user.getId());
            }
            //获取有效的广告
            dto.setState( BizConst.BIZ_STATUS_VALID);
            //开始分页
            Page<OTCAdvertVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //列表信息
            List<OTCAdvertVO> list = advertService.list( dto);
            //获取在线商户
            Map<Integer,String> online = redisPlugin.get( RedisConst.PLATFORM_MERCHANT_ONLINE);
            for(int idx = 0 ; idx < list.size() ; idx ++){
                OTCAdvertVO vo = list.get(idx);
                if(MapUtil.isNotEmpty(online) && online.containsKey( vo.getMerchantId())){
                    //在线
                    vo.setOnline( AppConst.FIELD_Y);
                }else{
                    vo.setOnline( AppConst.FIELD_N);
                }
            }
            //返回页面数据
            data.setData( PageVO.builder()
                                    .total( page.getTotal())
                                    .list( list).build());
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 获取广告详情
     * @param id
     * @return
     */
    @RequestMapping("/detail/{id}")
    public ResponseData detail(@PathVariable("id") Integer id){
        ResponseData data = ResponseData.ok();
        try {
            //获取详情
            OTCAdvertEntity entity = advertService.findByCondition( BaseDTO.builder()
                                                                .id( id)
                                                                .state( BizConst.BIZ_STATUS_VALID).build());
            //判断数据是否为null
            if( ObjectUtil.isEmpty( entity)){
                throw  ExUtils.error( LocaleKey.OTC_ADVERT_NOT_FIND);
            }
            //返回数据
            OTCAdvertVO vo = BeanUtil.toBean( entity, OTCAdvertVO.class);
            data.setData( vo);
        }catch (Exception ex){
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }
}
