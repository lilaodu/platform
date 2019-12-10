package com.blockchain.platform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PaymentDTO;
import com.blockchain.platform.pojo.entity.UserPaymentEntity;
import com.blockchain.platform.pojo.vo.UserPaymentVO;

import java.util.List;

/**
 * 付款管理
 *
 * @author ml
 * @version 1.0
 * @create 2019-05-24 7:52 PM
 **/
public interface IPaymentService extends IService<UserPaymentEntity>{

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
    Boolean add(UserPaymentEntity entity);

    /**
     *
     * 修改支付方式
     * @param dto
     * @return
     */
    Boolean modify(BaseDTO dto);

    /**
     * 查询稳定币价格
     * @return
     */
    List<UserPaymentEntity> getPayment();

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
