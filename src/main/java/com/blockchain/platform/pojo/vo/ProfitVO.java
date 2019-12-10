package com.blockchain.platform.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收益VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 3:09 PM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfitVO implements Serializable {

    /**
     * 代币符号
     */
    private String symbol;

    /**
     *  总量
     */
    private BigDecimal amount;
}
