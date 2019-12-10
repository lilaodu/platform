package com.blockchain.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.mapper.OTCChatMapper;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.OTCChatEntity;
import com.blockchain.platform.pojo.entity.OTCChatTemplateEntity;
import com.blockchain.platform.pojo.vo.OTCChatTemplateVO;
import com.blockchain.platform.pojo.vo.OTCChatVO;
import com.blockchain.platform.service.IOTCChatService;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * otc聊天服务实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 4:16 PM
 **/
@Service
public class OTCChatServiceImpl extends ServiceImpl<OTCChatMapper, OTCChatEntity> implements IOTCChatService {

    /**
     * otc聊天数据库接口
     */
    @Resource
    private OTCChatMapper mapper;

    /**
     * 缓存数据
     */
    @Resource
    private RedisPlugin redisPlugin;

    @Override
    @Transactional( rollbackFor = Exception.class)
    public List<OTCChatVO> query(BaseDTO dto) {
        //修改状态
        mapper.modify( BaseDTO.builder()
                    .orderNumber( dto.getOrderNumber())   //订单号
                    .userId( dto.getUserId())  //用户
                    .state(BizConst.ChatConst.STATE_READ).build());   //状态 -- 已读
        //获取聊天消息
        return mapper.query( dto);
    }

    @Override
    public Boolean send(OTCChatEntity entity) {
        return mapper.add( entity) > 0;
    }

    @Override
    public Boolean modify(OTCChatTemplateEntity entity) {
        //判断是新增还是删除
        Integer bool = IntUtils.INT_ZERO;
        if(IntUtils.isZero( entity.getId())){
            //新增
            bool = mapper.addTemplate( entity);
        }else{
            //删除
            bool = mapper.updateTemplate( entity);
        }
        return  bool > 0;
    }

    @Override
    public List<OTCChatTemplateVO> queryTemplate(PageDTO dto) {
        return mapper.queryTemplate( dto);
    }

    /**
     * 统计用户未读消息量
     * @param dto
     * @return
     */
    public Integer countUnRead(BaseDTO dto){

        List<String> merchants = redisPlugin.get( RedisConst.PLATFORM_OTC_MERCHANTS);

        if ( merchants.contains(StrUtil.toString( dto))) {
            dto.setState( BizConst.UserConst.OTC_USER_MERCHANT);
        } else {
            dto.setState( BizConst.UserConst.OTC_USER_NORMAL);
        }
        return mapper.countUnRead( dto);
    }
}
