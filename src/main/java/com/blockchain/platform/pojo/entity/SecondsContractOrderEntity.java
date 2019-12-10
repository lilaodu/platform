package com.blockchain.platform.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 秒合约订单实体类
 * @author zhangye
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_seconds_contract_order_flow")
public class SecondsContractOrderEntity extends Model<Model<?>> implements Serializable {

	/**
     * id
     */
	@Id
	@TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Integer id;
    
    /**
     * 下单用户id
     */
    private Integer userId;
    
    /**
     * 竞猜货币对
     * BTC_USDT
     */
    private String coinPair;
    
    /**
     * 押注金额
     */
    private BigDecimal price;
    
    /**
     * 支付钱的类型
     */
    private String payCoin;
    
    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 订单状态（1.已押注未开奖，2.已开奖，3.结算中，4.结算完成(完成打款)
     */
    private Integer state;
    
    /**
     * 选择的时间区
     */
    private Integer section;
    
    /**
     * 时间区的利率
     */
    private BigDecimal profit;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    
    /**
     * 1.买涨
     * 2.买跌
     */
    private Integer type;
    
    /**
     * 1.未知/2.没猜中/3.猜中/4.平
     */
    private Integer isWin;
    
    /**
     * 押注时的价格
     */
    private BigDecimal startPrice;
    
    /**
     * 结束时的价格
     */
    private BigDecimal endPrice;
    
    /**
     * 押注时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date dealingTime;
    
    /**
     * 结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date settlementTime;
    
    /**
     * 我得到的钱
     */
    private BigDecimal winPrice;
    
    
    /**
     * 将押注金额,转换成usdt的价格
     */
    private BigDecimal toUsdt;



}
