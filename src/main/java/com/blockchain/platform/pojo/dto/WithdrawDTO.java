package com.blockchain.platform.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.blockchain.platform.i18n.LocaleKey;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提币DTO
 *
 * @author zjl
 **/
@Data
public class WithdrawDTO implements Serializable {

    /**
     * 提币代币
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String symbol;

    /**
     * 提币地址
     */
    @NotEmpty
    private String address;

    /**
     * 提币数量
     */
    @NotNull
    private BigDecimal number;

    /**
     * 资金密码
     */
    @NotEmpty(message = LocaleKey.USER_CIPHER_PASSWORD_ERROR)
    private String password;

    /**
     * 短信密码
     */
    @NotEmpty(message = LocaleKey.SMS_CAPTCHA_NOT_NULL)
    private String sms;

    /**
     * 邮箱验证码
     */
    @NotEmpty(message = LocaleKey.MAIL_CAPTCHA_NOT_NULL)
    private  String eml;
}
