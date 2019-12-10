package com.blockchain.platform.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: 审核工作流实体类
 * @author: zjl
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_draw_money_flow")
public  class DrawMoneyFlowEntity implements Serializable {

    /**
     * 主键ID
     */
    @Id
    private int id;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 版本号
     */
    @Column(name = "version")
    private int version;
    /**
     * 操作用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 出入记录ID  外键
     */
    @Column(name = "flow_id")
    private Integer flowId;

    /**
     * 当前节点
     */
    @Column(name = "flow_node")
    private String flowNode;


    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 入库时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
