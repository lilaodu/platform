package com.blockchain.platform.service.impl;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.mapper.PaymentMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PaymentDTO;
import com.blockchain.platform.pojo.entity.UserPaymentEntity;
import com.blockchain.platform.pojo.vo.UserPaymentVO;
import com.blockchain.platform.service.IPaymentService;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 付款管理 实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 10:52 PM
 **/
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, UserPaymentEntity> implements IPaymentService {

    /**
     * 支付数据库接口
     */
    @Resource
    private PaymentMapper mapper;

    @Override
    public List<UserPaymentVO> query(BaseDTO dto) {
        return mapper.query( dto);
    }

    @Override
    public Boolean add(UserPaymentEntity entity) {
        //新增支付
        return saveOrUpdate( entity);
    }

    @Override
    public Boolean modify(BaseDTO dto) {
        return mapper.update( dto) > 0;
    }

	@Override
	public List<UserPaymentEntity> getPayment() {
		
		return null;
	}

    @Override
    public List<UserPaymentVO> list(BaseDTO dto) {
        return mapper.list( dto);
    }

    @Override
    public List<String> type(BaseDTO dto) {
        return mapper.type( dto);
    }
}
