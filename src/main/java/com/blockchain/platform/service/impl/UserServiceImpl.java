package com.blockchain.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.gt.GeetestConfig;
import com.blockchain.platform.gt.GeetestLib;
import com.blockchain.platform.mapper.UserFavCoinMapper;
import com.blockchain.platform.mapper.UserMapper;
import com.blockchain.platform.mapper.UserWalletMapper;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.UserFavCoinVO;
import com.blockchain.platform.pojo.vo.UserVO;
import com.blockchain.platform.service.IDictionaryService;
import com.blockchain.platform.service.IUserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 用户管理接口实现类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:51 AM
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
    @Resource
    private IDictionaryService dictService;

    @Resource
    UserWalletMapper userWalletMapper;

    /**
     * 用户数据库接口
     */
    @Resource
    private UserMapper mapper;

    /**
     * 用户自选货币排行
     */
    @Resource
    private UserFavCoinMapper userFavCoinMapper;

    @Override
    public List<UserVO> query(PageDTO dto) {
        return mapper.query( dto);
    }

    @Override
    public UserEntity findUserByCondition(UserDTO dto) {
        return mapper.findByCondition( dto);
    }

    @Override
    public Boolean modify(UserEntity entity) {
        return mapper.updateById(entity) > 0;
    }

	@Override
	public List<UserFavCoinVO> favorTokens(BaseDTO dto) {
		
		return userFavCoinMapper.favorTokens( dto);
	}

    /**
     * 人机验证
     * @param dto
     * @return
     */
    @Override
    public Boolean validRobotLogin(RobotDTO dto) {
        boolean result = false;
        // 初始化SDK
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        GeetestDTO geetestDTO = GeetestDTO.builder()
                .client_type("web") //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
                .user_id( dto.getTxHash()) //网站用户id
                .ip_address( dto.getIp()).build(); //传输用户请求验证时所携带的IP
        HashMap<String, String> param = JSON.parseObject(JSONUtil.toJsonStr( geetestDTO),
                new TypeReference<HashMap<String, String>>(){});
        int gtResult = 0;
        // 服务状态
        int gt_server_status_code = dto.getGt_server_status();
        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest( dto.getGeetest_challenge(),
                    dto.getGeetest_validate(),
                    dto.getGeetest_seccode(), param);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            gtResult = gtSdk.failbackValidateRequest(dto.getGeetest_challenge(),
                    dto.getGeetest_validate(),
                    dto.getGeetest_seccode());
        }
        if (gtResult == 1) {
            // 验证成功
            result = true;
        }
        return result;
    }

    /**
     * 注册的
     * @param entity 用户实体
     * @param code 邀请码
     * @return
     */

    @Override
    @Transactional(rollbackFor = {Exception.class, BcException.class})
    public Boolean register(UserEntity entity, String code) {
        List<CoinEntity> list = dictService.coin( BaseDTO.builder().state( BizConst.BIZ_STATUS_VALID).build());

        // 数据权限编码
        StringBuffer authority = new StringBuffer();
        if( StrUtil.isNotEmpty( code)){
            // 查询父级
            UserEntity parent = mapper.findByCondition( UserDTO.builder()
                    .invitationCode( code)
                    .state(BizConst.BIZ_STATUS_VALID).build());
            if ( ObjectUtil.isNotEmpty( parent)){
                // 上级权限
                authority.append( parent.getAuthority());
            }
        }
        int k = mapper.insert(entity);
        for (int idx = 0;idx < list.size(); idx ++) {
            CoinEntity e = list.get(idx);
            UserWalletEntity userWalletEntity=new UserWalletEntity();
            userWalletEntity.setSymbol(e.getSymbol());
            userWalletEntity.setUserId(entity.getId());
            userWalletMapper.insert(userWalletEntity);
        }
        //用户注册成功并且上级不为null
        if ( k >  0 ) {
            authority.append( entity.getId()).append( StrUtil.UNDERLINE);
            // 修改用户权限
            UserEntity db = UserEntity.builder()
                                        .authority( authority.toString())
                                        .id( entity.getId()).build();
            //把当前id加到父级id的下级里
            k = mapper.updateById(db);
        }

        return k > 0;
    }

    @Override
    public List<UserEntity> queryList(BaseDTO dto) {
        return mapper.queryList( dto);
    }

    @Override
    public List<UserMessageEntity> message(PageDTO dto) {
        return mapper.message( dto);
    }

    @Override
    public UserMessageEntity findMessageByCondition(BaseDTO dto) {
        return mapper.findMessageByCondition( dto);
    }

    @Override
    public Boolean modifyMessage(UserMessageEntity entity) {
        return mapper.modifyMessage( entity) > 0;
    }

    @Override
    public UserDTO findLoginUser(UserDTO dto) {
        return mapper.findLoginUser( dto);
    }

    @Override
    public List<UserEntity> findTeam(UserDTO dto) {
        //获取当前登录用户的下级用户
        List<UserEntity> list = mapper.findTeam( dto);
        //递归查询
        list = recursion( list);
        return list;
    }

    private List<UserEntity> recursion(List<UserEntity> list){
        //遍历用户列表
        for(int idx = 0; idx < list.size(); idx ++){
            UserEntity entity = list.get( idx);
            //获取当前登录用户的下级用户
            List<UserEntity> vos = mapper.findTeam( UserDTO.builder().id( entity.getId()).build());
            if(CollUtil.isNotEmpty( vos)){
                list = CollUtil.addAllIfNotContains( list, vos);
                recursion( vos);
            }
        }
        return list;
    }
}
