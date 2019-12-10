package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.EmailConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.UserChequeEntity;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.vo.ChequeVO;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.service.IChequeService;
import com.blockchain.platform.service.IUserService;
import com.blockchain.platform.utils.EmailUtils;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import com.blockchain.platform.utils.SMSUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 提币地址管理
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 12:13 PM
 **/
@RestController
@RequestMapping("/cheque")
public class ChequeController extends BaseController {

    /**
     * 地址管理接口
     */
    @Resource
    private IChequeService chequeService;

    /**
     * 用户管理接口
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

    /**
     * 获取用户提币地址
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/query")
    public ResponseData query(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //设置用户id
            dto.setUserId( user.getId());
            //开始分页
            Page<ChequeVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //数据列表
            List<ChequeVO> list = chequeService.query( dto);
            //返回数据
            data.setData(PageVO.builder()
                                    .total( page.getTotal())
                                    .list( list).build());
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 列表（不分页）
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/list")
    public ResponseData list(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //设置用户id
            dto.setUserId( user.getId());
            //page对象
            Page<ChequeVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize(), Boolean.FALSE);
            //列表
            List<ChequeVO> list = chequeService.query( dto);
            //返回数据
            data.setData( list);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 新增或编辑提币地址
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/modify")
    public ResponseData modify(@RequestBody @Valid ChequeDTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid);
            }
            // 登录用户
            UserDTO user = getLoginUser( request);
            //用户详情
            UserEntity userEntity = userService.findUserByCondition( user);
            //提币详情
            UserChequeEntity entity = BeanUtil.toBean( dto, UserChequeEntity.class);
            // 当前用户
            entity.setUserId( user.getId());
            // 新增
            if ( IntUtils.isZero( entity.getId())) {
                // 新增验证短信验证码
                //短信验证码
                String sms = redisPlugin.get(StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_ADDRESS, userEntity.getMobile()));
                //邮箱验证码
                String email = redisPlugin.get(StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_ADDRESS, userEntity.getEmail()));
                if( !StrUtil.equals( dto.getSms(), sms) && !StrUtil.equals( dto.getEml(), email)){
                    throw ExUtils.error( LocaleKey.USER_INVALID_VERIFICATION_CODE);
                }
                // 状态
                entity.setState( BizConst.BIZ_STATUS_VALID);
            } else {
                //获取详情
                UserChequeEntity db = chequeService.findByCondition( BaseDTO.builder()
                                                                .id( entity.getId())
                                                                .userId( dto.getId()).build());
                if ( ObjectUtil.isEmpty( db)) {
                    throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
                }
                //设置版本号
                entity.setVersion( db.getVersion());
            }
            //新增或编辑
            Boolean bool = chequeService.modify( entity);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 删除提币地址
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/delete/{id}")
    public ResponseData delete(@PathVariable("id") Integer id, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            // 获取当前删除地址详情
            UserChequeEntity entity = chequeService.findByCondition( BaseDTO.builder()
                                                                        .id( id)
                                                                        .userId( user.getId()).build());
            if ( ObjectUtil.isEmpty( entity)) {
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
            // 设置为无效状态
            UserChequeEntity cheque = UserChequeEntity.builder()
                                                    .id( id)   //id
                                                    .userId( user.getId())
                                                    .state( BizConst.BIZ_STATUS_FAILED).build();  //状态（无效）
            //删除
            Boolean bool = chequeService.delete( cheque);
            if(!bool){
                throw ExUtils.error( LocaleKey.SYS_OPERATE_FAILED);
            }
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 添加地址发送验证码
     * @param request
     * @return
     */
    @RequestMapping("/captcha")
    public ResponseData captcha(@RequestBody @Valid CipherCaptchaDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if (valid.hasErrors()) {
                throw ExUtils.error( valid);
            }
            // 登录用户
            UserDTO user = getLoginUser( request);
            UserEntity entity = userService.findUserByCondition( UserDTO.builder()
                                                                    .id( user.getId())
                                                                    .state( BizConst.BIZ_STATUS_VALID).build());
            if ( ObjectUtil.isEmpty( entity)) {
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
            // 验证资金密码
            String ins = SecureUtil.md5( StrUtil.addSuffixIfNot( dto.getPassword(), BizConst.BIZ_SECRET_CODE));
            if ( !StrUtil.equals( ins, entity.getCipher())) {
                throw ExUtils.error( LocaleKey.USER_CIPHER_PASSWORD_ERROR);
            }
            // 获取之前验证码
            //String captcha = getCaptchaCode(RedisConst.PLATFORM_REDIS_ADDRESS, user.getUsername());
            String captcha = StrUtil.EMPTY;
            if( StrUtil.equals( dto.getType(), BizConst.MOBILE)){
                captcha = getCaptchaCode(RedisConst.PLATFORM_REDIS_CIPHER, entity.getMobile());
            }else{
                captcha = getCaptchaCode(RedisConst.PLATFORM_REDIS_CIPHER, entity.getEmail());
            }
            if( StrUtil.isNotEmpty( captcha)){
                throw ExUtils.error( LocaleKey.USER_CAPTCHA_HAS_SEND);
            }
            //验证码
            String code = RandomUtil.randomNumbers( AppConst.DEFAULT_CODE_LENGTH);
            // 发送验证码
            if( StrUtil.equals( dto.getType(), BizConst.MOBILE)){
                //保存缓存
                redisPlugin.set(StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_ADDRESS, entity.getMobile()),
                                                 code, AppConst.VERIFICATION_CODE_TIME);
                //发送短信验证码
                smsUtils.sendMessage( BizConst.SMS_TYPE_ADDRESS, entity.getMobile(), new String[] { code });
            }
            if( StrUtil.equals( dto.getType(), BizConst.EMAIL)){
                //保存缓存
                redisPlugin.set( StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_ADDRESS, entity.getEmail()),
                                                    code , AppConst.VERIFICATION_CODE_TIME);
                //发送邮箱验证码
                emailUtils.sendMessage(BizConst.SMS_TYPE_ADDRESS, entity.getEmail(), EmailConst.EMAIL_CAPTCHA_CHEQUE, new String[]{ code});
            }
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
}
