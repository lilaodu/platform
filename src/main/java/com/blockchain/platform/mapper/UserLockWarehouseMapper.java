package com.blockchain.platform.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UpgradeDTO;
import com.blockchain.platform.pojo.vo.DirectUserVO;
import com.blockchain.platform.pojo.vo.UnlockWarehouseFlowVO;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.annotation.Sharding;
import com.blockchain.platform.pojo.entity.UserLockWarehouseEntity;

@Mapper
//@Sharding(table = "user_wallet",field = "coin")
public interface UserLockWarehouseMapper extends BaseMapper<UserLockWarehouseEntity> {
	
	

	
	/**
	 * 查询锁仓总量
	 * @param userId
	 * @return
	 */
	BigDecimal queryLockNumTotal(int userId);


	/**
	 * 查询用户直推数据
	 * @param dto
	 * @return
	 */
	DirectUserVO findUserDirectData(UpgradeDTO dto);
	
	
	/**
	 * 销毁的记录销毁列表
	 */
	List<UnlockWarehouseFlowVO> noUnlock(int userId);
	
	/**
	 * 所有的记录列表
	 * @param userId
	 * @return
	 */
	List<UnlockWarehouseFlowVO> allUnlock(int userId);
	
}
