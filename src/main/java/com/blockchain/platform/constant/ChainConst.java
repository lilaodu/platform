package com.blockchain.platform.constant;

/**
 * 钱包区块常量
 *
 * @author ml
 * @version 1.0
 * @create 2019-09-11 3:36 PM
 **/
public class ChainConst {

    /**
     * 充币协议
     */
     public  static String PROTOCOL = "omni";

    /**
     * 代币
     */
    public  static  String DEFAULT_SYMBOL = "USDT";

    /**
     * 提币钱包地址
     */
    public static String CHAIN_WITHDRAW = "/chain/withdraw";

    /**
     * 创建新的地址
     */
    public static String CHAIN_ADDRESS_CREATE = "/chain/newAccount/";

    /**
     * 提币数量
     */
    public  static String AMOUT = "amout";

    /**
     * 提币代币
     */
    public  static String SYMBOL = "symbol";

    /**
     * 提币地址
     */
    public  static String ADDRESS = "toAddress";

    /**
     * 错误提示
     */
    public  static String ERROR = "您的提币申请正在审核中，请耐心等待";
}
