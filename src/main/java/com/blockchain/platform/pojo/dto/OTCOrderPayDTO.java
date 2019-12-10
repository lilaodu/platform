package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * OTC订单付款数据传输DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-21 9:07 PM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY )
public class OTCOrderPayDTO implements Serializable {

    /**
     * 订单id
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private Integer id;

    /**
     * 支付方式
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private String payType;

    /**
     * 支付账号
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private String payAccount;

}
