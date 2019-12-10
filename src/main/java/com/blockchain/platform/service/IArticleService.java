package com.blockchain.platform.service;


import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.ArticleEntity;

/**
 * 文章服务类接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 4:56 PM
 **/
public interface IArticleService {

    /**
     * 获取文章详情
     * @param dto
     * @return
     */
    ArticleEntity findByCondition(BaseDTO dto);
}
