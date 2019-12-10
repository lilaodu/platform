package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 密码修改DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 10:51 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PwdDTO implements Serializable {

    /**
     * 老密码
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private String password;

    /**
     * 新密码
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private String newPassword;

    /**
     * 再次输入密码
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private String againPassword;

    /**
     * 手机验证码
     */
    private String sms;

    /**
     * 邮箱验证码
     */
    private  String eml;

}
