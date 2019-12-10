package com.blockchain.platform.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@Entity
@TableName("t_market_configs")
public class MarketConfigEntity implements Serializable {

	
    @Id
    private Integer id;

    
    @Column(name = "market")
    private String market;

    
    @Column(name = "state")
    private Integer state;

    
    @Column(name = "in_date")
    private Date inDate;

    
    @Column(name = "version")
    private Integer version;
    
    
    @Column(name = "sn")
    private Integer sn;
    
    
    
}
