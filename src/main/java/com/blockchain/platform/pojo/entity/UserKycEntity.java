package com.blockchain.platform.pojo.entity;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@TableName("t_user_kyc")
public class UserKycEntity extends Model<Model<?>> implements Serializable {
	
	@Id
    private Integer id;
	
	/**
	 * 名字
	 */
    private String firstName;

    /**
     * 姓氏
     */
    private String lastName;

    /**
     * sex
     */
    private String sex;

    /**
     * 国家或地区
     */
    private String country;

    /**
     * 住址
     */
    private String address;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * number
     */
    private String idNo;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 城市
     */
    private String city;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 手持照片
     */
    private String picHold;

    /**
     * 证件 正面照
     */
    private String picFace;

    /**
     * 证件 背面照片
     */
    private String picBack;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 编辑时间
     */
    private Date updateTime;

    /**
     * 认证审核理由
     */
    private String remark;

    /**
     * 当前KYC 步骤
     */
//    @Transient
    @TableField(exist=false)
    private Integer step;

}