package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 升级VO
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-06 8:15 PM
 **/
@Data
public class UpgradeVO implements Serializable {


    private BigDecimal directTotal;


    /**
     * 直推人数
     */
    private Integer directNum;


}
