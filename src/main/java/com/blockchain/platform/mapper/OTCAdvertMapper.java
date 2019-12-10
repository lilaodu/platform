package com.blockchain.platform.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.OTCAdvertPageDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.OTCAdvertEntity;
import com.blockchain.platform.pojo.vo.OTCAdvertVO;
import com.blockchain.platform.pojo.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * otc广告接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 5:25 PM
 **/
@Mapper
public interface OTCAdvertMapper extends BaseMapper<OTCAdvertEntity> {

    /**
     * 列表 -- 我的广告
     * @param dto
     * @return
     */
    List<OTCAdvertVO> query(PageDTO dto);


    OTCAdvertEntity findByNumber(@Param(value = "number") String number);

    /**
     * 发布广告
     * @param entity
     * @return
     */
    Integer add(OTCAdvertEntity entity);

    /**
     * 编辑
     * @param entity
     * @return
     */
    Integer update(OTCAdvertEntity entity);

    /**
     * 广告详情
     * @param dto
     * @return
     */
    OTCAdvertEntity findByCondition(BaseDTO dto);

    /**
     * 获取出售的广告列表
     * @param dto
     * @return
     */
    List<OTCAdvertVO> list(PageDTO dto);

    /**
     * 详情
     * @param dto
     * @return
     */
    OTCAdvertEntity detail(BaseDTO dto);
}
