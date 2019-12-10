package com.blockchain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.UserPaymentEntity;
import com.blockchain.platform.pojo.vo.UserPaymentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 付款管理数据库接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 10:55 PM
 **/
@Mapper
public interface PaymentMapper extends BaseMapper<UserPaymentEntity> {

    /**
     * 获取当前登录用户有效的支付方式
     * @param dto
     * @return
     */
    List<UserPaymentVO> query(BaseDTO dto);

    /**
     * 新增支付方式
     * @param entity
     * @return
     */
    Integer add(UserPaymentEntity entity);

    /**
     * 删除支付方式
     * @param dto
     * @return
     */
    Integer update(BaseDTO dto);

    /**
     * 获取用户支持的支付方式
     * @param dto
     * @return
     */
    List<UserPaymentVO> list(BaseDTO dto);

    /**
     * 获取用户支持的支付方式
     * @param dto
     * @return
     */
    List<String> type(BaseDTO dto);
}
