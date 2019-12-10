package com.blockchain.platform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.OTCChatEntity;
import com.blockchain.platform.pojo.entity.OTCChatTemplateEntity;
import com.blockchain.platform.pojo.vo.OTCChatTemplateVO;
import com.blockchain.platform.pojo.vo.OTCChatVO;

import java.util.List;

/**
 * otc聊天服务接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 4:14 PM
 **/
public interface IOTCChatService extends IService<OTCChatEntity> {

    /**
     * 聊天消息列表
     * @param dto
     * @return
     */
    List<OTCChatVO> query(BaseDTO dto);

    /**
     * 发送聊天消息
     * @param entity
     * @return
     */
    Boolean send(OTCChatEntity entity);

    /**
     * 新增或编辑聊天模板消息
     * @param entity
     * @return
     */
    Boolean modify(OTCChatTemplateEntity entity);

    /**
     * 获取模板消息列表
     * @param dto
     * @return
     */
    List<OTCChatTemplateVO> queryTemplate(PageDTO dto);

    /**
     * 用户未读消息
     * @param dto
     * @return
     */
    Integer countUnRead(BaseDTO dto);
}
