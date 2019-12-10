package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {

    /**
     * 订单id
     */
    private Integer id;

    /**
     * 评论意见（不满意，一般，不满意）
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String opinion;
}
