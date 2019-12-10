package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 直推用户VO
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-09 5:12 PM
 **/
@Data
public class DirectUserVO implements Serializable {


    /**
     * 直推人数
     */
    private Integer num;


    /**
     * 锁仓金额
     */
    private BigDecimal amount;

}
