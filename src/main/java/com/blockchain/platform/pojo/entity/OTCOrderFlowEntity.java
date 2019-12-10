package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * otc订单记录实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 4:03 PM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_otc_order_flow")
public class OTCOrderFlowEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 商户的用户id
     */
    private  Integer merchantId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 代币符号
     */
    private String symbol;

    /**
     * 订单用户钱包变更前
     */
    private BigDecimal userWalletBefore;

    /**
     * 订单用户钱包变更后
     */
    private  BigDecimal userWalletAfter;

    /**
     * 商户钱包变更前
     */
    private  BigDecimal merchantWalletBefore;

    /**
     * 商户钱包变更后
     */
    private  BigDecimal merchantWalletAfter;

    /**
     * 流水类型
     */
    private String type;

    /**
     * 创建时间
     */
    private Date createTime;
}
