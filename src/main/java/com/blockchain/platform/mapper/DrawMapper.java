package com.blockchain.platform.mapper;

import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.LuckDrawDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.LuckDrawEntity;
import com.blockchain.platform.pojo.entity.LuckDrawLogEntity;
import com.blockchain.platform.pojo.vo.LuckDrawConfigVO;
import com.blockchain.platform.pojo.vo.LuckDrawLogVO;
import com.blockchain.platform.pojo.vo.LuckDrawVO;
import com.blockchain.platform.pojo.vo.PrizeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 抽奖数据库接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-13 6:25 PM
 **/
@Mapper
public interface DrawMapper {

    /**
     * 抽奖列表接口
     * @param dto
     * @return
     */
    List<LuckDrawVO> query(BaseDTO dto);

    /**
     * 获取奖项列表
     * @param dto
     * @return
     */
    List<LuckDrawConfigVO> list(BaseDTO dto);

    /**
     * 抽奖活动记录
     * @param dto
     * @return
     */
    List<LuckDrawLogVO> log(PageDTO dto);

    /**
     * 抽奖历史记录
     * @param dto
     * @return
     */
    List<LuckDrawLogVO> history(BaseDTO dto);

    /**
     * 统计用户抽奖次数
     * @param dto
     * @return
     */
    PrizeVO count(BaseDTO dto);

    /**
     * 获取抽奖活动信息
     * @param dto
     * @return
     */
    LuckDrawDTO findDraw(BaseDTO dto);

    /**
     * 添加抽奖记录
     * @param entity
     * @return
     */
    Integer addLog(LuckDrawLogEntity entity);

    /**
     * 统计抽奖活动的记录条数
     * @param dto
     * @return
     */
    Integer countLog(BaseDTO dto);

    /**
     * 详情
     * @param dto
     * @return
     */
    LuckDrawEntity findByCondition(BaseDTO dto);
}
