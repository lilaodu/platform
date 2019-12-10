package com.blockchain.platform.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UploadDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.UserAdviceEntity;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.pojo.vo.UserAdviceVO;
import com.blockchain.platform.service.IAdviceService;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import com.blockchain.platform.utils.NetUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * 咨询建议控制器
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 1:41 PM
 **/
@RestController
@RequestMapping("/advice")
public class AdviceController extends BaseController {

    /**
     * 咨询建议服务接口
     */
    @Resource
    private IAdviceService adviceService;

    /**
     * 建议-分页列表
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
            UserDTO user = getLoginUser( request);
            //设置用户id
            dto.setUserId( user.getId());
            //获取当前用户的咨询建议
            //开始分页
            Page<UserAdviceVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //列表查询
            List<UserAdviceVO> list = adviceService.query( dto);
            //返回数据
            data.setData( PageVO.builder()
                                    .total( page.getTotal())
                                    .list( list).build());
        } catch (Exception ex) {
        	throw ExUtils.error(LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 新增咨询
     * @param entity
     * @param valid
     * @param request
     * @return
     */
    @RequestMapping("/modify")
    public ResponseData modify(@RequestBody @Valid UserAdviceEntity entity, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if(valid.hasErrors()){
                throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
            }
            //设置咨询类型为文本类型
            entity.setType(BizConst.AdviceConst.ADVICE_TYPE_TEXT);
            //登录用户
            UserDTO user = getLoginUser( request);
            //设置用户id
            entity.setUserId( user.getId());
            //设置咨询状态
            entity.setState( BizConst.AdviceConst.ADVICE_STATE_APPLY);
            //获取最后一次有效会话
            UserAdviceEntity lastAdvice = adviceService.findUserLastAdvice( BaseDTO.builder()
                                                                        .state( BizConst.AdviceConst.ADVICE_STATE_APPLY)
                                                                        .userId( user.getId()).build());
            //判断是否是第一次发起会话
            if(ObjectUtil.isEmpty( lastAdvice)){
                //生成新的咨询单号
                entity.setBillNo(StrUtil.toString(DateUtil.current( Boolean.TRUE)));
            }else {
                //同一个会话
                entity.setBillNo( lastAdvice.getBillNo());
            }
            //新增或编辑
            Boolean bool = adviceService.modify( entity);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }catch (Exception ex){
        	throw ExUtils.error(LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 上传图片
     * @param request
     * @return
     */
    @RequestMapping("/upload")
    public ResponseData upload(HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //获取上传对象
            UploadDTO dto = BizUtils.getUploadPic( request);
            // 登录用户
            UserDTO user = getLoginUser( request);
            // 图片上传人
            dto.setUserId( user.getId());
            // 图片上传路径
            dto.setPathType( AppConst.BAK_IMAGE_PATH);
            // 上传文件
            HttpResponse response = HttpRequest.post(NetUtils.getUploadUrl())
                                        .header("content-type", AppConst.RESPONSE_CONTENT_TYPE)
                                        .body(JSON.toJSONString( dto))
                                        .execute();

            if ( !IntUtils.equals(response.getStatus(), HttpStatus.HTTP_OK)) {
                throw ExUtils.error( LocaleKey.FILE_UPLOAD_FAILED);
            }
            //会话实体
            UserAdviceEntity entity = UserAdviceEntity.builder()
                                            .type( BizConst.AdviceConst.ADVICE_TYPE_IMG)
                                            .content( response.body()).build();
            // 获取用户的上一次有效对话
            UserAdviceEntity lastAdvice = adviceService.findUserLastAdvice( BaseDTO.builder()
                                                .state( BizConst.AdviceConst.ADVICE_STATE_APPLY)
                                                .userId( user.getId()).build());
            //设置用户id
            entity.setUserId( user.getId());
            //会话状态
            entity.setState( BizConst.AdviceConst.ADVICE_STATE_APPLY);
            // 第一次发起会话
            if (ObjectUtil.isEmpty( lastAdvice)) {
                //生成新的咨询单号
                entity.setBillNo(StrUtil.toString(DateUtil.current( Boolean.TRUE)));
            } else {
                //同一个会话
                entity.setBillNo( lastAdvice.getBillNo());
            }
            Boolean bool = adviceService.modify( entity);
            if ( !bool) {
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        } catch (Exception ex) {
        	throw ExUtils.error(LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
}
