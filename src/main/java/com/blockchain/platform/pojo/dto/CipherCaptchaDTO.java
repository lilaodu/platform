package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 资金密码修改验证码
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 1:07 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CipherCaptchaDTO implements Serializable {

    /**
     * 原资金密码
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String password;

    /**
     * 类型
     */
    private String type;

}
