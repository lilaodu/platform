package com.blockchain.platform.service;

import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.GradeDTO;
import com.blockchain.platform.pojo.dto.KlineDTO;
import com.blockchain.platform.pojo.dto.TaskDTO;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 定时任务服务接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-26 10:13 AM
 **/
public interface ITaskService {

    /**
     * 筛选用户信息
     * @param dto
     * @return
     */
    List<UserEntity> findUserByCondition(TaskDTO dto);

    /**
     * 获取最后一次升级记录
     * @param dto
     * @return
     */
    UserUpgradeFlowEntity findLastUpgrade(BaseDTO dto);


    /**
     * 查询解锁收益数据
     * @param dto
     * @return
     */
    List<UnLockProfitVO> findUnLockProfitData(TaskDTO dto);


    /**
     * 查询昨日合约交易数据
     * @param dto
     * @return
     */
    List<ContractProfitVO> findContractProfitData(TaskDTO dto);

    /**
     * 添加日志
     * @param entity
     * @return
     */
    Boolean addLockProfitEntity(UserLockProfitEntity entity);

    /**
     * 团队交易量
     * @param dto
     * @return
     */
    BigDecimal teamTradeVolume(TaskDTO dto);

    /**
     * 用户升降级
     * @param entity
     */
    Boolean addUserUpgradeFlow(UserUpgradeFlowEntity entity);

    /**
     * 升级直推VO
     * @param dto
     * @return
     */
    Map<Integer, UpgradeDirectVO> findDirectHold(TaskDTO dto);

    /**
     * 交易收益记录
     * @param entity
     * @return
     */
    Boolean addTradeProfitEntity(UserTradeProfitEntity entity);
}
