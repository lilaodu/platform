package com.blockchain.platform.service;

import com.blockchain.platform.pojo.entity.OrderFlowEntity;

/**
 * 挖矿策略接口
 * @author zhangye
 *
 */
public interface IMiningService {
	
	 /**
     * 处理昨日交易手续费分红
     * 1.1、1代刷单交易手续费18%的奖励。2代的15%奖励。3代的12%奖励
     * 1.2、高级节点--本团队所有会员购买各挖矿币种交易手续费的20%分润
     * @Author: DengWei
     * @param:  * @param null
     * @return:
     * @Date: 2019-05-24 21:45
     */
    void doYesterdayTradeFee();

    /**
     *  处理当日挖矿奖励
     *  2.1、节点--交易释放上限=待挖矿数量×2%  最高160个或者10笔
     * 	2.2、初级节点--交易释放上限=待挖矿数量×2.5%  最高200个或者10笔
     * 	2.3、中级节点--交易释放上限=待挖矿数量×3.25%  最高260个或者10笔
     * 	2.4、高级节点--交易释放上限=待挖矿数量×3.25%  最高260个或者10笔
     * @Author: DengWei
     * @param:  * @param null
     * @return:
     * @Date: 2019-05-24 22:01
     */
    void doDigAssets(OrderFlowEntity event);

   
}
