package com.blockchain.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import com.blockchain.platform.pojo.vo.RankingVO;

import java.util.List;

/**
 * otc订单接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-21 8:37 PM
 **/
public interface IOrderFlowService extends IService<OrderFlowEntity> {

    /**
     * 用户自选行情
     * @param dto
     * @return
     */
    List<RankingVO> optional(BaseDTO dto);
}
