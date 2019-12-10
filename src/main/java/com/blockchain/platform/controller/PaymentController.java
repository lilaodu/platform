package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.TypeConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.alibaba.fastjson.JSON;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PaymentDTO;
import com.blockchain.platform.pojo.dto.UploadDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.entity.UserKycEntity;
import com.blockchain.platform.pojo.entity.UserPaymentEntity;
import com.blockchain.platform.pojo.vo.UserPaymentVO;
import com.blockchain.platform.service.IKycService;
import com.blockchain.platform.service.IPaymentService;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import com.blockchain.platform.utils.NetUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.sql.Struct;
import java.util.HashMap;
import java.util.List;

/**
 * 用户支付方式控制器
 *
 * @author zhangye
 **/
@RestController
@RequestMapping("/payment")
public class PaymentController extends BaseController {

    /**
     * 支付方式接口
     */
    @Resource
    private IPaymentService paymentService;

    /**
     * 用户服务接口
     */
    @Resource
    private IKycService kycService;

    /**
     * 查询用户支付方式
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/query")
    public ResponseData query(@RequestBody BaseDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //获取登录用户信息
            UserDTO user = getLoginUser( request);
            //获取当前用户有效的支付方式
            dto.setUserId( user.getId());
            //排除无效的支付方式
            dto.setState(BizConst.BIZ_STATUS_FAILED);
            //列表
            List<UserPaymentVO> list = paymentService.query( dto);
            //返回页面数据
            data.setData( list);
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 新增
     * @param dto
     * @return
     */
    @RequestMapping("/add")
    public ResponseData add(@RequestBody @Valid PaymentDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if(valid.hasErrors()){
                throw ExUtils.error( valid);
            }
            //获取登录用户信息
            UserDTO user = getLoginUser( request);
            //获取用户认证详情
            UserKycEntity kyc = kycService.findByCondition( BaseDTO.builder()
                                        .userId( user.getId())
                                        .state( BizConst.BIZ_STATUS_FAILED).build());
            //添加支付方式必须通过kyc c1认证,且支付的姓名必须与实名认证的一致
            //认证姓名
            String username = StrUtil.concat( Boolean.FALSE, kyc.getFirstName(), kyc.getLastName());
            if( ObjectUtil.isEmpty( kyc) ||
                    IntUtils.compareTo(BizConst.KycConst.K1_OK, kyc.getState()) ||
                        !StrUtil.equals(dto.getUsername(), username)){
                throw ExUtils.error( LocaleKey.PAYMENT_IS_DIFFERENT_WITH_KYC);
            }
            //copy 属性
            UserPaymentEntity entity = BeanUtil.toBean( dto, UserPaymentEntity.class);
            //设置用户id
            entity.setUserId( user.getId());
            Boolean bool = paymentService.add( entity);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }catch ( Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 删除支付方式
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public ResponseData delete(@PathVariable("id") Integer id, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //设置参数
            BaseDTO dto = BaseDTO.builder().
                                    state( BizConst.PaymentConst.STATE_FAILED).
                                    userId( user.getId()).
                                    id( id).build();
            //删除
            Boolean bool = paymentService.modify( dto);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 设为默认支付方式
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/priority/{id}")
    public ResponseData priority(@PathVariable("id") Integer id, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            // 登录用户
            UserDTO user = getLoginUser( request);
            //设置参数
            BaseDTO dto = BaseDTO.builder()
                                    .userId( user.getId())
                                    .state( BizConst.PaymentConst.STATE_N).build();
            //将当前用户所有支付方式更新为非默认
            Boolean bool = paymentService.modify( dto);
            if(bool){
                //设置默认支付方式
                dto.setId( id);
                dto.setState( BizConst.PaymentConst.STATE_Y);
                bool = paymentService.modify( dto);
            }
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 上传信息图片
     * @param request
     * @return
     */
    @RequestMapping("/upload")
    public ResponseData upload( HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //文件上传对象
            UploadDTO ud = BizUtils.getUploadPic( request);
            //登录用户
            UserDTO user = getLoginUser(request);
            // 图片上传对象C2C_IMAGE_PATH
            ud.setUserId( user.getId());
            // 上传路径
            ud.setPathType( TypeConst.TYPE_UPLOAD_USER);
            // 上传文件
            HttpResponse response = HttpRequest.post(NetUtils.getUploadUrl())
                                .header("content-type", "application/json")
                                .body(JSON.toJSONString(ud))
                                .execute();
            if ( !IntUtils.equals(response.getStatus(), HttpStatus.HTTP_OK)) {
                throw ExUtils.error( LocaleKey.FILE_UPLOAD_FAILED);
            }
            data.setData(response.body());
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.FILE_UPLOAD_FAILED);
        }
        return data;
    }
    
    /** 
     *  收款信息
     */ 
     @RequestMapping("/Payments")
     public ResponseData queryPayment() {
         ResponseData data = ResponseData.ok();
         try {
             HashMap<String, Object> hMap = new HashMap<String, Object>();

             hMap.put(BizConst.BIZ_MAS_DATA, paymentService.getPayment());

             hMap.put(BizConst.AssetsConst.QUERY_VCODE, RandomUtil.randomNumbers(8));

             data.setData(hMap);
         } catch (Exception ex) {
             ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
         }
         return data;
     }

    /**
     * 获取用户的支付方式
     * @param request
     * @return
     */
     @RequestMapping("/list")
     public ResponseData list(HttpServletRequest request){
         ResponseData data = ResponseData.ok();
         try {
             //登录用户
             UserDTO user = getLoginUser( request);
             //获取用户支持的支付方式
             List<UserPaymentVO> list = paymentService.list( BaseDTO.builder()
                                                .userId( user.getId())
                                                .state( BizConst.PaymentConst.STATE_FAILED).build());
             //返回数据
             data.setData( list);
         }catch (Exception ex){
             ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
         }
         return data;
     }

    /**
     * 获取用户支持的支付方式
     * @param request
     * @return
     */
    @RequestMapping("/type")
    public ResponseData type(HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //获取用户支持的支付方式
            List<String> list = paymentService.type( BaseDTO.builder()
                                        .userId( user.getId())
                                        .state( BizConst.PaymentConst.STATE_FAILED).build());
            //返回数据
            data.setData( list);
        }catch (Exception ex){
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }
    
}
