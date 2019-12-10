package com.blockchain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.annotation.Sharding;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.DealFlowEntity;
import com.blockchain.platform.pojo.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 盘口数据接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-29 3:13 PM
 **/
@Mapper
@Sharding(table = "deal_flow",field = "symbol")
public interface DealFlowMapper extends BaseMapper<DealFlowEntity> {

    /**
     * 获取盘口数据
     * @param dto
     * @return
     */
    DealFlowEntity findLastDeal(BaseDTO dto);

    /**
     * 获取有交易流水的用户
     * @param dto
     * @return
     */
    List<UserVO>  findUserDealFlow(BaseDTO dto);
}
