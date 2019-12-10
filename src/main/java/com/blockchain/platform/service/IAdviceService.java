package com.blockchain.platform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.UserAdviceEntity;
import com.blockchain.platform.pojo.vo.UserAdviceVO;

import java.util.List;

/**
 * 咨询建议服务接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 1:42 PM
 **/
public interface IAdviceService extends IService<UserAdviceEntity> {

    /**
     * 分页列表
     * @param dto
     * @return
     */
    List<UserAdviceVO> query(PageDTO dto);

    /**
     * 新增或编辑
     * @param entity
     * @return
     */
    Boolean modify(UserAdviceEntity entity);

    /**
     * 详情
     * @param dto
     * @return
     */
    UserAdviceEntity findByCondition(BaseDTO dto);

    /**
     *
     * 获取用户最后一次会话
     * @param dto
     * @return
     */
    UserAdviceEntity findUserLastAdvice(BaseDTO dto);

}
