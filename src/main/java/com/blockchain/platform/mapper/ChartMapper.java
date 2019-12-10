package com.blockchain.platform.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.blockchain.platform.pojo.dto.KlineDTO;
import com.blockchain.platform.pojo.vo.KlineVO;

import java.util.List;
import java.util.Map;

/**
 * 图标类数据接口
 */
@Mapper
public interface ChartMapper {


    /**
     * 数据库kline
     * @param dto
     * @return
     */
    List<KlineVO> tvKline(Map<String, Object> dto);


    /**
     * 查询历史K线数据
     * @param dto
     * @return
     */
    List<KlineVO> history(KlineDTO dto);
}
