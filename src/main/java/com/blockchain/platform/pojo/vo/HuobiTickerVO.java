package com.blockchain.platform.pojo.vo;

import com.blockchain.platform.pojo.entity.HuobiKLineEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 数据
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-15 7:22 PM
 **/
@Data
public class HuobiTickerVO implements Serializable {

    /**
     * 状态
     */
    private String status;

    /**
     * 状态
     */
    private String ch;

    /**
     * 最终数据
     */
    private HuobiKLineEntity tick;


    /**
     * 数据
     */
    private List<HuobiKLineEntity> data;
}
