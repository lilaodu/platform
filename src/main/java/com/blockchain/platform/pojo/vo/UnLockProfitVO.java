package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 解锁收益VO
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-07 11:24 AM
 **/
@Data
public class UnLockProfitVO implements Serializable {

    /**
     * 自己
     */
    private Integer userId;

    /**
     * 订单ID
     */
    private String productId;

    /**
     * 父级
     */
    private Integer parentId;

    /**
     * 解锁量
     */
    private BigDecimal unLockTotal;

    /**
     * 当前用户星球计划等级
     */
    private Integer lockLv;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 级差剩余
     */
    private BigDecimal gradationRate = BigDecimal.ZERO;
}
