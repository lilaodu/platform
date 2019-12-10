package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 场外OTC交易DTO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-18 10:58 AM
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutsideDTO implements Serializable {
    /**
     * 购买价格
     */
    private BigDecimal price;

    /**
     * 购买总量
     */
    private BigDecimal number;

    /**
     * 交易密码
     */
    private String password;
    /**
     * 支付方式名称
     */
    private String payName;

    /**
     * 支付方式
     */
    private String payType;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 支付地址
     */
    private String address;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 支付姓名
     */
    private String userName;

    /**
     * 账号
     */
    private String account;
    /**
     * 随机码
     */
    private String vCode;

    /**
     * 付款至账户id
     */
    private Integer paymentId;

    /**
     * 订单类型
     */
    private String type;

    /**
     * 订单生成时间
     */
    private String inDate;

    /**
     * 现在时间
     */
    private String nowTime;

    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 订单id
     */
    private Integer id;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 订单状态
     */
    private String status;
    /**
     * 买单持续时间
     */
    private String buyTime;
    /**
     * 卖单持续时间
     */
    private String sellTime;

    /**
     *
     */
    private Integer adminUserId;

    private String symbol;

    private String addressTo;

    private String addressFrom;

    private String flowType;

    private String phone;

    private String userPhone;

}
