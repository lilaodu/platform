package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

import com.blockchain.platform.i18n.LocaleKey;

import java.io.Serializable;

/**
 * 交易授权
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-24 1:39 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeAuthDTO implements Serializable {

    /**
     * 资金密码
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String cipher;

}
