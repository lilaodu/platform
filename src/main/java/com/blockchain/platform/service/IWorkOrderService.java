package com.blockchain.platform.service;

import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.WorkOrderDetailsEntity;
import com.blockchain.platform.pojo.entity.WorkOrderEntity;
import com.blockchain.platform.pojo.vo.WorkOrderVO;

import java.util.List;

/**
 * 工单服务接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-26 2:37 PM
 **/
public interface IWorkOrderService {

    /**
     * 我的工单
     * @param dto
     * @return
     */
    List<WorkOrderVO> query(PageDTO dto);

    /**
     * 提交工单
     * @param entity
     * @return
     */
    Boolean modify(WorkOrderEntity entity);

    /**
     * 回复
     * @param dto
     * @return
     */
    Boolean reply(WorkOrderDetailsEntity dto);

    /**
     * 工单详情
     * @param dto
     * @return
     */
    WorkOrderEntity findByCondition(BaseDTO dto);
}
