package com.blockchain.platform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.TrustDTO;
import com.blockchain.platform.pojo.entity.OTCTrustEntity;
import com.blockchain.platform.pojo.vo.OTCTrustVO;

import java.util.List;

/**
 * otc信任管理服务接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-19 9:21 PM
 **/
public interface IOTCTrustService extends IService<OTCTrustEntity> {

    /**
     * 信任管理查询
     * @param dto
     * @return
     */
    List<OTCTrustVO> query(PageDTO dto);

    /**
     * （信任/屏蔽）用户 | 举报广告
     * @param dto
     * @return
     */
    Boolean modify(TrustDTO dto);
}
