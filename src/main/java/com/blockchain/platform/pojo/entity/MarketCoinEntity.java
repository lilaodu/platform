package com.blockchain.platform.pojo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 市场货币信息
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 12:11 PM
 **/
@Data
@Entity
@TableName("t_market_coin")
public class MarketCoinEntity implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 货币
     */
    @Column(name = "market")
    private String market;

    /**
     * 表名
     */
    @Column(name = "table_name")
    private String tableName;

    /**
     * 货币简称
     */
    @Column(name = "symbol")
    private String symbol;

    /**
     * 比例
     */
    @Column(name = "rate")
    private BigDecimal rate;

    /**
     * 通知分发ws url
     */
    @Column(name = "ws_url")
    private String wsUrl;

    /**
     * 撮合分发url
     */
    @Column(name = "match_url")
    private String matchUrl;

    /**
     * 排序
     */
    @Column(name = "sn")
    private Integer sn;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;

    /**
     * 记录时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 开盘时间
     */
    @Column(name = "day_open")
    private String dayOpen;
    
    /**
     * 是否是新币
     */
    @Column(name = "is_new")
    private String isNew;



    //获取交易对名称
    public String selectPairName()
    {
        return symbol+"_"+market;

    }

    
}