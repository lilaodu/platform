package com.blockchain.platform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.FavCoinDTO;
import com.blockchain.platform.pojo.entity.UserFavCoinEntity;

/**

 **/
public interface IUserFavCoinService extends IService<UserFavCoinEntity> {

	/**
     * 用户自选货币
     * @param dto
     * @return
     */
    UserFavCoinEntity findFavByCondition(FavCoinDTO dto);
}
