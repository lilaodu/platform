package com.blockchain.platform.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.OTCOrderEntity;
import com.blockchain.platform.pojo.entity.OTCOrderFlowEntity;
import com.blockchain.platform.pojo.entity.OTCRepresentationEntity;
import com.blockchain.platform.pojo.vo.OTCOrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * otc订单接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-21 8:39 PM
 **/
@Mapper
public interface OTCOrderMapper extends BaseMapper<OTCOrderEntity> {

    /**
     * 我的订单
     * @param dto
     * @return
     */
    List<OTCOrderVO> query(PageDTO dto);


    /**
     * 编辑订单
     * @param entity
     * @return
     */
    Integer update(OTCOrderEntity entity);

    /**
     * 订单详情
     * @param dto
     * @return
     */
    OTCOrderEntity findByCondition(BaseDTO dto);

    /**
     * 统计某个广告的手续费
     * @param dto
     * @return
     */
    BigDecimal countRate(BaseDTO dto);

    /**
     * 申述
     * @param entity
     * @return
     */
    Integer representation(OTCRepresentationEntity entity);

    /**
     * 订单列表
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

    /**
     * 添加订单交易流水
     * @param entity
     * @return
     */
    Integer addFlow(OTCOrderFlowEntity entity);

    /**
     * 获取订单
     * @param baseDTO
     * @return
     */
    List<OTCOrderEntity> queryUncompleted(BaseDTO baseDTO);

    /**
     * 取消订单
     * @param entity
     * @return
     */
    Integer updateCancel(OTCOrderEntity entity);
}
