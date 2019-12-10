package com.blockchain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.ChainWithdrawEntity;
import com.blockchain.platform.pojo.entity.OTCTrustEntity;
import com.blockchain.platform.pojo.vo.DepositVO;
import com.blockchain.platform.pojo.vo.WithdrawVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 代币流水数据库接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-22 2:22 PM
 **/
@Mapper
public interface ChainMapper  extends BaseMapper<ChainWithdrawEntity> {

    /**
     * 充币流水记录
     * @param dto
     * @return
     */
    List<DepositVO> deposit(PageDTO dto);

    /**
     * 提币流水记录
     * @param dto
     * @return
     */
    List<WithdrawVO> withdraw(PageDTO dto);

    /**
     * 提币流水详情
     * @param dto
     * @return
     */
    ChainWithdrawEntity findWithdraw(BaseDTO dto);

    /**
     * 新增提币流水
     * @param entity
     * @return
     */
    Integer addWithdrawFlow(ChainWithdrawEntity entity);

    /**
     * 修改提币流水
     * @param entity
     * @return
     */
    Integer modifyWithdraw(ChainWithdrawEntity entity);
}
