/**
 * @program: exchange
 * @description: toDo
 * @author: DengWei
 * @create: 2019-05-18
 **/
package com.blockchain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import com.blockchain.platform.annotation.Sharding;
import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import java.util.List;
import java.util.Map;

/**
 * @program: TradeMapper
 * @description: 交易撮合映射
 **/
@Mapper
@Sharding(table = {"deal_flow", "order_flow"})
public interface TradeMapper  extends BaseMapper<OrderFlowEntity> {

//	int frozenUserWallet(OrderFlowEntity entity);
	
	int addOrder(OrderFlowEntity entity);
	
//	int mdfUserBaseWalletsForList(Map map);
	
//	int mdfUserWalletsForList(Map map);
	
	int mdfOrder(OrderFlowEntity entity);
	
//	int mdfUserWallet(OrderFlowEntity entity);
	
//	int mdfBaseUserWallet(OrderFlowEntity entity);
	
	int mdfOrderList(Map map);
	
	int addTradeFlows(Map map);
	
	OrderFlowEntity queryOrder(OrderFlowEntity entity);
	
//	int mdfUserWalletForCancel(OrderFlowEntity entity);
	
	int cancelOrder(OrderFlowEntity entity);
	
	/**
     * 查询总记录条数
     * @param map
     * @return
     */
    Map<String, Object> queryUserDeputes(Map<String, Object> map);
    
    /**
     * 委托订单
     * @param map
     * @return
     */
    List<Map<String, Object>> queryDeputesPage(Map<String, Object> map);
    
    /**
     * 成交记录总条数
     * @param map
     * @return
     */
    Map<String, Object> queryUserDeals(Map<String, Object> map);
    
    /**
     * 查询成交记录数据
     * @param map
     * @return
     */
    List<Map<String, Object>> queryDealsPage(Map<String, Object> map);
    
    /**
     * 查询订单列表 卖单查买单列表，买单查卖单列表
     * @param event
     * @return
     */
    List<OrderFlowEntity> queryOrderListByType(OrderFlowEntity event);

}
