package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 合约收益VO
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-09 2:04 PM
 **/
@Data
public class ContractProfitVO implements Serializable {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 父级ID
     */
    private Integer parentId;

    /**
     * 日交易额
     */
    private BigDecimal amount;

    /**
     * 级差收益
     */
    private BigDecimal gradationProfit = BigDecimal.ZERO;

    /**
     * 当前用户级别
     */
    private String rank;

    /**
     * 代币
     */
    private String symbol;
}
