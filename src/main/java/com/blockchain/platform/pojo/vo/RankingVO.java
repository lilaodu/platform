package com.blockchain.platform.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

import com.blockchain.platform.utils.BigDecimalUtils;

/**
 * 排行榜VO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-15 8:04 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingVO implements Serializable {

    /**
     * 当前交易货币
     */
    private String symbol;

    /**
     * 交易市场
     */
    private String market;

    /**
     * 货币全称
     */
    private String coinFullname;

    /**
     * 图标
     */
    private String icon;

    /**
     * 小数位数
     */
    private String decLength;

    /**
     * 类型
     */
    private String type;

    /**
     * 是否首页显示
     */
    private String isHome;

    /**
     * 开盘价
     */
    private BigDecimal open;

    /**
     * 最高价
     */
    private BigDecimal high;

    /**
     * 最低价
     */
    private BigDecimal low;

    /**
     * 收盘价
     */
    private BigDecimal close;

    /**
     * 成交量
     */
    private BigDecimal total;

    /**
     * 成交金额
     */
    private BigDecimal totalAmount;

    /**
     * 涨跌率
     */
    private BigDecimal changeRange = BigDecimal.ZERO;

    /**
     * 涨跌值
     */
    private BigDecimal changeValue = BigDecimal.ZERO;

    /**
     * 最低卖出
     */
    private BigDecimal highestBid;

    /**
     * 最高买入
     */
    private BigDecimal lowestAsk;

    /**
     * 是否是新币
     */
    private String isNew;

    /**
     * 排序权重
     */
    private Integer sn;

    /**
     * 涨跌率
     * @return
     */
    public BigDecimal getChangeRange() {
        return BigDecimalUtils.isZero( changeRange) ? BigDecimal.ZERO : changeRange;
    }
}
