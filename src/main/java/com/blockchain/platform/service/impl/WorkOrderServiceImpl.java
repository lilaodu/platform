package com.blockchain.platform.service.impl;

import com.blockchain.platform.mapper.WorkOrderMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.WorkOrderDetailsEntity;
import com.blockchain.platform.pojo.entity.WorkOrderEntity;
import com.blockchain.platform.pojo.vo.WorkOrderVO;
import com.blockchain.platform.service.IWorkOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工单服务实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-26 2:37 PM
 **/
@Service
public class WorkOrderServiceImpl implements IWorkOrderService {

    /**
     * 工单数据库接口
     */
    @Resource
    private WorkOrderMapper mapper;

    @Override
    public List<WorkOrderVO> query(PageDTO dto) {
        return mapper.query( dto);
    }

    @Override
    public Boolean modify(WorkOrderEntity entity) {
        return mapper.modify( entity) > 0;
    }

    @Override
    public Boolean reply(WorkOrderDetailsEntity entity) {
        return mapper.reply( entity) > 0;
    }

    @Override
    public WorkOrderEntity findByCondition(BaseDTO dto) {
        return null;
    }
}
