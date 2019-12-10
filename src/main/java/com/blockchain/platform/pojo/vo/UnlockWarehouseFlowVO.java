package com.blockchain.platform.pojo.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class UnlockWarehouseFlowVO implements Serializable {
	
    private Integer id;
	
	/**
	 * 用户id
	 */
	private Integer userId;
	
	/**
	 * 解锁时间
	 */
	private Date unlockDate;
	
	/**
	 * 距离锁仓那天的天数(按日期天数算,不是时间天数)
	 */
	private Integer spaceDayNum;
	
	/**
	 * 当次解锁数量
	 */
	private BigDecimal unlockNum;
	
	/**
	 * 当前解锁后,已解锁的总量
	 */
	private BigDecimal unlockNumZ;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 *
	 */
	private Integer version;
	
	/**
	 * 状态
	 */
	private Integer state;
	
	/**
	 * 任务有效次数
	 */
	private Integer taskNum;
	
	/**
	 * 对应锁仓订单的id
	 */
	private Integer userLockWarehouseId;
	
	private String symbol;
	
	/**
	 * 对应锁仓订单的价格
	 */
	private BigDecimal lockNum;
	
	/**
	 * 过期没解锁的总数量
	 */
	private BigDecimal expireNum;
}
