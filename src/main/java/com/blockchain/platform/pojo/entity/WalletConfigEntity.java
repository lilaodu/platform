package com.blockchain.platform.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

@Data 
@Entity
@TableName(value = "t_wallet_config")
public class WalletConfigEntity extends Model<Model<?>> implements Serializable {

	/**
	 * id
	 */
	@Id
    private Integer id;

	/**
     * 代币
     */
    private String symbol;

	/**
	 * 智能合约地址
	 */
	private String contractAddress;

	/**
	 * 系统钱包地址
	 */
	private String walletAddress;

	/**
	 * 钱包密吗
	 */
	private String walletPassword;

	/**
	 * 代币key
	 */
	private String coinKey;

	/**
	 * 区块确认数
	 */
	private Integer confirmSum;

	/**
	 * 用户钱包余额下限。低于此值，需要重发手续费
	 */
	private BigDecimal gasLowerLimit;

	/**
	 * 手续费
	 */
	private BigDecimal fee;

	/**
	 * 小数位数
	 */
	private Integer decimals;

	/**
	 * 默认区块高度|用于系统在区块链中，查找用户转入资产
	 */
	private Integer defaultBlockNumber;

	/**
	 * APIKEY
	 */
	private String apiKey;

	/**
	 * 钱包资产归集gas上限
	 */
	private Integer gasLimitCollect;

	/**
	 * 手续费发放gas上限
	 */
	private Integer gasLimitSendFee;

	/**
	 * 钱包资产用户提现gas上限
	 */
	private Integer gasLimitOut;

	/**
	 * 钱包资产归集gasprice;为0时，系统动态取当时eth的gasprice
	 */
	private BigDecimal gasPriceCollect;

	/**
	 * 手续费发放gasprice;为0时，系统动态取当时eth的gasprice
	 */
	private BigDecimal gasPriceFee;

	/**
	 * 钱包资产用户提现gasprice;为0时，系统动态取当时eth的gasprice
	 */
	private BigDecimal gasPriceOut;

	/**
	 * API提供方url地址
	 */
	private String url;

	/**
	 * 钱包服务地址
	 */
	private String serviceUrl;

	/**
	 * 代币大类
	 */
	private String coinType;

	/**
	 * 代币小类
	 */
	private String coinKind;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 状态
	 */
	private Integer state;

	/**
	 * 版本号
	 */
	private String version;

}
