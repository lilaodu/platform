package com.blockchain.platform.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.TypeConst;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.UserKycEntity;
import com.blockchain.platform.pojo.vo.UserKycVO;
import com.blockchain.platform.service.IKycService;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import com.blockchain.platform.utils.NetUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 实名认证
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 3:39 PM
 **/
@RestController
@RequestMapping("/kyc")
public class KycController extends BaseController {

    /**
     * kyc 接口
     */
    @Resource
    private IKycService kycService;

    /**
     * c1 认证
     * @param dto
     * @param valid
     * @param request
     * @return
     */
    @RequestMapping("/authC1")
    public ResponseData authC1(@RequestBody AuthC1DTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid.getFieldError().getDefaultMessage());
            }
            //登录用户
            UserDTO user = getLoginUser( request);
            //获取用户认证详情
            UserKycEntity entity = kycService.findByCondition(BaseDTO.builder()
                                                            .userId( user.getId())
                                                            .state( BizConst.KycConst.K1_NO).build());
            //copy 用户认证信息
            if ( ObjectUtil.isNotEmpty( entity)) {
                BeanUtil.copyProperties( dto, entity);
            } else {
                entity = BeanUtil.toBean( dto, UserKycEntity.class);
            }
            //k1 默认通过
            entity.setState( BizConst.KycConst.K1_OK);
            //设置用户id
            entity.setUserId( user.getId());
            //C1认证
            Boolean bool = kycService.modify( entity);
            if ( !bool) {
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        } catch (Exception ex) {
            if ( ex instanceof DuplicateKeyException) {
                ExUtils.error( ex, LocaleKey.SYS_SUBMIT_PROGRESS);
            } else {
                ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
            }
        }
        return data;
    }


    /**
     * c2 认证
     * @param dto
     * @param valid
     * @param request
     * @return
     */
    @RequestMapping("/authC2")
    public ResponseData authC2(@RequestBody AuthC2DTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid.getFieldError().getDefaultMessage());
            }
            //登录用户
            UserDTO user = getLoginUser( request);
            //获取用户认证详情
            UserKycEntity entity = kycService.findByCondition(BaseDTO.builder()
                                                                    .userId( user.getId())
                                                                    .state( BizConst.KycConst.K2_OK).build());
            //判断用户是否经过C1认证
            if ( ObjectUtil.isEmpty( entity)) {
                throw ExUtils.error( LocaleKey.USER_C1_NOT_ACCESS);
            }
            // 复制数据
            BeanUtil.copyProperties( dto, entity);
            // 提交审核
            entity.setState( BizConst.KycConst.K2_SUB);
            //C2认证
            Boolean bool = kycService.modify( entity);
            if ( !bool) {
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }


    /**
     * 上传图片
     * @param
     * @return
     * @throws Exception 
     */
    @RequestMapping("/upload")
    public ResponseData upload(@RequestParam Map map, HttpServletRequest request) throws Exception{
        ResponseData data = ResponseData.ok();
//        try {
            //图片对象
            PicDTO pic = BeanUtil.toBean( map, PicDTO.class);
            //登录用户
            UserDTO user = getLoginUser(request);
            // 图片内容，图片类型
            UploadDTO ud = BizUtils.getUploadPic( request);
            // 查询用户认证详情
            UserKycEntity entity = kycService.findByCondition( BaseDTO.builder()
                                                    .userId(user.getId())
                                                    .state(BizConst.KycConst.K2_OK).build());
            //判断用户是否经过认证
            if ( ObjectUtil.isEmpty( entity)) {
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
            // 图片上传人
            ud.setUserId( user.getId());
            // 图片上传路径
            ud.setPathType( TypeConst.TYPE_UPLOAD_KYC);
            // 上传文件
            HttpResponse response = HttpRequest.post(NetUtils.getUploadUrl())
                                        .header("content-type", "application/json")
                                        .body(JSON.toJSONString(ud))
                                        .execute();

            // 提示文件上传失败
            if (!IntUtils.equals(response.getStatus(), HttpStatus.HTTP_OK)) {
                throw ExUtils.error( LocaleKey.FILE_UPLOAD_FAILED);
            }
            // 设置数据
            if ( StrUtil.isNotEmpty( entity.getPicBack())) {
                entity.setPicBack( StrUtil.EMPTY);
            }
            if ( StrUtil.isNotEmpty( entity.getPicFace())) {
                entity.setPicFace( StrUtil.EMPTY);
            }
            if ( StrUtil.isNotEmpty( entity.getPicHold())) {
                entity.setPicHold( StrUtil.EMPTY);
            }
            // 本次修改数据
            BeanUtil.setFieldValue( entity, pic.getName(), response.body());
            //C2认证
            Boolean bool = kycService.modify( entity);
            if( !bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
            //返回数据
            data.setData(response.body());
//        } catch (Exception ex){
//            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
//        }
        return data;
    }


    /**
     * 用户KYC情况
     * @return
     */
    @RequestMapping("/details")
    public ResponseData details(HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO userDTO = getLoginUser(request);
            // 用户KYC情况
            UserKycEntity entity = kycService.findByCondition( BaseDTO.builder()
                                                                    .userId(userDTO.getId())
                                                                    .state(BizConst.KycConst.STATUS_FAILED).build());
            if ( ObjectUtil.isNotEmpty( entity)) {
                //返回数据
                data.setData( BeanUtil.toBean( entity, UserKycVO.class));
            } else {
                // 尚未进行KYC认证
                data.setData( Boolean.FALSE);
            }
        } catch (Exception ex){
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED );
        }
        return data;
    }
}
