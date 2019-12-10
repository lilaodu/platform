package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户支付方式配置
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 12:11 PM
 **/
@Data
@Entity
@TableName(value = "t_user_payment")
public class UserPaymentEntity implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 支付方式
     */
    @Column(name = "pay_type")
    private String payType;

    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 记录时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 账号
     */
    @Column(name = "account")
    private String account;

    /**
     * 用户名称
     */
    @Column(name = "username")
    private String username;

    /**
     * 银行名称
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 是否管理员
     */
    @Column(name = "is_admin")
    private String isAdmin;

    /**
     * 支付人
     */
    @Column(name = "pay_name")
    private String payName;



}