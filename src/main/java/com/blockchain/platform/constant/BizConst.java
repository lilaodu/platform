
package com.blockchain.platform.constant;

import java.math.BigDecimal;

/**
 * 业务级常量
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-11 11:52 AM
 **/
public interface BizConst {

    /**
     * 非默认
     */
    Integer BIZ_STATUS_N = 2;

    /**
     * 默认
     */
    Integer BIZ_STATUS_Y = 3;

    /**
     * 默认有效状态,valid
     */
    Integer BIZ_STATUS_VALID = 1;

    /**
     * 逻辑删除
     */
    Integer BIZ_STATUS_FAILED = 0;

    /**
     * 无效状态
     */
    Integer BIZ_STATUS_EXPIRY = 2;

    /**
     * 注册短信
     */
    String SMS_TYPE_REGISTER = "register";

    /**
     * 修改资金密码短信
     */
    String SMS_TYPE_CIPHER = "cipher";

    /**
     * 忘记密码短信
     */
    String SMS_TYPE_FORGET = "forget";

    String CHANGE_CIPHER="change-cipher";


    /**
     * 绑定邮箱，电话短信
     */
    String SMS_TYPE_BIND = "bind";

    /**
     * 购买短信
     */
    String SMS_TYPE_WITHDRAW = "withdraw";

    /**
     * 购买短信
     */
    String SMS_TYPE_ORDER_FINISH = "otcfinish";

    /**
     * 出售短信
     */
    String SMS_TYPE_DEPOSIT = "deposit";

    /**
     * 提币
     */
    String SMS_TYPE_EXTRACT = "extract";

    String BIZ_MAS_SN = "sn";

    /**
     * 绑定
     */
    String BIND_TYPE = "new";

    /**
     * 添加收款地址
     */
    String SMS_TYPE_ADDRESS = "address";


    String BIZ_SECRET_CODE = "72&62%￥##";

    String BIZ_MAS_DATA = "data";

    String BIZ_MAS_RATES = "rates";

    /**
     * 汇率请求地址
     */
    String RATE_URL = "";

    /**
     * 邮箱注册
     */
    String EMAIL = "email";

    /**
     * 电话注册
     */
    String MOBILE = "mobile";

    /**
     * 默认休眠时间
     */
    Long DEFAULT_SLEEP_TIME = 200L;

    String TOKEN_VALIDATOR = "BTC";

    String DEFAULT_RATE = "DEFAULT";

    String BASE_TOKEN = "BTBT";

    String USDT_TOKEN = "USDT";

    /**
     * 抽奖
     */
    String DRAW_DEFAULT_PRIZE = "谢谢参与";

    /**
     * 奖项类型
     */
    String DRAW_TYPE_PRIZE = "prize";

    /**
     * 抽奖触发按钮
     */
    String DRAW_TYPE_BUTTON = "button";

    /**
     * 平台币
     */
    String PLATFORM_COIN = "BTBT";

    interface UserConst {

        /**
         * 谷歌二步 抬头
         */
        String GOOGLE_TITLE = "Btbt";

        /**
         * 用户未填写上级
         */
        Integer USER_NO_PARENT = 0;

        /**
         * 普通用户
         */
        int OTC_USER_NORMAL = 0;

        /**
         * 商户
         */
        int OTC_USER_MERCHANT = 1;

    }

    interface EditorConst {

        String UPLOAD_CONFIG = "config";
    }

    interface TradeConst {

        /**
         * 订单状态委托
         */
        int ORDER_TYPE_PENDING = 1;
        /**
         * 订单状态 完成
         */
        int ORDER_TYPE_FINISH = 2;
        /**
         * 订单状态 撤单
         */
        int ORDER_TYPE_CANCEL = 3;
        /**
         * 订单状态 撤单中
         */
        int ORDER_TYPE_CANCELING = 4;
        
        /**
         * 交易类型 买入
         */
        String TRADE_TYPE_INPUTS_EN = "BUY";

        /**
         * 交易类型 卖出
         */
        String TRADE_TYPE_OUTS_EN = "SELL";

        /**
         * 默认长度
         */
        int BIGDECIMAL_MAX_LENGTH = 8;
        /**
         * 默认手续费
         */
        BigDecimal FINAL_FEERATE = new BigDecimal(0.001);

        String TRADE_SCOPE_THREE_DAY = "3D";

        String TRADE_SCOPE_ONE_WEEK = "1W";

        String TRADE_SCOPE_ALL = "ALL";

        /**
         * OTC货币
         */
        String OTC_SYMBOL = "USDT";
    }

    /**
     * 订单常量
     */
    interface OrderConst {
        /**
         * 盘口 买卖数据 长度
         */
        Integer DEFAULT_HANDICAP_LENGTH = 5;

        /**
         * 盘口 交易历史 长度
         */
        Integer DEFAULT_HISTORY_LENGTH = 50;
    }

    /**
     * otc 订单常量
     */
    interface OTCOrderConst {

        /**
         * 订单类型 -- 买
         */
        String TYPE_BUY = "BUY";

        /**
         * 订单类型 -- 卖
         */
        String TYPE_SELL = "SELL";

        /**
         * 无效
         */
        Integer STATE_DELETE = 0;

        /**
         * 已下单
         */
        Integer STATE_APPLY = 1;

        /**
         * 已取消
         */
        Integer STATE_CANCEL = 2;

        /**
         * 已付款
         */
        Integer STATE_PAY = 3;

        /**
         * 已放币
         */
        Integer STATE_ADOPT = 4;

        /**
         * 已评论
         */
        Integer STATE_COMMENT = 5;

        /**
         * 状态值 -- 进行中
         */
        String TYPE_ON = "ON";

        /**
         * 状态值 -- 已取消
         */
        String TYPE_CANCEL = "CANCEL";

        /**
         * 状态值 -- 已完成
         */
        String TYPE_COMPLETE = "COMPLETE";

        /**
         * 评论 -- 不满意
         */
        String COMMENT_UNSATISFY = "UNSATISFY";

        /**
         * 评论 -- 一般
         */
        String COMMENT_COMMONLY = "COMMONLY";

        /**
         * 评论 -- 满意
         */
        String COMMENT_SATISFY = "SATISFY";
    }

    /**
     * 参数常量
     */
    interface ParamsConst {
        /**
         * 场外币购买价格配置
         */
        String BASE_COIN_BUY_PRICE = "BASE_COIN_BUY_PRICE";

        /**
         * 场外币出售价格配置
         */
        String BASE_COIN_SELL_PRICE = "BASE_COIN_SELL_PRICE";

        /**
         * 场外币购买时间限制
         */
        String OTC_BUY_TIME = "OTC_BUY_TIME";

        /**
         * 场外币出售时间限制
         */
        String OTC_SELL_TIME = "OTC_SELL_TIME";

        /**
         * 场外币购买时间限制
         */
        String REGISTER_BONUS = "REGISTER_BONUS";

        /**
         * 场外币出售时间限制
         */
        String REGISTER_PARENT_BONUS = "REGISTER_PARENT_BONUS";

        /**
         * 广告手续费配置信息
         */
        String ADVERT_FEE = "ADVERT_FEE";

        /**
         * 广告活动手续费
         */
        String ACTIVITY_FEE = "ACTIVITY_FEE";

        /**
         * 个人活动手续费
         */
        String PERSONAL_FEE = "PERSONAL_FEE";

        /**
         * 广告配置
         */
        String ADVERT_CONFIG = "ADVERT_CONFIG";
    }


    interface AssetsConst {

        //入账
        String ASSETS_FLOW_TYPE_INPUTS = "INPUTS";
        //出账
        String ASSETS_FLOW_TYPE_OUTS = "OUTS";
        //转账
        String ASSETS_FLOW_TYPE_TRANSFER = "TRANSFER";

        //场外币购买价格配置
        String PARAM_BASE_COIN_BUY_PRICE = "BASE_COIN_BUY_PRICE";
        //场外币出售价格配置
        String PARAM_BASE_COIN_SELL_PRICE = "BASE_COIN_SELL_PRICE";

        //奖励
        String ASSETS_FLOW_TYPE_BONUS = "BONUS";


        //注册奖励
        String ASSETS_FLOW_TYPE_GENBONUS = "GENBONUS";

        //场外币购买时间限制
        String PARAM_OTC_BUY_TIME = "OTC_BUY_TIME";
        //场外币出售时间限制
        String PARAM_OTC_SELL_TIME = "OTC_SELL_TIME";
        //场外币购买时间限制
        String PARAM_REGISTER_BONUS = "REGISTER_BONUS";
        //场外币出售时间限制
        String PARAM_REGISTER_PARENT_BONUS = "REGISTER_PARENT_BONUS";

        //场外交易vcode
        String QUERY_VCODE = "vCode";
        /**
         * 未支付
         */
        String STATE_UNPAY = "UNPAY";

        /**
         * 申请
         */
        String STATE_APPLY = "APPLY";

        /**
         * 预审
         */
        String STATE_PRETRIAL = "PRETRIAL";

        /**
         * 初审
         */
        String STATE_FIRST = "FIRST";

        /**
         * 复审
         */
        String STATE_REVIEW = "REVIEN";

        /**
         * 终审
         */
        String STATE_FINAL = "FINAL";

        /**
         * 审核完成
         */
        String STATE_COMPLETE = "COMPLATE";

        /**
         * 退回状态
         */
        String STATE_BACK = "BACK";

        /**
         * 取消
         */
        String  STATE_CANCEL = "CANCEL";
    }

    /**
     * 文章 常量
     */
    interface ArticleConst {

        /**
         * 到期
         */
        Integer STATE_EXPIRY = 0;

        /**
         * 标记状态
         */
        int STATE_COMPILE = 1;


        /**
         * 上架
         */
        int STATE_PUBLISH = 2;
    }


    interface WalletConst {

        //钱包类型 -- 钱包账户
        String WALLET_TYPE_T = "T";

        //钱包类型 -- 币币账户
        String WALLET_TYPE_TRADE = "TRADE";

        //钱包类型 -- OTC账户
        String WALLET_TYPE_OTC = "OTC";

        //钱包类型 -- 挖矿账户
        String WALLET_TYPE_DIG = "DIG";
    }

    /**
     * 咨询建议常量
     */
    interface AdviceConst {

        /**
         * 咨询申请
         */
        Integer ADVICE_STATE_APPLY = 1;

        /**
         * 咨询建议已回复
         */
        Integer ADVICE_STATE_COMPLETE = 2;

        /**
         * 管理员删除该留言
         */
        Integer ADVICE_STATE_DELETE = 0;

        /**
         * 文本
         */
        String ADVICE_TYPE_TEXT = "TEXT";

        /**
         * 图片
         */
        String ADVICE_TYPE_IMG = "IMG";
    }

    /**
     * kyc 常量
     */
    interface KycConst {
        /**
         * 申请待审核
         */
        Integer STATUS_APPLY = 2;

        /**
         * 退回
         */
        Integer STATUS_RETREAT = -1;

        /**
         * 可用
         */
        Integer STATUS_VALID = 3;

        /**
         * 取消验证
         */
        Integer STATUS_UNVERIFY = 1;

        /**
         * failed
         */
        Integer STATUS_FAILED = 0;

        /**
         * 无效状态
         */
        Integer KYC_DELETE = 0;
        /**
         * k1 提交审核
         */
        Integer K1_SUB = 1;
        /**
         * k1审核不通过
         */
        Integer K1_NO = 2;
        /**
         * k1 审核通过
         */
        Integer K1_OK = 3;
        /**
         * k2提交审核
         */
        Integer K2_SUB = 4;
        /**
         * k2 审核不通过
         */
        Integer K2_NO = 5;
        /**
         * k2审核通过
         */
        Integer K2_OK = 6;
        

        /**
         * 手持照
         */
        String FIELD_HOLD = "picHold";

        /**
         * 正面照
         */
        String FIELD_FACE = "picFace";

        /**
         * 背后照
         */
        String FIELD_BACK = "picBack";
    }

    interface TrustConst {

        /**
         * 方法类型-添加
         */
        String TRUST_ADD = "ADD";

        /**
         * 方法类型-取消
         */
        String TRUST_CANCEL = "CANCEL";

        /**
         * 类型 -- 信任
         */
        String TYPE_TRUST = "TRUST";

        /**
         * 类型 -- 屏蔽
         */
        String TYPE_SHIELD = "SHIELD";

        /**
         * 类型 -- 举报
         */
        String TYPE_ACCUSATION = "ACCUSATION";
    }

    interface ChatConst {
        /**
         * 文本
         */
        String ADVICE_TYPE_TEXT = "TEXT";

        /**
         * 图片
         */
        String ADVICE_TYPE_IMG = "IMG";

        /**
         * 未读
         */
        String ADVICE_TYPE_NUM = "NUMBER";


        /**
         * 未读
         */
        String ADVICE_TYPE_HISTORY = "HISTORY";

        /*
         * 状态 -- 商户未读
         */
        Integer STATE_MERCHANT_UNREAD = 1;

        /**
         * 用户未读
         */
        Integer STATE_USER_UNREAD = 2;

        /**
         * 已读
         */
        Integer STATE_READ = 3;
    }

    interface AdvertConst {
        /**
         * 广告限制条数
         */
        Integer ADVERT_LIMIT_NUM = 5;

        /**
         * 广告类型 -- 卖
         */
        String TYPE_SELL = "SELL";

        /**
         * 广告类型 -- 买
         */
        String TYPE_BUY = "BUY";

        /**
         * 上架
         */
        Integer STATE_VALID = 1;

        /**
         * 下架
         */
        Integer STATE_INVALID = 2;

        /**
         * 删除
         */
        Integer STATE_DELETE = 0;
    }

    /**
     * otc 聊天常量
     */
    interface OTCChatConst{
        /**
         * 消息类型 -- 文本
         */
        String TYPE_TEXT = "TEXT";

        /**
         * 消息类型 -- 图片
         */
        String TYPE_IMG = "IMG";
    }

    /**
     * 秒合约常量
     */
    interface SecondsContract{
    	
    	/**
    	 * 每个人同时拥有的订单数(5单)
    	 */
    	Integer ORDER_COUNT = 5;
    	
    	/**
    	 * 1.已押注未开奖
    	 */
    	Integer STATE_STAKE = 1;
    	
    	/**
    	 * 2.已开奖
    	 */
    	Integer STATE_LOTTERY = 2;
    	
    	/**
    	 * 3.结算中
    	 */
    	Integer STATE_RESULTING = 3;
    	
    	/**
    	 * 4.结算完成(完成打款)
    	 */
    	Integer STATE_FINAL = 4;
    	
    	/**
    	 * 1.未知
    	 */
    	Integer RESULT_UNKNOWN = 1;
    	
    	/**
    	 * 2.未猜中
    	 */
    	Integer RESULT_FAIL = 2;
    	
    	/**
    	 * 3.猜中
    	 */
    	Integer RESULT_WIN = 3;
    	
    	/**
    	 * 4.平
    	 */
    	Integer RESULT_FLAT = 4;
    	
    	/**
    	 * 涨
    	 */
    	Integer RISE = 1;
    	
    	/**
    	 * 跌
    	 */
    	Integer FALL = 2;
    	
    	/**
    	 * 没涨没跌
    	 */
    	Integer FLAT = 3;
    	
    	BigDecimal ONE = new BigDecimal(1);
    	
    	BigDecimal ZERO = new BigDecimal(0);
    	
    }
    
    /**
     * 查询语句
     */
    interface sql{
    	String STATE = "state";
    	String userId = "user_id";
    	String createTime = "create_time";
    	String ID = "id";
    	String TYPE = "type";
    	String COINPAIR = "coin_pair";
    	String UserLockWarehouseId = "user_lock_warehouse_id";//对应锁仓订单的id
    	String SYMBOL = "symbol";//对应锁仓订单的id
    	String UNLOCKNUM = "unlock_num";//解锁数量
    	String DATE = "date";//时间戳
    	
    }

    /**
     * 支付常量
     */
    interface PaymentConst{
        /**
         * 无效
         */
        Integer STATE_FAILED = 0;

        /**
         * 非默认
         */
        Integer STATE_N = 1;

        /**
         * 默认
         */
        Integer STATE_Y = 2;
    }

    /**
     * 排行榜常量
     */
    interface Ranking{
        /**
         * 涨
         */
        String RANKING_RISE = "rise";

        /**
         * 跌
         */
        String RANKING_FALL = "fall";

        /**
         * 新币
         */
        String RANKING_NEW = "coin";
    }

    /**
     * otc商户常量
     */
    interface MerchantConst{
        /**
         * 无效(取消)
         */
        Integer STATE_INVALID = 0;

        /**
         * 状态--申请
         */
        Integer STATE_APPLY = 1;

        /**
         * 通过
         */
        Integer STATE_COMPLETE = 2;

        /**
         * 拒绝（驳回）
         */
        Integer STATR_REFUSE = 3;

    }

    /**
     * 钱包地址状态
     */
    interface WalletAddressConst{
        /**
         * 可用
         */
        Integer STATE_VALID = 1;

        /**
         * 已用
         */
        Integer STATE_USED = 2;
    }

    /**
     * 升级配置状态
     */
    interface UpgradeConst {

        /**
         * 星球升级
         */
        String UPGRADE_TYPE_LOCK = "LOCK";


        /**
         * 合约升级
         */
        String UPGRADE_TYPE_CONTRACT = "CONTRACT";

        /**
         * 自动升级
         */
        Integer UPGRADE_TYPE_1 = 1;

        /**
         * 自动降级
         */
        Integer UPGRADE_TYPE_2 = 2;

        /**
         * 人工升级
         */
        Integer UPGRADE_TYPE_3 = 3;

        /**
         * 人工降级
         */
        Integer UPGRADE_TYPE_4 = 4;

        /**
         * 提示降级
         */
        Integer UPGRADE_TYPE_5 = 5;

        /**
         * 四
         */
        Integer LOCK_LV_3 = 3;
    }

    interface ProfitConst {

        /**
         * 合约交易
         */
        Integer TRADE_PROFIT_1 = 1;


        /**
         * 币币交易
         */
        Integer TRADE_PROFIT_2 = 2;
    }


    interface ChainConst{
        /**
         * 状态 -- 成功
         */
        String STATUS_SUCCESS = "CONFIRM";

        /**
         *
         */
        String STATUS_FAILED = "NOT_CONFIRM";
    }
}
