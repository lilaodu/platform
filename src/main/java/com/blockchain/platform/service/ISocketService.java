package com.blockchain.platform.service;

import java.util.List;
import java.util.Map;

import com.blockchain.platform.pojo.dto.WsChatDTO;
import com.blockchain.platform.pojo.vo.CapitalVO;
import com.blockchain.platform.pojo.vo.DealVO;
import com.blockchain.platform.pojo.vo.OrderVO;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.pojo.vo.RankingVO;
import com.blockchain.platform.pojo.vo.WsChatVO;
import com.huobi.client.model.Candlestick;

/**
 * 消息通知服务
 *
 * @author zhangye
 **/
public interface ISocketService {

    List<Map<String, List<OrderVO>>> findWsKByCondition(String symbol);
    
    List<DealVO> findWsHistoryByCondition(String symbol);
    
    RankingVO findWsTickerByCondition(String symbol);
    
    Map<String, CapitalVO> findWsCapitalByCondition(String symbol, String user);
    
    PageVO findWsActivesByCondition(String symbol, String user);

    /**
     * 查询当前链接用户消息记录
     * @param orderId
     * @param advertId
     * @param token
     * @return
     */
    WsChatVO findUserChat(Integer orderId, Integer advertId, String token);

    /**
     * 添加聊天消息
     * @param dto
     * @return
     */
    Boolean addChat(WsChatDTO dto);
}
