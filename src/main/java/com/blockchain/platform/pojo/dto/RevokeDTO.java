package com.blockchain.platform.pojo.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.blockchain.platform.i18n.LocaleKey;

import java.io.Serializable;

/**
 * 撤单DTO
 *
 * @author zhangye
 **/
@Data
public class RevokeDTO implements Serializable {

    /**
     * 订单号
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private Integer id;

    /**
     * 交易对
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String symbol;

    /**
     * 'BUY' : 'SELL'
     */
    private String type;
}
