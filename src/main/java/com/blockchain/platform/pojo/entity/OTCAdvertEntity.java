package com.blockchain.platform.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.baomidou.mybatisplus.annotation.TableName;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * otc广告实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 11:48 AM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_otc_advert")
public class OTCAdvertEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 广告类型（买，卖）
     */
   @Column(name = "type")
    private String type;

    /**
     * 广告编号
     */
    @Column(name = "advert_Number")

    private String advertNumber;

    /**
     * 价格
     */
    @Column(name = "price")

    private BigDecimal price;

    /**
     * 限额（上限）
     */
    @Column(name = "limit_up")

    private BigDecimal limitUp;

    /**
     * 限额（下限）
     */
    @Column(name = "limit_down")

    private BigDecimal limitDown;

    /**
     * 数量
     */
    @Column(name = "num")

    private BigDecimal num;

    /**
     * 剩余数量
     */
    @Column(name = "surplus")

    private BigDecimal surplus;

    /**
     * 货币简称
     */
    @Column(name = "symbol")

    private String symbol;

    /**
     * 付款方式
     */
    @Column(name = "pay_type")

    private String payType;

    /**
     * 状态(1:上架，2:下架,0:删除)
     */
    @Column(name = "state")

    private Integer state;

    /**
     * 留言备注
     */
    @Column(name = "remark")

    private String remark;

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
     * 发布广告用户id
     */
    @Column(name = "user_id")

    private Integer userId;

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
     * 商户昵称
     */
    @TableField(exist = false)
    private String nickName = StrUtil.EMPTY;

    /**
     * 头像
     */
    @TableField(exist = false)
    private String headImg = StrUtil.EMPTY;




}
