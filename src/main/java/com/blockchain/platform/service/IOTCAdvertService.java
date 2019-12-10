package com.blockchain.platform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.OTCAdvertDTO;
import com.blockchain.platform.pojo.dto.OTCAdvertPageDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.OTCAdvertEntity;
import com.blockchain.platform.pojo.vo.OTCAdvertVO;
import com.blockchain.platform.pojo.vo.UserInfoVO;

import java.util.List;

/**
 * otc广告服务接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 5:29 PM
 **/
public interface IOTCAdvertService extends IService<OTCAdvertEntity> {

    /**
     * 列表 -- 我的广告
     * @param dto
     * @return
     */
    List<OTCAdvertVO> query(PageDTO dto);

    /**
     * 发布广告
     * @param entity
     * @return
     */
    Boolean modify(OTCAdvertEntity entity);

    /**
     * 下架广告
     */
    Boolean invalid(OTCAdvertEntity dto);

    /**
     * 获取出售的广告列表
     * @param dto
     * @return
     */
    List<OTCAdvertVO> list(PageDTO dto);

    /**
     * 广告详情
     * @param dto
     * @return
     */
    OTCAdvertEntity findByCondition(BaseDTO dto);

    /**
     * 详情
     * @param dto
     * @return
     */
    OTCAdvertEntity detail(BaseDTO dto);
}
