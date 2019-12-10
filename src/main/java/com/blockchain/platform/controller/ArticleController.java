package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.ArticleEntity;
import com.blockchain.platform.pojo.vo.ArticleVO;
import com.blockchain.platform.service.IArticleService;
import com.blockchain.platform.utils.ExUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 文章控制器
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 5:06 PM
 **/
@RestController
@RequestMapping("article")
public class ArticleController extends BaseController {

    /**
     * 文章服务类接口
     */
    @Resource
    private IArticleService articleService;

    /**
     * 文章详情
     * @param id
     * @return
     */
    @RequestMapping("/{id}")
    public ResponseData detail(@PathVariable(name = "id") Integer id){
        ResponseData data = ResponseData.ok();
        try {
            //获取有效的文章详情
            ArticleEntity entity = articleService.findByCondition( BaseDTO.builder()
                                                                        .state(BizConst.ArticleConst.STATE_PUBLISH)
                                                                        .id( id).build());
            if(ObjectUtil.isNotEmpty( entity)){
                //返回页面数据
                data.setData(BeanUtil.toBean( entity, ArticleVO.class));
            }
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }


}
