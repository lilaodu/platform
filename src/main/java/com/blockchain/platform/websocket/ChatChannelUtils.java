package com.blockchain.platform.websocket;

import io.netty.channel.ChannelId;
import org.yeauty.pojo.Session;

import java.util.concurrent.ConcurrentHashMap;


/**
 * 聊天用户记录
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-08-30 4:22 PM
 **/
public class ChatChannelUtils {


    /**
     * 商户
     * key 用户id
     * value orderId
     */
    public static ConcurrentHashMap<Integer, Session> merchant = new ConcurrentHashMap<>();


    /**
     * 正常用户
     * key 订单ID
     *
     */
    public static ConcurrentHashMap<Integer, Session> user = new ConcurrentHashMap<>();


    public static ConcurrentHashMap<ChannelId, Integer> values = new ConcurrentHashMap<>();

}
