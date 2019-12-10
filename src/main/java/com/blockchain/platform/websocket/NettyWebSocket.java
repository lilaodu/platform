package com.blockchain.platform.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.alibaba.fastjson.JSON;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.constant.SubscribeConst;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.plugins.spring.SpringPlugin;
import com.blockchain.platform.pojo.dto.SubscribeDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.CoinEntity;
import com.blockchain.platform.pojo.entity.HuobiKLineEntity;
import com.blockchain.platform.pojo.entity.SecondsContractOrderEntity;
import com.blockchain.platform.pojo.vo.CapitalVO;
import com.blockchain.platform.pojo.vo.DealVO;
import com.blockchain.platform.pojo.vo.OrderVO;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.pojo.vo.RankingVO;
import com.blockchain.platform.pojo.vo.WsVO;
import com.blockchain.platform.service.ISocketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.yeauty.annotation.OnBinary;
import org.yeauty.annotation.OnClose;
import org.yeauty.annotation.OnError;
import org.yeauty.annotation.OnEvent;
import org.yeauty.annotation.OnMessage;
import org.yeauty.annotation.OnOpen;
import org.yeauty.annotation.ServerEndpoint;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;

import javax.annotation.Resource;

/**
 * 交易websocket 连接
 */
//@Component
//@ServerEndpoint(value = "/trade/socket.io", port = 7379)
public class NettyWebSocket {

	/**
	 * 锁
	 */
	private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);

	/**
	 * 缓存对象
	 */
	public static RedisPlugin redisPlugin;

	private static ISocketService socketService;

	/**
	 * 获取用户数据
	 * 
	 * @param token
	 * @return
	 */
	public UserDTO getUserData(String token) {
		if (ObjectUtil.isEmpty(redisPlugin)) {
			redisPlugin = SpringPlugin.getBean(RedisPlugin.class);
		}
		UserDTO dto = null;
		if (StrUtil.isNotEmpty(token)) {
			dto = redisPlugin.hget(RedisConst.PLATFORM_USER_DATA, token);
		}
		return dto;
	}
	
	public ISocketService getISocketService(ISocketService socketService) {
		if (ObjectUtil.isEmpty(socketService)) {
			socketService = SpringPlugin.getBean(ISocketService.class);
		}
		return socketService;
	}

	/**
	 * 开启连接
	 * 
	 * @param session
	 * @param headers
	 * @param parameterMap
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {
	}

	/**
	 * 关闭当前连接
	 * 
	 * @param session
	 * @throws IOException
	 */
	@OnClose
	public void onClose(Session session) throws IOException {
		removeClient(session);
	}

	/**
	 * 连接异常
	 * 
	 * @param session
	 * @param throwable
	 */
	@OnError
	public void onError(Session session, Throwable throwable) {
		removeClient(session);
	}

	/**
	 * 收到消息
	 * 
	 * @param session
	 * @param message
	 */
	@OnMessage
	public void onMessage(Session session, String message) {
		
		if (!StrUtil.isEmpty(message)) {
			
			// 验证订阅消息
			try {
				SubscribeDTO dto = JSON.parseObject(message, SubscribeDTO.class);
				
				
				switch (dto.getType()) {
					
					//订阅
					case SubscribeConst.TOPIC_SECONDS_CONTRACT://秒合约开奖订阅
						saveClientUserNoSymbol(ChannelUtils.secondsContract, dto, session );
						break;
					case SubscribeConst.TOPIC_BALANCE://账户余额变化订阅
						saveClientUserNoSymbol(ChannelUtils.balance,dto, session );
						break;
						
					
					case SubscribeConst.TOPIC_TICKER://行情订阅
						saveClientSymbolNoUser(ChannelUtils.ticker,dto, session );
						break;
					case SubscribeConst.TOPIC_K_BUY_SELL://K线买卖盘订阅
						saveClientSymbolNoUser(ChannelUtils.K,dto, session );
						break;
					case SubscribeConst.TOPIC_DETAIL://history订阅
						saveClientSymbolNoUser(ChannelUtils.history,dto, session );
						break;
					case SubscribeConst.TOPIC_HUOBI_TICKER://火币行情转发通道
						saveClientSymbolNoUser(ChannelUtils.huobiTicker,dto, session );
						break;
					case SubscribeConst.TOPIC_ENTRUST://委托化订阅
						saveClientUserSymbol(ChannelUtils.entrust,dto, session );
						break;	

					//取消订阅
					case SubscribeConst.UN_TOPIC_SECONDS_CONTRACT:
						removClientUserNoSymbol(ChannelUtils.secondsContract, dto, session);
						break;
					case SubscribeConst.UN_TOPIC_BALANCE:
						removClientUserNoSymbol(ChannelUtils.balance, dto, session);
						break;
						
						
					case SubscribeConst.UN_TOPIC_TICKER:
						removClientSymbolNoUser(ChannelUtils.ticker, dto, session);
						break;
					case SubscribeConst.UN_TOPIC_K_BUY_SELL:
						removClientSymbolNoUser(ChannelUtils.K, dto, session);
						break;
					case SubscribeConst.UN_TOPIC_DETAIL:
						removClientSymbolNoUser(ChannelUtils.history, dto, session);
						break;
					case SubscribeConst.UN_TOPIC_HUOBI_TICKER:
						removClientSymbolNoUser(ChannelUtils.huobiTicker, dto, session);
						break;	
					
						
					case SubscribeConst.UN_TOPIC_ENTRUST:
						removClientUserSymbol(ChannelUtils.entrust, dto, session);
						break;
					
						
					
						
					//心跳维护
					case SubscribeConst.TOPIC_KEEP_ALIVE:
						session.sendText(JSONUtil.toJsonStr(new ArrayList<Object>(2){{
							add("heartbeat");
							add("ping");
						}}));
						
						break;
						
	
					default:
						break;
				}
			} catch (Exception e) {
	        }
			
		}

	}

	/**
	 * 订阅
	 * 不需要用户但需要交易对
	 * (历史,买卖盘,行情,火币行情转发)
	 */
	public void saveClientSymbolNoUser(ConcurrentHashMap<String, ConcurrentHashSet<String>> contract, SubscribeDTO dto, Session session) {
		try {
			rwLock.writeLock().lock();
			if (session.isActive() && session.isOpen()) {
				//判断是否已有
				ConcurrentHashSet<String> holder = contract.containsKey(dto.getSymbol())? contract.get(dto.getSymbol()) : new ConcurrentHashSet();
				holder.add(session.id().asShortText());
				// session 容器
				ChannelUtils.values.put(session.id().asShortText(), session);
				
				switch (dto.getType()) {
					case SubscribeConst.TOPIC_TICKER://行情订阅
						sendTickerContractText(dto.getSymbol(),session);
						break;
					case SubscribeConst.TOPIC_K_BUY_SELL://K线买卖盘订阅
						sendKContractText(dto.getSymbol(),session);
						break;
					case SubscribeConst.TOPIC_DETAIL://history订阅
						sendHistoryContractText(dto.getSymbol(),session);
						break;
					default:
						break;
				}
				contract.put(dto.getSymbol(),holder);
			}
		} finally {
			rwLock.writeLock().unlock();
		}
	}
	
	/**
	 * (订阅秒合约开奖,订阅钱包余额变化)
	 * 需要用户但不需要交易对
	 */
	public void saveClientUserNoSymbol(ConcurrentHashMap<String, String> contract, SubscribeDTO dto, Session session) {
		try {
			rwLock.writeLock().lock();
			if (session.isActive() && session.isOpen()) {
				UserDTO user = getUserData(dto.getToken());
				if (!ObjectUtil.isEmpty(user)) {
					contract.put(user.getId().toString(),session.id().asShortText());
					ChannelUtils.values.put(session.id().asShortText(), session);
					
					switch (dto.getType()) {
						case SubscribeConst.TOPIC_BALANCE://钱包余额
							sendCapitalContractText(dto.getSymbol(),session,user.getId().toString());
							break;
						default:
							break;
					}
				}
				
			}
		} finally {
			rwLock.writeLock().unlock();
		}
	}
	
	/**
	 * 订阅我的委托
	 * 需要用户且需要交易对
	 */
	public void saveClientUserSymbol(ConcurrentHashMap<String, ConcurrentHashMap<String, String>> contract, SubscribeDTO dto, Session session){
		try {
			rwLock.writeLock().lock();
			if (session.isActive() && session.isOpen()) {
				UserDTO user = getUserData(dto.getToken());
				if (!ObjectUtil.isEmpty(user)) {
					//判断是否已有
					ConcurrentHashMap<String, String> holder = contract.containsKey(dto.getSymbol())? contract.get(dto.getSymbol()) : new ConcurrentHashMap();
					holder.put(user.getId().toString(),session.id().asShortText());
					ChannelUtils.values.put(session.id().asShortText(), session);
					
					switch (dto.getType()) {
						case SubscribeConst.TOPIC_ENTRUST://账户余额变化订阅
							sendEntrustContractText(dto.getSymbol(),session,user.getId().toString());
							break;
						default:
							break;
					}
					contract.put(dto.getSymbol(),holder);
				}
			}
		} finally {
			rwLock.writeLock().unlock();
		}
	}
	

	
	
	
	
	
	/**
	 * 移除订阅,但是ws还连着
	 * (秒合约开奖,账户余额变化)
	 */
	public void removClientUserNoSymbol(ConcurrentHashMap<String, String> clients, SubscribeDTO dto,Session session) {
		try {
			rwLock.writeLock().lock();
			UserDTO user = getUserData(dto.getToken());
			if (ObjectUtil.isEmpty(user)) {
				clients.remove(user.getId().toString());
			}
		} finally {
			rwLock.writeLock().unlock();
		}	
		
	}
	
	/**
	 * 移除订阅,但是ws还连着
	 * (历史,买卖盘,行情,火币行情转发)
	 * ConcurrentHashMap<交易对, ConcurrentHashSet<SessionId>>
	 */
	public void removClientSymbolNoUser(ConcurrentHashMap<String, ConcurrentHashSet<String>> contract, SubscribeDTO dto,Session session) {
		try {
			rwLock.writeLock().lock();
			if( contract.containsKey(dto.getSymbol()) ) {
				ConcurrentHashSet<String> holder = contract.get(dto.getSymbol());
				holder.remove(session.id().asShortText());
				contract.put(dto.getSymbol(),holder);
			}
		} finally {
			rwLock.writeLock().unlock();
		}	
	}
	
	/**
	 * 移除订阅,但是ws还连着
	 * (我的委托)
	 * ConcurrentHashMap<交易对, ConcurrentHashMap<userId, SessionId>>
	 */
	public void removClientUserSymbol(ConcurrentHashMap<String, ConcurrentHashMap<String, String>> contract, SubscribeDTO dto,Session session) {
		try {
			rwLock.writeLock().lock();
			UserDTO user = getUserData(dto.getToken());
			if (ObjectUtil.isEmpty(user)) {
				if( contract.containsKey(dto.getSymbol()) ) {
					ConcurrentHashMap<String, String> holder = contract.get(dto.getSymbol());
					holder.remove(user.getId().toString());

					contract.put(dto.getSymbol(),holder);
				}
			}
		} finally {
			rwLock.writeLock().unlock();
		}	
	}
	
	/**
	 * 移除订阅,但是ws还连着
	 * (火币行情通道)
	 * ConcurrentHashMap<交易对, ConcurrentHashSet<SessionId>>
	 */
	public void removClientHuoBiTicker(ConcurrentHashMap<String, ConcurrentHashSet<String> > contract, SubscribeDTO dto,Session session) {
		try {
			rwLock.writeLock().lock();
			if( contract.containsKey(dto.getSymbol()) ) {
				ConcurrentHashSet<String> holder = contract.get(dto.getSymbol());
				holder.remove(session.id().asShortText());
				contract.put(dto.getSymbol(),holder);
			}
		} finally {
			rwLock.writeLock().unlock();
		}	
	}
	
	
	/**
	 * 主动踢出订阅
	 * ConcurrentHashMap<String, ConcurrentHashMap<String, String>>
	 */
	public void removClientSymbolUser(ConcurrentHashMap<String, ConcurrentHashMap<String, String>> contract, Session session) {
		try {
			rwLock.writeLock().lock();
			for(ConcurrentHashMap.Entry<String, ConcurrentHashMap<String, String>> entry: contract.entrySet()) {
				ConcurrentHashMap<String, String> value = entry.getValue();
				for(ConcurrentHashMap.Entry<String, String> entry2: value.entrySet() ) {
					if(StrUtil.equals(entry2.getValue(), session.id().asShortText())) {
						value.remove(entry2.getKey());
					}
				}
				contract.put(entry.getKey(),value);
			}
		} finally {
			rwLock.writeLock().unlock();
		}	
	}
	/**
	 * 主动踢出订阅
	 * ConcurrentHashMap<String, ConcurrentHashSet<tring>>
	 */
	public void removClientSymbol(ConcurrentHashMap<String, ConcurrentHashSet<String>> contract, Session session) {
		try {
			rwLock.writeLock().lock();
			for(ConcurrentHashMap.Entry<String, ConcurrentHashSet<String>> entry: contract.entrySet()) {
				ConcurrentHashSet<String> value = entry.getValue();
				value.remove(session.id().asShortText());
				contract.put(entry.getKey(),value);
			}
		} finally {
			rwLock.writeLock().unlock();
		}	
	}
	/**
	 * 主动踢出订阅
	 * ConcurrentHashMap<String,String>
	 */
	public void removClient(ConcurrentHashMap<String, String> contract, Session session) {
		try {
			rwLock.writeLock().lock();
			for(ConcurrentHashMap.Entry<String, String> entry: contract.entrySet() ) {
				if(StrUtil.equals(entry.getValue(), session.id().asShortText())) {
					contract.remove(entry.getKey());
				}
			}
		} finally {
			rwLock.writeLock().unlock();
		}	
	}
	

	/**
	 * 异常信息
	 * 通过用户订阅的
	 * @param session
	 */
	public void removeClient(Session session) {
		try {
			rwLock.writeLock().lock();
			//移除所有订阅
			removClient(ChannelUtils.balance,session);
			removClient(ChannelUtils.secondsContract,session);
			
			removClientSymbol(ChannelUtils.history,session);
			removClientSymbol(ChannelUtils.K,session);
			removClientSymbol(ChannelUtils.ticker,session);
			removClientSymbol(ChannelUtils.huobiTicker,session);
			
			removClientSymbolUser(ChannelUtils.entrust,session);
			
			ChannelUtils.values.remove(session.id().asShortText());
		} finally {
			rwLock.writeLock().unlock();
		}
	}
	
	
	
	
	
	/**
     * 秒合约开奖通知
     * @param secondsContractOrder
     */
    public void sendSecondsContractText(SecondsContractOrderEntity secondsContractOrder) {
    	String sessionId = ChannelUtils.secondsContract.get(secondsContractOrder.getUserId().toString());
    	if(!StrUtil.isEmpty(sessionId)) {
    		Session session = ChannelUtils.values.get(sessionId);
        	session.sendText(JSONUtil.toJsonStr(new ArrayList<Object>() {{
        		add("seconds");
            	add(secondsContractOrder);
        	}}));
    	}
    }
    /**
     * K线买卖盘信息通知
     * @param symbol
	 * @param value 通知内容
     */
    public void sendKContractText(String symbol, List<Map<String, List<OrderVO>>> value) {
    	ConcurrentHashMap<String, ConcurrentHashSet<String>> contract = ChannelUtils.K;
    	if(contract.containsKey(symbol)) {
    		ConcurrentHashSet<String> holder = contract.get(symbol);
    		for(String sessionId: holder) {
    			Session session = ChannelUtils.values.get(sessionId);
    			if(ObjectUtil.isNotEmpty( session) && session.isOpen() && session.isActive()) {
    				session.sendText(JSONUtil.toJsonStr(new ArrayList<Object>() {{
    		    		add("kbuysell");
    		        	add( value);
    		    	}}));
    			}
			}
    	}
    }
    /**
 	*  历史交易通知
     */
    public void sendHistoryContractText(String symbol, List<DealVO> vo) {
    	ConcurrentHashMap<String, ConcurrentHashSet<String>> contract = ChannelUtils.history;
    	if(contract.containsKey(symbol)) {
    		ConcurrentHashSet<String> holder = contract.get(symbol);
    		for(String sessionId: holder ) {
    			Session session = ChannelUtils.values.get(sessionId);
    			if( ObjectUtil.isNotEmpty( session) && session.isActive() && session.isOpen()) {
    				session.sendText(JSONUtil.toJsonStr(new ArrayList<Object>() {{
    		    		add("history");
    		        	add(vo);
    		    	}}));
    			}
			}
    	}
    }
    /**
 	*  行情通知
     */
    public void sendTickerContractText(String symbol, RankingVO vo) {
    	ConcurrentHashMap<String, ConcurrentHashSet<String>> contract = ChannelUtils.ticker;
    	if(contract.containsKey(symbol)) {
    		ConcurrentHashSet<String> holder = contract.get(symbol);
    		for(String sessionId : holder ) {
    			Session session = ChannelUtils.values.get(sessionId);
    			if( ObjectUtil.isNotEmpty( session) && session.isOpen() && session.isActive()) {
    				session.sendText(JSONUtil.toJsonStr(new ArrayList<Object>() {{
    		    		add("ranking");
    		        	add(vo);
    		    	}}));
    			}
			}
    	}
    }
    /**
     * 我的委托订阅
     */
    public void sendEntrustContractText(String symbol,String userId) {
    	ConcurrentHashMap<String, ConcurrentHashMap<String, String>> contract = ChannelUtils.entrust;
    	if(contract.containsKey(symbol)) {
    		ConcurrentHashMap<String, String> holder = contract.get(symbol);
    		String sessionId = holder.get(userId);
    		if(!StrUtil.isEmpty(sessionId)) {
    			Session session = ChannelUtils.values.get(sessionId);
				PageVO vo = getISocketService(socketService).findWsActivesByCondition(symbol,userId);
				session.sendText(JSONUtil.toJsonStr(new ArrayList<Object>() {{
		    		add("entrust");
		        	add(vo);
		    	}}));
    		}
    	}
    }
    /**
     * 用户资产订阅
     */
    public void sendCapitalContractText(String symbol,String userId) {
    	ConcurrentHashMap<String,String> balance = ChannelUtils.balance;
    	String sessionId = balance.get(userId);
    	if(!StrUtil.isEmpty(sessionId)) {
    		Session session = ChannelUtils.values.get(sessionId);
    		Map<String, CapitalVO> vo = getISocketService(socketService).findWsCapitalByCondition(symbol,userId);
			session.sendText(JSONUtil.toJsonStr(new ArrayList<Object>() {{
	    		add("capital");
	        	add(vo);
	    	}}));
    	}
    }
    /**
 	*  火币行情转发通知
     */
    public void sendHuoBiTickerContractText(String symbol, HuobiKLineEntity vo) {
    	ConcurrentHashMap<String, ConcurrentHashSet<String>> contract = ChannelUtils.huobiTicker;
    	if(contract.containsKey(symbol)) {
    		ConcurrentHashSet<String> holder = contract.get(symbol);
    		for(String sessionId :holder) {
    			Session session = ChannelUtils.values.get(sessionId);
    			if( ObjectUtil.isNotEmpty( session) && session.isOpen() && session.isActive()) {
    				session.sendText(JSONUtil.toJsonStr(new ArrayList<Object>() {{
    		    		add("HuobiTicker");
    		        	add(vo);
    		    	}}));
    			}
    		}
    	}
    }
    
    
    
    /**
     * 用户资产,订阅之后直接发送的
     */
    public void sendCapitalContractText(String symbol,Session session,String userId) {
    	
    	Map<String, CapitalVO> vo = getISocketService(socketService).findWsCapitalByCondition(symbol,userId);
    	session.sendText(JSONUtil.toJsonStr(new ArrayList< Object>() {{
    		add("capital");
        	add(vo);
    	}}));
    }
    /**
     * K线买卖盘信息,订阅之后直接发送的
     */
    public void sendKContractText(String symbol,Session session) {
    	List<Map<String, List<OrderVO>>> vo = getISocketService(socketService).findWsKByCondition(symbol);
    	session.sendText(JSONUtil.toJsonStr(new ArrayList< Object>() {{
    		add("kbuysell");
        	add(vo);
    	}}));
    }
    /**
 	*  历史交易通知,订阅之后直接发送的
     */
    public void sendHistoryContractText(String symbol,Session session) {
    	List<DealVO> DealVOList = getISocketService(socketService).findWsHistoryByCondition(symbol); 	
    	session.sendText(JSONUtil.toJsonStr(new ArrayList< Object>() {{
    		add("history");
        	add(DealVOList);
    	}}));
    }
    /**
     * 行情通知,订阅之后直接发送的
     */
    public void sendTickerContractText(String symbol,Session session) {
    	RankingVO rankingVO = getISocketService(socketService).findWsTickerByCondition(symbol);
    	session.sendText(JSONUtil.toJsonStr(new ArrayList< Object>() {{
    		add("ranking");
        	add(rankingVO);
    	}}));
    }
    /**
     * 我的委托,订阅之后直接发送的
     */
    public void sendEntrustContractText(String symbol,Session session,String userId) {
    	PageVO vo = getISocketService(socketService).findWsActivesByCondition(symbol,userId);
    	session.sendText(JSONUtil.toJsonStr(new ArrayList< Object>() {{
    		add("entrust");
        	add(vo);
    	}}));
    }
    
    
    
    
    

}
