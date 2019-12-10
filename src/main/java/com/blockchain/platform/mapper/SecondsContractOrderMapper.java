package com.blockchain.platform.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.entity.SecondsContractOrderEntity;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SecondsContractOrderMapper extends BaseMapper<SecondsContractOrderEntity> {

	/**
	 * 扣款
	 * @param entity
	 * @return
	 */
//	int deductionUserWallet(SecondsContractOrderEntity entity);

//	int addOrder(SecondsContractOrderEntity entity);
	
	
	/**
	 * 获取用户今天的单子
	 * @return 
	 */
	List<SecondsContractOrderEntity> queryOrderOnDay(Integer userId);
	int count(@Param(value = "userId")Integer userId);

	List<SecondsContractOrderEntity>queryUnCompleted();

}
