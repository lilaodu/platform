package com.blockchain.platform.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.CurrencyMarketTradeDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.SecondsContractOrderEntity;

public interface ISecondsContractOrderService extends IService<SecondsContractOrderEntity>{
	
	/**
	 * 添加秒合约订单
	 * @param entity
	 * @return
	 */
	boolean addSecondsContractOrderEntity(SecondsContractOrderEntity entity);
	public boolean cancelOrder(SecondsContractOrderEntity entity);

	/**
	 * 开奖接口
	 */
	void lottery(SecondsContractOrderEntity entity);
	
	/**
	 * 获取当前对应货币的价格(可能会有修改数据)
	 * @return
	 */
	BigDecimal getCurrencyMarketTrade(String symbol);
	
	/**
	 * 分页获取我当前的订单
	 */
	IPage<SecondsContractOrderEntity> actives(PageDTO dto,UserDTO loginUser);
	
	/**
	 * 获取进行中的秒合约订单
	 */
	List<SecondsContractOrderEntity> activeings(UserDTO loginUser);
	
	/**
	 * 获取当前我未完成的订单数,根据状态
	 * 
	 */
	int incompleteOrder(Integer state, UserDTO loginUser);
	
	
}
