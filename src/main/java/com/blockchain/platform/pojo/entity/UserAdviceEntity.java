package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.blockchain.platform.i18n.LocaleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 咨询建议entity
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 1:23 PM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_advice")
public class UserAdviceEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 业务号
     */
    @Column(name = "bill_no")
    private String billNo;

    /**
     * 消息类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 咨询内容
     */
    @Column(name = "content")
    @NotEmpty(message = LocaleKey.ADVICE_CONTENT_NOT_NULL)
    private String content;

    /**
     * 咨询用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 咨询时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 咨询状态
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;
}
