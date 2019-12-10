package com.blockchain.platform.disruptor;

import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import com.blockchain.platform.pojo.entity.TradeFlowEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 撮合清算实体
 * @author David.Li
 * @version 1.0
 * @create 2019-09-19 8:37 PM
 **/
@Data
public class LiquidationDTO implements Serializable {

    /**
     * 清算实体
     */
    private OrderFlowEntity entity;


    /**
     * 流水记录
     */
    private List<TradeFlowEntity> flowEntities;


    /**
     * 成交记录
     */
    private List<OrderFlowEntity> orderEntities;
}
