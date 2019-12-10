package com.blockchain.platform.pojo.vo;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * web socket 返回对象
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-06-15 3:50 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WsVO implements Serializable {

    /**
     * 当前交易情况
     */
    private RankingVO tick;

    /**
     * 买盘数据
     */
    private Map<String, List<OrderVO>> buys = MapUtil.newHashMap();

    /**
     * 卖盘数据
     */
    private Map<String, List<OrderVO>> sells = MapUtil.newHashMap();

    /**
     * 成交历史
     */
    private List<DealVO> history;

    /**
     * 用户个人委托列表
     */
    private PageVO actives;

    /**
     * 用户个人成交记录
     */
    private PageVO deals;

    /**
     * 资产
     */
    private Map<String, CapitalVO> capital;
}
