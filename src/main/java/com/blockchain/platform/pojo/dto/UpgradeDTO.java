package com.blockchain.platform.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 升级查询公用DTO
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-06 6:10 PM
 **/
@Data
public class UpgradeDTO implements Serializable {

    /**
     * 直推等级
     */
    private Object directChildLv;

    /**
     * 当前用户ID
     */
    private Integer userId;


    /**
     * 持有量
     */
    private BigDecimal total;
}
