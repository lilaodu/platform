package com.blockchain.platform.pojo.entity;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户升级实体类
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-06 4:48 PM
 **/
@Data
public class UserUpgradeEntity implements Serializable {

    @Id
    private Integer id;

    /**
     * 类型
     */
    private String type;

    /**
     * 等级节点
     */
    private String lv;

    /**
     * 个人数量
     */
    private BigDecimal ownTotal;

    /**
     * 直推人数
     */
    private Integer directNum;

    /**
     * 直推团队用户总量
     */
    private BigDecimal directTotal;

    /**
     * 直推个人总量
     */
    private BigDecimal directOwnTotal;

    /**
     * 直推下级用户
     */
    private String directChildLv;

    /**
     * 直推收益
     */
    private BigDecimal directProfit;

    /**
     * 团队奖励
     */
    private BigDecimal teamProfit;

    /**
     * 平级奖励
     */
    private BigDecimal sameProfit;

    /**
     * 直推代数
     */
    private Integer directLayer;

    /**
     * 团队层次
     */
    private Integer teamLayer;

    /**
     * 是否开启级差奖励
     */
    private Integer gradation;


    private Integer sn;

    private Integer state;
}
