package com.blockchain.platform.constant;

/**
 * 订阅常量
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-08-30 10:50 AM
 **/
public interface SubscribeConst {


    /**
     * 交易深度
     */
    String TOPIC_DEPTH = "subscribe_market_depth";

    /**
     * 登录用户账号信息
     */
    String TOPIC_ACCOUNT = "subscribe_market_account";

    /**
     * 账户余额
     */
    String TOPIC_BALANCE = "subscribe_balance";

    /**
     * 交易行情
     */
    String TOPIC_TICKER = "subscribe_market_ticker";

    /**
     * k线买卖盘
     */
    String TOPIC_K_BUY_SELL = "subscribe_k_buy_sell";

    /**
     * 交易历史
     */
    String TOPIC_DETAIL = "subscribe_market_history";

    /**
     * 秒合约订单开奖
     */
    String TOPIC_SECONDS_CONTRACT = "subscribe_seconds_contract";

    /**
     * 我的委托
     */
    String TOPIC_ENTRUST = "subscribe_entrust";

    /**
     * 火币行情转发通道
     */
    String TOPIC_HUOBI_TICKER = "subscribe_huobi_ticker";


    String TOPIC_HUOBI_HISTORY = "subscribe_huobi_history";

    /**
     * 取消订阅交易深度
     */
    String UN_TOPIC_DEPTH = "unsubscribe_market_depth";

    /**
     * 取消订阅账号信息
     */
    String UN_TOPIC_ACCOUNT = "unsubscribe_market_account";

    /**
     * 取消订阅行情
     */
    String UN_TOPIC_TICKER = "unsubscribe_market_ticker";

    /**
     * 取消订阅历史
     */
    String UN_TOPIC_DETAIL = "unsubscribe_market_detail";

    /**
     * 取消订阅秒合约订单开奖
     */
    String UN_TOPIC_SECONDS_CONTRACT = "unsubscribe_seconds_contract";

    /**
     * k线买卖盘
     */
    String UN_TOPIC_K_BUY_SELL = "unsubscribe_k_buy_sell";

    /**
     * 账户余额
     */
    String UN_TOPIC_BALANCE = "unsubscribe_balance";

    /**
     * 我的委托
     */
    String UN_TOPIC_ENTRUST = "unsubscribe_entrust";
    
    /**
     * 我的委托
     */
    String UN_TOPIC_HUOBI_TICKER = "unsubscribe_huobi_ticker";


    /**
     * 心跳连接
     */
    String TOPIC_KEEP_ALIVE = "pong";


    /**
     * 请求历史
     */
    String TOPIC_CHAT_REQ = "chat_req";


    /**
     * 录入
     */
    String TOPIC_CHAT_ENTER = "chat_enter";


    /**
     * 监听
     */
    String TOPIC_CHAT_MONITOR = "chat_monitor";


    /**
     * 中奖历史
     */
    String TOPIC_DRAW_HISTORY = "draw_history";


    /**
     * 中奖记录
     */
    String TOPIC_DRAW_RECORD = "draw_record";

    String UN_TOPIC_DRAW_HISTORY = "un_draw_history";


    String UN_TOPIC_DRAW_RECORD = "un_draw_record";
}
