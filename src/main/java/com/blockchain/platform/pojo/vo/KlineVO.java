package com.blockchain.platform.pojo.vo;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * k线VO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-11 10:51 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KlineVO implements Serializable {

    /**
     * history 数据
     */
    private BigDecimal c;

    /**
     * 时间戳
     */
    private Long t;

    /**
     * 开盘价
     */
    private BigDecimal o;

    /**
     * 最高价
     */
    private BigDecimal h;

    /**
     * 最低价
     */
    private BigDecimal l;

    /**
     *  volume
     */
    private BigDecimal v;

}
