package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.TypeConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.WorkOrderDetailsEntity;
import com.blockchain.platform.pojo.entity.WorkOrderEntity;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.pojo.vo.WorkOrderVO;
import com.blockchain.platform.service.IWorkOrderService;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import com.blockchain.platform.utils.NetUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 工单控制器
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-26 2:37 PM
 **/
@RestController
@RequestMapping("/work")
public class WorkOrderController extends BaseController {

    /**
     * 工单服务接口
     */
    @Resource
    private IWorkOrderService workOrderService;

    /**
     * 我的工单
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/query")
    public ResponseData query(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //开始分页
            Page<WorkOrderVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //列表
            List<WorkOrderVO> list = workOrderService.query( dto);
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
     * 提交工单
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/modify")
    public ResponseData modify(@RequestBody WorkOrderDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //copy属性
            WorkOrderEntity entity = BeanUtil.toBean( dto, WorkOrderEntity.class);
            //用户是否有未完结的工单
            WorkOrderEntity work = workOrderService.findByCondition( BaseDTO.builder()
                                                    .userId( user.getId())
                                                    .state(BizConst.BIZ_STATUS_VALID).build());
            if(ObjectUtil.isNotEmpty( work)){
                throw ExUtils.error( LocaleKey.USER_WORK_NOT_END);
            }
            //用户id
            entity.setUserId( user.getId());
            //提交
            Boolean bool = workOrderService.modify( entity);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 证明材料上传
     * @param request
     * @return
     */
    @RequestMapping("/material")
    public ResponseData upload(HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //文件上传对象
            UploadDTO ud = BizUtils.getUploadPic( request);

            // 图片上传对象，备份文件路径
            ud.setUserId( user.getId());
            // 上传路径
            ud.setPathType( TypeConst.TYPE_UPLOAD_OTHER);
            // 上传文件
            HttpResponse response = HttpRequest.post(NetUtils.getUploadUrl())
                                        .header("content-type", "application/json")
                                        .body(JSON.toJSONString(ud))
                                        .execute();
            if ( !IntUtils.equals(response.getStatus(), HttpStatus.HTTP_OK)) {
                throw ExUtils.error( LocaleKey.FILE_UPLOAD_FAILED);
            }
            data.setData( response.body());
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.FILE_UPLOAD_FAILED);
        }
        return data;
    }

    /**
     * 回复
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/reply")
    public ResponseData reply(@RequestBody WorkOrderDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //copy属性
            WorkOrderDetailsEntity entity = BeanUtil.toBean( dto, WorkOrderDetailsEntity.class);
            //用户id
            entity.setUserId( user.getId());
            //回复
            Boolean bool = workOrderService.reply( entity);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
}
