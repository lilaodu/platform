package com.blockchain.platform.pojo.entity;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户升降级记录
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-07 5:52 PM
 **/
@Data
public class UserUpgradeFlowEntity implements Serializable {

    @Id
    private Integer id;

    /**
     * 用户USERID
     */
    private Integer userId;

    /**
     * 改变后等级
     */
    private String rankLevel;

    /**
     * 历史等级
     */
    private String historyLevel;

    /**
     * 秒合约 CONTRACT
     * 锁仓 LOCK
     */
    private String type;

    /**
     * 管理员用户
     */
    private Integer adminUserId;

    /**
     * 1、自动升级
     * 2、自动降级
     * 3、手动升级
     * 4、手动降级
     */
    private Integer state;

    /**
     * 升级状态
     */
    private Date createTime;
}
