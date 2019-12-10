package com.blockchain.platform.enums;

/**
 * 重复表单提交验证方法
 * @author denglong
 * @create 2019年07月11日13:14:51
 */
public enum Method {

    OTC_BUY( Const.M_OTC_BUY),

    OTC_SELL( Const.M_OTC_SELL),

    ORDER_BOOK( Const.M_ORDER_BOOK),

    ASSETS_TRANSFER ( Const.M_ASSETS_TRANSFER),

    ASSETS_WITHDRAWAL ( Const.M_ASSETS_WITHDRAWAL),

    WALLET_SIGNATURE ( Const.M_WALLET_SIGNATURE),

    OTC_ADVERT( Const.M_OTC_ADVERT),

    DRAW_PRIZE( Const.M_DRAW_PRIZE),

    SC_ORDER( Const.M_SC_ORDER);

    /**
     * 参数值
     */
    private String value;

    /**
     * 枚举构造器
     * @param value
     */
    Method(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }


    interface Const {

        /**
         * OTC 订单购买
         */
        String M_OTC_BUY = "OTC_BUY";

        /**
         * OTC 订单售卖
         */
        String M_OTC_SELL = "OTC_SELL";

        /**
         * 订单提交方法
         */
        String M_ORDER_BOOK = "ORDER_BOOK";

        /**
         * 资产划转
         */
        String M_ASSETS_TRANSFER = "ASSETS_TRANSFER";

        /**
         * 资产提币
         */
        String M_ASSETS_WITHDRAWAL = "ASSETS_WITHDRAWAL";

        /**
         * 抽奖
         */
        String M_DRAW_PRIZE = "DRAW_PRIZE";

        /**
         * 钱包划转签名
         */
        String M_WALLET_SIGNATURE = "WALLET_SIGNATURE";

        /**
         * OTC广告
         */
        String M_OTC_ADVERT = "OTC_ADVERT";

        String M_SC_ORDER = "M_SC_ORDER";
    }

}
