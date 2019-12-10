package com.blockchain.platform.constant;


/**
 * web socket 常量
 */
public interface WsConst {

    /**
     * 空token
     */
    String BIZ_WS_TOKEN = "empty";

    /**
     * 空订单id
     */
    String BIZ_WS_ORDER = "empty";


    String WS_TYPE_CHAT = "/chart.io";


    /**
     * 用户授权
     */
    String PROTOCOL = "Sec-WebSocket-Protocol";

    /**
     * 请求数据
     */
    String SYMBOL = "symbol";

    /**
     * 订单ID
     */
    String ORDER_ID = "orderId";

    /**
     * 广告ID
     */
    String ADVERT_ID = "advertId";

    /**
     * 消息内容
     */
    String CONTENT = "content";


    String MESSAGE_TYPE_DR = "dir";

}
