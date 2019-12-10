package com.blockchain.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.stereotype.Service;

import com.blockchain.platform.mapper.ChartMapper;
import com.blockchain.platform.pojo.dto.KlineDTO;
import com.blockchain.platform.pojo.vo.KlineVO;
import com.blockchain.platform.service.IChartService;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 图表类服务实现类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-23 10:34 AM
 **/
@Service
public class ChartServiceImpl implements IChartService {

    /**
     * 图表数据
     */
    @Resource
    private ChartMapper mapper;

    @Override
    public List<KlineVO> tvKline(KlineDTO dto) {
        return mapper.tvKline( BeanUtil.beanToMap( dto));
    }

    @Override
    public List<KlineVO> history(KlineDTO dto) {
        return mapper.history( dto);
    }
    
}
