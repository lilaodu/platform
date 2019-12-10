package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.*;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blockchain.platform.annotation.DuplicateVerify;
import com.blockchain.platform.constant.*;
import com.blockchain.platform.enums.Method;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.mapper.ChainMapper;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.DepositVO;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.pojo.vo.WithdrawVO;
import com.blockchain.platform.service.IChainService;
import com.blockchain.platform.service.IUserService;
import com.blockchain.platform.service.IUserWalletService;
import com.blockchain.platform.utils.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 代币流水控制器
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-22 2:22 PM
 **/
@RestController
@RequestMapping("/chain")
public class ChainController extends BaseController {
    @Resource
    private ChainMapper mapper;
    /**
     * 代币出入账交易记录
     */
    @Resource
    private IChainService chainService;

    /**
     * 用户服务接口
     */
    @Resource
    private IUserService userService;

    /**
     * 用户钱包服务接口
     */
    @Resource
    private IUserWalletService walletService;

    /**
     * 短信 工具类
     */
    @Resource
    private SMSUtils smsUtils;

    /**
     * 邮箱 工具类
     */
    @Resource
    private EmailUtils emailUtils;

    /**
     * 获取代币对应的钱包地址
     * @return
     */
    @RequestMapping("/address")
    public ResponseData address(@RequestBody BaseDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //状态
            dto.setState( BizConst.BIZ_STATUS_VALID);
            //获取钱包地址
            data.setData( chainService.findWalletAddress( dto));
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return  data;
    }

    /**
     * 充币流水
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/deposit/flow")
    public ResponseData deposit(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //开始分页
            Page<DepositVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //列表
            List<DepositVO> list = chainService.deposit( dto);
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
     * 提币流水
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/withdraw/flow")
    public ResponseData withdraw(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //开始分页
            Page<DepositVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //列表
            List<WithdrawVO> list = chainService.withdraw( dto);
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
     * 提币短信
     * @param dto
     * @param valid
     * @param request
     * @return
     */
    @RequestMapping("/captcha")
    public ResponseData captcha(@RequestBody @Valid CipherCaptchaDTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if (valid.hasErrors()) {
                throw ExUtils.error( valid);
            }
            // 登录用户
            UserDTO user = getLoginUser( request);
            //用户详情
            UserEntity entity = userService.findUserByCondition( UserDTO.builder()
                                            .id( user.getId())
                                            .state( BizConst.BIZ_STATUS_VALID).build());
            if( StrUtil.isEmpty( entity.getEmail()) || StrUtil.isEmpty( entity.getMobile())){
                throw ExUtils.error( LocaleKey.USER_MOBILE_OR_EMAIL_IS_NUll);
            }
            // 是否实名认证
            if ( !IntUtils.equals( entity.getVerified(), BizConst.KycConst.K2_OK)) {
                throw ExUtils.error( LocaleKey.USER_C2_NOT_ACCESS);
            }
            // 验证资金密码
            String ins = SecureUtil.md5( StrUtil.addSuffixIfNot( dto.getPassword(), BizConst.BIZ_SECRET_CODE));
            if ( !StrUtil.equals( ins, entity.getCipher())) {
                throw ExUtils.error( LocaleKey.USER_CIPHER_PASSWORD_ERROR);
            }
            // 获取之前验证码
            String captcha = StrUtil.EMPTY;
            if( StrUtil.equals( dto.getType(), BizConst.MOBILE)){
                captcha = getCaptchaCode(RedisConst.PLATFORM_REDIS_WITHDRAWAL, entity.getMobile());
            }else{
                captcha = getCaptchaCode(RedisConst.PLATFORM_REDIS_WITHDRAWAL, entity.getEmail());
            }
            if( StrUtil.isNotEmpty( captcha)){
                throw ExUtils.error( LocaleKey.USER_CAPTCHA_HAS_SEND);
            }
            //验证码
            String code = RandomUtil.randomNumbers( AppConst.DEFAULT_CODE_LENGTH);
            //设置缓存
            if( StrUtil.equals( dto.getType(), BizConst.MOBILE)){
                redisPlugin.set(StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_WITHDRAWAL, entity.getMobile()),
                                                                code, AppConst.VERIFICATION_CODE_TIME);
                // 发送短信验证码
                smsUtils.sendMessage(BizConst.SMS_TYPE_WITHDRAW, entity.getMobile(), new String[]{code});
            }else{
                redisPlugin.set(StrUtil.addSuffixIfNot( RedisConst.PLATFORM_REDIS_WITHDRAWAL, entity.getEmail()),
                                                                code, AppConst.VERIFICATION_CODE_TIME);
                //发送邮件
                emailUtils.sendMessage(BizConst.SMS_TYPE_WITHDRAW, user.getEmail(), EmailConst.EMAIL_CAPTCHA_WITHDRAW, new String[]{code});
            }
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_PARAM_ERROR);
        }
        return data;
    }

    /**
     * 发起提币
     * @param dto
     * @param valid
     * @param request
     * @return
     */
    @RequestMapping("/withdraw")
    @DuplicateVerify(method = Method.ASSETS_WITHDRAWAL)
    public ResponseData withdraw(@RequestBody @Valid WithdrawDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid);
            }
            // 登录用户
            UserDTO user = getLoginUser( request);
            //用户详情
            UserEntity userEntity = userService.findUserByCondition( UserDTO.builder()
                                                .id( user.getId())
                                                .state( BizConst.BIZ_STATUS_VALID).build());
            // 是否实名认证
            if ( !IntUtils.equals( userEntity.getVerified(), BizConst.KycConst.K2_OK)) {
                throw ExUtils.error( LocaleKey.USER_C2_NOT_ACCESS);
            }
            // 短信验证码
            String sms = getCaptchaCode(RedisConst.PLATFORM_REDIS_WITHDRAWAL, userEntity.getMobile());
            //邮箱验证
            String email = getCaptchaCode( RedisConst.PLATFORM_REDIS_WITHDRAWAL, userEntity.getEmail());
            //判断
            if ( !StrUtil.equals( sms, dto.getSms()) || !StrUtil.equals( email, dto.getEml())) {
                throw ExUtils.error(LocaleKey.USER_INVALID_VERIFICATION_CODE);
            }
            //资金密码
            String ins = SecureUtil.md5( StrUtil.addSuffixIfNot( dto.getPassword(), BizConst.BIZ_SECRET_CODE));
            // 验证资金密码
            if ( !StrUtil.equals(ins, userEntity.getCipher())) {
                // 资金密码错误
                throw ExUtils.error( LocaleKey.USER_CIPHER_PASSWORD_ERROR);
            }
            // 当前货币
            CoinEntity coinEntity = getToken( dto.getSymbol());
            if ( ObjectUtil.isEmpty( coinEntity)) {
                throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
            }
            //判断当前代币是否允许转出
            if( StrUtil.equals( coinEntity.getAllowOut(), AppConst.FIELD_N)){
                throw ExUtils.error( LocaleKey.COIN_IS_NOT_ALLOW_WITHDRAW);
            }
            //判断当前货币提币是否低于最低提币值
            if(BigDecimalUtils.compareTo( coinEntity.getLowerLimitOut(), dto.getNumber())){
                throw ExUtils.error( LocaleKey.USER_WITHDRAW_NUMBER_LESS_LIMIT);
            }
            // 当前用户钱包信息
            UserWalletEntity walletEntity = walletService.findByCondition( BaseDTO.builder()
                                                                .symbol( dto.getSymbol())
                                                                .userId( user.getId()).build());
            // 验证转出
            if ( StrUtil.isEmpty(coinEntity.getFeeSymbol()) || StrUtil.equals( coinEntity.getSymbol(), coinEntity.getFeeSymbol())) {
                //转出货币与手续费货币一致）
                BizUtils.allowOut( coinEntity, coinEntity, walletEntity, walletEntity, dto.getNumber());
            } else {
                //手续费货币
                CoinEntity fee = getToken( coinEntity.getFeeSymbol());
                //手续费货币钱包
                UserWalletEntity feeEntity = walletService.queryUserWallet( UserWalletDTO.builder()
                                                                .symbol( coinEntity.getFeeSymbol())
                                                                .userId( user.getId()).build());
                BizUtils.allowOut(coinEntity, fee, walletEntity, feeEntity, dto.getNumber());
            }
            //响应数据
            ChainWithdrawEntity entity = new ChainWithdrawEntity();

            entity.setTime( DateUtil.currentSeconds());
            entity.setCoinCode( coinEntity.getSymbol());
            entity.setUserId( user.getId());
            entity.setRealFee( coinEntity.getOutFee());
            entity.setFeeCoinCode( coinEntity.getFeeSymbol());
            entity.setId( UUID.randomUUID().toString());
            entity.setAddress(dto.getAddress());
            entity.setTime(System.currentTimeMillis());
            dto.setNumber(dto.getNumber().subtract(entity.getRealFee()));
            entity.setNumber(dto.getNumber());

            Boolean bool = chainService.modify( entity);

            if( !bool){
                throw ExUtils.error( LocaleKey.USER_WITHDRAW_FAILED);
            }
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.USER_WITHDRAW_FAILED);
        }
        return data;
    }
}
