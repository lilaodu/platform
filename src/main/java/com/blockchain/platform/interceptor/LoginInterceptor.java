package com.blockchain.platform.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.NetUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录拦截器
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-07-14 11:40 AM
 **/
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 缓存
     */
    @Resource
    private RedisPlugin redisPlugin;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 传入token
        String token = request.getHeader( AppConst.TOKEN);

        if( StrUtil.isEmpty( token) || StrUtil.equals(token, StrUtil.NULL)) {
            throw ExUtils.notLogin( LocaleKey.LOGON_HAS_EXPIRED);
        }
        // 登录对象
        UserDTO dto = redisPlugin.hget( RedisConst.PLATFORM_USER_DATA , token);
        if( ObjectUtil.isEmpty( dto)){
            throw ExUtils.notLogin( LocaleKey.LOGON_HAS_EXPIRED);
        }

        // 缓存数据token
        String oriToken  = redisPlugin.get( dto.getUsername());
        if( StrUtil.isEmpty( oriToken) || !StrUtil.equals( oriToken, token)) {
            // 为了防止数据溢出，溢出历史key
            redisPlugin.hdel( RedisConst.PLATFORM_USER_DATA, token);
            throw ExUtils.notLogin( LocaleKey.LOGON_HAS_EXPIRED);
        }

        // 当前用户登录IP
        String ip = NetUtils.getLoginIp( request);
        if(!StrUtil.equals( ip, dto.getIp())) {
            throw ExUtils.notLogin( LocaleKey.LOGON_IP_EXCEPTION);
        }
        // 登录成功 更新 过期时间
        redisPlugin.set( dto.getUsername(), token, AppConst.USER_LOGIN_TIME);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
