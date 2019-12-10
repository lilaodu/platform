package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blockchain.platform.constant.*;
import com.blockchain.platform.gt.GeetestConfig;
import com.blockchain.platform.gt.GeetestLib;
import com.blockchain.platform.gt.NECaptchaVerifier;
import com.blockchain.platform.gt.VerifyResult;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.entity.UserFavCoinEntity;
import com.blockchain.platform.pojo.entity.UserMessageEntity;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.IUserFavCoinService;
import com.blockchain.platform.service.IUserService;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.EmailUtils;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.GoogleAuthUtils;
import com.blockchain.platform.utils.IntUtils;
import com.blockchain.platform.utils.NetUtils;
import com.blockchain.platform.utils.SMSUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import cn.hutool.core.date.DateUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:29 AM
 **/
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    /**
     * 用户服务接口
     */
    @Resource
    private IUserService userService;

    /**
     * 短信服务工具
     */
    @Resource
    private SMSUtils smsUtils;

    /**
     * 邮箱服务工具
     */
    @Resource
    private EmailUtils emailUtils;
    
    @Resource
    private IUserFavCoinService userFavCoinService;

    /**
     * 分页查询数据
     * @param dto
     * @return
     */
    @RequestMapping("/query")
    public ResponseData query(@RequestBody PageDTO dto) {
        ResponseData data = ResponseData.ok();
        try {
            Page page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());

            List<UserVO> list = userService.query( dto);

            data.setData( PageVO.builder()
                                .total( page.getTotal())
                                .list( list).build());
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }
    
    
    /**
     * 获取服务器时间戳
     */
    @RequestMapping(value = "/timestamp",method = {RequestMethod.OPTIONS,RequestMethod.GET,RequestMethod.POST})
    public ResponseData timestamp(){
        ResponseData data = ResponseData.ok();
        try {
            // 服务器时间戳
            data.setData( DateUtil.currentSeconds());
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 用户修改登录密码
     * @param dto
     * @return
     */
    @RequestMapping("/logon/password")
    public ResponseData mdfLoginPassword(@RequestBody @Valid PwdDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                 throw ExUtils.error( valid.getFieldError().getDefaultMessage());
            }
            // 两次密码输入不一致
            if( !StrUtil.equals( dto.getAgainPassword(), dto.getNewPassword())) {
                throw ExUtils.error( LocaleKey.USER_PASSWORD_IN_DISACCORD);
            }
            // 当前登录用户
            UserDTO user = getLoginUser( request);
            // 查询
            UserEntity entity = userService.findUserByCondition( UserDTO.builder()
                                                                            .id( user.getId())
                                                                            .state( BizConst.BIZ_STATUS_VALID).build());
            if( ObjectUtil.isEmpty( entity)){
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
            // 加密元密码
            String pwd = SecureUtil.md5( StrUtil.addSuffixIfNot(dto.getPassword(), BizConst.BIZ_SECRET_CODE));
            if( !StrUtil.equals( pwd, entity.getPassword())){
                throw ExUtils.error( LocaleKey.USER_ORI_PASSWORD_ERROR);
            }
            // 新密码
            String nPwd = SecureUtil.md5( StrUtil.addSuffixIfNot(dto.getNewPassword(), BizConst.BIZ_SECRET_CODE ));
            UserEntity db = UserEntity.builder()
                                        .id( entity.getId())
                                        .password( nPwd).build();
            //修改用户密码
            Boolean bool = userService.modify( db);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }


    /**
     * 用户修改交易密码
     * @param dto
     * @return
     */
    @RequestMapping("/cipher/password")
    public ResponseData mdfTradePassword(@RequestBody @Valid PwdDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid);
            }
            // 两次密码输入不一致
            if( !StrUtil.equals( dto.getAgainPassword(), dto.getNewPassword())) {
                throw ExUtils.error( LocaleKey.USER_PASSWORD_IN_DISACCORD);
            }
            // 当前登录用户
            UserDTO user = getLoginUser( request);
            // 当前用户详情
            UserEntity entity = userService.findUserByCondition( UserDTO.builder()
                                                .id( user.getId())
                                                .state( BizConst.BIZ_STATUS_VALID).build());
            if( ObjectUtil.isEmpty( entity)){
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
//            String captcha = StrUtil.EMPTY;
//            //判断用户是绑定的电话号码还是邮箱,以电话号码优先
//            if(StrUtil.isNotEmpty( user.getMobile())){
//                //获取修改资金密码验证码
//                captcha = getCaptchaCode(RedisConst.PLATFORM_REDIS_CIPHER, user.getMobile());
//            }else{
//                captcha = getCaptchaCode(RedisConst.PLATFORM_REDIS_CIPHER, user.getEmail());
//            }
            //手机验证码
            String mCode = getCaptchaCode(RedisConst.PLATFORM_REDIS_CIPHER, entity.getMobile());
            //邮箱验证码
            String eCode = getCaptchaCode(RedisConst.PLATFORM_REDIS_CIPHER, entity.getEmail());
            //判断验证码是否一致
            if ( !StrUtil.equals( mCode, dto.getSms()) && !StrUtil.equals( eCode, dto.getEml())) {
                throw ExUtils.error( LocaleKey.USER_INVALID_VERIFICATION_CODE);
            }
            // 加密元密码
            String pwd = SecureUtil.md5( StrUtil.addSuffixIfNot(dto.getPassword(), BizConst.BIZ_SECRET_CODE ));
            if( !StrUtil.equals( pwd, entity.getCipher())){
                throw ExUtils.error( LocaleKey.USER_ORI_PASSWORD_ERROR);
            }
            // 新密码
            String nPwd = SecureUtil.md5( StrUtil.addSuffixIfNot(dto.getNewPassword(), BizConst.BIZ_SECRET_CODE ));
            UserEntity db = UserEntity.builder()
                                        .id( entity.getId())
                                        .cipher( nPwd).build();
            Boolean bool = userService.modify(db);
            if(bool){
                // 修改完成之后 重置 资金密码过期时间
                user.setExpires( AppConst.USER_UNAUTHORIZED);
                // 更新缓存中 用户信息
                setLoginUser( request, user);
                if( StrUtil.isNotEmpty( user.getMobile())){
                    //发送短信验证码
                    smsUtils.sendMessage( BizConst.CHANGE_CIPHER,user.getMobile(), new String[]{""});
                }else{
                    //发送邮箱验证码
                    emailUtils.sendMessage(BizConst.CHANGE_CIPHER, user.getEmail(), BizConst.CHANGE_CIPHER,new String[]{""});
                }
            }
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 修改资金密码发送验证码
     * @param request
     * @return
     */
    @RequestMapping("/cipher/captcha")
    public ResponseData captcha(@RequestBody CipherCaptchaDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            // 登录用户
            UserDTO user = getLoginUser( request);
            //获取登录用户详情
            UserEntity entity = userService.findUserByCondition( UserDTO.builder()
                                                                    .id( user.getId())
                                                                    .state( BizConst.BIZ_STATUS_VALID).build());
            if ( ObjectUtil.isEmpty( entity)) {
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
            //判断用户是否手机或邮箱未绑定
            if( StrUtil.isEmpty( entity.getMobile()) || StrUtil.isEmpty( entity.getEmail())){
                throw ExUtils.error( LocaleKey.USER_MOBILE_OR_EMAIL_IS_NUll);
            }
            //判断用户资金密码是否正确
            String pwd = SecureUtil.md5( StrUtil.addSuffixIfNot(dto.getPassword(), BizConst.BIZ_SECRET_CODE ));
            if( !StrUtil.equals( pwd, entity.getCipher())){
                throw ExUtils.error( LocaleKey.USER_ORI_PASSWORD_ERROR);
            }
            // 获取验证码
            //String captcha = getCaptchaCode(RedisConst.PLATFORM_REDIS_CIPHER, user.getUsername());
            String captcha = StrUtil.EMPTY;
            if( StrUtil.equals( dto.getType(), BizConst.MOBILE)){
                captcha = getCaptchaCode(RedisConst.PLATFORM_REDIS_CIPHER, entity.getMobile());
            }else{
                captcha = getCaptchaCode(RedisConst.PLATFORM_REDIS_CIPHER, entity.getEmail());
            }
            //判断验证码是否已发送过
            if( StrUtil.isNotEmpty( captcha)) {
                throw ExUtils.error( LocaleKey.USER_CAPTCHA_HAS_SEND);
            }
            // 验证码
            String code = RandomUtil.randomNumbers( AppConst.DEFAULT_CODE_LENGTH);
            //缓存key
            String key = StrUtil.EMPTY;
//            if(StrUtil.isNotEmpty( entity.getMobile())){
//                //缓存设置电话号码
//                key = StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_CIPHER, entity.getMobile());
//                // 发送短信验证码
//                smsUtils.sendMessage( BizConst.SMS_TYPE_CIPHER, user.getMobile(), new String[] { code});
//                //设置缓存
//                redisPlugin.set( key, code, AppConst.VERIFICATION_CODE_TIME);
//            }else if( StrUtil.isNotEmpty( entity.getEmail())){
//                //缓存设置邮箱地址
//                key = StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_CIPHER, user.getEmail());
//                //发送邮箱验证码
//                emailUtils.sendMessage(BizConst.SMS_TYPE_CIPHER, entity.getEmail() , EmailConst.EMAIL_CAPTCHA_CIPTCHER, new String[]{code});
//                //设置缓存
//                redisPlugin.set( key, code, AppConst.VERIFICATION_CODE_TIME);
//            }
            if( StrUtil.equals( dto.getType(), BizConst.MOBILE)){
                //缓存设置电话号码
                key = StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_CIPHER, entity.getMobile());
                // 发送短信验证码
                smsUtils.sendMessage( BizConst.SMS_TYPE_CIPHER, entity.getMobile(), new String[] { code});
            } else{
                //缓存设置邮箱地址
                key = StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_CIPHER, entity.getEmail());
                //发送邮箱验证码
                emailUtils.sendMessage(BizConst.SMS_TYPE_CIPHER, entity.getEmail() , EmailConst.EMAIL_CAPTCHA_CIPTCHER, new String[]{code});
            }
            //设置缓存
            redisPlugin.set( key, code, AppConst.VERIFICATION_CODE_TIME);
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
    
    /**
     * 用户添加自选货币
     * @param dto
     * @return
     */
    @RequestMapping("/optional")
    public ResponseData optional(@RequestBody @Valid FavCoinDTO dto,BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid.getFieldError().getDefaultMessage());
            }
            // 当前登录用户
            UserDTO user = getLoginUser( request);
            // 自选货币
            dto.setMarket( BizUtils.market( dto.getSymbol()));
            dto.setSymbol( BizUtils.token( dto.getSymbol()));
            dto.setUserId( user.getId());
            // 获取当前自选
            UserFavCoinEntity entity  = userFavCoinService.findFavByCondition( dto);
            if( BeanUtil.isEmpty( entity)) {
            	entity = UserFavCoinEntity.builder().symbol(dto.getSymbol()).market(dto.getMarket())
            			.state( BizConst.BIZ_STATUS_VALID).userId( user.getId()).build();
                userFavCoinService.save( entity);
            } else {
                if( IntUtils.equals( entity.getState(), BizConst.BIZ_STATUS_VALID)) {
                    entity.setState( BizConst.BIZ_STATUS_FAILED);
                } else {
                    entity.setState( BizConst.BIZ_STATUS_VALID);
                }
                entity.setUserId( user.getId());
                userFavCoinService.updateById(entity);
            }
            
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
    
    /**
     * 我的自选
     * @param request
     * @return
     */
    @RequestMapping("/favor")
    public ResponseData favor(HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            UserDTO user = getLoginUser( request);
            if(!BeanUtil.isEmpty(user)) {
                // 我的自选货币
                List<UserFavCoinVO> favors = userService.favorTokens( BaseDTO.builder().userId( user.getId()).state( BizConst.BIZ_STATUS_VALID).build());
                //返回列表
                List<RankingVO> userVoList = CollUtil.newArrayList();
                for(UserFavCoinVO favor : favors) {
                	List<RankingVO> voList = redisPlugin.hget(RedisConst.PLATFORM_RANKING, favor.getMarket());
                	if(BeanUtil.isEmpty(voList)) {
                		continue;
                	}
                	for(RankingVO vo : voList) {
                		if(StrUtil.equals(vo.getSymbol(), favor.getSymbol())) {
                			
                			userVoList.add(vo);
                		}
                	}
                }
                // 返回
                data.setData(userVoList);
            }
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 用户登录
     * @param valid
     * @param request
     * @return
     */
    @RequestMapping("/login")
    public ResponseData webLogin(@RequestBody @Valid LoginDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
            	throw ExUtils.error( valid);
            }
            // 人机验证
//            RobotDTO robotDTO = BeanUtil.toBean( dto, RobotDTO.class);
//            // 设置当前ip
//            robotDTO.setIp( NetUtils.getLoginIp( request));
//            Boolean b = userService.validRobotLogin( robotDTO);
//            if( !b) {
//            	throw ExUtils.error(LocaleKey.SYS_ROBOT_FAILED);
//            }
           /* NECaptchaVerifier wyVerifier = new NECaptchaVerifier();
            VerifyResult verifyResult = wyVerifier.verify(dto.getValidate(), dto.getUsername());
            System.out.println(verifyResult.isResult());
            if(!verifyResult.isResult()) {
            	throw ExUtils.error(LocaleKey.SYS_ROBOT_FAILED);
            }*/
            
            
            //用户信息
            UserDTO user = UserDTO.builder()
                        .state( BizConst.BIZ_STATUS_VALID).build();
            //验证邮箱或手机号
            if( StrUtil.equals( dto.getType(), BizConst.MOBILE)){
                //手机登录
                if( !isMobile( dto.getUsername())){
                    throw ExUtils.error( LocaleKey.USER_MOBILE_FORMAT_ERROR);
                }
                user.setMobile( dto.getUsername());
            }else if( StrUtil.equals( dto.getType(), BizConst.EMAIL)){
                //邮箱登录
                if( !isEmail( dto.getUsername())){
                    throw ExUtils.error( LocaleKey.USER_EMAIL_FORMAT_ERROR);
                }
                user.setEmail( dto.getUsername());
            }else {
                throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
            }
            //查询邮箱或手机号注册的用户
            UserEntity entity = userService.findUserByCondition( user);
            if( ObjectUtil.isEmpty( entity)){
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
            // 密码
            String Pwd = SecureUtil.md5( StrUtil.addSuffixIfNot(dto.getPassword(), BizConst.BIZ_SECRET_CODE ));
            if( !StrUtil.equals( Pwd, entity.getPassword())){//输入密码和原密码判断
                throw ExUtils.error( LocaleKey.USER_ORI_PASSWORD_ERROR);
            }
            // 获取token
            String token = getWebToken( dto.getUsername());
            // 需要缓存的数据
            UserDTO redisDTO = userService.findLoginUser( user);
            //设置用户名
            redisDTO.setUsername( dto.getUsername());
            // 登录ip
            redisDTO.setIp(NetUtils.getLoginIp( request) );//robotDTO.getIp()
            // 资金密码授权情况
            redisDTO.setExpires( AppConst.USER_UNAUTHORIZED);

            // 设置用户信息
            redisPlugin.hset( RedisConst.PLATFORM_USER_DATA, token, redisDTO);
            //entity转VO
            UserVO vo = BeanUtil.toBean( entity , UserVO.class);
            //设置用户名
            vo.setUsername( dto.getUsername());
            //存放token
            vo.setToken( token);
            //存放是否是商户
            vo.setIsMerchant( redisDTO.getIsMerchant());
            data.setData( vo);
        }catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
    /**
     * 初始化gt
     * @param type
     * @return
     */
//    @RequestMapping(value = "/geetest",method = {RequestMethod.OPTIONS,RequestMethod.GET,RequestMethod.POST})
//    public ResponseData initGt(@RequestBody BaseDTO baseDTO, HttpServletRequest request) {
//        ResponseData data = ResponseData.ok();
//        try {
//            GeetestLib gtSdk = new GeetestLib(
//                    GeetestConfig.getGeetest_id(),
//                    GeetestConfig.getGeetest_key(),
//                    GeetestConfig.isnewfailback()
//            );
//
//            // 初始化参数
//            GeetestDTO dto = GeetestDTO.builder().build();
//            if ( StrUtil.isNotEmpty( baseDTO.getType())) {
//                dto.setClient_type( baseDTO.getType());
//            }
//            // 验证IP
//            dto.setIp_address( NetUtils.getLoginIp( request));
//            // txHash
//            String txHash = IdUtil.simpleUUID();
//            dto.setUser_id( txHash);
//            // 初始化参数
//            HashMap<String, String> params = JSON.parseObject( JSONUtil.toJsonStr( dto),
//                    new TypeReference<HashMap<String , String>>(){});
//
//            int gtServerStatus = gtSdk.preProcess( params);
//            // 初始
//            String json = gtSdk.getResponseStr();
//            // 返回值
//            GeetestVO vo = JSONUtil.toBean( json, GeetestVO.class);
//            vo.setTxHash( txHash);
//            vo.setGt_server_status( gtServerStatus);
//            data.setData( vo);
//        } catch (Exception ex) {
//            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
//        }
//        return data;
//    }
    
    /**
     * 发送注册验证码
     * @param dto
     * @param valid
     * @return
     */
    @RequestMapping("register/captcha")
    public ResponseData captcha(@RequestBody @Valid CaptchaDTO dto, BindingResult valid) {
        ResponseData data = ResponseData.ok();
        try {
            if( valid.hasErrors()){
                throw ExUtils.error( valid);
            }
            //用户信息
            UserDTO user = judge( dto.getUsername());
            //注册类型
            if(StrUtil.equals( dto.getType(), BizConst.MOBILE)){
                //电话号码注册
                if(!isMobile( dto.getUsername())){
                    throw ExUtils.error( LocaleKey.USER_MOBILE_FORMAT_ERROR);
                }
            }else{
                //邮箱注册
                if(!isEmail( dto.getUsername())){
                    throw ExUtils.error( LocaleKey.USER_EMAIL_FORMAT_ERROR);
                }
            }
            // 判断用户是否注册
            //设置状态
            user.setState( BizConst.BIZ_STATUS_VALID);
            UserEntity entity = userService.findUserByCondition( user);
            if( ObjectUtil.isNotEmpty( entity)){
                throw ExUtils.error( LocaleKey.USER_HAS_REGISTER);
            }
            // 是否存在验证码
            String captcha = getCaptchaCode( RedisConst.PLATFORM_REDIS_REGISTER, dto.getUsername());
            if( StrUtil.isNotEmpty( captcha)){
                throw ExUtils.error( LocaleKey.USER_CAPTCHA_HAS_SEND);
            }
            // 手机验证码
            String code = RandomUtil.randomNumbers( AppConst.DEFAULT_CODE_LENGTH);
            //保存缓存
            redisPlugin.set( StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_REGISTER, dto.getUsername()),
                                                    code, AppConst.VERIFICATION_CODE_TIME);
            // 发送验证码
            if( StrUtil.equals( dto.getType(), BizConst.MOBILE)) {
                //发送短信验证码
                smsUtils.sendMessage(BizConst.SMS_TYPE_REGISTER, dto.getUsername(), new String[]{code});
            } else {
                //发送邮箱验证码
            	emailUtils.sendMessage(BizConst.SMS_TYPE_REGISTER, dto.getUsername(), EmailConst.EMAIL_CAPTCHA_REGISTER, new String[]{code});
            }

        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
    /**
     * 用户的注册
     * @return
     */
    @RequestMapping("/register")
    public ResponseData register(@RequestBody @Valid RegisterDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
//            if ( valid.hasErrors()) {
//                ExUtils.error( valid);
//            }
//            // 人机验证
//            RobotDTO robotDTO = BeanUtil.toBean( dto, RobotDTO.class);
//            // 注册IP
//            String ip = NetUtils.getLoginIp( request);
//            // 设置当前ip
//            robotDTO.setIp( ip);
//            //验证结果
//            Boolean bool = userService.validRobotLogin( robotDTO);
//            if( !bool) {
//                ExUtils.error(LocaleKey.SYS_ROBOT_FAILED);
//            }
        	//NECaptchaVerifier wyVerifier = new NECaptchaVerifier();
           // VerifyResult verifyResult = wyVerifier.verify( dto.getUsername());
           // System.out.println(verifyResult.isResult());
           /* if(!verifyResult.isResult()) {
            	throw ExUtils.error(LocaleKey.SYS_ROBOT_FAILED);
            }*/
        	
            UserDTO userDTO = UserDTO.builder()
                                .state( BizConst.BIZ_STATUS_VALID).build();
            //验证手机还是电话号码
            switch ( dto.getType()){
                //邮箱注册
                case BizConst.EMAIL :
                    if( !isEmail( dto.getUsername())){
                        throw ExUtils.error( LocaleKey.REGISTER_EMAIL_ERROR);
                    }
                    userDTO.setEmail( dto.getUsername());
                    break;
                //电话号码注册
                case BizConst.MOBILE :
                    if( !isMobile( dto.getUsername())){
                        throw ExUtils.error( LocaleKey.REGISTER_MOBILE_ERROR);
                    }
                    userDTO.setMobile( dto.getUsername());
            }
            // 获取当前用户
            UserEntity entity = userService.findUserByCondition( userDTO);
            // 判断用户是否已经注册
            if ( ObjectUtil.isNotEmpty( entity)) {
                throw ExUtils.error( LocaleKey.USER_HAS_REGISTER );
            }
            //注册用户信息
            UserEntity user = BeanUtil.toBean( dto, UserEntity.class);
            //判断用户是否输入了正确的邀请码
            Integer parentId = IntUtils.INT_ZERO;
            if( StrUtil.isNotEmpty( dto.getInvitationCode())){
                UserEntity parent = userService.findUserByCondition( UserDTO.builder()
                                                    .invitationCode( dto.getInvitationCode())
                                                    .state( BizConst.BIZ_STATUS_VALID).build());
                if( ObjectUtil.isEmpty( parent)){
                    throw ExUtils.error( LocaleKey.USER_INVITATION_CODE_ERROR);
                }
                parentId = parent.getId();

            }
            //上级id
            user.setParentId( parentId);
            //添加用户账号
            if( ObjectUtil.isNotEmpty( userDTO.getMobile())){
                //手机号码
                user.setMobile( userDTO.getMobile());
            }else{
                //邮箱
                user.setEmail( userDTO.getEmail());
            }
            // 获取缓存数据[注册验证类]
            String captcha = getCaptchaCode( RedisConst.PLATFORM_REDIS_REGISTER, dto.getUsername());
            //判断验证码
            if( StrUtil.isEmpty( captcha) || !StrUtil.equals( captcha, dto.getCode())){
                throw ExUtils.error( LocaleKey.USER_INVALID_VERIFICATION_CODE);
            }
            //谷歌两步验证key
            String secret = GoogleAuthUtils.generateSecretKey();
            user.setSecret( secret);
            // 把这个qrcode生成二维码，用google身份验证器扫描二维码就能添加成功
            user.setQrCode( GoogleAuthUtils.createGoogleAuthQRCodeData(secret,
                                                dto.getUsername() ,
                                                BizConst.UserConst.GOOGLE_TITLE));
            //用户注册IP
            user.setIp(NetUtils.getLoginIp( request));//ip
            //角色
            user.setSign( IntUtils.INT_ZERO);
            //用户状态 1
            user.setState( BizConst.BIZ_STATUS_VALID);
            // 用户自己邀请码
            user.setInvitationCode( RandomUtil.randomString( AppConst.DEFAULT_INVITATION_CODE_SIZE));
            // 密码
            user.setPassword( SecureUtil.md5( StrUtil.addSuffixIfNot( dto.getPassword(), BizConst.BIZ_SECRET_CODE)));
            // 资金密码(初始资金密码和用户登录密码一致)
            user.setCipher( SecureUtil.md5( StrUtil.addSuffixIfNot( dto.getPassword(), BizConst.BIZ_SECRET_CODE)));
            //api_secret
            user.setApiSecret( IdUtil.simpleUUID());
            //注册
            userService.register(user, dto.getInvitationCode());

        }catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 忘记密码的验证码
     * @param dto
     * @param valid
     * @return
     */
    @RequestMapping("/forget/captcha")
    public ResponseData fCaptcha(@RequestBody @Valid CaptchaDTO dto, BindingResult valid){
        ResponseData data = ResponseData.ok();
        try {
            if( valid.hasErrors()){
                throw ExUtils.error( valid);
            }
            //判断是否是邮箱或手机号码
            UserDTO user = judge( dto.getUsername());
            if( ObjectUtil.isEmpty( user)){
                throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
            }
            //用户信息
            //有效用户
            user.setState( BizConst.BIZ_STATUS_VALID);
            UserEntity entity = userService.findUserByCondition( user);
            if( ObjectUtil.isEmpty( entity)){
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
            //获取验证码
            String captcha = getCaptchaCode( RedisConst.PLATFORM_REDIS_FORGET, dto.getUsername());
            if( StrUtil.isNotEmpty( captcha)){
                throw ExUtils.error( LocaleKey.USER_CAPTCHA_HAS_SEND);
            }
            // 验证码
            String code = RandomUtil.randomNumbers( AppConst.DEFAULT_CODE_LENGTH);
            //保存缓存
            redisPlugin.set( StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_FORGET, dto.getUsername()),
                                        code, AppConst.VERIFICATION_CODE_TIME);
            //发送验证码
            if( StrUtil.isNotEmpty( user.getMobile())){
                //发送短信验证码
                smsUtils.sendMessage( BizConst.SMS_TYPE_FORGET, dto.getUsername(), new String[]{code});
            }else{
                //发送邮箱验证码
                emailUtils.sendMessage(BizConst.SMS_TYPE_FORGET, dto.getUsername(), EmailConst.EMAIL_CAPTCHA_FORGET, new String[]{code});
            }
        }catch (Exception ex){

        }
        return data;
    }

    /**
     * 忘记密码
     * @param dto
     * @param valid
     * @return
     */
    @RequestMapping("/forget")
    public ResponseData forget(@RequestBody @Valid ForgetDTO dto, BindingResult valid) {
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid);
            }
            //人机验证
//            RobotDTO robotDTO = BeanUtil.toBean( dto, RobotDTO.class);
//            boolean bool =  userService.validRobotLogin( robotDTO);
//            if( !bool){
//                // 提示人機驗證失敗
//                throw  ExUtils.error( LocaleKey.SYS_ROBOT_FAILED );
//            }
           /* NECaptchaVerifier wyVerifier = new NECaptchaVerifier();
            VerifyResult verifyResult = wyVerifier.verify(dto.getValidate(), dto.getUsername());
            System.out.println(verifyResult.isResult());
            if(!verifyResult.isResult()) {
            	throw ExUtils.error(LocaleKey.SYS_ROBOT_FAILED);
            }*/
            
            //判断用户账号
            UserDTO userDTO = judge( dto.getUsername());//将邮箱或手机号验证后放入DTO
            //都没有表示输入的格式不正确
            if(ObjectUtil.isEmpty( userDTO)){
                //格式不正确
            	throw ExUtils.error(LocaleKey.LOGIN_ACCOUNT_ERROR);
            }
            // 查询当前用户
            userDTO.setState( BizConst.BIZ_STATUS_VALID);
            UserEntity entity = userService.findUserByCondition( userDTO);
            // 判断用户是否存在
            if ( ObjectUtil.isEmpty( entity)) {
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
            // 获取缓存数据[注册验证类]
            String captcha = getCaptchaCode( RedisConst.PLATFORM_REDIS_FORGET, dto.getUsername());
            if( StrUtil.isEmpty( captcha) || !StrUtil.equals( captcha, dto.getCode())){
                throw ExUtils.error( LocaleKey.USER_INVALID_VERIFICATION_CODE);
            }
            String password = SecureUtil.md5( StrUtil.addSuffixIfNot( dto.getPassword(), BizConst.BIZ_SECRET_CODE));
            // 修改密码
            Boolean bool = userService.modify( UserEntity.builder()
                                            .id( entity.getId())  //用户id
                                            .password( password).build());  //新密码

            if (!bool) {
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        } catch (Exception ex) {
            ExUtils.error(ex ,LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 交易授权
     * @param dto
     * @param valid
     * @param request
     * @return
     */
    @RequestMapping("/auth")
    public ResponseData authorization(@RequestBody @Valid TradeAuthDTO dto,BindingResult valid,HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid.getFieldError().getDefaultMessage());
            }
            // 登录用户
            UserDTO user = getLoginUser( request);
            UserEntity entity = userService.getById(user.getId());
            if ( ObjectUtil.isEmpty( entity)){
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
            // 资金密码
            String ins = SecureUtil.md5( StrUtil.addSuffixIfNot( dto.getCipher(), BizConst.BIZ_SECRET_CODE));
            if ( !StrUtil.equals( ins, entity.getCipher())) {
                throw ExUtils.error( LocaleKey.USER_CIPHER_PASSWORD_ERROR);
            }
            // 设置 交易有效时长
            user.setExpires( BizUtils.getAuthExpires());
            // 重新设置
            setLoginUser(request, user);
            data.setData( user);
        } catch (Exception ex) {
            ExUtils.error( ex , LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
    /**
     * 用户退出系统
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    public ResponseData logout( HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //token
            String token = redisPlugin.get( user.getUsername());
            // 删除
            if ( StrUtil.isNotEmpty( token)) {
                //删除用户的redis缓存
                redisPlugin.hdel(RedisConst.PLATFORM_USER_DATA, token);
            }
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 获取用户的站内信
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/messages")
    public ResponseData messages(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //获取用户对应的站内信
            dto.setUserId( user.getId());
            //开始分页
            Page<UserMessageEntity> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //列表
            List<UserMessageEntity> list = userService.message( dto);
            //返回数据
            data.setData( PageVO.builder()
                            .total( page.getTotal())
                            .list( list).build());
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 站内信详情
     * @param id
     * @return
     */
    @RequestMapping("/message/{id}")
    public ResponseData message(@PathVariable("id") Integer id){
        ResponseData data = ResponseData.ok();
        try {
            //获取详情
            UserMessageEntity message = userService.findMessageByCondition( BaseDTO.builder()
                                                                    .id( id).build());
            //若为 未读 状态，
            if( IntUtils.equals( message.getState(), BizConst.BIZ_STATUS_FAILED)){
                //改为已读
                message.setState( BizConst.BIZ_STATUS_VALID);
                userService.modifyMessage( message);
            }
            //返回数据
            data.setData( message);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 绑定邮箱或电话验证码
     * @param dto
     * @return
     */
    @RequestMapping("/bind/captcha")
    public ResponseData bCaptcha(@RequestBody @Valid CaptchaDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if( valid.hasErrors()){
                throw ExUtils.error( valid);
            }
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户详情
            UserEntity entity = userService.findUserByCondition( user);
            if( ObjectUtil.isEmpty( entity)){
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
            //判断是绑定邮箱还是电话号码
            if( StrUtil.equals( dto.getType(), BizConst.MOBILE)){
                //电话号码
                if( StrUtil.isNotEmpty( entity.getMobile())){
                    throw ExUtils.error( LocaleKey.USER_MOBILE_HAS_BIND);
                }
                //电话号码格式错误
                if( !isMobile( dto.getUsername())){
                    throw ExUtils.error( LocaleKey.USER_MOBILE_FORMAT_ERROR);
                }
            }else{
                //绑定邮箱
                if( StrUtil.isNotEmpty( entity.getEmail())){
                    throw ExUtils.error( LocaleKey.USER_EMAIL_HAS_BIND);
                }
                //邮箱格式错误
                if( !isEmail( dto.getUsername())){
                    throw ExUtils.error( LocaleKey.USER_EMAIL_FORMAT_ERROR);
                }
            }
            //获取验证码
            String captcha = getCaptchaCode( RedisConst.PLATFORM_REDIS_BIND, dto.getUsername());
            if( StrUtil.isNotEmpty( captcha)){
                throw ExUtils.error( LocaleKey.USER_CAPTCHA_HAS_SEND);
            }
            //验证码
            String code = RandomUtil.randomNumbers( AppConst.DEFAULT_CODE_LENGTH);
            //绑定账号
            redisPlugin.set(StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_BIND, dto.getUsername()),
                                                    code, AppConst.VERIFICATION_CODE_TIME);
            //发送验证码
            if( StrUtil.equals( dto.getType(), BizConst.MOBILE)){
                smsUtils.sendMessage( BizConst.SMS_TYPE_BIND, dto.getUsername(), new String[]{code});
            }else{
                emailUtils.sendMessage(BizConst.SMS_TYPE_BIND, dto.getUsername(), EmailConst.EMAIL_CAPTCHA_BIND, new String[] {code});
            }
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 绑定
     * @param dto
     * @param valid
     * @param request
     * @return
     */
    @RequestMapping("/bind")
    public ResponseData bind(@RequestBody BindDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if( valid.hasErrors()){
                throw ExUtils.error( valid);
            }
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户详情
            UserEntity entity = userService.findUserByCondition( user);
            if( ObjectUtil.isEmpty( entity)){
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
            //登录用户信息
            UserVO vo = BeanUtil.toBean( entity, UserVO.class);
            //参数
            UserDTO userDTO = UserDTO.builder().state( BizConst.BIZ_STATUS_VALID).build();
            //修改对象
            UserEntity userEntity = UserEntity.builder().build();
            //绑定电话
            if( StrUtil.equals( dto.getType(), BizConst.MOBILE)){
                if( StrUtil.isNotEmpty( entity.getMobile())){
                    throw ExUtils.error( LocaleKey.USER_MOBILE_HAS_BIND);
                }
                if( !isMobile( dto.getUsername())){
                    throw ExUtils.error( LocaleKey.USER_MOBILE_FORMAT_ERROR);
                }
                userDTO.setMobile( dto.getUsername());
                entity = userService.findUserByCondition( userDTO);
                if( ObjectUtil.isNotEmpty( entity)){
                    throw ExUtils.error( LocaleKey.USER_MOBILE_HAS_BIND);
                }
                userEntity.setMobile( dto.getUsername());
            }else if( StrUtil.equals( dto.getType(), BizConst.EMAIL)){ //绑定邮箱
                if( StrUtil.isNotEmpty( entity.getEmail())){
                    throw ExUtils.error( LocaleKey.USER_EMAIL_HAS_BIND);
                }
                if( !isEmail( dto.getUsername())){
                    throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
                }
                userDTO.setEmail( dto.getUsername());
                entity = userService.findUserByCondition( userDTO);
                if( ObjectUtil.isNotEmpty( entity)){
                    throw ExUtils.error( LocaleKey.USER_EMAIL_HAS_BIND);
                }
                userEntity.setEmail( dto.getUsername());
            }else{
                throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
            }
            //获取原账号的验证码
            String captcha = getCaptchaCode( RedisConst.PLATFORM_REDIS_BIND, dto.getUsername());
            //判断验证码
            if( StrUtil.isEmpty( captcha) || !StrUtil.equals( captcha, dto.getCode())){
                throw  ExUtils.error( LocaleKey.USER_INVALID_VERIFICATION_CODE);
            }
            //绑定账号
            userEntity.setId( user.getId());
            Boolean bool = userService.modify( userEntity);
            //登录名
            vo.setUsername( user.getUsername());
            //token
            String token = request.getHeader( AppConst.TOKEN);
            vo.setToken( token);
            //是否是商户
            vo.setIsMerchant( user.getIsMerchant());
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }else{
                //更新登录用户redis缓存,以及登录用户信息
                switch ( dto.getType()){
                    case BizConst.MOBILE :
                        user.setMobile( dto.getUsername());
                        vo.setMobile( dto.getUsername());
                        break;
                    case BizConst.EMAIL :
                        user.setEmail( dto.getUsername());
                        vo.setEmail( dto.getUsername());
                        break;
                }
                // 更新登录用户信息
                redisPlugin.hset( RedisConst.PLATFORM_USER_DATA, token, user);
            }
            data.setData( vo);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 上传用户头像
     * @param request
     * @return
     */
    @RequestMapping("/upload")
    public ResponseData upload(@RequestParam Map<String, Object> param, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //文件上传对象
            UploadDTO ud = BizUtils.getUploadPic( request);
            //登录用户
            UserDTO user = getLoginUser(request);
            // BAK_IMAGE_PATH
            ud.setUserId( user.getId());
            // 上传路径
            ud.setPathType( TypeConst.TYPE_UPLOAD_USER);
            // 上传文件
            HttpResponse response = HttpRequest.post( NetUtils.getUploadUrl())
                                        .header(Header.CONTENT_TYPE, AppConst.RESPONSE_CONTENT_TYPE)
                                        .body(JSON.toJSONString(ud))
                                        .execute();
            if ( !IntUtils.equals(response.getStatus(), HttpStatus.HTTP_OK)) {
                throw ExUtils.error( LocaleKey.FILE_UPLOAD_FAILED);
            }
            data.setData( response.body());
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.FILE_UPLOAD_FAILED);
        }
        return data;
    }

    /**
     * 更改头像
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("head/image")
    public ResponseData head(@RequestBody BaseDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户详情
            UserEntity entity = userService.findUserByCondition( user);
            //更改用户头像
            UserEntity userEntity = UserEntity.builder()
                                    .headImage( dto.getUrl())   //头像地址
                                    .id( entity.getId()).build();  //id
            userService.modify( userEntity);
            //token
            String token = request.getHeader( AppConst.TOKEN);
            //修改缓存
            user.setHeadImage( dto.getUrl());
            redisPlugin.hset( RedisConst.PLATFORM_USER_DATA, token, user);
            //更改返回用户信息
            UserVO vo = BeanUtil.toBean( entity , UserVO.class);
            //设置用户名
            vo.setUsername( user.getUsername());
            //存放token
            vo.setToken( token);
            //存放是否是商户
            vo.setIsMerchant( user.getIsMerchant());
            //头像地址
            vo.setHeadImage( dto.getUrl());
            data.setData( vo);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

}
