package com.blockchain.platform.pojo.entity;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户合约收益记录
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-09 3:54 PM
 **/
@Data
public class UserTradeProfitEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 产生ID
     */
    private Integer productUserId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 收益类型
     * 1、合约交易
     * 2、币币交易
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 状态
     */
    private Integer state;

    /**
     * TOKEN
     */
    private String symbol;
}
