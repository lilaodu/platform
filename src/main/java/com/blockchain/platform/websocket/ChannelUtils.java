package com.blockchain.platform.websocket;

import org.yeauty.pojo.Session;

import cn.hutool.core.collection.ConcurrentHashSet;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 通道工具
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-08-30 11:50 AM
 **/
public class ChannelUtils {

    /**
     * 深度管理
     */
    public static ConcurrentHashMap<String, ConcurrentHashMap<String, Session>> depth = new ConcurrentHashMap<>();
    /**
     * 交易明细
     */
    public static ConcurrentHashMap<String, ConcurrentHashMap<String, Session>> detail = new ConcurrentHashMap<>();

    
    
    
    /**
     * history订阅不需要userid
     * ConcurrentHashMap<交易对, ConcurrentHashSet<SessionId>>
     */
    public static ConcurrentHashMap<String, ConcurrentHashSet<String>> history = new ConcurrentHashMap<>();
    /**
     * K线买卖盘订阅不需要userid
     * ConcurrentHashMap<交易对, ConcurrentHashSet<SessionId>>
     */
    public static ConcurrentHashMap<String, ConcurrentHashSet<String>> K = new ConcurrentHashMap<>();
    /**
     * 交易行情不需要userid
     * ConcurrentHashMap<交易对, ConcurrentHashSet<SessionId>>
     */
    public static ConcurrentHashMap<String, ConcurrentHashSet<String>> ticker = new ConcurrentHashMap<>();
    /**
     * 火币行情转发通道不需要userid
     * ConcurrentHashMap<交易对, ConcurrentHashSet<SessionId>>
     */
    public static ConcurrentHashMap<String, ConcurrentHashSet<String>> huobiTicker = new ConcurrentHashMap<>();
    
    
    
    
    /**
     * 我的委托需要userid
     * ConcurrentHashMap<交易对, ConcurrentHashMap<userId, SessionId>>
     */
    public static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> entrust = new ConcurrentHashMap<>();
    
    
    
    
    /**
     * 账户余额变化订阅
     * ConcurrentHashMap<userId,SessionId>
     */
    public static ConcurrentHashMap<String,String> balance = new ConcurrentHashMap<>();
    /**
     * 订阅秒合约
     * ConcurrentHashMap<userId,SessionId>
     */
    public static ConcurrentHashMap<String,String> secondsContract = new ConcurrentHashMap<>();
    
    
    
    
    
    /**
     * 储存所有用户webSession
     * ConcurrentHashMap<SessionId, Session>
     */
    public static ConcurrentHashMap<String, Session> values = new ConcurrentHashMap<>();

}
