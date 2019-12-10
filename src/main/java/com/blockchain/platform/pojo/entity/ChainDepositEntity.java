package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充币流水
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-24 4:11 PM
 **/
@Data
@Entity
@TableName(value = "t_chain_deposit")
public class ChainDepositEntity implements Serializable {

    /**
     * hash
     */
    @Id
    private String hash;

    /**
     * 转出金额
     */
    private BigDecimal amount;

    /**
     * 大类
     */
    private  String coinCode;

    /**
     * 块
     */
    private String module;

    /**
     * 状态
     */
    private String status;

    /**
     * 转入地址
     */
    private String toAddress;

    /**
     * memo
     */
    private String memo;

    /**
     * 交易产生时间
     */
    private Long timestamp;

    /**
     * 生成环境中的充值 orderId
     */
    private String orderId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 确认数
     */
    private Integer confirmNum;
}
