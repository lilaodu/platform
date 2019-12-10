package com.blockchain.platform.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.baomidou.mybatisplus.extension.plugins.pagination.DialectFactory;
import com.blockchain.platform.constant.SecretConst;
import com.blockchain.platform.dictionary.DictionaryFactory;
import com.blockchain.platform.disruptor.LinkedBlockTrade;
import com.blockchain.platform.mapper.UserMapper;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.service.*;
import com.blockchain.platform.utils.SignatureUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockchain.platform.annotation.DuplicateVerify;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.enums.Method;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.NotifyDTO;
import com.blockchain.platform.pojo.dto.OrderDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.RevokeDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.dto.UserWalletDTO;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;

/**
 * 交易订单控制器
 * 所有交易对表示动用"_"
 * @author zhangye
 */

@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {


    @Resource
    UserMapper userMapper;
	/**
     * 交易
     */
    @Resource
    private ITradeService tradeService;

    /**
     * 钱包接口
     */
    @Resource
    private IUserWalletService userWalletService;



    /**
     * 用户管理接口
     */
    @Resource
    private IUserService userService;

    @Resource
    private IKycService kycService;

    
    @Resource
    private IMatchDistributeService matchDistributeService;
   
	
	/**
     * 用户下单
     *
     * @param dto
     * @param valid
     * @param request
     * @return
     * 1.验证账号
     * 2.验证余额
     * 3.将订单放入队列
     * 
     */
    @RequestMapping("/business")
    @DuplicateVerify(method = Method.ORDER_BOOK)
    public ResponseData business(@RequestBody @Valid OrderDTO dto, BindingResult valid, HttpServletRequest request) {
    	ResponseData data = ResponseData.ok();
    	try {
            if (valid.hasErrors()) {//传参验证
            	throw ExUtils.error(valid.getFieldError().getDefaultMessage());
            }
            UserDTO user = getLoginUser(request);
            UserEntity userEntity=userMapper.selectById(user.getId());
            if(userEntity.getState().equals(IntUtils.INT_TWO)) throw ExUtils.error(LocaleKey.USER_LOCKED);
            // redis获取交易对数据
            MarketCoinEntity coin2TableEntity = getC2T(dto.getCoinPair());
            if (ObjectUtil.isEmpty(coin2TableEntity)) {//交易对不存在
                throw ExUtils.error(LocaleKey.SYS_PARAM_ERROR);
            }
            // 验证是否开盘
            if (!BizUtils.isOpen(coin2TableEntity)) {
                throw ExUtils.error( LocaleKey.MARKET_NOT_OPEN, new Object[] { dto.getCoinPair(),
                        DateUtil.format( coin2TableEntity.getCreateTime(), DatePattern.NORM_DATETIME_MINUTE_PATTERN)});
            }
            // 是否c1实名认证
//            UserDTO user = getLoginUser(request);
            UserKycEntity kycEntity = kycService.findByCondition(BaseDTO.builder()
                    .state( BizConst.BIZ_STATUS_FAILED)
                    .userId( user.getId()).build());
            if (ObjectUtil.isEmpty( kycEntity) || BizConst.KycConst.K1_OK > kycEntity.getStep()) {
                throw ExUtils.error(LocaleKey.USER_C1_NOT_ACCESS);
            }

            // 通过买卖判定是哪种钱包（前端传来的交易对EMC_USDT）,如果是“buy”，取USDT否则取“EMC”
            String symbol = StrUtil.equals(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN,dto.getType()) ? BizUtils.market( dto.getCoinPair()) : BizUtils.token( dto.getCoinPair());

            // 通过symbol和userid获取用户钱包
            UserWalletEntity walletEntity = userWalletService.queryUserWallet( 
            			UserWalletDTO.builder().symbol(symbol).userId( user.getId()).build());

            // 验证余额是否足够



            BigDecimal totalPrice = NumberUtil.mul(dto.getNum(), dto.getPrice());// 总额
            BigDecimal num = StrUtil.equals(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN,dto.getType()) ? totalPrice : dto.getNum();
            BizUtils.verifyWallet(walletEntity, num , symbol);
            // DTO填充实体参数
            OrderFlowEntity entity = BeanUtil.toBean(dto, OrderFlowEntity.class);
            entity.setUserId(user.getId());
            entity.setState(BizConst.TradeConst.ORDER_TYPE_PENDING);
            entity.setNum(dto.getNum());
            entity.setSurplusNum(dto.getNum());
            entity.setCoinPair(dto.getCoinPair());// 当前交易对
            entity.setTotalPrice(totalPrice);// 总额
            entity.setSurplusPrice(entity.getTotalPrice());
            entity.setFee(user.getFeeRate());//个人手续费比例
            entity.setCoinFee(BigDecimal.ONE);//此处活动手续费比例会根据活动和交易对变化，暂时默认为1
            entity.setSymbol(symbol);
            entity.setCreateDate(new Date());


            tradeService.addOrderEntity( entity);

            data.setMessage(LocaleKey.BUSINESS_ING);
        } catch (Exception ex) {
        	ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
		return data;
    }

    /**
     * 撮合分发接收点
     * @param entity
     * @param valid
     * @return
     */
    @RequestMapping("/tradeMatch")
    public ResponseData tradeMatch(@RequestBody @Valid OrderFlowEntity entity, BindingResult valid) {
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid);
            }

            if ( StrUtil.isEmpty( entity.getSignature()) ||
                    !StrUtil.equals( entity.getApiSecret(), SecretConst.API_SECRET)) {
                throw ExUtils.error( LocaleKey.ORDER_AUTH_ERROR);
            }

            // 验证签名
            String sign = SignatureUtils.signature( entity, entity.getApiSecret());
            if ( !StrUtil.equals( sign, entity.getSignature())) {
                throw ExUtils.error( LocaleKey.ORDER_AUTH_ERROR);
            }

            tradeService.addOrderEntity( entity);

            // 添加到队列
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }

    /**
     * 消息
     */
    final static Map<String, LinkedBlockTrade> queue = new ConcurrentHashMap<>();

    /**
     * 用户撤单
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/revoke")
    public ResponseData revoke(@RequestBody @Valid RevokeDTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if ( valid.hasErrors()) {
                throw ExUtils.error( valid.getFieldError().getDefaultMessage());
            }
            // 当前登录用户
            UserDTO user = getLoginUser(request);
            //创建一个撤单的订单对象
            OrderFlowEntity entity = OrderFlowEntity.builder().userId(user.getId()).id(dto.getId()).type(dto.getType()).symbol(dto.getSymbol()).coinPair(dto.getSymbol()).state(BizConst.TradeConst.ORDER_TYPE_CANCEL).build();
            //放入队列
            DictionaryFactory.blockTradeMap.get(entity.getCoinPair()).cancelOrder(entity);
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
    
    /**
     * 机器人下单
     *
     * @param dto
     * @return
     */
    @RequestMapping("/robotBook")
    public ResponseData robotBook(@RequestBody OrderDTO dto) {
        ResponseData data = ResponseData.ok();
        try {
        			
            // 获取交易对数据
        	MarketCoinEntity coin2TableEntity = getC2T(dto.getCoinPair());
            
            // 验证是否开盘
            if (ObjectUtil.isEmpty(coin2TableEntity) || !BizUtils.isOpen(coin2TableEntity)) {
                throw ExUtils.error( LocaleKey.MARKET_NOT_OPEN, new Object[] { dto.getCoinPair(),
                        DateUtil.format( coin2TableEntity.getCreateTime(), DatePattern.NORM_DATETIME_MINUTE_PATTERN)});
            }
            // 当前登录用户
            UserEntity user = userService.findUserByCondition(UserDTO.builder().id(dto.getUserId()).build());

            if ( ObjectUtil.isNotEmpty( user)) {

                if ( StrUtil.equals( BizConst.TradeConst.TRADE_TYPE_OUTS_EN, dto.getType())
                        || StrUtil.equals( BizConst.TradeConst.TRADE_TYPE_INPUTS_EN, dto.getType())) {

                    // 实际参数
                    OrderFlowEntity entity = BeanUtil.toBean(dto, OrderFlowEntity.class);
                    entity.setUserId(user.getId());

                    // 当前交易对
                    entity.setCoinPair(dto.getCoinPair());
                    // 总额
                    entity.setTotalPrice(NumberUtil.mul(dto.getNum(), dto.getPrice()));
                    // 剩余成交额
                    entity.setSurplusPrice(entity.getTotalPrice());
                    entity.setFee(user.getFeeRate());
                    entity.setState(BizConst.TradeConst.ORDER_TYPE_PENDING);
                    entity.setNum(dto.getNum());
                    entity.setSurplusNum(dto.getNum());
                    entity.setCoinFee(BigDecimal.ONE);
                    //放入队列
                    tradeService.addOrderEntity( entity);


                } else {
                    //创建一个撤单的订单对象
                    OrderFlowEntity entity = OrderFlowEntity.builder()
                            .userId(user.getId()).id( dto.getId())
                            .type(dto.getType())
                            .symbol(dto.getCoinPair())
                            .coinPair(dto.getCoinPair())
                            .state(BizConst.TradeConst.ORDER_TYPE_CANCEL).build();

                    DictionaryFactory.blockTradeMap.get(entity.getCoinPair()).cancelOrder(entity);

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
    
    
    /**
     * 获取订单
     * @param dto:
     * coinPair=ALL//交易对(BTC_USDT)
     * pageSize=10
     * pageNum=1
     * paging=false//是否分页
	 * scope=ALL//查询范围(3D)
	 * tabIndex=0//序号，前端发的订单种类，委托、历史(0是委托1是历史)
	 * type=ALL//类型(BUY或者SELL)
	 * 
	 * 
     * @param request
     * @return
     */
    @RequestMapping("/actives")
    public ResponseData actives(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        dto.setSymbol(dto.getCoinPair());
        //PageDTO(id=null, market=null, pageSize=10, pageNum=1, start=null, end=null, type=ALL, chooseType=null, userId=null, advertId=null, orderId=null, symbol=null, coinPair=null, state=null, paging=false, scope=3D, tabIndex=0, opinion=null, title=null)
        try {
            // 非买卖 传入 空
            if ( !StrUtil.equals(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN, dto.getType()) && !StrUtil.equals(BizConst.TradeConst.TRADE_TYPE_OUTS_EN, dto.getType())) {
                dto.setType( StrUtil.EMPTY);
//            	throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
            }
            // 获取交易对信息
            Map<String, MarketCoinEntity> config = redisPlugin.get( RedisConst.PLATFORM_PAIRS_CONFIG);
           
            if (StrUtil.isEmpty( dto.getCoinPair()) || MapUtil.isEmpty( config) || !config.containsKey( dto.getCoinPair())) {
                dto.setSymbol( StrUtil.EMPTY);
                dto.setCoinPair( StrUtil.EMPTY);
//            	throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
            } else {
                dto.setSymbol( config.get(dto.getCoinPair()).getTableName());
                dto.setCoinPair( config.get(dto.getCoinPair()).getTableName());
            }
            Date now = DateUtil.date();
            // 时间范围
            if( StrUtil.equals(BizConst.TradeConst.TRADE_SCOPE_ONE_WEEK, dto.getScope()) ) {
                dto.setStart( DateUtil.format(DateUtil.offset(now,  DateField.WEEK_OF_YEAR,-1), DatePattern.NORM_DATE_FORMAT));
                // 当前
                dto.setEnd( DateUtil.format( now, DatePattern.NORM_DATETIME_FORMAT));
            } else if (StrUtil.equals(BizConst.TradeConst.TRADE_SCOPE_THREE_DAY, dto.getScope())) {
                dto.setStart( DateUtil.format(DateUtil.offset(now,  DateField.DAY_OF_YEAR,-1), DatePattern.NORM_DATE_FORMAT));
                // 当前
                dto.setEnd( DateUtil.format( now, DatePattern.NORM_DATETIME_FORMAT));
            } else {
                dto.setStart( StrUtil.EMPTY);
                dto.setEnd( StrUtil.EMPTY);
            }
            // 用户信息
            UserDTO user = getLoginUser( request);
            dto.setUserId( user.getId());
            PageVO vo = null;
            if ( IntUtils.isZero( dto.getTabIndex())) {
                vo = tradeService.queryUserDeputes( dto);//委托
            } else {
                vo = tradeService.queryUserDeals( dto);//历史
            }
            data.setData( vo);
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }
	
}
