package com.blockchain.platform.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.blockchain.platform.i18n.LocaleKey;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充值DTO
 *
 * @author zjl
 **/
@Data
public class DepositDTO implements Serializable {

    /**
     * 提币地址
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String symbol;


    /**
     * 提币数量
     */
    @NotNull
    private BigDecimal number;

}
