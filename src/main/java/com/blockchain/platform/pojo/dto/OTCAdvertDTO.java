package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.annotation.LimitedVerify;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.utils.IntUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * OTC广告 数据传输DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 5:59 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY )
public class OTCAdvertDTO implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 广告类型
     */
    @LimitedVerify(values = {BizConst.AdvertConst.TYPE_SELL, BizConst.AdvertConst.TYPE_BUY}, message = LocaleKey.ADVERT_TYPE_ERROR)
    private String type;

    /**
     * 价格
     */
    @NotNull(message = LocaleKey.ADVERT_PRICE_IS_NULL)
    @Min(value = 0, message = LocaleKey.ADVERT_PRICE_LESS_ZERO)
    private BigDecimal price;

    /**
     * 广告编号
     */
    private String advertNumber;

    /**
     * 货币简称
     */
    private String symbol;

    /**
     * 限额（上限）
     */
    @Min(value = 0, message = LocaleKey.ADVERT_LIMIT_LESS_ZERO)
    private BigDecimal limitUp;

    /**
     * 限额（下限）
     */
    @Min(value = 0, message = LocaleKey.ADVERT_LIMIT_LESS_ZERO)
    private BigDecimal limitDown;

    /**
     * 付款方式
     */
    @NotBlank(message = LocaleKey.ADVERT_NOT_CHOOSE_PAYMENT)
    private String  payType;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 留言备注
     */
    private String remark;

    /**
     * 汇率
     */
    private String rate;

    /**
     * 广告限制条数(-1 表示不限制)
     */
    private Integer limit = IntUtils.NEGATIVE_ONE;
}
