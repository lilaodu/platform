package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * C1认证DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 3:44 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthC1DTO implements Serializable {

    /**
     * 姓氏
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String lastName;

    /**
     * 名字
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String firstName;

    /**
     * 国家
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private String country;

    /**
     * 证件类型
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private String idType;

    /**
     * 证件号码
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String idNo;
}
