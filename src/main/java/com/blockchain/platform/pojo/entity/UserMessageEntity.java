package com.blockchain.platform.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@TableName(value = "t_user_message")
public class UserMessageEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 消息状态
     */
    private Integer state;

    /**
     * 标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;
}
