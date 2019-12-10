package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * OTC 聊天数据传输DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-24 10:34 AM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OTCChatDTO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 订单编号
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String orderNumber;

    /**
     * 广告编号
     */
    private String advertNumber;

    /**
     * 内容
     */
    @NotBlank(message = LocaleKey.OTC_CHAT_CONTENT_NULL)
    private String content;
}
