package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/**
 * 工单实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-26 2:37 PM
 **/
@Data
@Entity
@TableName(value = "t_work_order")
public class WorkOrderEntity implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 问题
     */
    private String question;

    /**
     * 原因
     */
    private String reason;

    /**
     * 证明材料
     */
    private String material;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 编辑时间
     */
    private Date updateTime;
}
