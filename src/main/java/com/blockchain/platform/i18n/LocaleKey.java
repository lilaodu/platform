package com.blockchain.platform.i18n;

/**
 * 国际化 KEY
 */
public interface LocaleKey {

    /**
     * 请勿重复提交
     */
    String SYS_SUBMIT_PROGRESS = "sys_submit_progress";

    String SYS_UPLOAD_FILE_MAX = "sys_upload_file_max";

    String SYS_ERROR = "sys_error";
    
    /**
     * 参数传入错误
     */
    String SYS_PARAM_ERROR = "sys_param_error";

    /**
     * 操作失败
     */
    String SYS_OPERATE_FAILED = "sys_operate_failed";

    /**
     * 操作频繁，请稍后再试
     */
    String SYS_OPERATE_TOO_FREQUENT = "sys_operate_too_frequent";

    /**
     * 查询失败
     */
    String SYS_QUERY_FAILED = "sys_query_failed";
    
    /**
     * 获取火币行情失败
     */
    String HUOBI_HISTORY_TICKER_FAILED = "huobi_history_ticker";

    /**
     * 人机验证失败
     */
    String SYS_ROBOT_FAILED = "sys_robot_failed";

    /**
     * 登录已失效
     */
    String LOGON_HAS_EXPIRED = "logon_has_expired";

    /**
     * 用户账号格式不正确
     */
    String LOGIN_ACCOUNT_ERROR = "login_account_error";

    /**
     * 登录IP异常
     */
    String LOGON_IP_EXCEPTION = "logon_ip_exception";

    /**
     * 用户尚未授权
     */
    String USER_UNAUTHORIZED = "user_unauthorized";

    /**
     * 用户电话已绑定
     */
    String USER_MOBILE_HAS_BIND = "user_mobile_has_bind";

    /**
     * 用户邮箱已绑定
     */
    String USER_EMAIL_HAS_BIND = "user_email_has_bind";

    /**
     * 手机号码格式错误
     */
    String USER_MOBILE_FORMAT_ERROR = "user_mobile_format_error";

    /**
     * 邮箱格式错误
     */
    String USER_EMAIL_FORMAT_ERROR = "user_email_format_error";

    /**
     * 用户邮箱或手机是空
     */
    String USER_MOBILE_OR_EMAIL_IS_NUll = "user_mobole_or_eamil_is_null";

    /**
     * 用户邀请码错误
     */
    String USER_INVITATION_CODE_ERROR = "user_invitation_code_error";

    /**
     * 用户登录失败
     */
    String USER_LOGIN_FAILED = "user_login_failed";

    String USER_LOCKED="user_locked";

    /**
     * 用户未找到
     */
    String USER_NOT_FIND = "user_not_find";

    /**
     * 尚未完成C2认证
     */
    String USER_C2_NOT_ACCESS = "user_c2_not_access";

    /**
     * 尚未完成C1认证
     */
    String USER_C1_NOT_ACCESS = "user_c1_not_access";

    /**
     * 邀请码填写错误
     */
    String USER_PARENT_NOT_FIND = "user_parent_not_find";

    /**
     * 邀请码不能为空
     */
    String USER_INVITATION_CODE_NOT_NULL = "user_invitation_code_not_null";

    /**
     * 短信验证码不能为空
     */
    String USER_CODE_NOT_NULL = "user_code_not_null";

    /**
     * 用户已注册
     */
    String USER_HAS_REGISTER = "user_has_register";

    /**
     * 用户一提交申请
     */
    String USER_HAS_APPLY_MERCHANT = "user_has_apply_merchant";

    /**
     * 用户已经是商户
     */
    String USER_HAS_MERCHANT = "user_has_merchant";

    /**
     * 无效的验证码
     */
    String USER_INVALID_VERIFICATION_CODE = "user_invalid_verification_code";

    /**
     * 验证码已发送
     */
    String USER_CAPTCHA_HAS_SEND = "user_captcha_has_send";

    /**
     * 两次密码输入不一致
     */
    String USER_PASSWORD_IN_DISACCORD = "user_password_in_disaccord";

    /**
     * 原密码错误
     */
    String USER_ORI_PASSWORD_ERROR = "user_ori_password_error";

    /**
     * 资金密码错误
     */
    String USER_CIPHER_PASSWORD_ERROR = "user_cipher_password_error";

    /**
     * 重置资金密码失败
     */
    String USER_RESET_CIPHER_FAILED = "user_reset_cipher_failed";

    /**
     * OTC
     */
    String OTC_TRANSFER_ONLY_USDT = "otc_transfer_only_usdt_error";
    /**
     * 交易提示
     * 交易数量 min  max 有误
     */
    String ORDER_MIN_BOOK_ERROR = "order_min_book_error";

    /**
     *
     */
    String ORDER_MAX_BOOK_ERROR = "order_max_book_error";

    /**
     * 交易数量不能为空
     */
    String ORDER_PRICE_NOT_NULL = "order_price_not_null";

    /**
     * 交易数量不能为空
     */
    String ORDER_NUMBER_NOT_NULL = "order_number_not_null";


    /**
     * 密钥
     */
    String ORDER_BOOK_KEY_ERROR = "order_book_key_error";

    /**
     * 密码
     */
    String ORDER_BOOK_SECRET_ERROR = "order_book_secret_error";

    /**
     * 授权失败
     */
    String ORDER_AUTH_ERROR = "order_auth_error";

    /**
     * 交易市场尚未开盘
     */
    String MARKET_NOT_OPEN = "market_not_open";

    /**
     * 交易类型错误
     */
    String ORDER_TYPE_ERROR = "order_type_error";

    /**
     * 文件上传失败
     */
    String FILE_UPLOAD_FAILED = "file_upload_failed";

    /**
     * 钱包余额不足
     */
    String WALLET_INSUFFICIENT_FUNDS = "wallet_insufficient_funds";

    /**
     * 代币不允许 转入
     */
    String TOKEN_TRANSFER_NOT_INPUTS = "token_transfer_not_inputs";

    String TOKEN_LIMIT_IN_ERROR = "token_limit_in_error";

    /**
     * 代币不允许 转出
     */
    String TOKEN_TRANSFER_NOT_OUTS = "token_transfer_not_outs";


    String ADVICE_CONTENT_NOT_NULL = "advice_content_not_null";

    /**
     * 支付方式不能为空
     */
    String PAY_TYPE_IS_NULL = "pay_type_is_null";
	/**
	 * 交易进行中
	 */
    String BUSINESS_ING = "business_ing";
    /**
     * 手机号格式错误
     */
    String REGISTER_MOBILE_ERROR="register_mobile_error";

    /**
     * 邮箱号格式错误
     */
    String REGISTER_EMAIL_ERROR="register_email_error";

    /**
     * otc 广告发布超过限制条数
     */
    String OTC_ADVERT_LIMIT_EXCEED = "otc_advert_limit_exceed";

    /**
     * otc广告不支持支付方式
     */
    String OTC_ADVERT_NOT_ALLOW_PAYMENT = "otc_advert_not_allow_payment";

    /**
     * 广告未找到
     */
    String OTC_ADVERT_NOT_FIND = "otc_advert_not_find";

    /**
     * 下架后才能编辑广告
     */
    String OTC_ADVERT_NEED_INVALID = "otc_advert_need_invalid";

    /**
     * 只有商户能发布广告
     */
    String OTC_NOT_MERCHANT = "otc_not_merchant";

    /**
     * 广告类型错误
     */
    String ADVERT_TYPE_ERROR = "otc_advert_type_error";

    /**
     * 广告价格为null
     */
    String ADVERT_PRICE_IS_NULL = "otc_advert_price_is_null";

    /**
     * 广告限制不能小于0
     */
    String ADVERT_LIMIT_LESS_ZERO = "advert_limit_less_zero";

    /**
     * 价格不能小于0
     */
    String ADVERT_PRICE_LESS_ZERO = "advert_price_less_zero";

    /**
     * otc 广告没有选择支付方式
     */
    String ADVERT_NOT_CHOOSE_PAYMENT = "advert_not_choose_payment";

    /**
     * 广告有进行中的订单，请勿下架
     */
    String ADVERT_NOT_TO_BE_INVALID = "advert_not_to_be_invalid";

    /**
     * otc 订单类型错误
     */
    String OTC_ORDER_TYOE_ERROR = "otc_order_type_error";

    /**
     * otc 订单不能取消
     */
    String OTC_ORDER_NOT_CANCEL = "otc_order_not_cancel";

    /**
     * otc 订单不能放款
     */
    String OTC_ORDER_NOT_PAY = "otc_order_not_pay";

    /**
     * OTC 订单不能放币
     */
    String OTC_ORDER_NOT_ADOPT = "otc_order_not_adopt";


    /**
     * OTC 卖方或者买方没有放币或者打款
     */
    String OTC_ORDER_NOT_SURE = "otc_order_not_sure";

    /**
     * OTC 订单已经取消
     */
    String OTC_ORDER_CANCEL = "otc_order_cancel";


    /**
     * otc 订单未找到
     */
    String OTC_ORDER_NOT_FIND = "otc_order_not_find";

    /**
     * 广告存在未完成的订单
     */
    String OTC_ORDER_NOT_COMPLETE = "otc_order_not_complete";

    /**
     * otc 订单购买数量超过
     */
    String OTC_ORDER_NUM_EXCEED = "otc_order_num_exceed";

    /**
     * otc 未支付订单数量超过限制
     */
    String OTC_APPLY_ORDER_TOO_MUCH = "otc_apply_order_too_much";

    /**
     * 订单金额小于0
     */
    String OTC_ORDER_AMOUNT_LESS_ZERO = "otc_order_amount_less_zero";

    /**
     * otc 订单可售卖数量不足
     */
    String OTC_ORDER_NUM_LACK = "otc_order_num_lack";

    /**
     * otc 购买数量低于下限
     */
    String OTC_OTDER_NUM_LESS_LIMIT = "otc_order_num_less_limit";

    /**
     * otc订单购买数量必须大于0
     */
    String OTC_ORDER_NUM_LESS_ZERO = "otc_order_num_less_zero";

    /**
     * 用户不能操作自己的广告
     */
    String USER_NOT_OPERATE_OWN_ADVERT = "user_not_operate_own_advert";

    /**
     * 秒合约订单生成失败
     */
    String SC_ORDER_CREATE_FAIL = "sc_order_create_fail";
    
    /**
     * 个人秒合约订单上线
     */
    String SC_ORDER_TOP_LIMIT = "sc_order_top_limit";


    String SC_PRICE_LIMIT = "sc_price_limit";

    /**
     * 不能同账户划转
     */
    String TRANSFER_WALLET_SAME = "transfer_account_same";

    /**
     * 当前划转目的账户不支持当前货币
     */
    String TRANSFER_TO_WALLET_ERROR = "transfer_to_wallet_error";

    /**
     * 未找到抽奖活动
     */
    String DRAW_NOT_FIND = "draw_not_find";

    /**
     * 用户当前可抽奖次数为0
     */
    String DRAW_NUMBER_IS_ZERO = "draw_number_is_zero";

    /**
     * 锁仓数量已上限
     */
    String LOCK_WAREHOUSE_TOP = "lock_warehouse_top";

    /**
     * 用户提币数量低于下限值
     */
    String USER_WITHDRAW_NUMBER_LESS_LIMIT = "user_withdraw_number_less_limit";

    /**
     * 用户有未结束的工单
     */
    String USER_WORK_NOT_END = "user_work_not_end";
    
    /**
     * 用户所选的秒合约配置不存在
     */
    String SECONDS_CONTRACT_CONFIG_NULL = "seconds_contract_config_null";

    /**
     * 签名不能为空
     */
    String SIGNATURE_NOT_NULL = "signature_not_null";

    /**
     * 数字签名效验失败
     */
    String SIGNATURE_VALID_ERROR = "signature_valid_error";

    /**
     * 添加支付方式用户信息与认证信息不一致
     */
    String PAYMENT_IS_DIFFERENT_WITH_KYC = "payment_is_different_with_kyc";

    /**
     * 当前代币不允许提币
     */
    String COIN_IS_NOT_ALLOW_WITHDRAW = "coin_is_not_allow_withdraw";

    /**
     * 用户提币失败
     */
    String USER_WITHDRAW_FAILED = "user_withdraw_failed";

    /**
     * OTC 聊天内容不能为空
     */
    String OTC_CHAT_CONTENT_NULL = "otc_chat_content_null";


    String OTC_CHAT_ORDER = "otc_chat_order";


    String MAIL_CAPTCHA_NOT_NULL = "mail_captcha_not_null";


    String SMS_CAPTCHA_NOT_NULL = "sms_captcha_not_null";
}
