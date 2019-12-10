package com.blockchain.platform.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 提币地址配置
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 12:11 PM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_cheque")
public class UserChequeEntity implements Serializable {

    @Id
    private Integer id;

    /**
     * 代币
     */
    @Column(name = "token")
    private String token;

    /**
     * 所属用户
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 钱包地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "in_date")
    private Date inDate;

    /**
     * 数据状态
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;
}
