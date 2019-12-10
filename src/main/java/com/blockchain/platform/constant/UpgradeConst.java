
package com.blockchain.platform.constant;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;

/**
 * 升级常量
 *
 * @author ml
 * @version 1.0
 * @create 2019-09-04 10:46 PM
 **/
public interface UpgradeConst {

    /**
     * 地主
     */
    String GRADE_AC = "AC";

    /**
     * 富农
     */
    String GRADE_DC = "DC";

    /**
     * 中农
     */
    String GRADE_RC = "RC";

    /**
     * 普通成员
     */
    String GRADE_NC = "NC";

    /**
     * 团长直推用户合约账户资产
     */
    BigDecimal RC_KYC_ASSET = NumberUtil.toBigDecimal( 50);

    /**
     * 师长直推用户合约账户资产
     */
    BigDecimal DC_KYC_ASSET = NumberUtil.toBigDecimal( 100);

    /**
     * 军长直推用户合约账户资产
     */
    BigDecimal AC_KYC_ASSET = NumberUtil.toBigDecimal( 200);

    /**
     * 团长个人合约账户资产
     */
    BigDecimal RC_ASSET = NumberUtil.toBigDecimal( 1000);

    /**
     * 师长个人合约账户资产
     */
    BigDecimal DC_ASSET = NumberUtil.toBigDecimal( 3000);

    /**
     * 军长个人合约账户资产
     */
    BigDecimal AC_ASSET = NumberUtil.toBigDecimal( 5000);

    /**
     * 团长直推用户人数
     */
    Integer RC_KYC_NUM = 3;

    /**
     * 师长直推用户人数
     */
    Integer DC_KYC_NUM = 5;

    /**
     * 军长直推用户人数
     */
    Integer AC_KYC_NUM = 10;

    /**
     * 团长团队交易总额
     */
    BigDecimal RC_TEAM_ASSET = NumberUtil.toBigDecimal( 200000);

    /**
     * 师长团队交易总额
     */
    BigDecimal DC_TEAM_ASSET = NumberUtil.toBigDecimal( 800000);

    /**
     * 军长团队交易总额
     */
    BigDecimal AC_TEAM_ASSET = NumberUtil.toBigDecimal( 2000000);

    /**
     * 干预类型
     */
    String CONTRACT_INTERVENE = "CONTRACT";
}
