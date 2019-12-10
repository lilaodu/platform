package com.blockchain.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.RobotDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.entity.UserMessageEntity;
import com.blockchain.platform.pojo.vo.UserFavCoinVO;
import com.blockchain.platform.pojo.vo.UserVO;

import java.util.List;

/**
 * 用户管理接口
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:51 AM
 **/
public interface IUserService extends IService<UserEntity> {

    /**
     * 分页查询数据
     * @param dto
     * @return
     */
    List<UserVO> query(PageDTO dto);

    /**
     * 条件查询当前对象
     * @param dto
     * @return
     */
    UserEntity findUserByCondition(UserDTO dto);


    /**
     * 新增或者修改
     * @param entity
     * @return
     */
    Boolean modify(UserEntity entity);

    /**
     * 自选列表
     * @param dto
     * @return
     */
    List<UserFavCoinVO> favorTokens(BaseDTO dto);

    /**
     * 人机验证
     * @return
     */
    Boolean validRobotLogin(RobotDTO dto);
    
    /**
     * 用户注册
     * @param entity
     * @param code 邀请码
     * @return
     */
    Boolean register(UserEntity entity,String code);

    /**
     * 获取不分页的用户列表
     * @param dto
     * @return
     */
    List<UserEntity> queryList(BaseDTO dto);

    /**
     *
     * 用户站内信
     * @param dto
     * @return
     */
    List<UserMessageEntity> message(PageDTO dto);

    /**
     * 站内信详情
     * @param dto
     * @return
     */
    UserMessageEntity findMessageByCondition(BaseDTO dto);

    /**
     * 更改站内信状态
     * @param entity
     * @return
     */
    Boolean modifyMessage(UserMessageEntity entity);

    /**
     * 登录用户信息
     * @param dto
     * @return
     */
    UserDTO findLoginUser( UserDTO dto);

    /**
     * 获取用户的团队人员列表
     * @param dto
     * @return
     */
    List<UserEntity> findTeam(UserDTO dto);
}
