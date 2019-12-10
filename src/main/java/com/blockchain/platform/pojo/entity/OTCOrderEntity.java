package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.blockchain.platform.utils.BigDecimalUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 广告订单实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 12:06 AM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_otc_order")
public class OTCOrderEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 订单类型（卖，买）
     */
    private String type;

    /**
     * 数量
     */
    private BigDecimal num;

    /**
     * 报价
     */
    private BigDecimal price;

    /**
     * 订单编号
     */
    @Column(name = "order_number")
    private String orderNumber;

    /**
     * 交易状态（已下单，已取消，收款，评论，删除）
     */
    @Column(name = "state")

    private Integer state;

    /**
     * 订单所属用户
     */
    @Column(name = "user_id")

    private  Integer userId;

    /**
     * 金额
     */
    @Column(name = "amount")

    private BigDecimal amount;

    /**
     * 广告id
     */
    @Column(name = "advert_id")

    private Integer advertId;

    /**
     * 货币简称
     */
    @Column(name = "symbol")

    private String symbol;

    /**
     * 汇率
     */
    @Column(name = "rate")

    private String rate;

    /**
     * 手续费
     */
    @Column(name = "fee_rate")

    private BigDecimal feeRate;

    /**
     * 打分(评论)
     */
    @Column(name = "opinion")

    private String opinion;

    /**
     * 付款方式
     */
    @Column(name = "pay_type")

    private  String payType;

    /**
     * 付款账号
     */
    @Column(name = "pay_account")

    private String payAccount;
    /**
     * 收款
     */
    @Column(name = "receive_account")

    private String receiveAccount;

    /**
     * 备注码
     */

    @Column(name = "remark_code")

    private  String remarkCode;

    /**
     * 版本号
     */
    @Column(name = "version")

    private Integer version;

    /**
     * 创建时间
     */
    @Column(name = "create_time")

    private Date createTime;

    /**
     * 成交时间
     */
    @Column(name = "deal_time")
    private Date dealTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 商户ID
     */
    @Column(name = "merchant_id")

    private Integer merchantId;


    @Column(name="receive_address")
    String receiveAddress;
}
