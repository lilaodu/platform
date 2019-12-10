package com.blockchain.platform.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.OTCTrustEntity;
import com.blockchain.platform.pojo.vo.OTCTrustVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * otc信任管理接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-19 9:17 PM
 **/
@Mapper
public interface OTCTrustMapper extends BaseMapper<OTCTrustEntity> {

    /**
     * 信任管理查询
     * @param dto
     * @return
     */
    List<OTCTrustVO> query(PageDTO dto);

    /**
     * （信任/屏蔽）用户 | 举报广告
     * @param entity
     * @return
     */
    Integer add(OTCTrustEntity entity);

    /**
     * 取消 信任/屏蔽
     * @param entity
     * @return
     */
    Integer update(OTCTrustEntity entity);
}
