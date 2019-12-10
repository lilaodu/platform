package com.blockchain.platform.pojo.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.blockchain.platform.utils.BizUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单数据展示对象VO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-19 11:39 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderVO implements Serializable {

    /**
     * 交易ID
     */
    private Integer id;

    /**
     * 交易货币
     */
    private String coin;

    /**
     * 交易市场
     */
    private String market;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 订单总价
     */
    private BigDecimal total;

    /**
     * 购买数量
     */
    private BigDecimal number;

    /**
     * 订单类型
     */
    private String type;

    /**
     * 交易状态
     */
    private String status;

    /**
     * 委托时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date indate;

    /**
     * 剩余
     */
    private BigDecimal surplus;

    /**
     * 当前交易对
     */
    private String symbol;

    /**
     * 实际交易货币
     * @return
     */
    public String getCoin() {
        return StrUtil.isNotEmpty( symbol) ? BizUtils.token( getSymbol()) : coin;
    }

    /**
     * 实际交易市场
     * @return
     */
    public String getMarket() {
        return StrUtil.isNotEmpty( symbol) ? BizUtils.market( getSymbol()) : market;
    }

    public String getTime(){
        return ObjectUtil.isNotEmpty( indate) ? DateUtil.format(indate, "MM-dd HH:mm") : StrUtil.EMPTY;
    }
}
