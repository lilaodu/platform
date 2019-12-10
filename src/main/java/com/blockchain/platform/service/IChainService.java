package com.blockchain.platform.service;

import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.ChainWithdrawEntity;
import com.blockchain.platform.pojo.vo.DepositVO;
import com.blockchain.platform.pojo.vo.WithdrawVO;

import java.util.List;

/**
 * 代币流水服务接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-22 2:22 PM
 **/
public interface IChainService {



    /**
     * 获取钱包地址信息
     * @param dto
     * @return
     */
    String findWalletAddress(BaseDTO dto);

    /**
     * 充币记录
     * @param dto
     * @return
     */
    List<DepositVO>  deposit(PageDTO dto);

    /**
     * 提币记录
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
     * 编辑提币流水
     * @param entity
     * @return
     */
    Boolean modify(ChainWithdrawEntity entity);

    /**
     * 提币
     * @param entity
     * @return
     */
    Boolean withdrawal(ChainWithdrawEntity entity);


}
