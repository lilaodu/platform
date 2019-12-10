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
public class OTCMerchantDTO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 商户昵称
     */
    private String nickName;

    /**
     * 申请原因
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String reason;

}
