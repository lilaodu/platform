package com.blockchain.platform.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.entity.UnlockWarehouseFlowEntity;
import com.blockchain.platform.pojo.entity.UserLockWarehouseEntity;

@Mapper
public interface UnlockWarehouseFlowMapper extends BaseMapper<UnlockWarehouseFlowEntity> {
	
	UnlockWarehouseFlowEntity dayUnlockWarehouseFlow(Map<String, Object> param);
	
	List<UnlockWarehouseFlowEntity> dayFlowList(Integer userId);
	
	/**
	 * 查询当天该销毁的
	 */
	List<UserLockWarehouseEntity>  needDestruction();
	
}
