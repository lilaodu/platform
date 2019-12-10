package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 登录的DTO
 * @author zjl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO implements Serializable {
    /**
     * 电话号码或邮箱
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String username;

    /**
     * 登录密码
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String password;

    /**
     * 登录方式（邮箱，电话）
     */
    private String type;

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
