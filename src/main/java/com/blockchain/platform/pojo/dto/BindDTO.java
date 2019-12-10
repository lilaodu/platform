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
 * 绑定邮箱/电话DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-11 8:38 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BindDTO implements Serializable {

    /**
     * 新账号
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private String username;

    /**
     * 新账号验证码
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String code;

    /**
     * 类型（邮箱，电话）
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String type;

}
