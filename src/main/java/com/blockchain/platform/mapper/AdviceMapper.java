package com.blockchain.platform.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.UserAdviceEntity;
import com.blockchain.platform.pojo.vo.UserAdviceVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 咨询建议留言接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 1:45 PM
 **/
@Mapper
public interface AdviceMapper extends BaseMapper<UserAdviceEntity> {

    /**
     * 分页列表
     * @param dto
     * @return
     */
    List<UserAdviceVO> query(PageDTO dto);

    /**
     * 添加建议
     * @param entity
     * @return
     */
    Integer add(UserAdviceEntity entity);

    /**
     * 编辑建议
     * @param entity
     * @return
     */
    Integer update(UserAdviceEntity entity);

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
