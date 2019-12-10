package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

import com.blockchain.platform.i18n.LocaleKey;

import java.io.Serializable;

/**
 * 自选货币DTO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-16 11:34 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavCoinDTO implements Serializable{

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 交易对
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String symbol;

    /**
     * 交易市场
     */
    private String market;

}
