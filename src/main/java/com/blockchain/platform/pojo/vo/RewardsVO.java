package com.blockchain.platform.pojo.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 团队收益明细
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 3:08 PM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RewardsVO implements Serializable {

    /**
     * 时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private String time;

    /**
     * 代币
     */
    private String symbol;

    /**
     * 总量
     */
    private BigDecimal amount;

    /**
     * 产生
     */
    private Integer productUserId;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 前端固定
     * @return
     */
    public String getUid() {
        return StrUtil.fillBefore( StrUtil.toString( productUserId + 10000), '0', 7);
    }
}
