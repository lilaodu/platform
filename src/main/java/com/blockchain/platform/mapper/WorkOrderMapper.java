package com.blockchain.platform.mapper;

import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.WorkOrderDetailsEntity;
import com.blockchain.platform.pojo.entity.WorkOrderEntity;
import com.blockchain.platform.pojo.vo.WorkOrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 工单数据库接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-26 2:37 PM
 **/
@Mapper
public interface WorkOrderMapper {

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
    Integer modify(WorkOrderEntity entity);

    /**
     * 回复
     * @param entity
     * @return
     */
    Integer reply(WorkOrderDetailsEntity entity);

    /**
     * 详情
     * @param dto
     * @return
     */
    WorkOrderEntity findByCondition(BaseDTO dto);
}
