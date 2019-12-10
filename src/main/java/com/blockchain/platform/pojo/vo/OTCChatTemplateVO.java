package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * otc聊天模板VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 12:24 AM
 **/
@Data
public class OTCChatTemplateVO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 内容
     */
    private String content;

}
