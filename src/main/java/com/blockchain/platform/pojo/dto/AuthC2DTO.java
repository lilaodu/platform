package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * C2认证DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 3:45 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthC2DTO implements Serializable {

    /**
     * 手持照片
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String picHold;

    /**
     * 证件 正面照
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String picFace;


    /**
     * 证件 背面照片
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String picBack;
}
