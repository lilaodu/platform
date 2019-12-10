package com.blockchain.platform.pojo.entity;

import lombok.Builder;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@Entity
@TableName("t_user_fav_coin")
public class UserFavCoinEntity implements Serializable {

    
    @Id
    private Integer id;

    
    private Integer userId;

    
    private String symbol;

    
    private String market;

    /**
     * 1:EXPIRY,2:VALID
     */
    private Integer state;

    
    private Date inDate;

    
    private Integer version;
}