package com.blockchain.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.mapper.ChequeMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.UserChequeEntity;
import com.blockchain.platform.pojo.vo.ChequeVO;
import com.blockchain.platform.service.IChequeService;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 提币地址管理服务实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 12:16 PM
 **/
@Service
public class ChequeServiceImpl extends ServiceImpl<ChequeMapper, UserChequeEntity> implements IChequeService {

    @Resource
    private ChequeMapper mapper;

    @Override
    public List<ChequeVO> query(PageDTO dto) {
        return mapper.query( dto);
    }

    @Override
    public Boolean modify(UserChequeEntity entity) {
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
    public UserChequeEntity findByCondition(BaseDTO dto) {
        return mapper.findByCondition( dto);
    }

    @Override
    public Boolean delete(UserChequeEntity entity) {
        return mapper.update( entity) > 0;
    }

}
