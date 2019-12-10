package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户支付方式显示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 5:34 PM
 **/
@Data
public class UserPaymentVO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 支付名称
     */
    private String payName;

    /**
     * 支付账号
     */
    private String account;

    /**
     * 地址
     */
    private String address;

    /**
     * 用户名
     */
    private String username;

    /**
     * 银行名称
     */
    private  String bankName;

    /**
     * 状态
     */
    private Integer state;
}
