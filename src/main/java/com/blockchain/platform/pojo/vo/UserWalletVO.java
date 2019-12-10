package com.blockchain.platform.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 资产展示的VO
 * @author zjl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  UserWalletVO {

    /**
     * 资产
     */
    private String token;

    /**
     * 钱包地址
     */
    private String walletAddress;

    /**
     * 钱包账号 余额
     */
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * 冻结
     */
    private BigDecimal frozenBalance = BigDecimal.ZERO;

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
}
