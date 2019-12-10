package com.blockchain.platform.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.OTCChatDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.OTCAdvertEntity;
import com.blockchain.platform.pojo.entity.OTCChatEntity;
import com.blockchain.platform.pojo.entity.OTCChatTemplateEntity;
import com.blockchain.platform.pojo.entity.OTCOrderEntity;
import com.blockchain.platform.pojo.vo.OTCChatTemplateVO;
import com.blockchain.platform.pojo.vo.OTCChatVO;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.pojo.vo.UnreadVO;
import com.blockchain.platform.service.IOTCAdvertService;
import com.blockchain.platform.service.IOTCChatService;
import com.blockchain.platform.service.IOTCOrderService;
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
import java.util.List;


/**
 * OTC 聊天控制器
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 4:12 PM
 **/
@RestController
@RequestMapping("/otc/chat")
public class OTCChatController extends BaseController {

    /**
     * otc 聊天服务
     */
    @Resource
    private IOTCChatService chatService;

    /**
     * otc订单 服务
     */
    @Resource
    private IOTCOrderService orderService;

    /**
     * otc 广告服务
     */
    @Resource
    private IOTCAdvertService advertService;

    /**
     * 用户未读消息
     * @param request
     * @return
     */
    @RequestMapping("/unread")
    public ResponseData unread(HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            UserDTO user = getLoginUser( request);
            Integer count = chatService.countUnRead( BaseDTO.builder().userId( user.getId()).build());
            // 未读消息
            UnreadVO vo = new UnreadVO();
            vo.setCount( count);
            data.setData( vo);
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 聊天消息列表
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/query")
    public ResponseData query(@RequestBody BaseDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //获取聊天记录列表
            List<OTCChatVO> list = chatService.query( dto);
            //返回信息
            data.setData( list);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 发送
     * @param dto
     * @return
     */
    @RequestMapping("/send")
    public ResponseData send(@RequestBody @Valid OTCChatDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if( valid.hasErrors()){
                throw ExUtils.error( valid);
            }
            //登录用户
            UserDTO user = getLoginUser( request);
            //获取当前聊天的订单
            OTCOrderEntity order = orderService.findByCondition( BaseDTO.builder()
                                        .orderNumber( dto.getOrderNumber()).build());
            if(ObjectUtil.isEmpty( order)){
                throw ExUtils.error( LocaleKey.OTC_ORDER_NOT_FIND);
            }
            //获取广告详情
            OTCAdvertEntity advert = advertService.detail( BaseDTO.builder()
                                                    .id( order.getAdvertId()).build());
            //发送消息
            OTCChatEntity entity = BeanUtil.toBean( dto, OTCChatEntity.class);
            //用户id
            entity.setUserId( user.getId());
            //广告编号
            entity.setAdvertNumber( StrUtil.isEmpty(advert.getAdvertNumber()) ? StrUtil.EMPTY : advert.getAdvertNumber());
            //状态
            if(IntUtils.equals( order.getUserId(), user.getId())) {
                entity.setState( BizConst.ChatConst.STATE_MERCHANT_UNREAD);
            } else {
                entity.setState( BizConst.ChatConst.STATE_USER_UNREAD);
            }

            Boolean bool = chatService.send( entity);
            if( !bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 聊天模板消息列表
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/template")
    public ResponseData template(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //设置用户id
            dto.setUserId( user.getId());
            //有效的模板消息
            dto.setState(BizConst.BIZ_STATUS_VALID);
            //开始分页
            Page<OTCChatTemplateVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //获取列表信息
            List<OTCChatTemplateVO> list = chatService.queryTemplate( dto);
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
     * 新增聊天模板消息
     * @param entity
     * @return
     */
    @RequestMapping("/template/add")
    public ResponseData addTemplate(@RequestBody @Valid OTCChatTemplateEntity entity, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //设置用户id
            entity.setUserId( user.getId());
            //新增
            Boolean bool = chatService.modify( entity);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 删除模板消息
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/template/delete/{id}")
    public ResponseData deleteTemplate(@PathVariable("id") Integer id, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //初始化参数
            OTCChatTemplateEntity entity = OTCChatTemplateEntity.builder()
                                                .id( id)   //id
                                                .userId( user.getId())  //用户id
                                                .state( BizConst.BIZ_STATUS_FAILED).build();  //模板消息状态
            Boolean bool = chatService.modify( entity);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

}
