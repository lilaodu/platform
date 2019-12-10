package com.blockchain.platform.pojo.vo;

import com.blockchain.platform.utils.IntUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 抽奖次数，kyc认证人数，秒合约订单数
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-13 6:23 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrizeVO implements Serializable {

    /**
     * 抽奖总次数
     */
    private Integer num = IntUtils.INT_ZERO;

    /**
     * kyc直推人数
     */
    private Integer kyc = IntUtils.INT_ZERO;

    /**
     * 秒合约订单数
     */
    private Integer contract = IntUtils.INT_ZERO;

    /**
     * 已抽奖次数
     */
    private Integer draw;

    /**
     * 奖项
     */
    private Integer prize;

    /**
     * 中奖数量
     */
    private BigDecimal prizeNum;

    /**
     * 中奖代币
     */
    private String symbol;

    /**
     * 是否中奖
     */
    private String isPrize;
}
