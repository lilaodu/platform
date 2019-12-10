package com.blockchain.platform.mapper;

import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 团队Mapper
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-06 11:13 AM
 **/
@Mapper
public interface TeamMapper {


    /**
     * 推广奖励
     * @param dto
     * @return
     */
    ExtensionVO extension(BaseDTO dto);

    /**
     * 推广奖励
     * @param dto
     * @return
     */
    ExtensionVO extensionZT(BaseDTO dto);
    
    /**
     * 秒合约昨日收益
     * @param dto
     * @return
     */
    List<ProfitVO> contract(BaseDTO dto);
    
    /**
     * 秒合约USDT团队总 交易额
     * @param dto
     */
    ExtensionVO tradeQuota(BaseDTO dto);
    
    /**
     * 昨日总业绩
     * @param dto
     */
    ExtensionVO teamLockNum(BaseDTO dto);
    
    /**
     * 星球计划昨日收益
     * @param dto
     * @return
     */
    List<ProfitVO> profit(BaseDTO dto);

    /**
     * 秒合约收益明细
     * @param dto
     * @return
     */
    List<RewardsVO> contracts(PageDTO dto);

    /**
     * 星球计划收益明细
     * @param dto
     * @return
     */
    List<RewardsVO> profits(PageDTO dto);


    /**
     * 推广信息
     * @param dto
     * @return
     */
    List<TeamDetailVO> stage(PageDTO dto);
}
