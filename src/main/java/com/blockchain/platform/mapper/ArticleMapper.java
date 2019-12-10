package com.blockchain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.ArticleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文章数据库接口
 * @author ml
 * @create 2019-07-16 5:01 PM
 */
@Mapper
public interface ArticleMapper extends BaseMapper<ArticleEntity> {


    /**
     * 获取文章详情
     * @param dto
     * @return
     */
    ArticleEntity findByCondition(BaseDTO dto);
}