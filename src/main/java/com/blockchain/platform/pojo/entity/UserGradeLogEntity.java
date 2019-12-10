package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_grade_log")
public class UserGradeLogEntity implements Serializable {

    /**
     * id
     */
	@Id
    private Integer id;
	
	/**
	 * 用户id
	 */
	@Column(name = "user_id")
    private Integer userId;

    /**
     * 变更前等级
     */
	@Column(name = "grade_before")
    private String gradeBefore;

    /**
     * 变更后的等级
     */
	private String gradeAfter;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}