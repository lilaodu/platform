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
 * 参数配置
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 12:11 AM
 **/
@Data
@Entity
@TableName("t_params")
public class ParamsEntity implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 参数类型
     */
    @Column(name = "param_type")
    private String paramType;

    /**
     * 参数值
     */
    @Column(name = "param_value")
    private String paramValue;

    /**
     * 活动开始时间
     */
    @Column(name = "active_from")
    private Date activeFrom;

    /**
     * 活动结束时间
     */
    @Column(name = "active_to")
    private Date activeTo;

    /**
     * 持有、交易等 排名开始位数
     */
    @Column(name = "hold_from")
    private BigDecimal holdFrom;

    /**
     * 持有、交易等 排名结束位数（包含）
     */
    @Column(name = "hold_to")
    private BigDecimal holdTo;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 备注
     */
    @Column(name = "note")
    private String note;

    /**
     * 标记
     */
    @Column(name = "sign")
    private Integer sign;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;
}