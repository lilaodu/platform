package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.annotation.MobileVerify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 发送验证码DTO
 *
 * @author zjl
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CaptchaDTO implements Serializable {

    /**
     * 电话号码或邮箱
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String username;//参数

    /**
     * 验证码类型(电话号码，邮箱)
     */
    private String type;

    /**
     * 验证方法（登录，注册）
     */
    private String method;
}
