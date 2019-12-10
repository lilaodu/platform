package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 抽奖奖项实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-13 5:55 PM
 **/
@Data
@Entity
@TableName(value = "t_luck_draw_config")
public class LuckDrawConfigEntity implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 抽奖表id
     */
    private Integer drawId;

    /**
     * 文字
     */
    private String words;

    /**
     * 图片
     */
    private String image;

    /**
     * 概率
     */
    private Double probability;

    /**
     * 货币符号
     */
    private String symbol;

    /**
     * 数量
     */
    private BigDecimal num;

    /**
     * 类型
     */
    private String type;

    /**
     * 奖项(几等奖)
     */
    private  Integer prize;

    /**
     * 位置
     */
    private Integer position;

    /**
     * 创建时间
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
}
