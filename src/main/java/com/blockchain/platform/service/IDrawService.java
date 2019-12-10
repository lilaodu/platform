package com.blockchain.platform.service;


import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.DrawDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.vo.*;

import java.util.List;

/**
 * 抽奖服务接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-13 6:23 PM
 **/
public interface IDrawService  {

    /**
     * 获取抽奖列表
     * @param dto
     * @return
     */
    List<LuckDrawVO> query(BaseDTO dto);

    /**
     * 抽奖活动具体奖项
     * @param dto
     * @return
     */
    List<LuckDrawConfigVO> list(BaseDTO dto);

    /**
     * 抽奖活动记录
     * @param dto
     * @return
     */
    List<LuckDrawLogVO> log(PageDTO dto);

    /**
     * 历史中奖记录
     * @param dto
     * @return
     */
    List<LuckDrawLogVO> history(BaseDTO dto);

    /**
     * 统计抽奖次数
     * @param dto
     * @return
     */
    PrizeVO count(BaseDTO dto);

    /**
     * 用户抽奖
     * @param dto
     * @return
     */
    PrizeVO prize(BaseDTO dto) throws Exception;




    WinningVO doPrize(DrawDTO dto);
}
