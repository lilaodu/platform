package com.blockchain.platform.pojo.entity;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

@Data
@Entity
@TableName("t_user_lock_warehouse")
public class UserLockWarehouseEntity extends Model<Model<?>> implements Serializable {
	/**
     * id
     */
	@Id
	@TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Integer id;
	
	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Integer userId;
	
	/**
	 * 用户锁的币的数量
	 */
	@Column(name = "lock_num")
	private BigDecimal lockNum;
	
	/**
	 * 赠送的币数量
	 */
	@Column(name = "give_coin_num")
	private BigDecimal giveCoinNum;
	
	/**
	 * 当前解锁天数
	 */
	@Column(name = "unlock_day_num")
	private Integer unlockDayNum;
	
	/**
	 * 锁仓时间
	 */
	@Column(name = "lock_date")
	private Date lockDate;
	
	/**
	 * 当前解锁的总数量
	 */
	@Column(name = "unlock_num")
	private BigDecimal unlockNum;
	
	/**
	 * 过期没解锁的总数量
	 */
	@Column(name = "expire_num")
	private BigDecimal expireNum;
	
	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;
	
	/**
	 *
	 */
	@Column(name = "version")
	private Integer version;
	
	/**
	 * 状态
	 * 1.正常2.已完成3.其他(有问题)
	 */
	@Column(name = "state")
	private Integer state;
	
	/**
     * 币的类型
     */
	@Column(name = "symbol")
    private String symbol;
	
}
