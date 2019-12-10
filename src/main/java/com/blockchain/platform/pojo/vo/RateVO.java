package com.blockchain.platform.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 汇率展示对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 9:20 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateVO implements Serializable {

    /**
     * 标志
     */
    private String symbol;

    /**
     * 符号
     */
    private String label;

    /**
     * 最终价
     */
    private BigDecimal last;
}
