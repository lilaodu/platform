package com.blockchain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.entity.UserMessageEntity;
import com.blockchain.platform.pojo.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户数据库接口
 * @author denglong
 * @create 2019年07月14日11:39:31
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {


    /**
     * 查询用户新
     * @param dto
     * @return
     */
    List<UserVO> query(PageDTO dto);

    /**
     * 查询实体对象
     * @param dto
     * @return
     */
    UserEntity findByCondition(UserDTO dto);

    /**
     * 用户列表(不分页)
     * @param dto
     * @return
     */
    List<UserEntity> queryList(BaseDTO dto);

    /**
     * 修改用户信息
     * @param entity
     * @return
     */
    Integer update( UserEntity entity);

    /**
     * 用户站内信
     * @param dto
     * @return
     */
    List<UserMessageEntity> message(PageDTO dto);

    /**
     * 获取站内信详情
     * @param dto
     * @return
     */
    UserMessageEntity findMessageByCondition(BaseDTO dto);

    /**
     * 更改站内信状态
     * @param entity
     * @return
     */
    Integer modifyMessage(UserMessageEntity entity);

    /**
     * 登录用户缓存信息
     * @param dto
     * @return
     */
    UserDTO findLoginUser(UserDTO dto);

    /**
     * 用户的团队列表
     * @param dto
     * @return
     */
    List<UserEntity> findTeam(UserDTO dto);
}