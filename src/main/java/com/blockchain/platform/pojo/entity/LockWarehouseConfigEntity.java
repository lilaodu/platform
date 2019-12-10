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
@TableName("t_lock_warehouse_config")//@Table(name
public class LockWarehouseConfigEntity extends Model<Model<?>> implements Serializable {
	
	/**
     * id
     */
	@Id
	@TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Integer id;
	
	/**
	 * 锁币数量(btb)
	 */
	@Column(name = "lock_num")
	private String lockNum;
	
	/**
	 * 锁币后赠送的比例
	 */
	@Column(name = "gift_ratio")
	private BigDecimal giftRatio;
	
	/**
	 * 每天解锁的比例
	 */
	@Column(name = "unlock_day_ratio")
	private BigDecimal unlockDayRatio;
	
	/**
	 * 天数
	 */
	@Column(name = "day_num")
	private Integer dayNum;
	
	/**
	 * 需要做的秒合约任务次数
	 */
	@Column(name = "task_num")
	private String taskNum;
	
	/**
	 * 任务要求,消费比例(锁仓20000，完成任务每次交易必须大于200，交易5次，完成当日任务，解锁1%)
	 */
	@Column(name = "task_ratio")
	private BigDecimal taskRatio;
	
	/**
	 * 秒合约要求的币类型
	 */
	@Column(name = "coin")
	private String coin;
	
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
	 * 锁币上限值
	 */
	@Column(name = "lock_toplimit")
	private BigDecimal lockToplimit;
	
	@Column(name = "lock_explain")
	private String lockExplain;
	

	
	
	
	
}
