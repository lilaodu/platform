package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 提币流水
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-24 4:11 PM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_chain_withdraw")
public class ChainWithdrawEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 大类
     */
    @Column(name = "coin_code")

    private  String coinCode;

    /**
     * 块
     */
    @Column(name = "module")

    private String module;

    /**
     * 用户id
     */
    @Column(name = "user_id")

    private Integer userId;

    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 数量
     */
    @Column(name = "number")

    private BigDecimal number;

    /**
     * 状态
     */
    @Column(name = "status")

    private String status;

    /**
     * memo
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 费率
     */
    @Column(name = "fee")

    private BigDecimal fee;

    /**
     * 真实费率
     */
    @Column(name = "real_fee")
    private BigDecimal realFee;

    /**
     * hash
     */
    @Column(name = "hash")

    private String hash;

    /**
     * 手续费大类
     */
    @Column(name = "fee_coin_code")

    private String feeCoinCode;

    /**
     * 错误
     */
    @Column(name = "error")

    private String error;

    /**
     * 时间
     */
    @Column(name = "time")

    private long time;
}
