package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 资产划转传输对象
 * @author: zjl
 **/
@Data
public class TransferDTO implements Serializable {

    /**
     * 划转来源
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String accountFrom;

    /**
     * 划转至
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String accountTo;

    /**
     * 划转金额
     */
    @Min(value = 0, message = LocaleKey.SYS_PARAM_ERROR)
    private BigDecimal amount;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 货币类型
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String symbol;

    /**
     * 签名
     */
    @NotBlank(message = LocaleKey.SIGNATURE_NOT_NULL)
    private String signature;

    /**
     * 随机串
     */
    @NotBlank(message = LocaleKey.SIGNATURE_NOT_NULL)
    private String nonce;
}
