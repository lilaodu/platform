package com.blockchain.platform.pojo.vo;

import com.blockchain.platform.constant.BizConst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * otc 聊天VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 12:24 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTCChatVO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 类型
     */
    private String type;

    /**
     * 是否本人的消息
     */
    private String own;

    /**
     * 内容
     */
    private String content;

    /**
     * 当前用户头像
     */
    private String headImage;

    /**
     * 聊天时间
     */
    private Date time;

    /**
     * 时间戳
     */
    private Long stamp;

    /**
     * 获取当前聊天时间的时间戳
     * @return
     */
    public Long getStamp() {
        return time.getTime();
    }
}
