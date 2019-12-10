package com.blockchain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.DrawMoneyFlowEntity;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.dto.UserWalletDTO;
import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import com.blockchain.platform.pojo.entity.SecondsContractOrderEntity;
import com.blockchain.platform.pojo.entity.UserAssetsFlowEntity;
import com.blockchain.platform.pojo.entity.UserLockWarehouseEntity;
import com.blockchain.platform.pojo.entity.UserWalletEntity;
import com.blockchain.platform.pojo.vo.CapitalVO;
import com.blockchain.platform.pojo.vo.DigVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
/**
 * 用户钱包数据库接口
 */
@Mapper
public interface UserWalletMapper  extends BaseMapper<UserWalletEntity> {

	/**
     * 用户otc钱包信息
     * @return
     */
    List<UserAssetsFlowEntity> pendingFlow(UserDTO userDTO);

	/**
    *  查询资产
    */
    List<CapitalVO> queryCapital(BaseDTO dto);
    
    /**
     *  所有查询资产
     */
     List<CapitalVO> queryCapitalAll(BaseDTO dto);

	/**
	 * 验证余额
	 * @param walletDTO
	 * @return
	 */
	UserWalletEntity validateBalance(UserWalletDTO walletDTO);

	/**
	 *  更新钱包资产
	 */
	int updateTAssets(UserWalletDTO walletDTO);

	/**
	 * 更新交易钱包资产
	 * @param walletDTO
	 * @return
	 */
	int updateAssets(UserWalletDTO walletDTO);

	/**
	 * 更新OTC资产
	 * @param walletDTO
	 * @return
	 */
	int updateOTCAssets(UserWalletDTO walletDTO);

	/**
	 * 查询资产流水c2c
	 * @param dto
	 * @return
	 */
	List<UserAssetsFlowEntity> queryAssetsFlow(PageDTO dto);
	/**
	 * 插入流水
	 * @param entity
	 * @return
	 */
	int addAssetsFlow(UserAssetsFlowEntity entity);
	/**
	 * 跟新钱包转出
	 * @param map
	 * @return
	 */
	int updateWalletByOuts(Map map);
	/**
	 * 插入工作记录
	 * @param flowEntity
	 * @return
	 */
	int addWorkFlow(DrawMoneyFlowEntity flowEntity);
	/**
	 * 挖矿账户统计
	 * @param userDTO
	 * @return
	 */
	DigVO dig(UserDTO userDTO);

	/**
	 * 获取用户钱包详情
	 * @param dto
	 * @return
	 */
	UserWalletEntity findByCondition(BaseDTO dto);

	/**
	 * 更新用户对应货币OTC账户信息
	 * @param dto
	 * @return
	 */
	Integer updateUserWallet(UserWalletDTO dto);

	/**
	 * 创建用户钱包
	 * @param dto
	 * @return
	 */
	Integer addUserWallet(UserWalletDTO dto);

	/**
	 * 获取用户可用余额
	 * @param dto
	 * @return
	 */
	BigDecimal findUsableWallet(BaseDTO dto);
	
	
	int frozenUserWallet(OrderFlowEntity entity);
	
	int mdfUserBaseWalletsForList(Map map);
	
	int mdfUserWalletsForList(Map map);
	
	int mdfUserWallet(OrderFlowEntity entity);
	
	int mdfBaseUserWallet(OrderFlowEntity entity);
	
	int mdfUserWalletForCancel(OrderFlowEntity entity);
	
	/**
	 * 锁仓
	 * @param entity
	 * @return
	 */
	int lockUserWallet(UserLockWarehouseEntity entity);
	
	/**
	 * 赠送
	 * @param entity
	 * @return
	 */
	int giveUserWallet(UserLockWarehouseEntity entity);
	
	/**
	 * 解仓
	 * @param unLock
	 * @param userId
	 * @return
	 */
	int unlockUserWallet(BaseDTO basDto);

	/**
	 * 创建用户钱包,添加地址
	 * @param entity
	 * @return
	 */
	Integer addWallet(UserWalletEntity entity);

	/**
	 * 修改用户钱包地址
	 */
	Integer updateWallet(UserWalletEntity entity);

	/**
	 * 获取用户合约账户资产
	 * @param dto
	 * @return
	 */
	List<Map<String, List<CapitalVO>>> findUserOtcAsset(BaseDTO dto);
	
	
	int deductionUserWallet(SecondsContractOrderEntity entity);

	/**
	 * 获取用户钱包地址
	 * @return
	 */
	UserWalletEntity findWalletAddress(BaseDTO dto);


}
