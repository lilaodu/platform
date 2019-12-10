package com.blockchain.platform.mapper;

import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.KlineDTO;
import com.blockchain.platform.pojo.dto.TaskDTO;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 定时任务数据接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-26 10:16 AM
 **/
@Mapper
public interface TaskMapper {

    /**
     * 筛选用户信息
     * @param dto
     * @return
     */
    List<UserEntity> findUserByCondition(TaskDTO dto);

    /**
     * 获取最后一次更新信息
     * @param dto
     * @return
     */
    UserUpgradeFlowEntity findLastUpgrade(BaseDTO dto);


    /**
     * 添加收益记录
     * @param entity
     * @return
     */
    Integer addLockProfitEntity(UserLockProfitEntity entity);

    /**
     * 添加用户升降级记录
     * @param entity
     * @return
     */
    Integer addUserUpgradeFlow(UserUpgradeFlowEntity entity);

    /**
     * 直推数据
     * @param dto
     * @return
     */
    List<UserWalletEntity> findDirectHold(TaskDTO dto);

    /**
     * 添加交易收益
     * @param entity
     * @return
     */
    Integer addTradeProfitEntity(UserTradeProfitEntity entity);

    /**
     * 团队交易量
     * @param dto
     * @return
     */
    BigDecimal teamTradeVolume(TaskDTO dto);

    /**
     * 昨日合约收益数据
     * @param dto
     * @return
     */
    List<ContractProfitVO> findContractProfitData(TaskDTO dto);


    /**
     * 昨日解锁数据
     * @param dto
     * @return
     */
    List<UnLockProfitVO> findUnLockProfitData(TaskDTO dto);
}
