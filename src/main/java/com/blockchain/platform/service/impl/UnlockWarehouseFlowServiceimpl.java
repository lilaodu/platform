package com.blockchain.platform.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.ExConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.mapper.SecondsContractOrderMapper;
import com.blockchain.platform.mapper.UnlockWarehouseFlowMapper;
import com.blockchain.platform.mapper.UserLockWarehouseMapper;
import com.blockchain.platform.mapper.UserWalletMapper;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.LockWarehouseConfigEntity;
import com.blockchain.platform.pojo.entity.SecondsContractOrderEntity;
import com.blockchain.platform.pojo.entity.UnlockWarehouseFlowEntity;
import com.blockchain.platform.pojo.entity.UserLockWarehouseEntity;
import com.blockchain.platform.pojo.vo.UnlockWarehouseFlowVO;
import com.blockchain.platform.service.IUnlockWarehouseFlowService;
import com.blockchain.platform.utils.IntUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

@Service
@Log4j2
public class UnlockWarehouseFlowServiceimpl extends ServiceImpl<UnlockWarehouseFlowMapper, UnlockWarehouseFlowEntity> implements IUnlockWarehouseFlowService {

	@Resource
    private UnlockWarehouseFlowMapper mapper;
	
	@Resource
    private UserLockWarehouseMapper userLockWarehousemapper;
	
	@Resource
    private RedisPlugin redisPlugin;
	
	@Resource
	private SecondsContractOrderMapper secondsContractOrderMapper;
	
	@Resource
    private UserWalletMapper userWalletMapper;
	
	/**
	 * 为用户解仓
	 * 
	 * 1.传入用户id
	 * 2.修改用户锁仓表
	 * 3.更新用户解锁流水
	 * 
	 */
	@Transactional(rollbackFor = {Exception.class})
	public UnlockWarehouseFlowEntity unlockWarehouse(Integer userLockWarehouseId,Integer userId,UnlockWarehouseFlowEntity unlockWarehouseFlowEntity) {
		
		//配置
		LockWarehouseConfigEntity lockWarehouseConfig = redisPlugin.get(RedisConst.LOCK_WAREHOUSE_CONFIG);
		int configDayNum = lockWarehouseConfig.getDayNum();//规定天数
		BigDecimal configDayRatio = lockWarehouseConfig.getUnlockDayRatio();//每天解锁的比例
		//查询用户锁仓情况
		QueryWrapper<UserLockWarehouseEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(BizConst.sql.STATE, BizConst.BIZ_STATUS_VALID).eq(BizConst.sql.ID,userLockWarehouseId).eq(BizConst.sql.userId,userId);
    	UserLockWarehouseEntity entity = userLockWarehousemapper.selectOne(queryWrapper);
    	if(entity == null) {
    		return null;
    	}
    	//判断100天到了没//不需要判断是否解锁完
    	Date lockDate = entity.getLockDate();
    	long day = DateUtil.between(lockDate, new Date(), DateUnit.DAY);
    	if(day > configDayNum) {
    		entity.setState(BizConst.BIZ_STATUS_EXPIRY);
    		userLockWarehousemapper.updateById(entity);
    		return null;
    	}
    	//判断总解锁数量是否对
    	BigDecimal NewUnlockNumZ = (configDayRatio.multiply(entity.getLockNum())).add(entity.getUnlockNum());
    	if(NewUnlockNumZ.compareTo(entity.getLockNum()) == 1 ) {
    		return null;
    	}
    	//修改用户锁仓情况
    	if(unlockWarehouseFlowEntity.getTaskNum() == 5) {
    		entity.setUnlockDayNum(entity.getUnlockDayNum() + IntUtils.INT_ONE);
    	}
    	entity.setUnlockNum(NewUnlockNumZ);
    	entity.setVersion(entity.getVersion()+ IntUtils.INT_ONE);
    	if(userLockWarehousemapper.updateById(entity) <= IntUtils.INT_ZERO) {
    		return null;
    	}
    	//解仓(修改钱包)并添加解锁流水
    	//修改钱包
    	BaseDTO basDto = new BaseDTO();		
    	basDto.setUnlockNum(configDayRatio.multiply(entity.getLockNum()));
    	basDto.setUserId(userId);
    	basDto.setSymbol(unlockWarehouseFlowEntity.getSymbol());
    	int num = userWalletMapper.unlockUserWallet(basDto);
		if(num <= 0) {
			throw new BcException(ExConst.SYS_ERROR );
		}
		//更新当天解锁流水
		unlockWarehouseFlowEntity.setUserId(userId);
		unlockWarehouseFlowEntity.setUnlockDate(new Date());
		unlockWarehouseFlowEntity.setSpaceDayNum((int)day);
		unlockWarehouseFlowEntity.setUnlockNum(configDayRatio.multiply(entity.getLockNum()).add(unlockWarehouseFlowEntity.getUnlockNum()));
		unlockWarehouseFlowEntity.setUnlockNumZ(NewUnlockNumZ);
		mapper.updateById(unlockWarehouseFlowEntity);
		if(num <= 0) {
			throw new BcException(ExConst.SYS_ERROR );
		}

		return unlockWarehouseFlowEntity;
	}
	
	/**
	 * 解锁任务要求
	 * (当天5次BTB,每次数额为用户锁币总数的1%.才算完成,每天只能解锁二次)
	 * 获取所有锁仓订单,计算锁币总和,计算需要的消费金额
	 * 判断秒合约消费额,如果符合任务要求,则创建解锁流水,并加一次
	 * 判断次数是否符合任务次数要求
	 * 提交符合的解锁的锁币订单给解锁接口
	 * @param sCOEntity
	 * @return
	 */
	public void taskDemand(SecondsContractOrderEntity sCOEntity) {
		//查询用户所有锁币订单
		QueryWrapper<UserLockWarehouseEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(BizConst.sql.STATE, BizConst.BIZ_STATUS_VALID).eq(BizConst.sql.userId,sCOEntity.getUserId());
		List<UserLockWarehouseEntity> entityList = userLockWarehousemapper.selectList(queryWrapper);
		BigDecimal lockTotal = new BigDecimal("0");
		for(UserLockWarehouseEntity userLockWarehouse : entityList) {
			lockTotal = lockTotal.add(userLockWarehouse.getLockNum());
		}
		//获取锁仓配置
    	LockWarehouseConfigEntity lockWarehouseConfig = redisPlugin.get(RedisConst.LOCK_WAREHOUSE_CONFIG);
    	//计算解锁所需的消费额
    	BigDecimal lockPrice = lockTotal.multiply(lockWarehouseConfig.getTaskRatio());
    	//判断当次秒合约消费额是否到
    	if(StrUtil.equalsIgnoreCase(sCOEntity.getPayCoin(), lockWarehouseConfig.getCoin()) && lockPrice.compareTo(sCOEntity.getPrice()) < 1 ) {
    		//提前创建解锁流水,用于添加任务有效次数
    		Map<String,Object> param=new HashMap<String,Object>();
    		param.put(BizConst.sql.STATE,BizConst.BIZ_STATUS_VALID);
    		param.put(BizConst.sql.userId,sCOEntity.getUserId());
    		String[] TaskNums = lockWarehouseConfig.getTaskNum().split(",");
    		for(int j=0;j<entityList.size();j++){
    			param.put(BizConst.sql.UserLockWarehouseId,entityList.get(j).getId());
    			
    			//获取当天解锁的数据
    			UnlockWarehouseFlowEntity unlockWarehouseFlowEntity = mapper.dayUnlockWarehouseFlow(param);
    			if(!BeanUtil.isEmpty(unlockWarehouseFlowEntity) ) {
    				//genxing
    				unlockWarehouseFlowEntity.setTaskNum(unlockWarehouseFlowEntity.getTaskNum() + IntUtils.INT_ONE);
    				mapper.updateById(unlockWarehouseFlowEntity);
    			}else {
    				//chuangjian
    	    		UnlockWarehouseFlowEntity newUnlockWarehouseFlowEntity = new UnlockWarehouseFlowEntity();
    	    		newUnlockWarehouseFlowEntity.setUserId(sCOEntity.getUserId());
    	    		newUnlockWarehouseFlowEntity.setUnlockDate(new Date());
    	    		newUnlockWarehouseFlowEntity.setCreateTime(new Date());
    	    		newUnlockWarehouseFlowEntity.setTaskNum(IntUtils.INT_ONE);
    	    		newUnlockWarehouseFlowEntity.setSymbol(entityList.get(j).getSymbol());
    	    		newUnlockWarehouseFlowEntity.setUserLockWarehouseId(Integer.parseInt(param.get(BizConst.sql.UserLockWarehouseId).toString()));
    				mapper.insert(newUnlockWarehouseFlowEntity);
    			}
    			//是否完成任务次数
    	    	if( !BeanUtil.isEmpty(unlockWarehouseFlowEntity) && Arrays.asList(TaskNums).contains(StrUtil.toString(unlockWarehouseFlowEntity.getTaskNum()))  ) {
    	    		unlockWarehouse(entityList.get(j).getId(), sCOEntity.getUserId(),unlockWarehouseFlowEntity);
    	    	}
			}
		}
	}
	
	
	/**
	 * 获取当天任务有效次数
	 */
	public Integer dayTaskNum(Integer userId) {
		
		List<UnlockWarehouseFlowEntity> unlockWarehouseFlowEntitys = mapper.dayFlowList(userId);
		
		int okNum = 0 ;
		for(UnlockWarehouseFlowEntity entity : unlockWarehouseFlowEntitys) {
			okNum = okNum > entity.getTaskNum() ? okNum : entity.getTaskNum();
		}
		
		//查询用户所有锁币订单
//		QueryWrapper<UserLockWarehouseEntity> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq(BizConst.sql.STATE, BizConst.BIZ_STATUS_VALID).eq(BizConst.sql.userId,userId);
//		List<UserLockWarehouseEntity> entityList = userLockWarehousemapper.selectList(queryWrapper);
//		BigDecimal lockTotal = new BigDecimal("0");
//		for(UserLockWarehouseEntity userLockWarehouse : entityList) {
//			lockTotal = lockTotal.add(userLockWarehouse.getLockNum());
//		}
//		//获取锁仓配置
//    	LockWarehouseConfigEntity lockWarehouseConfig = redisPlugin.get(RedisConst.LOCK_WAREHOUSE_CONFIG);
//    	//计算解锁所需的消费额
//    	BigDecimal lockPrice = lockTotal.multiply(lockWarehouseConfig.getTaskRatio());
//    	//查询今天用户秒合约的单子
//    	List<SecondsContractOrderEntity> OrderOnDay = secondsContractOrderMapper.queryOrderOnDay(userId);
//    	//对比判断
//    	int okNum = 0 ;
//    	for(SecondsContractOrderEntity secondsContractOrder : OrderOnDay) {
//    		if(StrUtil.equalsIgnoreCase(secondsContractOrder.getPriceCion(), lockWarehouseConfig.getCoin()) && lockPrice.compareTo(secondsContractOrder.getPrice()) < 1 ) {
//    			okNum = okNum +1;
//    		}
//    	}
    	
    	return okNum;
	}
	
	
	
	/**
	 * 当天如果没有进行秒合约,接就是没有触发解锁
	 * 也需要生成flow
	 * 
	 * 查询用户秒合约订单
	 * 判断用户对应秒合约订单,当天的流水
	 * 添加流水
	 * 
	 */
	public void destruction() {
		//查询所有锁币订单
		List<UserLockWarehouseEntity> warehouses =  mapper.needDestruction();
		LockWarehouseConfigEntity lockWarehouseConfig = redisPlugin.get(RedisConst.LOCK_WAREHOUSE_CONFIG);
        //增加-3小时
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date());
		rightNow.add(Calendar.HOUR, -3);
		Date dt1 = rightNow.getTime();
  
		for(UserLockWarehouseEntity warehouse : warehouses) {
			
			BigDecimal ratio = lockWarehouseConfig.getUnlockDayRatio();
    		warehouse.setExpireNum((ratio.multiply(warehouse.getLockNum())).add(warehouse.getExpireNum()));
    		warehouse.updateById();
    		
			if(BeanUtil.isEmpty(warehouse.getId())) {
				UnlockWarehouseFlowEntity newUnlockWarehouseFlowEntity = new UnlockWarehouseFlowEntity();
	    		newUnlockWarehouseFlowEntity.setUserId(warehouse.getUserId());
	    		newUnlockWarehouseFlowEntity.setUnlockDate(dt1);
	    		newUnlockWarehouseFlowEntity.setCreateTime(dt1);
	    		newUnlockWarehouseFlowEntity.setTaskNum(IntUtils.INT_ONE);
	    		newUnlockWarehouseFlowEntity.setSymbol(warehouse.getSymbol());
	    		newUnlockWarehouseFlowEntity.setUnlockNum(new BigDecimal("0"));
	    		newUnlockWarehouseFlowEntity.setUserLockWarehouseId(warehouse.getId());
	    		mapper.insert(newUnlockWarehouseFlowEntity);
			}
			
			
		}
	}

	/**
	 * 统计销毁列表
	 */
	public List<UnlockWarehouseFlowVO> noUnlock(Integer userId) {
		return userLockWarehousemapper.noUnlock(userId);
	}

	/**
	 * 销毁的记录销毁列表
	 */
	public List<UnlockWarehouseFlowVO> allUnlock(Integer userId) {
		return userLockWarehousemapper.allUnlock(userId);
	}
	
	

}
