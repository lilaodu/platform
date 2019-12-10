package com.blockchain.platform.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.OTCChatEntity;
import com.blockchain.platform.pojo.entity.OTCChatTemplateEntity;
import com.blockchain.platform.pojo.vo.OTCChatTemplateVO;
import com.blockchain.platform.pojo.vo.OTCChatVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * otc 聊天接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 4:18 PM
 **/
@Mapper
public interface OTCChatMapper extends BaseMapper<OTCChatEntity> {

    /**
     * 聊天消息列表
     * @param dto
     * @return
     */
    List<OTCChatVO> query(BaseDTO dto);

    /**
     * 新增聊天消息
     * @param entity
     * @return
     */
    Integer add(OTCChatEntity entity);

    /**
     * 新增模板消息
     * @param entity
     * @return
     */
    Integer addTemplate(OTCChatTemplateEntity entity);

    /**
     * 编辑模板消息
     * @param entity
     * @return
     */
    Integer updateTemplate(OTCChatTemplateEntity entity);

    /**
     * 获取模板消息列表
     * @param dto
     * @return
     */
    List<OTCChatTemplateVO> queryTemplate(PageDTO dto);

    /**
     * 未读消息
     * @param dto
     * @return
     */
    Integer countUnRead(BaseDTO dto);

    /*
     * 修改聊天状态
     * @param dto
     * @return
     */
    Integer modify(BaseDTO dto);
}
