package com.blockchain.platform.pojo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.blockchain.platform.pojo.entity.OrderFlowEntity.OrderFlowEntityBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 交易流水实体
 *
 * @author zhangye
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeFlowEntity extends Model<Model<?>> implements Serializable {
	
	@Id
	@TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private int id;

    /**
     *交易价格
     */
    private BigDecimal price;

    /**
     * 交易数量
     */
    private BigDecimal num;

    /**
     * 交易总价
     */
    private BigDecimal totalPrice;

    /**
     * 交易时间
     */
    private long dealTime;
    
    /**
     * 流水类型 买入/卖出
     */
    private String type;

    /**
     * 购买用户ID
     */
    private int buyUserId;

    /**
     * 购买订单ID
     */
    private int buyId;

    /**
     * 出售用户ID
     */
    private int sellUserId;

    /**
     * 出售订单ID
     */
    private int sellId;

    /**
     * 买家手续费费率
     */
    private BigDecimal buyFee;

    /**
     * 买家手续费
     */
    private BigDecimal buyCharge;
    
    /**
     * 卖家手续费费率
     */
    private BigDecimal sellFee;

    /**
     * 卖家手续费
     */
    private BigDecimal sellCharge;
    
    /**
     * 交易对
     */
    private String coinPair;

    private Date createDate;

    private Integer version;

    private String state;

    public TradeFlowEntity(BigDecimal price, String type, int buyUserId, int buyId, int sellUserId, int sellId,BigDecimal buyFee,BigDecimal sellFee) {
        this.price = price;
        this.type = type;
        this.buyUserId = buyUserId;
        this.buyId = buyId;
        this.sellUserId = sellUserId;
        this.sellId = sellId;
        this.buyFee = buyFee;
        this.sellFee = sellFee;
    }

}
