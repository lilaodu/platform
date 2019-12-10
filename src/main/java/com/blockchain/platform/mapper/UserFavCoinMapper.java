package com.blockchain.platform.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.FavCoinDTO;
import com.blockchain.platform.pojo.entity.UserFavCoinEntity;
import com.blockchain.platform.pojo.vo.UserFavCoinVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 咨询建议留言接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 1:45 PM
 **/
@Mapper
public interface UserFavCoinMapper extends BaseMapper<UserFavCoinEntity> {

	/**
     * 用户自选货币
     * @param dto
     * @return
     */
    UserFavCoinEntity findFavByCondition(FavCoinDTO dto);

    /**
     * 获取用户自选列表
     * @param dto
     * @return
     */
    List<UserFavCoinVO> favorTokens(BaseDTO dto);
}
