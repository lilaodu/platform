package com.blockchain.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.UserAssetsFlowEntity;

import java.util.List;

/**
 * 用户账户流水
 * @author zjl
 */
public interface IUserAssetsFlowService extends IService<UserAssetsFlowEntity> {

    /**
     * 用户流水
     * @param dto
     * @return
     */
    IPage<UserAssetsFlowEntity> getAssetsFlow(PageDTO dto);
}
