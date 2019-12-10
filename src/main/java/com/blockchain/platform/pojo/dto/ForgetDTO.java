package com.blockchain.platform.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import com.blockchain.platform.i18n.LocaleKey;

import java.io.Serializable;

/**
 * 忘记密码DTO
 *
 * @author zjl
 **/
@Data
public class ForgetDTO implements Serializable {

	/**
     * 电话号码或邮箱
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String username;//参数
    /**
     * 短信验证码
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String code;

    /**
     * 登录密码
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String password;

    /**
     * 极验验证状态
     */
    private Integer gt_server_status;

    /**
     * txHash
     */
    private String txHash;

    /**
     * 验证密钥
     */
    private String geetest_seccode;

    /**
     * 验证标识
     */
    private String geetest_validate;

    /**
     * 质疑值
     */
    private String geetest_challenge;
    
    /**
     * 网易验证标识
     */
    private String validate;
}
