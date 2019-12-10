package com.blockchain.platform.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户钱包实体类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2018-01-24 下午12:35
 **/
@Data 
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_wallet")
public class UserWalletEntity implements Serializable {

	@Id
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
     * 钱包余额(币币)=冻结+可用的
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenBalance;
    
    /**
     * 钱包余额(秒合约)
     */
    private BigDecimal balanceT;

    /**
     * 冻结金额
     */
    private BigDecimal frozenT;
    
    /**
     * 钱包余额(otc)
     */
    private BigDecimal balanceOtc;

    /**
     * 冻结金额
     */
    private BigDecimal frozenOtc;

    /**
     * 个人币种手续费
     */
    private BigDecimal rate;


    /**
     * 入库时间
     */
    private Date createTime;

    /**
     * 版本号
     */
    private int version ;

    /**
     * 修改时间
     */
    private Date updateTime;
}
