package com.blockchain.platform.pojo.entity;

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
@TableName("t_unlock_warehouse_flow")
public class UnlockWarehouseFlowEntity extends Model<Model<?>> implements Serializable {
	
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
	 * 解锁时间
	 */
	@Column(name = "unlock_date")
	private Date unlockDate;
	
	/**
	 * 距离锁仓那天的天数(按日期天数算,不是时间天数)
	 */
	@Column(name = "space_day_num")
	private Integer spaceDayNum;
	
	/**
	 * 当次解锁数量
	 */
	@Column(name = "unlock_num")
	private BigDecimal unlockNum;
	
	/**
	 * 当前解锁后,已解锁的总量
	 */
	@Column(name = "unlock_num_z")
	private BigDecimal unlockNumZ;
	
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
	 */
	@Column(name = "state")
	private Integer state;
	
	/**
	 * 任务有效次数
	 */
	@Column(name = "task_num")
	private Integer taskNum;
	
	/**
	 * 对应锁仓订单的id
	 */
	@Column(name = "user_lock_warehouse_id")
	private Integer userLockWarehouseId;
	
	@Column(name = "symbol")
	private String symbol;


	
}
