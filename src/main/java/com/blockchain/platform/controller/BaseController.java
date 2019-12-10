package com.blockchain.platform.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.CoinEntity;
import com.blockchain.platform.pojo.entity.MarketCoinEntity;
import com.blockchain.platform.utils.IntUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 基础控制器父类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:29 AM
 **/
public abstract class BaseController {

    /**
     * 缓存工具
     */
    @Resource
    public RedisPlugin redisPlugin;


    /**
     * 默认语言
     */
    private static List<String> defaultLang = CollUtil.newArrayList("zh-CN","en-US");

    /**
     * 用户token
     * @param request
     * @return
     */
    private static String token(HttpServletRequest request) {
        return request.getHeader( AppConst.TOKEN);
    }

    /**
     * 获取 web 用户登录 token
     * @param username
     * @return
     */
    protected String getWebToken(String username) {
        String token = redisPlugin.get( username);
        if( StrUtil.isEmpty( token)){
            token = IdUtil.simpleUUID();
        }
        // 用户信息
        UserDTO dto = redisPlugin.hget( RedisConst.PLATFORM_USER_DATA, token);
        if( !ObjectUtil.isEmpty( dto)) {
            redisPlugin.hdel( RedisConst.PLATFORM_USER_DATA, token);
            // 重新获取token
            token = IdUtil.simpleUUID();
        }
        // 重新设置对应关系
        redisPlugin.set( username, token, AppConst.USER_LOGIN_TIME);
        return token;
    }
    
    /**
     * 获取当前用户登录对象
     * @param request
     * @return
     */
    protected UserDTO getLoginUser(HttpServletRequest request) {
    	String token = token( request);
        if( !StrUtil.isEmpty( token)){
            return redisPlugin.hget(RedisConst.PLATFORM_USER_DATA, token);
        }
        return null;
    }

    /**
     * 重新设置登录对象
     * @param request
     * @param dto
     */
    protected void setLoginUser(HttpServletRequest request, UserDTO dto){
        String token = request.getHeader(AppConst.TOKEN);
        if( StrUtil.isNotEmpty( token)){
            redisPlugin.hset(RedisConst.PLATFORM_USER_DATA, token, dto);
        }
    }

    /**
     * 限制开始
     * @return
     */
    protected Integer getLimitBegin() {
        return AppConst.DEFAULT_PAGE_NO - 1;
    }

    /**
     * 获取注册验证码
     * @param username
     * @return
     */
    protected String getCaptchaCode(String type, String username) {
        if( StrUtil.isNotEmpty( username)) {
            return redisPlugin.get( StrUtil.addSuffixIfNot( type, username));
        }
        return StrUtil.EMPTY;
    }
    
    /**
     * 判断是邮箱
     * @param username
     * @return
     */
    protected static Boolean isEmail(String username) {
        //邮箱正则
    	String email = AppConst.EMAIL;
    	//开始匹配邮箱
    	Matcher m = Pattern.compile(email).matcher( username);
    	//判断
    	return m.matches();
    }

    /**
     * 判断是电话号码
     * @param username
     * @return
     */
    protected static Boolean isMobile(String username){
        //手机号码正则
        String phone = AppConst.PHONE;
        //开始匹配手机号码
        Matcher m = Pattern.compile( phone).matcher( username);
        //判断
        return m.matches();
    }

    /**
     * 判断是邮箱还是电话号码
     * @param username
     * @return
     */
    protected static UserDTO judge( String username){
        //返回对象
        UserDTO user = UserDTO.builder().build();
        //邮箱正则
        String email = AppConst.EMAIL;
        //手机号码正则
        String phone = AppConst.PHONE;
        //开始匹配邮箱
        Matcher m = Pattern.compile(email).matcher( username);
        //开始匹配手机号码
        Matcher m2 = Pattern.compile( phone).matcher( username);
        //判断
        if( m.matches()){
            //邮箱匹配
            user.setEmail( username);
        }else if( m2.matches()){
            //电话匹配
            user.setMobile( username);
        }
        return user;
    }

    
    /**
     * 获取当前货币
     * @param token
     * @return
     */
    protected CoinEntity getToken(String token) {
        Map<String, CoinEntity> config = redisPlugin.get( RedisConst.PLATFORM_COIN_CONFIG);

        if (MapUtil.isNotEmpty( config) && config.containsKey( token)) {
            return config.get( token);
        }
        return null;
    }
    
    /**
     * 获取交易对配置
     * 通过交易对符号获取该交易对的配置信息
     * 
     * redis("platform_pairs_config",Map(CoinPair:Coin2TableEntity))
     * 
     * @param coinPair 交易对
     * @return
     */
    protected MarketCoinEntity getC2T(String coinPair) {
    	Map<String, MarketCoinEntity> config = redisPlugin.get( RedisConst.PLATFORM_PAIRS_CONFIG);
        if (MapUtil.isNotEmpty( config) && config.containsKey( coinPair)) {
            return config.get( coinPair);
        }
        return null;
    }

    /**
     * 当前语言
     * @param request
     * @return
     */
    protected String getLang(HttpServletRequest request) {
        String lang = request.getHeader( AppConst.LANG);
        if (StrUtil.isEmpty( lang) || !CollUtil.contains(defaultLang, lang)) {
            lang = defaultLang.get( IntUtils.INT_ZERO.intValue());
        }
        return lang;
    }
    
    
}
