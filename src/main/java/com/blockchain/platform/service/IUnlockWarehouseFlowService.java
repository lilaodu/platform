package com.blockchain.platform.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.SecondsContractOrderEntity;
import com.blockchain.platform.pojo.entity.UnlockWarehouseFlowEntity;
import com.blockchain.platform.pojo.entity.UserLockWarehouseEntity;
import com.blockchain.platform.pojo.vo.UnlockWarehouseFlowVO;

/**
 * 锁仓解锁接口
 * @author zhangye
 *
 */
public interface IUnlockWarehouseFlowService extends IService<UnlockWarehouseFlowEntity> {

	/**
	 * 完成任务后为用户解仓
	 */
	UnlockWarehouseFlowEntity unlockWarehouse(Integer userLockWarehouseId,Integer userId,UnlockWarehouseFlowEntity unlockWarehouseFlowEntity);
	
	/**
	 * 任务要求
	 * @param sCOEntity
	 * @return
	 */
	void taskDemand(SecondsContractOrderEntity sCOEntity);

	Integer dayTaskNum(Integer userId);
	
	void destruction();
	
	
	/**
	 * 销毁的记录销毁列表
	 */
	List<UnlockWarehouseFlowVO> noUnlock(Integer userId);
	
	/**
	 * 销毁的记录销毁列表
	 */
	List<UnlockWarehouseFlowVO> allUnlock(Integer userId);
	
}
