package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 签名DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-30 4:33 PM
 **/
@Data
public class SignDTO implements Serializable {

    /**
     * 当前代币
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String symbol;

    /**
     * 随机数字
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String nonce;


    /**
     * 时间戳
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private Long timestamp;

}
