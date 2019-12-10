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
import java.io.Serializable;
import java.util.Date;

/**
 * otc聊天管理实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 4:03 PM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_otc_chat")
public class OTCChatEntity implements Serializable {

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
     * 广告编号
     */
    private String advertNumber;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 类型（文本："text"，图片："img"）
     */
    private String type;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;
}
