/**
 * @program: exchange
 * @description: toDo
 * @author: DengWei
 * @create: 2019-05-18
 **/
package com.blockchain.platform.constant;

/**
 * @program: ExConst
 * @description: 异常常量
 * @author: DengWei
 * @create: 2019-05-18 16:28
 **/
public interface ExConst {

    /**
     * 未查询到钱包信息
     */
    String WALLET_NO_WALLET = "wallet.no_wallet";


    /**
     *钱包余额不足
     */
    String  WALLET_INSUFFICIENT_BALANCE = " wallet.insufficient_balance";

    /**
     * 未查询到有效订单
     */
    String  ORDER_NO_ORDER = "order.no_order";

    /**
     * 流水记录失败
     */
    String  TRADE_FLOWS_ERROR = "trade.flows_error";

    /**
     * 买卖单更新失败
     */
    String  TRADE_ORDERS_ERROR = "trade.orders_error";

    /**
     *买卖单更新代币失败
     */
    String  TRADE_ORDER_COIN_ERROR = "trade.orders_coin_error";

    /**
     * IP 或者賬號異常
     */
    String SYS_IP_ERROR = "sys.ip_error";

    /**
     * 人機驗證失敗
     */
    String SYS_ROBOT_ERROR = "sys.robot_error";

    /**
     * 系统出错，包含逻辑错误，数据错误
     */
    String SYS_ERROR = "sys.error";

    /**
     * 未知的语言标识
     */
    String SYS_UNKNOWN_LANG = "sys.unknown_lang";

    /**
     * api請求錯誤
     */
    String SYS_ACTION_ERROR = "sys.action_error";

    /**
     * 接口调用成功
     */
    String SYS_OK = "sys.call_ok";

    /**
     * 接口调用失败
     */
    String SYS_FAILED = "sys.call_failed";

    /**
     * 传入参数错误
     */
    String SYS_PARAM_ERROR = "sys.param_error";

    /**
     * 需要填入google 二步
     */
    String SYS_NEED_GOOGLE = "sys.google_error";

    /**
     * 無效鏈接
     */
    String SYS_URL_ERROR = "sys.url_error";

    /**
     * 传入参数错误
     */
    String SYS_PARAM_ERROR_2 = "sys.param_error_other";

    /**
     * 数据插入错误 <EXP>: 插入数据 rows <= 0
     */
    String SYS_INSERT_NULL = "sys.insert_null";

    /**
     * 数据修改错误 <EXP>: 修改数据 rows <= 0
     */
    String SYS_UPDATE_NULL = "sys.update_null";
}
