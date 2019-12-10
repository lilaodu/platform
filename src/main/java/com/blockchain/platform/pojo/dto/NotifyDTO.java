package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.pojo.vo.WsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 通知消息
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyDTO implements Serializable {

    /**
     * 交易币
     */
    @NotEmpty
    private String symbol;

    /**
     * 通知用户
     */
    private Set<String> userIds;

    /**
     * 通知信息
     */
    private WsVO vo;
    
}
