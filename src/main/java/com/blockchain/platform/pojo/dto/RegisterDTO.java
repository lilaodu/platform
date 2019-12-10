package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

import com.blockchain.platform.annotation.MobileVerify;
import com.blockchain.platform.i18n.LocaleKey;

import java.io.Serializable;

/**
 * 注册用户传输DTO
 *
 * @author zjl
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO implements Serializable {

	/**
     * 电话号码或邮箱
     */
    @MobileVerify(message = LocaleKey.SYS_PARAM_ERROR)
    private String username;//账号

    /**
     * 登录密码
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String password;


    /**
     * 邀请码
     */
//    @NotEmpty(message = LocaleKey.USER_INVITATION_CODE_NOT_NULL)
    private String invitationCode;

    /**
     * 短信验证码
     */
    @NotEmpty(message = LocaleKey.USER_CODE_NOT_NULL)
    private String code;

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
     * 注册类型
     */
    private String type;
    
    /**
     * 网易验证标识
     */
    private String validate;
    
}
