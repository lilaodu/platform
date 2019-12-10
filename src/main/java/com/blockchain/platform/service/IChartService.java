package com.blockchain.platform.service;

import java.util.List;

import com.blockchain.platform.pojo.dto.KlineDTO;
import com.blockchain.platform.pojo.vo.KlineVO;

/**
 * 图片类服务接口
 *
 **/
public interface IChartService {

    /**
     * 获取tradeview k 线
     * @param dto
     * @return
     */
    List<KlineVO> tvKline(KlineDTO dto);

    /**
     * 历史k线数据
     * @param dto
     * @return
     */
    List<KlineVO> history(KlineDTO dto);

}
