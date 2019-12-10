package com.blockchain.platform.pojo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户解锁奖励
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-07 4:59 PM
 **/
@Data
@Entity
@Table(name = "t_user_lock_profit")
public class UserLockProfitEntity implements Serializable {

    @Id
    private Integer id;

    /**
     * 奖励货币
     */
    private String symbol;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 产生订单
     */
    private String productId;

    /**
     * 产生奖励用户
     */
    private Integer productUserId;

    /**
     * 本次奖励余额
     */
    private BigDecimal amount;


    /**
     * 创建时间
     */
    private Date createTime;
}
