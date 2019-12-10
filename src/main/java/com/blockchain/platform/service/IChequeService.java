package com.blockchain.platform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.UserChequeEntity;
import com.blockchain.platform.pojo.vo.ChequeVO;

import java.util.List;

/**
 * 提币地址管理服务接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 12:15 PM
 **/
public interface IChequeService extends IService<UserChequeEntity> {

    /**
     * 分页查询地址管理接口
     * @param dto
     * @return
     */
    List<ChequeVO> query(PageDTO dto);

    /**
     * 新增或编辑
     * @param entity
     * @return
     */
    Boolean modify(UserChequeEntity entity);

    /**
     * 详情
     * @param dto
     * @return
     */
    UserChequeEntity findByCondition(BaseDTO dto);

    /**
     * 删除提币地址
     * @param entity
     * @return
     */
    Boolean delete( UserChequeEntity entity);
}
