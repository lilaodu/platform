package com.blockchain.platform.pojo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * K线存储数据
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 6:31 PM
 **/
@Data
@Entity
@TableName("t_ex_kline_data")
public class KlineEntity  extends Model<Model<?>> implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 时间
     */
    private Long t;

    /**
     * 开盘
     */
    private BigDecimal o;

    /**
     * 收盘
     */
    private BigDecimal c;

    /**
     * 最高
     */
    private BigDecimal h;

    /**
     * 最低
     */
    private BigDecimal l;

    /**
     * 交易量
     */
    private BigDecimal v;

    /**
     * 类型
     */
    private Long resolution;

    /**
     * 交易对
     */
    private String symbol;

    /**
     * 创建时间
     */
    private Date createTime;
}
