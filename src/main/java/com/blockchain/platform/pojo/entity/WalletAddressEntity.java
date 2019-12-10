package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data 
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_wallet_address_list")
public class WalletAddressEntity  implements Serializable {

	/**
	 * id
	 */
	@Id
    private Integer id;

	/**
	 * 系统钱包地址
	 */
	private String address;

	/**
	 * 钱包密吗
	 */
	private String password;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 代币大类
	 */
	private String coinType;

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
