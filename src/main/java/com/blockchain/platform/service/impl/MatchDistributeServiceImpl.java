package com.blockchain.platform.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.hutool.http.Header;
import com.blockchain.platform.constant.SecretConst;
import com.blockchain.platform.utils.SignatureUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.blockchain.platform.config.SlbConfig;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.entity.MarketCoinEntity;
import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import com.blockchain.platform.service.IMatchDistributeService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;

@Service
public class MatchDistributeServiceImpl implements IMatchDistributeService{

	/**
     * slb配置
     */
    @Resource
    private SlbConfig config;
    
    @Resource
    private RedisPlugin redisPlugin;


    @Async
	public void match(OrderFlowEntity entity) throws Exception{

    	// 配置信息
		Map<String, MarketCoinEntity> holder = redisPlugin.get(RedisConst.PLATFORM_PAIRS_CONFIG);

		MarketCoinEntity marketCoinEntity = holder.get(entity.getCoinPair());

		// 加密
		// 设置请求密钥
		entity.setApiSecret( SecretConst.API_SECRET);

		// 签名
		entity.setSignature( SignatureUtils.signature( entity, entity.getApiSecret()));

		HttpRequest.post(marketCoinEntity.getMatchUrl()).header(Header.CONTENT_TYPE, AppConst.RESPONSE_CONTENT_TYPE)
				.body(JSON.toJSONString(entity)).execute();
	}

}
