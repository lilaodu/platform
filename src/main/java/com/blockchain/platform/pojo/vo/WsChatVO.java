package com.blockchain.platform.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * web socket 返回对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-23 3:50 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WsChatVO implements Serializable {

    /**
     * 用户聊天列表消息
     */
    List<OTCChatVO> list;
}
