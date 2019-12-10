package com.blockchain.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.mapper.ArticleMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.ArticleEntity;
import com.blockchain.platform.service.IArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 文章接口实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 5:01 PM
 **/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleEntity> implements IArticleService {

    /**
     * 文章数据库接口
     */
    @Resource
    private ArticleMapper mapper;

    @Override
    public ArticleEntity findByCondition(BaseDTO dto) {
        return mapper.findByCondition( dto);
    }
}
