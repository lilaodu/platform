package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 我的团队显示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-26 11:36 AM
 **/
@Data
public class TeamVO implements Serializable {

    /**
     * 代币符号
     */
    private String symbol;

    /**
     *  总量
     */
    private BigDecimal amount;
}
