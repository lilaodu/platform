package com.blockchain.platform.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 资产展示VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CapitalVO implements Serializable {

    /**
     * 资产
     */
    private String token;

    /**
     * 钱包地址
     */
    private String address;

    /**
     * 钱包账号 余额
     */
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * 冻结
     */
    private BigDecimal frozen = BigDecimal.ZERO;

    /**
     * 可用
     */
    private BigDecimal usable = BigDecimal.ZERO;

    /**
     * otc 总额
     */
    private BigDecimal balanceOtc = BigDecimal.ZERO;

    /**
     * otc 账户冻结
     */
    private BigDecimal frozenOtc = BigDecimal.ZERO;

    /**
     * otc 可用
     */
    private BigDecimal usableOtc = BigDecimal.ZERO;

    /**
     * 交易账户
     */
    private BigDecimal balanceT = BigDecimal.ZERO;

    /**
     * 交易账户冻结
     */
    private BigDecimal frozenT = BigDecimal.ZERO;

    /**
     * 交易账户可用
     */
    private BigDecimal usableT = BigDecimal.ZERO;

    /**
     * 资产顺序
     */
    private Integer sn;

    /**
     * 是否支持合约账户
     */
    private String isT;

    /**
     * 是否支持法币账户
     */
    private String isOtc;
}
