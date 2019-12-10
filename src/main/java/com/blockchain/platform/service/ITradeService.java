package com.blockchain.platform.service;

import java.util.List;
import java.util.Map;

import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import com.blockchain.platform.pojo.vo.DealVO;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.pojo.vo.RankingVO;

/**
 *交易有关接口
 */
public interface ITradeService {

    /**
     * 添加对象
     * @return
     */
    boolean addOrderEntity(OrderFlowEntity entity) throws Exception;

    /**
     * 查询对象
     * @param event
     * @return
     */
    OrderFlowEntity viewEntity(OrderFlowEntity event);

    /**
     * 获取用户委托记录
     * @param dto
     * @return
     */
    PageVO queryUserDeputes(PageDTO dto);

    /**
     * 撮合交易事物处理，（更新用户余额，交易流水记录，撮合后的订单状态，余额更改
     * @return
     */
    public boolean addTradeFlowList(Map<String, Object> map) throws Exception;

    /**
     * 获取订单列表ByType
     * @param event
     * @return
     */
    public List<OrderFlowEntity> getOrderListByType(OrderFlowEntity event);

    /**
     *  撤单
     *  @param event
     *  @return
     */
    public void cancelOrder(OrderFlowEntity event) throws Exception;

    /**
     * 交易历史
     * @param dto
     * @return
     */
    List<DealVO> queryDeals(BaseDTO dto);
    
    /**
     * 盘口数据
     * @param dto
     * @return
     */
    List<WsOrderDTO> queryHandicap(BaseDTO dto);

    /**
     * 用户成交记录
     * @param dto
     * @return
     */
    PageVO queryUserDeals(PageDTO dto);
    
    /**
     * 查询tick
     * @param dto
     * @return
     */
    RankingVO queryRankingByCondition(TickDTO dto);

    /**
     * 执行通知
     * @param event
     */
    void executeNotify(OrderFlowEntity event);



    }