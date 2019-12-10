package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.blockchain.platform.annotation.DuplicateVerify;
import com.blockchain.platform.constant.ApiConst;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.enums.Method;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.CoinEntity;
import com.blockchain.platform.pojo.entity.UserAssetsFlowEntity;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.entity.UserWalletEntity;
import com.blockchain.platform.pojo.vo.CapitalVO;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.pojo.vo.SignVO;
import com.blockchain.platform.pojo.vo.UserWalletVO;
import com.blockchain.platform.service.IUserService;
import com.blockchain.platform.service.IUserWalletService;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.ExUtils;
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

/**
 * 用户钱包操作
 *@author zhangye
 * 
 **/
@RestController
@RequestMapping("/wallet")
public class UserWalletController extends BaseController {

    /**
     * 钱包服务接口
     */
    @Resource
    private IUserWalletService userWalletService;

    /**
     * 用户服务接口
     */
    @Resource
    private IUserService userService;

    /**
     * 资产明细
     * @param request
     * @return
     */
    @RequestMapping("/details")
    public ResponseData details(@RequestBody BaseDTO dto, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //状态
            dto.setState( BizConst.BIZ_STATUS_VALID);
            //获取用户资产明细
            List<CapitalVO> vos = userWalletService.getAssets( dto);
            //返回数据
            data.setData( vos);
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 账户流水
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/flow")
    public ResponseData flow(@RequestBody PageDTO dto, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //设置用户id
            dto.setUserId(user.getId());
            // 开始分页
            Page<UserAssetsFlowEntity> info  = PageHelper.startPage( dto.getPageNum(),dto.getPageSize());
            // 查询数据
            List<UserAssetsFlowEntity> list = userWalletService.getAssetsFlow(dto);
            // 返回数据
            data.setData( PageVO.builder()
                                    .list(list)
                                    .total(info.getTotal()).build());

         } catch (Exception ex) {
            ExUtils.error( ex , LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 获取数字签名
     * @param request
     * @return
     */
    @RequestMapping("/signature")
    @DuplicateVerify(method = Method.WALLET_SIGNATURE)
    public ResponseData signature(@RequestBody @Valid SignDTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid);
            }
            // 用户信息
            UserDTO user = getLoginUser( request);
            //用户token
            String token = request.getHeader( AppConst.TOKEN);
            // 验证随机串
            String nonce = Base64.encode( StrUtil.concat(Boolean.FALSE, token,
                                        StrUtil.toString( dto.getTimestamp()),
                                        ApiConst.OTC_SECRET));
            //验证数字签名
            if ( !StrUtil.equals( nonce, dto.getNonce())) {
                throw ExUtils.error( LocaleKey.SIGNATURE_NOT_NULL);
            }
            //用户详情
            UserEntity entity = userService.findUserByCondition( UserDTO.builder()
                                                                    .id( user.getId())
                                                                    .state( BizConst.BIZ_STATUS_VALID).build());
            if( ObjectUtil.isEmpty( entity)){
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
            // 通过apisecret 进行加密
            StringBuffer salt = new StringBuffer();
            salt.append( token)
                        .append( entity.getApiSecret())
                        .append( dto.getNonce())
                        .append( dto.getTimestamp());
            // md5加密
            String sign = SecureUtil.md5( salt.toString());
            //
            SignVO vo = SignVO.builder()
                            .timestamp( dto.getTimestamp())
                            .signature( sign).build();
            //拼接key
            String key = StrUtil.concat(Boolean.FALSE, StrUtil.toString( user.getId()), StrUtil.UNDERLINE, token);
            //设置缓存
            redisPlugin.set(key, vo, 10000l);
            //返回数据
            data.setData( sign);
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 划转
     * @param request
     * @return
     */
    @RequestMapping("/transfer")
    @DuplicateVerify(method = Method.ASSETS_TRANSFER)
    public ResponseData transfer(@RequestBody @Valid TransferDTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if( valid.hasErrors()){
                throw ExUtils.error( valid);
            }
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户token
            String token = request.getHeader( AppConst.TOKEN);
            // 验证 签名
            SignVO vo = redisPlugin.get( StrUtil.concat(Boolean.FALSE,
                                            StrUtil.toString( user.getId()), StrUtil.UNDERLINE, token));
            if ( ObjectUtil.isEmpty( vo)) {
                throw ExUtils.error( LocaleKey.SIGNATURE_VALID_ERROR);
            }
            //判断是否是同账户划转
            if( StrUtil.equals( dto.getAccountFrom(), dto.getAccountTo())){
                throw ExUtils.error( LocaleKey.TRANSFER_WALLET_SAME);
            }
            //用户详情
            UserEntity entity = userService.findUserByCondition( UserDTO.builder()
                                                    .id( user.getId())
                                                    .state( BizConst.BIZ_STATUS_VALID).build());
            if( ObjectUtil.isEmpty( entity)){
                throw ExUtils.error( LocaleKey.USER_NOT_FIND);
            }
            //拼接 key
            StringBuffer salt = new StringBuffer();
            salt.append( token)
                        .append( entity.getApiSecret()) //api_secret
                        .append( dto.getNonce())  //签名
                        .append( vo.getTimestamp());   //时间戳
            //对签名加密
            String signature = SecureUtil.md5( salt.toString());
            if ( StrUtil.isEmpty( vo.getSignature())
                        || !StrUtil.equals( vo.getSignature(), signature)
                        || !StrUtil.equals( signature, dto.getSignature())
                        || !StrUtil.equals( vo.getSignature(), dto.getSignature())) {
                throw ExUtils.error( LocaleKey.SIGNATURE_VALID_ERROR);
            }
            //设置用户id
            dto.setUserId(user.getId());
            // 当前货币
            CoinEntity coin = getToken( dto.getSymbol());
            //获取当前用户钱包
            UserWalletEntity wallet = userWalletService.queryUserWallet( UserWalletDTO.builder()
                                                                        .symbol( dto.getSymbol())
                                                                        .userId( user.getId()).build());
            //验证划转
            BizUtils.allowTransfer( dto, coin, wallet);
            //执行划转
            Boolean bool = userWalletService.transferAssets(dto);
            //返回数据
            data.setData( bool);
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
    
    /**
     * 充值
     * @param dto
     * @param valid
     * @return
     */
    @RequestMapping("/deposit")
    public ResponseData deposit(@RequestBody @Valid DepositDTO dto, BindingResult valid, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid);
            }
            //货币配置
            Map<String, CoinEntity> config = redisPlugin.get( RedisConst.PLATFORM_COIN_CONFIG);
            //判断是否为空
            if ( MapUtil.isEmpty( config) || !config.containsKey( dto.getSymbol())) {
                throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
            }
            // 代币
            CoinEntity coinEntity = config.get( dto.getSymbol());
            //是否允许转入
            BizUtils.allowIn(coinEntity, dto.getNumber());
            // 登录用户
            UserDTO user = getLoginUser( request);
            // 存储信息
            UserAssetsFlowEntity entity = BeanUtil.toBean( dto, UserAssetsFlowEntity.class);
            // 数量
            entity.setAmount( dto.getNumber());
            entity.setUserId( user.getId());
            entity.setType(BizConst.AssetsConst.ASSETS_FLOW_TYPE_INPUTS);
            entity.setState(BizConst.AssetsConst.STATE_APPLY);
            // 转入地址
            entity.setAddressTo( coinEntity.getWalletAddress());
            // 审核流水
            DrawMoneyFlowEntity drawMoneyFlowEntity = DrawMoneyFlowEntity.builder().build();
            drawMoneyFlowEntity.setUserId( user.getId());
            drawMoneyFlowEntity.setFlowNode( BizConst.AssetsConst.STATE_APPLY);

            userWalletService.addAssetsFlow( entity, drawMoneyFlowEntity);
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_PARAM_ERROR);
        }
        return data;
    }

    /**
     * 获取用户钱包信息
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/info")
    public ResponseData info(@RequestBody BaseDTO dto,HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try{
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //获取用户钱包
            UserWalletEntity entity = userWalletService.findByCondition( dto);
            //copy属性
            UserWalletVO vo = BeanUtil.toBean( entity, UserWalletVO.class);
            if( ObjectUtil.isNotEmpty( entity)){
                //代币
                vo.setToken( entity.getSymbol());
            }
            //返回数据
            data.setData( vo);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }
    
}
