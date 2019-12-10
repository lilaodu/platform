package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@TableName("t_user_team")
public class UserTeamEntity implements Serializable {

    /**
     * id
     */
	@Id
    private Integer id;
	
	/**
	 * 用户id
	 */
	@Column(name = "user_id")
    private String userId;

    /**
     * 直推人数
     */
    @Column(name = "num")
    private Integer num;

    /**
     * 团队交易总额
     */
    @Column(name = "total")
    private BigDecimal total;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;

    /**
     * 编辑时间
     */
    @Column(name = "update_time")
    private Date updateTime;

}