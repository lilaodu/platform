package com.blockchain.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.OTCAdvertEntity;
import com.blockchain.platform.pojo.entity.OTCOrderEntity;
import com.blockchain.platform.pojo.entity.OTCRepresentationEntity;
import com.blockchain.platform.pojo.vo.OTCOrderVO;

import java.util.List;

/**
 * otc订单接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-21 8:37 PM
 **/
public interface IOTCOrderService extends IService<OTCOrderEntity> {

    /**
     * 我的订单
     * @param dto
     * @return
     */
    List<OTCOrderVO> query(PageDTO dto);

    /**
     * 买入
     */
    Boolean buy(OTCOrderEntity entity, OTCAdvertEntity advert);

    /**
     * 编辑
     * @param entity
     * @return
     */
    Boolean modify(OTCOrderEntity entity);

    /**
     * 获取订单详情
     * @param dto
     * @return
     */
    OTCOrderEntity findByCondition(BaseDTO dto);

    /**
     * 放币操作
     * @param entity
     * @return
     */
    Boolean adopt(OTCOrderEntity entity);

    /**
     * 申述
     * @param entity
     * @return
     */
    Boolean representation(OTCRepresentationEntity entity);

    /**
     * 获取otc订单列表
     * @param dto
     * @return
     */
    List<OTCOrderEntity> list(BaseDTO dto);

    /**
     * 订单详情
     * @param dto
     * @return
     */
    OTCOrderVO details(BaseDTO dto);


     boolean cancelOtcOrder(OTCOrderEntity orderEntity );
}
