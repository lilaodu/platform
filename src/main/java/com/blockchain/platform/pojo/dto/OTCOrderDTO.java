package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.annotation.LimitedVerify;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * OTC订单显示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-21 9:07 PM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY )
public class OTCOrderDTO implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 类型(买入/卖出)
     */
    @LimitedVerify(values = {BizConst.OTCOrderConst.TYPE_SELL, BizConst.OTCOrderConst.TYPE_BUY}, message = LocaleKey.OTC_ORDER_TYOE_ERROR)
    private String type;

    /**
     * 广告id
     */
    @NotNull(message = LocaleKey.SYS_PARAM_ERROR)
    private Integer advertId;

    /**
     * 购买金额
     */
    @Min(value = 0, message = LocaleKey.OTC_ORDER_AMOUNT_LESS_ZERO)
    private BigDecimal amount;

    /**
     * 购买数量
     */
    @Min(value = 0, message = LocaleKey.OTC_ORDER_NUM_LESS_ZERO)
    private BigDecimal num;

    String payType;

    private String payAccount;




}
