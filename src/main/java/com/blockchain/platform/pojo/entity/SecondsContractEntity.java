package com.blockchain.platform.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 秒合约配置类
 * @author zhangye
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@TableName("t_seconds_contract_config")
public class SecondsContractEntity extends Model<Model<?>> implements Serializable {
	
	/**
     * id
     */
	@Id
	@TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Integer id;
    
    /**
     * 交易对中前者的数量选择
     */
    private String coinNums;
    
    /**
     * 交易对中后者的数量选择
     */
    private String marketNums;
    
    /**
     * 时间区间选择
     */
    private String sections;
    
    /**
     * 利率%与时间对应
     */
    private String profit;
    
    /**
     * 竞猜货币对
     * BTC_USDT
     */
    private String coinPair;
    
    /**
     * 版本号
     */
    private Integer version;
    
    /**
     * 有效状态
     */
    private Integer state;
    
    /**
     * 可支付的币种(ETH_USDT_BTC_....)
     */
    private String symbol;
    
    /**
     * 开盘时间
     */
    private Date openTime;



}
