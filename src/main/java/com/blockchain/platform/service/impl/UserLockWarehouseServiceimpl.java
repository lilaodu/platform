package com.blockchain.platform.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.mapper.UserMapper;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.UpgradeDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.entity.UserUpgradeEntity;
import com.blockchain.platform.pojo.vo.DirectUserVO;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.constant.ExConst;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.mapper.UserLockWarehouseMapper;
import com.blockchain.platform.mapper.UserWalletMapper;
import com.blockchain.platform.pojo.entity.UserLockWarehouseEntity;
import com.blockchain.platform.service.IUserLockWarehouseService;

@Service
public class UserLockWarehouseServiceimpl extends ServiceImpl<UserLockWarehouseMapper, UserLockWarehouseEntity> implements IUserLockWarehouseService {
	
	@Resource
    private UserLockWarehouseMapper mapper;
	
	@Resource
    private UserWalletMapper userWalletMapper;

	@Resource
	private UserMapper userMapper;

	/**
	 * 缓存数
	 */
	@Resource
	private RedisPlugin redisPlugin;
	
	/**
	 * 添加锁仓订单,并且操作钱包
	 * @param entity
	 * @return
	 */
	@Transactional(rollbackFor = {Exception.class})
	public boolean addLockWarehouseAndWallet(UserLockWarehouseEntity entity) {
		
		//锁仓
		int num = userWalletMapper.lockUserWallet(entity);
		//赠送
		int num2 = userWalletMapper.giveUserWallet(entity);
		//添加订单
		int num3 = mapper.insert(entity);
		
		if(num > 0 && num2 > 0 && num3 > 0 ){
			return true;
        } else {
            throw new BcException(ExConst.SYS_ERROR );
        }
	}

	/**
	 * 查询锁仓总量
	 * @param userId
	 * @return
	 */
	public BigDecimal queryLockNumTotal(int userId) {
		return mapper.queryLockNumTotal(userId);
	}


	/**
	 * 锁闭账号升级
	 * @param userId
	 */
	@Async
	@Override
	public void upgrade(Integer userId) {
		UserDTO dto = UserDTO.builder().id( userId).build();
		// 获取当前用户
		UserEntity entity = userMapper.findByCondition( dto);

		// 获取配置信息
		Map<String, UserUpgradeEntity> config = redisPlugin.hget( RedisConst.PLATFORM_UPGRADE_CONFIG,
				BizConst.UpgradeConst.UPGRADE_TYPE_LOCK);

		// 计算父类
		if ( IntUtils.greaterThanZero( entity.getParentId())
				&& MapUtil.isNotEmpty( config)) {
			recursion( entity, config);
		}
	}

	/**
	 * 递归数据
	 * @param entity
	 * @param config
	 */
	public void recursion( UserEntity entity, Map<String, UserUpgradeEntity> config) {
		// 获取上级用户
		UserEntity upg = userMapper.findByCondition(UserDTO.builder().id( entity.getParentId()).build());

		int lockLv = upg.getLockLv();

		// 升级条件
		UserUpgradeEntity userUpgradeEntity = config.get( lockLv ++);

		if ( ObjectUtil.isNotEmpty( userUpgradeEntity)) {
			// 当前等级策略
			int directChildLv = IntUtils.toInt( userUpgradeEntity.getDirectChildLv());

			UpgradeDTO dto = new UpgradeDTO();
			if ( directChildLv > 0) {
				dto.setDirectChildLv( directChildLv);
			}
			// 当前用户
			dto.setUserId( upg.getId());


			DirectUserVO data = mapper.findUserDirectData( dto);

			Integer directNum = data.getNum();

			BigDecimal directTotal = data.getAmount();

			// 是否执行升级
			if ( BigDecimalUtils.compare( directTotal, userUpgradeEntity.getDirectTotal())
					&& IntUtils.compare( directNum, userUpgradeEntity.getDirectNum())) {

				UserEntity db = new UserEntity();
				db.setLockLv( IntUtils.toInt( userUpgradeEntity.getLv()));
				db.setId( upg.getId());
				userMapper.update( db);

				// 获取升级
				recursion( upg, config);
			}
		}
	}
}
