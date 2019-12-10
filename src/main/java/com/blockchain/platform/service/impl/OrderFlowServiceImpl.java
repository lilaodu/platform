package com.blockchain.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.mapper.OrderFlowMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.RankingDTO;
import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import com.blockchain.platform.pojo.vo.RankingVO;
import com.blockchain.platform.service.IOrderFlowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * otc订单服务实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-21 8:38 PM
 **/
@Service
public class OrderFlowServiceImpl extends ServiceImpl<OrderFlowMapper, OrderFlowEntity> implements IOrderFlowService {

    /**
     * 订单数据接口
     */
    @Resource
    private OrderFlowMapper mapper;

    @Override
    public List<RankingVO> optional(BaseDTO dto) {
        //返回数据
        List<RankingVO> vos = new ArrayList<>();
        // 实际数据
        List<RankingDTO> ranking = mapper.optional( dto);
        // 格式化数据
        for (int idx = 0;idx < ranking.size(); idx ++) {
            RankingDTO rankingDTO = ranking.get( idx);
            // 创建
            RankingVO vo = BeanUtil.toBean( rankingDTO.getTick(), RankingVO.class);
            // 复制其他属性
            BeanUtil.copyProperties( rankingDTO, vo);
            vos.add( vo);
        }
        return vos;
    }
}
