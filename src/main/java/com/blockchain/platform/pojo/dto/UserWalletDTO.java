package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户钱包DTO
 * @author zjl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWalletDTO  implements Serializable{
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 所属用户ID
     */
    private int userId;

    /**
     * 钱包类型
     */
    private String symbol;

    /**
     * 钱包地址
     */
    private String walletAddress;

    /**
     * 钱包密码
     */
    private String password;

    /**
     * 钱包余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenBalance ;

    /**
     * 个人币种手续费
     */
    private BigDecimal rate;
}
