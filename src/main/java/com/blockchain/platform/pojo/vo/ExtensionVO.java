package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 推广VO
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-05 9:11 PM
 **/
@Data
public class ExtensionVO implements Serializable {

    /**
     * 用户秒合约等级
     */
    private String contractLv;

    /**
     * 星球计划等级
     */
    private Integer lockLv;

    /**
     * 团队人数
     */
    private Integer teamNum;


    private Integer realTeamNum;

    /**
     * 直推人数
     */
    private Integer directNum;

    /**
     * 认证完成
     */
    private Integer realDirectNum;

    /**
     * 星球计划参与人数
     */
    private Integer lockNum;

    /**
     * 团长人数
     */
    private Integer rcNum;

    /**
     * 师长人数
     */
    private Integer dcNum;

    /**
     * 军长人数
     */
    private Integer acNum;

    /**
     * 合约直推人数
     */
    private Integer contractDirectNum;

    /**
     * 合约团队人数
     */
    private Integer contractTeamNum;
    
    
    
    //添加查询直推个节点人数
    private Integer rcSum;
    private Integer dcSum;
    private Integer acSum;
    private Integer cjSum;
    private Integer zjSum;
    private Integer gjSum;
    private Integer zcSum;
    
    /**
     * 秒合约USDT总 交易额
     */
    private BigDecimal tradeQuota;
    
    /**
     * 昨日总业绩
     */
    private BigDecimal teamLockNum;
    
    
}
