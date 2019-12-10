package com.blockchain.platform.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.utils.ExUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 资金密码拦截器
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-16 12:13 PM
 **/
public class CipherInterceptor implements HandlerInterceptor {

    /**
     * 缓存记录
     */
    @Resource
    private RedisPlugin RedisPlugin;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 当前用户token
        String token = request.getHeader( AppConst.TOKEN);
        // 用户新
        UserDTO dto = RedisPlugin.hget(RedisConst.PLATFORM_USER_DATA, token);
        if(ObjectUtil.isEmpty( dto)) {
            throw ExUtils.unAuth( LocaleKey.USER_UNAUTHORIZED);
        }
        // 有效时间
        Long express = dto.getExpires();
        if (ObjectUtil.isEmpty( express) || express.equals( AppConst.USER_UNAUTHORIZED)) {
            throw ExUtils.unAuth( LocaleKey.USER_UNAUTHORIZED);
        }
        // 当前时间
        Long now = DateUtil.currentSeconds();
        if ( now > express) { // 授权已过期
            throw ExUtils.unAuth( LocaleKey.USER_UNAUTHORIZED);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
