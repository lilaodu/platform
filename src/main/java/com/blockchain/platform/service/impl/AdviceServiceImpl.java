package com.blockchain.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.mapper.AdviceMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.UserAdviceEntity;
import com.blockchain.platform.pojo.vo.UserAdviceVO;
import com.blockchain.platform.service.IAdviceService;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 咨询建议留言接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 1:43 PM
 **/
@Service
public class AdviceServiceImpl extends ServiceImpl<AdviceMapper, UserAdviceEntity> implements IAdviceService {

    /**
     * 咨询建议数据接口
     */
    @Resource
    private AdviceMapper mapper;


    @Override
    public List<UserAdviceVO> query(PageDTO dto) {
        return mapper.query( dto);
    }

    @Override
    public Boolean modify(UserAdviceEntity entity) {
        //判断是新增还是编辑
        Integer bool = IntUtils.INT_ZERO;
        if(IntUtils.isZero( entity.getId())){
            //新增
            bool = mapper.add( entity);
        }else{
            //编辑
            bool = mapper.update( entity);
        }
        return bool > 0;
    }

    @Override
    public UserAdviceEntity findByCondition(BaseDTO dto) {
        return mapper.findByCondition( dto);
    }

    @Override
    public UserAdviceEntity findUserLastAdvice(BaseDTO dto) {
        return mapper.findUserLastAdvice( dto);
    }
}
