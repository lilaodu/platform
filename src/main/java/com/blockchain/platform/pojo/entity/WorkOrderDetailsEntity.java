package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/**
 * 工单详情实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-26 2:37 PM
 **/
@Data
@Entity
@TableName(value = "t_work_order_details")
public class WorkOrderDetailsEntity implements Serializable {

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
     * 工单id
     */
    private Integer workId;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;
}
