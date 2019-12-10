package com.blockchain.platform.pojo.vo;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

/**
 * 成交记录VO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-19 11:46 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DealVO implements Serializable {

    /**
     * 成交单价
     */
    private BigDecimal price;

    /**
     * 成交数量
     */
    private BigDecimal number;

    /**
     * 成交类型
     */
    private String type;

    /**
     * 时间
     */
    private String time;

    /**
     * 日期
     */
    private String date;
}
