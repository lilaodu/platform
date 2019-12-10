package com.blockchain.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockchain.platform.mapper.UserAssetsFlowMapper;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.UserAssetsFlowEntity;
import com.blockchain.platform.service.IUserAssetsFlowService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 用户流水实现
 * @author zjl
 */
@Service
public class UserAssetsFlowServiceImpl extends ServiceImpl<UserAssetsFlowMapper,UserAssetsFlowEntity> implements IUserAssetsFlowService {

    private UserAssetsFlowMapper mapper;
    @Override
    public IPage<UserAssetsFlowEntity> getAssetsFlow(PageDTO dto) {
        //分页
        IPage<UserAssetsFlowEntity> page =new Page<>(dto.getPageNum(),dto.getPageSize());
        IPage<UserAssetsFlowEntity> pageList= mapper.selectPage(page,null);//查询,没有条件

       return pageList;

    }
}
