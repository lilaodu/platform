package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.blockchain.platform.i18n.LocaleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * otc聊天模板管理实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 4:07 PM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_otc_chat_template")
public class OTCChatTemplateEntity implements Serializable {

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
     * 状态(1:有效，0：无效)
     */
    private Integer state;

    /**
     * 聊天内容
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;
}
