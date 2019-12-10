package com.blockchain.platform.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.annotation.Sharding;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.RankingDTO;
import com.blockchain.platform.pojo.dto.WsOrderDTO;
import com.blockchain.platform.pojo.entity.ArticleEntity;
import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import com.blockchain.platform.pojo.vo.DealVO;
import com.blockchain.platform.pojo.vo.OrderVO;
import java.util.List;
import java.util.Map;

/**
 * 查询交易订单接口
 *
 * @author zhangye
 **/
@Mapper
@Sharding(table = {"deal_flow", "order_flow"},field = "symbol")
public interface OrderFlowMapper extends BaseMapper<OrderFlowEntity> {

	/**
     * 货币排行榜
     * @param dto
     * @return
     */
    List<RankingDTO> queryTokenRanking(BaseDTO dto);
	
    /**
     * 成交历史
     * @param dto
     * @return
     */
    List<DealVO> queryDeals(BaseDTO dto);

    /**
     * 单个货币行情
     * @param map
     * @return
     */
    Map<String, Object> queryTick(Map<String, Object> map);
    
    /**
     * 查询盘口数据
     * @param dto
     */
    List<WsOrderDTO> queryHandicap(BaseDTO dto);
    
    
    /**
     * 用户委托订单
     * @param dto
     * @return
     */
    List<OrderVO> queryUserDelegate(PageDTO dto);

    /**
     * 用户自选行情
     * @param dto
     * @return
     */
    List<RankingDTO> optional(BaseDTO dto);
    
}
