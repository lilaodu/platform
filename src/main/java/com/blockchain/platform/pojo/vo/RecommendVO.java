package com.blockchain.platform.pojo.vo;

import com.blockchain.platform.utils.BigDecimalUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 直推人员合约账户VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-26 6:45 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY )
public class RecommendVO implements Serializable {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 个人合约账户余额
     */
    private BigDecimal asset = BigDecimal.ZERO;

    /**
     * 判断
     * @return
     */
    public BigDecimal getAsset() {
        return BigDecimalUtils.isZero( asset) ? BigDecimal.ZERO : asset;
    }
}
