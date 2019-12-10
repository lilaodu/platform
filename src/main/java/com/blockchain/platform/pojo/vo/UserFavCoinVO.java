package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户自选货币 展示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-08 3:05 PM
 **/
@Data
public class UserFavCoinVO implements Serializable {

    /**
     * 货币
     */
    private String symbol;

    /**
     * 交易市场
     */
    private String market;
}
