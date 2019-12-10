package com.blockchain.platform.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.mapper.LockWarehouseConfigMapper;
import com.blockchain.platform.pojo.entity.LockWarehouseConfigEntity;
import com.blockchain.platform.service.ILockWarehouseConfigService;

@Service
public class LockWarehouseConfigServiceimpl extends ServiceImpl<LockWarehouseConfigMapper, LockWarehouseConfigEntity> implements ILockWarehouseConfigService {

}
