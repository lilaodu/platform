package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 直推升级VO
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-09 11:41 AM
 **/
@Data
public class UpgradeDirectVO implements Serializable {

    /**
     * 持有量
     */
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * 直推等级
     */
    private String rank;


    /**
     * 用户ID
     */
    private Integer userId;

}
