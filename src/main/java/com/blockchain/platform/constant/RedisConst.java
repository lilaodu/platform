package com.blockchain.platform.constant;

/**
 * 应用系统常量
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:35 AM
 **/
public interface RedisConst {
	
	/**
     * redis 用户对象
     */
    String PLATFORM_USER_DATA = "platform_user";

    /**
     * 首页banner
     */
    String PLATFORM_BANNER = "platform_banner";

    /**
     * 首页通知
     */
    String PLATFORM_NOTICE = "platform_notice";

    /**
     * 首页文章
     */
    String PLATFORM_ARTICLE = "platform_article";

    /**
     * 货币配置
     */
    String PLATFORM_COIN_CONFIG = "platform_coin_config";

    /**
     * 法币配置
     */
    String PLATFORM_CURRENCY_CONFIG = "platform_currency_config";

    /**
     * 交易对配置
     */
    String PLATFORM_PAIRS_CONFIG = "platform_pairs_config";
    
    /**
     * 参数信息
     */
    String PLATFORM_PARAMS = "platform_params";

    /**
     * 汇率配置
     */
    String PLATFORM_RATE_CONFIG = "platform_rate_config";

    /**
     * 首页排行榜
     */
    String PLATFORM_HOME_RANKING = "platform_home_ranking";

    /**
     * 平台货币对应的汇率
     */
    String PLATFORM_RATE = "platform_rate_";

    /**
     * 所有的排行榜
     */
    String PLATFORM_ALL_RANKING = "platform_all_ranking";

    /**
     * 配置信息 排行榜 默认通过 缓存
     */
    String PLATFORM_RANKING = "platform_ranking";

    /**
     * 买单
     */
    String PLATFORM_HANDICAP_BUY = "platform_handicap_buy";
    
    /**
     * 卖单
     */
    String PLATFORM_HANDICAP_SELL = "platform_handicap_sell";

    String PLATFORM_KLINE = "platform_kline_";


    /**
     * 缓存资金修改短信
     */
    String PLATFORM_REDIS_CIPHER = "platform_redis_cipher_";

    /**
     * 收币地址验证码
     */
    String PLATFORM_REDIS_ADDRESS = "platform_redis_address_";

    /**
     * 提币
     */
    String PLATFORM_REDIS_WITHDRAWAL = "platform_redis_withdrawal_";

    /**
     * 提币邮件
     */
    String PLATFORM_WITHDRAWAL = "withdrawal";

    /**
     * 注册验证码
     */
    String PLATFORM_REDIS_REGISTER = "platform_redis_register_";

    /**
     * 用户忘记密码
     */
    String PLATFORM_REDIS_FORGET = "platform_redis_forget_";

    /**
     * OTC聊天记录
     */
    String PLATFORM_OTC_CHAT = "platform_otc_chat_";

    String PLATFORM_HISTORY = "platform_history";

    /**
     * OTC 卖币
     */
    String PLATFORM_OTC_SELL = "platform_otc_sell_";

    /**
     * 商户是否在线
     */
    String PLATFORM_MERCHANT_ONLINE = "platform_merchant_online";
    
    /**
     * 秒合约配置
     */
    String SECONDS_CONTRACT_CONFIG = "seconds_contract_config";

    /**
     * 代币信息
     */
    String SECONDS_CONTRACT_TOKEN = "seconds_contract_token";

    /**
     * 秒合约排行
     */
    String SECONDS_CONTRACT_RANKING = "seconds_contract_ranking";

    /**
     * ios版本号
     */
    String IOS_VERSION = "iosVersion";
    /**
     * ios下载地址
     */

    String IOS_DOWN = "iosDownloadAddress";

    /**
     * android 版本号
     */

    String ANDROID_VERSION = "androidVersion";
    
    /**
     * android 版本号
     */
    String ANDROID_DOWN = "androidDownloadAddress";
    
    /**
     * 市场
     */
    String MARKET = "market";

    /**
     * 绑定的验证码
     */
    String PLATFORM_REDIS_BIND = "platform_redis_bind";
    
    /**
     * 锁仓配置
     */
    String LOCK_WAREHOUSE_CONFIG = "lock_warehouse_config";

    /**
     * 抽奖活动列表
     */
    String DRAW_LIST = "draw_list";

    /**
     * 抽奖活动概率信息
     */
    String DRAW_ACTIVITY = "draw_activity";
    
    /**
     * 用户当天秒合约次数,几对应价格,和支付币种
     */
    String SECONDS_CONTRACT_NUM = "seconds_contract_num";
    
    /**
     * 火币ws数据
     */
    String HUOBI_WS_DATA = "huobi_ws_data_";


    /**
     * 火币ws数据kline
     */
    String HUOBI_WS_KLINE = "huobi_kline_data_";

    /**
     * OTC广告
     */
    String PLATFORM_OTC_ADVERT = "platform_otc_advert";

    /**
     * 商户信息
     */
    String PLATFORM_OTC_MERCHANTS = "platform_otc_merchants";

    /**
     * 用户升级配置信息
     */
    String PLATFORM_UPGRADE_CONFIG = "platform_upgrade_config";


    String PLATFORM_HUOBI_CLOSE = "platform_huobi_close";

    /**
     * 钱包配置
     */
    String PLATFORM_WALLET_CONFIG = "platform_wallet_config";
}
