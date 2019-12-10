package com.blockchain.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.DrawMoneyFlowEntity;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.dto.UserWalletDTO;
import com.blockchain.platform.pojo.entity.UserAssetsFlowEntity;
import com.blockchain.platform.pojo.entity.UnlockAssetsEntity;
import com.blockchain.platform.pojo.entity.UserAssetsFlowEntity;
import com.blockchain.platform.pojo.entity.UserWalletEntity;
import com.blockchain.platform.pojo.vo.CapitalVO;
import com.blockchain.platform.pojo.vo.DigVO;
import com.blockchain.platform.pojo.dto.TransferDTO;
import com.blockchain.platform.pojo.vo.UserWalletVO;

import java.util.List;


/**
 * 用户钱包管理接口
 **/
public interface IUserWalletService  extends IService<UserWalletEntity> {


    /**
     * 新增或者修改
     * @param entity
     * @return
     */
    Boolean modify(UserWalletEntity entity);

    /**
     * 查询用户资产
     * @param dto
     * @return
     */

    List<CapitalVO> getAssets(BaseDTO dto);
    
    /**
     * 查询用户所有资产
     * @param dto
     * @return
     */
    List<CapitalVO> getAssetsAll(BaseDTO dto);
    
    /** 
     *  划转冻结
     */ 
    CapitalVO transferPending(UserDTO userDTO);
     
     /** 
      *  验证余额
      */ 
    UserWalletEntity validateBalance(UserWalletDTO userWalletDTO);
    
    /**
     *  获取资产流水
     */
     List<UserAssetsFlowEntity> getAssetsFlow(PageDTO dto);
     
     /** 
      *  带解锁列表
      */
     List<UnlockAssetsEntity> queryUnlockTxFlow();
     
      /**
       * 查询用户资产
       */
     UserWalletEntity queryUserWallet(UserWalletDTO walletDTO);
     /**
      * 划转资产
      * @param dto
      * @return
      */
     boolean transferAssets(TransferDTO dto);
     /**
      * 添加资产流水
      * @param entity
      * @param flowEntity
      * @return
      */
     boolean addAssetsFlow(UserAssetsFlowEntity entity, DrawMoneyFlowEntity flowEntity);
     /**
      * 当日挖矿信息
      * @param userDTO
      * @return
      */
     DigVO dig(UserDTO userDTO);

    /**
     * 获取用户所有的钱包信息
     * @param dto
     * @return
     */
     UserWalletEntity findByCondition(BaseDTO dto);

    /**
     * 更新用户对应货币OTC账户信息
     * @param dto
     * @return
     */
     Boolean updateUserWallet(UserWalletDTO dto);
}
