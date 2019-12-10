package com.blockchain.platform.constant;

/**
 * 应用系统常量
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:35 AM
 **/
public interface AppConst {

    /**
     * 授权标识
     */
    String TOKEN = "token";

    /**
     * 消息
     */
    String MESSAGE = "message";

    /**
     * 状态
     */
    String STATUS = "status";

    /**
     * 提币状态
     */
    String SUCCESS = "success";

    /**
     * 失败
     */
    String FALIED = "failed";

    /**
     * 语言
     */
    String LANG = "lang";

    /**
     * 逻辑判断 是
     */
    String FIELD_Y = "Y";

    /**
     * 逻辑判断 否
     */
    String FIELD_N = "N";

    /**
     * 提币hash
     */
    String HASH = "hash";

    /**
     * 中国汇率简称
     */
    String CNY = "CNY";

    /**
     * 美国汇率简称
     */
    String USD = "USD";

    /**
     * 统一字段默认常量
     */
    String FILED_COLUMN_PREFIX = "COLUMN_";
    
    /**
     * 默认页数
     */
    Integer DEFAULT_PAGE_NO = 1;

    /**
     * 默认每页现实条数
     */
    Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 默认每个用户允许未支付订单的数量
     */
    Integer DEFAULT_OTC_ORDER_APPLY = 8;

    /**
     * 展示限制数量
     */
    Integer DEFAULT_LIMIT_SIZE = 5;

    /**
     * 未授权
     */
    Long USER_UNAUTHORIZED = -1L;

    /**
     * 验证码长度
     */
    Integer DEFAULT_CODE_LENGTH = 4;

    /**
     * 订单备注码长度
     */
    Integer DEFAULT_REMARK_CODE = 6;

    /**
     * 订单长度
     */
    Integer DEFAULT_OTC_ORDER_LENGTH = 8;

    /**
     * 订单长度
     */
    Integer ORDER_NUMBER_LENGTH = 8;

    /**
     * 验证码有效时长
     */
    Long VERIFICATION_CODE_TIME =  300L;

    /**
     * 数字签名有效时长
     */
    Long SIGN_KEEP_TIME = 10L;

    /**
     * 默认交易市场
     */
    String DEFAULT_MARKET = "USDT";

    //图片名称
    String KEY_IMAGE_NAME = "img";

    /**
     * 请求相应头
     */
    String RESPONSE_CONTENT_TYPE = "application/json; charset=UTF-8";

    /**
     * OTC 图片上传路径
     */
    String OTC_IMAGE_PATH = "C2C_IMAGE_PATH";

    /**
     * 分页
     */
    static final Boolean DO_PAGE_YES = true;

    /**
     * 备用图片上传路径
     */
    String BAK_IMAGE_PATH = "BAK_IMAGE_PATH";

    /**
     * 工单图片上传路径
     */
    String WORK_IMAGE_PATH = "WORK_IMAGE_PATH";

    /**
     * k线保持时间
     */
    Long KLINE_KEEPING_TIME = 60 * 10L;

    /**
     * KYC 图片上传路径
     */
    String KYC_IMAGE_PATH = "KYC_IMAGE_PATH";
    /**
     * 用户登录时长
     */
    static final Long USER_LOGIN_TIME = 7*24*60*60l;

    /**
     * 手就好正则匹配
     */
    public static final String PHONE = "^1[3456789]\\d{9}$";

    /**
     * 预想正则匹配
     */
    public static final String EMAIL = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\\\.][A-Za-z]{2,3}([\\\\.][A-Za-z]{2})?$";

    /**
     * 邀请码长度
     */
    Integer DEFAULT_INVITATION_CODE_SIZE = 7;
    
    /**
     * 秒
     */
  	Integer SECONDS = 1000;
    
    
}
