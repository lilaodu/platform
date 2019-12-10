package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.blockchain.platform.utils.IntUtils;
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
 * 商户实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 11:30 AM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_otc_merchant")
public class OTCMerchantEntity implements Serializable {

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
     * 商户昵称
     */
    private String nickName;

    /**
     * 申请原因
     */
    private String reason;

    /**
     * 交易数量
     */
    private Integer num = IntUtils.INT_ZERO;

    /**
     * 好评率
     */
    private String praise;

    /**
     * 信任我的人数
     */
    private Integer trustedNum = IntUtils.INT_ZERO;

    /**
     * 成为商户的时间
     */
    private Date merchantTime;

    /**
     * 第一次交易时间
     */
    private Date firstTradeTime;

    /**
     *  创建时间
     */
    private Date createTime;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 修改时间
     */
    private Date updateTime;
}
