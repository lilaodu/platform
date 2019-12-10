package com.blockchain.platform.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import cn.hutool.core.util.ObjectUtil;
import com.blockchain.platform.dictionary.DictionaryFactory;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.HuobiKLineEntity;
import com.blockchain.platform.utils.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.config.SlbConfig;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.ExConst;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.mapper.SecondsContractOrderMapper;
import com.blockchain.platform.mapper.UserWalletMapper;
import com.blockchain.platform.pojo.entity.SecondsContractOrderEntity;
import com.blockchain.platform.service.ISecondsContractOrderService;
import com.blockchain.platform.utils.IntUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;

@Service
public class SecondsContractOrderServiceImpl extends ServiceImpl<SecondsContractOrderMapper, SecondsContractOrderEntity> implements ISecondsContractOrderService {

	@Resource
    private SecondsContractOrderMapper mapper;
	
	@Resource
	private UserWalletMapper userWalletMapper;


	@Autowired
	DictionaryFactory dictionaryFactory;
	
	/**
     * slb配置
     */
    @Resource
    private SlbConfig config;
    
	ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	
	/**
	 * 添加秒合约订单,并且操作钱包
	 * @param entity
	 * @return
	 */
	@Transactional(rollbackFor = {Exception.class},isolation = Isolation.REPEATABLE_READ)
    public boolean addSecondsContractOrderEntity(SecondsContractOrderEntity entity) {
		if (ObjectUtil.isNotEmpty( entity.getId())) {
			SecondsContractOrderEntity staus = mapper.selectById(entity.getId());
			if(staus.getState() > 1) return false;
		}
		//扣款
		int num = userWalletMapper.deductionUserWallet(entity);
		if(num > 0){
			num = entity.getId() == null?mapper.insert(entity):mapper.updateById(entity);
        } else {
            throw new BcException(ExConst.SYS_ERROR );
        }
		//添加
		return num >0?true:false;
    }


	@Transactional(rollbackFor = {Exception.class},isolation =Isolation.REPEATABLE_READ )
	public boolean cancelOrder(SecondsContractOrderEntity entity)
	{



		entity=this.getById(entity.getId());
		if(entity.getState()!=1) return false;
		int userId=entity.getUserId();
		BigDecimal amount=entity.getPrice();
		String symbol=entity.getPayCoin();
		int flag= userWalletMapper.updateTAssets(UserWalletDTO.builder()
				.userId(userId)
				.symbol(symbol)
				.balance(amount.negate())
				.frozenBalance(BigDecimal.ZERO)
				.build());



		mapper.deleteById(entity.getId());
		return flag>0;

	}

	/**
	 * 开奖
	 * 1.获取当前价格
	 * 2.判断输赢
	 * 4.操作钱包
	 * 5.通知
	 * 6.更新订单
	 */
	public void lottery(SecondsContractOrderEntity entity) {
		//获取当前价格secondsContractEntity.getSymbol()
		BigDecimal currencyMarketTrade = getCurrencyMarketTrade(entity.getCoinPair());
		entity.setEndPrice(currencyMarketTrade);//new BigDecimal("50")
		//判断输赢
		int bs = entity.getStartPrice().compareTo(entity.getEndPrice());
		if((IntUtils.equals(entity.getType(), BizConst.SecondsContract.RISE) && bs == -1) || 
		   (IntUtils.equals(entity.getType(), BizConst.SecondsContract.FALL) && bs == 1) ) {//猜中
			entity.setIsWin(BizConst.SecondsContract.RESULT_WIN);
			//给钱
			entity.setWinPrice(
					BigDecimalUtils.divi(
							entity.getPrice().multiply(entity.getProfit()),
							new BigDecimal(100)
					).add(entity.getPrice())
			);
		}
		else if((IntUtils.equals(entity.getType(), BizConst.SecondsContract.RISE) && bs == 1) ||
		   (IntUtils.equals(entity.getType(), BizConst.SecondsContract.FALL) && bs == -1)
				|| IntUtils.equals(entity.getType(), BizConst.SecondsContract.FLAT) ) {//没猜中
			entity.setIsWin(BizConst.SecondsContract.RESULT_FAIL);
			//不给钱
			entity.setWinPrice(BizConst.SecondsContract.ZERO);
		}else{//
			entity.setWinPrice(BigDecimal.valueOf(0));
			entity.setIsWin(BizConst.SecondsContract.RESULT_FAIL);
		}
		entity.setSettlementTime(new Date());
		entity.setState(4);
		//更新订单，操作钱包
		addSecondsContractOrderEntity(entity);
		//通知
//		Set<String> users = CollUtil.newHashSet();
//		users.add(entity.getUserId().toString());
//		NotifyDTO dto = NotifyDTO.builder().symbol(entity.getCoinPair()).userIds(users).build();
		for (String action : slb()) {
			cachedThreadPool.execute(new Runnable() {
				public void run() {
					HttpRequest.post(action).header("content-type", AppConst.RESPONSE_CONTENT_TYPE)
							.body(JSON.toJSONString(entity)).execute();
				}
			});
		}
		
		
	}
	
	/**
     * 秒合约开奖通知地址
     * @return
     */
    protected List<String> slb() {
        List<String> ips = CollUtil.newArrayList();
        String [] values = StrUtil.split( config.getIpConfig(), StrUtil.COMMA);

        for (String ip : values) {
            ips.add( StrUtil.concat(Boolean.FALSE, ip, StrUtil.COLON,
                    config.getPort(),
                    config.getContextPath(),
                    config.getScaction()));
        }
        return ips;
    }
	

	/**
	 * 获取当前价格
	 * symbol（ethusdt）
	 * @return
	 */
	public BigDecimal getCurrencyMarketTrade(String symbol) {




		HuobiKLineEntity kLineEntity= dictionaryFactory.getLastHuoBiKline(symbol);
		
		return kLineEntity.getClose();
	}

	
	public IPage<SecondsContractOrderEntity> actives(PageDTO dto, UserDTO loginUser) {
		QueryWrapper<SecondsContractOrderEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(BizConst.sql.userId, loginUser.getId()).orderByAsc(BizConst.sql.STATE).orderByDesc(BizConst.sql.createTime);
    	IPage<SecondsContractOrderEntity> ipage = new Page<>(dto.getPageNum(),dto.getPageSize());
    	return mapper.selectPage(ipage,queryWrapper);
        
	}
	
	public List<SecondsContractOrderEntity> activeings( UserDTO loginUser) {
		
		List<SecondsContractOrderEntity> activeings = null;
		try {
			QueryWrapper<SecondsContractOrderEntity> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq(BizConst.sql.userId, loginUser.getId()).eq(BizConst.sql.STATE, IntUtils.INT_ONE).orderByAsc(BizConst.sql.createTime);;
			activeings = mapper.selectList(queryWrapper);
		} catch (Exception e) {
			return activeings;
		}
		
		return activeings;
        
	}


	@Override
	public int incompleteOrder(Integer state,UserDTO loginUser) {
		QueryWrapper<SecondsContractOrderEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(BizConst.sql.STATE, state).eq(BizConst.sql.userId, loginUser.getId());
		List<SecondsContractOrderEntity> entitys = mapper.selectList(queryWrapper);
		
		return entitys.size();
	}

	
}
