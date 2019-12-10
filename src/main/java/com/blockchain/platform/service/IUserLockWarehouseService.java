package com.blockchain.platform.service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.entity.UserLockWarehouseEntity;
import com.blockchain.platform.pojo.entity.UserUpgradeEntity;

public interface IUserLockWarehouseService extends IService<UserLockWarehouseEntity> {

	/**
	 * 添加锁币订单并操作钱包
	 */
	boolean addLockWarehouseAndWallet(UserLockWarehouseEntity entity);
	
	/**
	 * 查询用户当前锁仓总和
	 */
	BigDecimal queryLockNumTotal(int userId);


	/**
	 * 执行升级
	 * @param userId
	 */
	void upgrade(Integer userId);
	
}
