package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.blockchain.platform.annotation.LimitedVerify;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.i18n.LocaleKey;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单传输对象
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-11 9:55 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO implements Serializable {

    /**
     * 交易对(ETC-USDT)
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    public String coinPair;

    /**
     * 交易类型(BUY,SELL)
     */
    @LimitedVerify(values = {BizConst.TradeConst.TRADE_TYPE_OUTS_EN, BizConst.TradeConst.TRADE_TYPE_INPUTS_EN}, message = LocaleKey.ORDER_TYPE_ERROR)
    private String type;

    /**
     * 单价
     */
    @NotNull(message = LocaleKey.ORDER_PRICE_NOT_NULL)
    @DecimalMin(value = "0.00000001", message = LocaleKey.OTC_ORDER_AMOUNT_LESS_ZERO)
    private BigDecimal price;

    /**
     * 数量
     */
    @NotNull(message = LocaleKey.ORDER_NUMBER_NOT_NULL)
    @DecimalMin(value = "0.00000001", message = LocaleKey.OTC_ORDER_NUM_LESS_ZERO)
    private BigDecimal num;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 下单用户id
     */
    private Integer userId;


    /**
     * 撤单ID
     */
    private Integer id;
}
