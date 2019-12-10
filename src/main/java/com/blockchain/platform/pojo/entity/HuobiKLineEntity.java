package com.blockchain.platform.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.blockchain.platform.pojo.entity.LuckDrawLogEntity.LuckDrawLogEntityBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_huobi_kline_data")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HuobiKLineEntity extends Model<Model<?>> implements Serializable {
	
	/**
     * id
     */
    @Id
    private Integer id;

    /**
     * 时间
     */
    private Long date;

    /**
     * 最高
     */
    private BigDecimal high;

    /**
     * 最低
     */
    private BigDecimal low;
    
    /**
     * 开盘
     */
    private BigDecimal op;

    /**
     * 收盘
     */
    private BigDecimal cl;

    @Transient
    private BigDecimal close;

    @Transient
    private BigDecimal open;

    /**
     * 交易量
     */
    private BigDecimal volume;

    /**
     *
     */
    private BigDecimal vol;
    
    /**
     * 交易对(BTC_USDT)
     */
    private String symbol;
    
    
    private String interv;
    
    private Long count;
    
    private BigDecimal amount;
    


}
