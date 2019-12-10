package com.blockchain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.UserChequeEntity;
import com.blockchain.platform.pojo.vo.ChequeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 提币地址实现接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 12:19 PM
 **/
@Mapper
public interface ChequeMapper extends BaseMapper<UserChequeEntity> {

    /**
     * 分页查询地址管理接口
     * @param dto
     * @return
     */
    List<ChequeVO> query(PageDTO dto);

    /**
     * 添加
     * @param entity
     * @return
     */
    Integer add(UserChequeEntity entity);

    /**
     * 修改
     * @param entity
     * @return
     */
    Integer update(UserChequeEntity entity);

    /**
     * 详情
     * @param dto
     * @return
     */
    UserChequeEntity findByCondition(BaseDTO dto);
}
