package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.mapper.KycMapper;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.OTCMerchantDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.OTCMerchantEntity;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.entity.UserKycEntity;
import com.blockchain.platform.pojo.vo.OTCMerchantVO;
import com.blockchain.platform.service.IKycService;
import com.blockchain.platform.service.IOTCMerchantService;
import com.blockchain.platform.service.IUserService;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * OTC 商户控制器
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-12 1:45 PM
 **/
@RestController
@RequestMapping("/otc/merchant")
public class OTCMerchantController extends BaseController {

    /**
     * 商户服务接口
     */
    @Resource
    private IOTCMerchantService merchantService;

    /**
     * 用户服务接口
     */
    @Resource
    private IUserService userService;
    @Resource
    IKycService kycService;

    /**
     * 申请成为商户
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/apply")
    public ResponseData apply(@RequestBody @Valid OTCMerchantDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if( valid.hasErrors()){
                throw ExUtils.error( valid);
            }
            //登录用户
            UserDTO user = getLoginUser( request);
            //获取用户详情
            UserEntity userEntity = userService.findUserByCondition( user);
            //判断用户是否通过kyc2的认证
            UserKycEntity kyc = kycService.findByCondition(BaseDTO.builder()
                    .state( BizConst.BIZ_STATUS_FAILED)
                    .userId( user.getId()).build());
            if (BizConst.KycConst.K1_OK > kyc.getStep()) {
                throw ExUtils.error(LocaleKey.USER_C1_NOT_ACCESS);
            }

            //获取商户信息
            OTCMerchantEntity merchant = merchantService.findByCondition( BaseDTO.builder()
                                                            .userId( user.getId()).build());
            //判断登录用户是否提交过申请
            if( ObjectUtil.isNotEmpty( merchant)
                    && IntUtils.equals( merchant.getState(), BizConst.MerchantConst.STATE_APPLY)){
                throw ExUtils.error( LocaleKey.USER_HAS_APPLY_MERCHANT);
            }
            //用户已经是商户
            if(ObjectUtil.isNotEmpty( merchant) &&
                    IntUtils.equals( merchant.getState(), BizConst.MerchantConst.STATE_COMPLETE)){  //审核通过
                throw ExUtils.error( LocaleKey.USER_HAS_MERCHANT);
            }
            //开始申请
            OTCMerchantEntity entity = OTCMerchantEntity.builder()
                                            .userId( user.getId())  //用户id
                                            .state( BizConst.MerchantConst.STATE_APPLY)  //状态（申请）
                                            .nickName( dto.getNickName())   //用户昵称
                                            .reason( dto.getReason()).build();  //申请原因
            if( ObjectUtil.isNotEmpty( merchant)){
                //重新申请
                entity.setId( merchant.getId());
                //版本号
                entity.setVersion( merchant.getVersion());
            }
            Boolean bool = merchantService.apply( entity);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 商户详情
     * @param request
     * @return
     */
    @RequestMapping("/details")
    public ResponseData details(HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //详情
            OTCMerchantEntity entity = merchantService.findByCondition( BaseDTO.builder()
                                                            .userId( user.getId()).build());
            //返回数据
            data.setData(BeanUtil.toBean( entity, OTCMerchantVO.class));
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }
}
