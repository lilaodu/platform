package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@TableName(value = "t_user_upgrade_intervene")
public class UserUpgradeInterveneEntity implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 干预时的等级
     */
    private String rank;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 干预时间
     */
    private Date createTime;

    /**
     * 版本号
     */
    private Integer version;
}

