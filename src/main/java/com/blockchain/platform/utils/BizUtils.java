package com.blockchain.platform.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blockchain.platform.constant.*;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.pojo.dto.TransferDTO;
import com.blockchain.platform.pojo.dto.TvDTO;
import com.blockchain.platform.pojo.dto.UploadDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.*;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 业务方法工具类
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 10:38 AM
 **/
public class BizUtils {

    /**
     * 获取上传图片
     * @param request
     * @return
     */
    public static final UploadDTO getUploadPic(HttpServletRequest request) throws Exception {
        // 图片内容，图片类型
        String img = null, imgType = "";
        // 上传图片信息
        if(request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;
            MultipartFile file = multi.getFile( AppConst.KEY_IMAGE_NAME);

             Base64.Encoder encoder =Base64.getEncoder();
            assert file != null;
            img = encoder.encodeToString(file.getBytes());
            // 判断图片类型
            imgType = FilenameUtils.getExtension(file.getOriginalFilename());

            if ( StrUtil.isEmpty( imgType)) {
                Map<String, String[]> map = request.getParameterMap();

                String fileName = request.getParameter(QueryConst.QUERY_NAME);

                if ( StrUtil.isNotEmpty( fileName)) {
                    imgType = StrUtil.splitToArray(fileName, StrUtil.C_DOT)[ IntUtils.INT_ONE];
                }
            }
        }else {
            throw ExUtils.error(new BcException(), LocaleKey.FILE_UPLOAD_FAILED);
        }
        return UploadDTO.builder()
                    .state( BizConst.BIZ_STATUS_VALID)
                    .imgType(imgType)
                    .imageContent( img).build();
    }

    /**
     * 获取对应Symbol
     * 买入时需要获取交易对中的后者
     * @param value 当前交易对
     * @return
     */
    public static String market(Object value) {
        if( ObjectUtil.isEmpty( value)){
            return null;
        }
        List<String> mks = StrUtil.splitTrim(StrUtil.toString(value), StrUtil.UNDERLINE);

        if( mks.size() > 1) {
            return mks.get( 1);
        }
        return StrUtil.toString( value);
    }

    /**
     *获取对应Symbol
     *买出时需要获取交易对中的前者
     * @param value 当前交易对
     * @return
     */
    public static String token(Object value) {
        if( ObjectUtil.isEmpty( value)){
            return null;
        }
        List<String> mks = StrUtil.splitTrim( StrUtil.toString(value), StrUtil.UNDERLINE);
        if( mks.size() > 1) {
            return mks.get( 0);
        }
        return StrUtil.toString( value);
    }
    
    /**
     * tv 开始时间
     * @param dto
     * @param open
     * @return
     */
    public static final Long getTvFrom(TvDTO dto, Long open) {
        return dto.getFrom() > open ? dto.getFrom() : open;
    }

    /**
     * 计算实际 分辨率
     * @param resolution
     * @return
     */
    public static final Long getCycle(String resolution){
        Long cycle = 0L;
        if( StrUtil.equals( resolution, TvConst.RESOLUTION_TYPE_DAY) ){
            cycle =  TvConst.SECOND_OF_DAY;
        } else if( StrUtil.equals(resolution, TvConst.RESOLUTION_TYPE_WEEK)
                || StrUtil.equals(resolution, TvConst.RESOLUTION_TYPE_WEEK_ERROR)){
            cycle =  TvConst.SECOND_OF_WEEK;
        } else {
            cycle = IntUtils.toLong( resolution) * TvConst.SECOND_OF_MINUTE;
        }
        return cycle;
    }

    /**
     * 授权过期时间
     * @return
     */
    public static final Long getAuthExpires(){
        DateTime endOfDay = DateUtil.endOfDay( DateUtil.date());
        return endOfDay.getTime() / 1000L;
    }

    /**
     * 是否获取数据库
      * @param dto
     * @param cycle
     * @return
     */
    public static final Boolean isGetRedis(TvDTO dto, Long cycle) {
        return dto.getTo() - dto.getFrom() > cycle * 10L;
    }

    /**
     * 转换为 时间戳
     * @param object
     * @return
     */
    public static Long timestamp(Object object) {
        if ( ObjectUtil.isEmpty( object)) {
            return DateUtil.currentSeconds();
        }
        if (object instanceof Date) {
            return ((Date) object).getTime() / 1000;
        }
        // 格式化时间
        Date date = DateUtil.parse( StrUtil.toString( object), DatePattern.NORM_DATETIME_PATTERN);
        return date.getTime() / 1000;
    }
    
    /**
     * 交易对是否开盘
     * @param entity
     * @return
     */
    public static Boolean isOpen(MarketCoinEntity entity) throws ParseException {
        Date current = new Date();
        if ( current.after( entity.getCreateTime())) {
            // 当前时间
            Long c = timeHms( StrUtil.EMPTY);
            String[] openDatas = StrUtil.split(entity.getDayOpen(),StrUtil.COMMA);
            for (int i = 0; i < openDatas.length; i++) {
                String dar = openDatas[i];
                // 验证 日 开盘时间
                List<String> times = Arrays.asList(StrUtil.split(dar, StrUtil.DASHED));
                // 开
                Long dayOpen = timeHms(times.get(IntUtils.INT_ZERO));
                // 关
                Long dayClose = timeHms(times.get(IntUtils.INT_ONE));
                if (c >= dayOpen && c <= dayClose) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    
    /**
     * 时间
     * @param date
     * @return
     */
    public static final Long timeHms(String date){
        SimpleDateFormat format = new SimpleDateFormat(DatePattern.NORM_TIME_PATTERN);
        Date dt = null;
        try {
            dt = format.parse(date);
        } catch (ParseException e) {
            try {
                Time time = new Time(System.currentTimeMillis());
                dt = format.parse(time.toString());
            } catch (ParseException e1) {
                return 1L;
            }
        }
        return dt.getTime() / 1000;
    }
    
    /**
     * 验证钱包余额
     * @param entity
     * @param amount
     */
    public static final void verifyWallet(UserWalletEntity entity, BigDecimal amount,String field){
        if ( ObjectUtil.isEmpty( entity)) {
            // 余额不不足
            throw ExUtils.error( LocaleKey.WALLET_INSUFFICIENT_FUNDS);
        }
        //可用余额
        BigDecimal usable = BigDecimal.ZERO;
        //根据传的字段计算对应的钱包余额
        switch (field){
            // 法币账户
            case BizConst.WalletConst.WALLET_TYPE_OTC :
                usable = NumberUtil.sub( entity.getBalanceOtc(), entity.getFrozenOtc());
                break;
            //  合约账户
            case BizConst.WalletConst.WALLET_TYPE_T :
                usable = NumberUtil.sub( entity.getBalanceT(), entity.getFrozenT());
                break;
            //币币账户
            default:
                usable = NumberUtil.sub( entity.getBalance(), entity.getFrozenBalance());
                break;
        }
        if ( usable.compareTo( amount) < 0) {
            throw ExUtils.error( LocaleKey.WALLET_INSUFFICIENT_FUNDS);
        }
    }

    /**
     * 手续费
     * @param num
     * @param symbol
     * @param param
     * @return
     */
    public static final BigDecimal rate(BigDecimal num,String symbol, String param){
        if(ObjectUtil.isEmpty( num)){
            throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
        }
        BigDecimal rate = BigDecimal.ZERO;
        //获取手续费配置信息(货币)
        Map<String,Object> advert =  JSON.parseObject( param, new TypeReference<Map<String, Object>>(){});
        if(MapUtil.isNotEmpty( advert)){
            if(advert.containsKey( symbol)){
                rate = BigDecimalUtils.multi( num, NumberUtil.toBigDecimal(StrUtil.toString(advert.get(symbol))));
            }else{
                rate = BigDecimalUtils.multi( num, NumberUtil.toBigDecimal(StrUtil.toString(advert.get(BizConst.DEFAULT_RATE))));
            }
        }
        //活动手续费的配置信息
//        Map<String,Object> activity =  JSON.parseObject( param.get( BizConst.ParamsConst.ACTIVITY_FEE),
//                                                                new TypeReference<Map<String, Object>>(){});
//        if(MapUtil.isNotEmpty( activity)){
//            if( activity.containsKey( symbol)){
//                rate = BigDecimalUtils.multi( rate, NumberUtil.toBigDecimal(StrUtil.toString(activity.get(symbol))));
//            }else{
//                rate = BigDecimalUtils.multi( rate, NumberUtil.toBigDecimal(StrUtil.toString(activity.get(BizConst.DEFAULT_RATE))));
//            }
//        }
//        //个人手续费的配置信息
//        Map<String,Object> personal =  JSON.parseObject( param.get( BizConst.ParamsConst.PERSONAL_FEE),
//                                                                new TypeReference<Map<String, Object>>(){});
//        if(MapUtil.isNotEmpty( personal)){
//            if( activity.containsKey( symbol)){
//                rate = BigDecimalUtils.multi( rate, NumberUtil.toBigDecimal(StrUtil.toString(personal.get(symbol))));
//            }else{
//                rate = BigDecimalUtils.multi( rate, NumberUtil.toBigDecimal(StrUtil.toString(personal.get(BizConst.DEFAULT_RATE))));
//            }
//        }
        return rate;
    }

    /**
     * 移除对应的奖项
     * @param value
     * @param values
     */
    public static void removePrize(List<Integer> value,Integer... values) {
        //遍历移除的多个数据
        for(Integer num : values){
            value.remove( num);
        }
    }

    /**
     * 允许转入
     * @param entity
     * @param number
     * @return
     */
    public static final void allowIn(CoinEntity entity, BigDecimal number){
        if ( !StrUtil.equals( entity.getAllowIn(), AppConst.FIELD_Y)) {
            throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
        }
        //
        if ( number.compareTo( entity.getLowerLimitIn()) < 0 ) {
            throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
        }
    }
    /**
     * 允许转出
     * @param entity
     * @param number
     */
    public static final void allowOut(CoinEntity entity,
                                      CoinEntity fee,
                                      UserWalletEntity wallet,
                                      UserWalletEntity feeWallet,
                                      BigDecimal number){
        //当前货币是否支持转出
        if ( !StrUtil.equals( entity.getAllowOut(), AppConst.FIELD_Y)) {
            throw ExUtils.error( LocaleKey.TOKEN_TRANSFER_NOT_OUTS);
        }
        //用户钱包
        if ( ObjectUtil.isEmpty( wallet)) {
            throw ExUtils.error( LocaleKey.WALLET_INSUFFICIENT_FUNDS); // 余额不不足
        }
        //用户当前钱包可用余额
        BigDecimal usable = NumberUtil.sub( wallet.getBalance(), wallet.getFrozenBalance());
        if ( usable.compareTo( number) < 0) {
            throw ExUtils.error( LocaleKey.WALLET_INSUFFICIENT_FUNDS); // 余额不不足
        }
        // 手续费 跟 当前货币一致
        if ( StrUtil.equals( fee.getSymbol(), entity.getSymbol())) {
            if ( usable.compareTo( number) < 0) {
                throw ExUtils.error( LocaleKey.WALLET_INSUFFICIENT_FUNDS); // 余额不不足
            }
        } else { // 手续费 跟 当前货币不一致
            //手续费货币钱包
            if ( ObjectUtil.isEmpty( feeWallet)) {
                throw ExUtils.error( LocaleKey.WALLET_INSUFFICIENT_FUNDS); // 余额不不足
            }
            //手续费钱包可用余额
            BigDecimal feeUsable = NumberUtil.sub( feeWallet.getBalance(), feeWallet.getFrozenBalance());
            if ( feeUsable.compareTo( fee.getOutFee()) < 0) {
                throw ExUtils.error( LocaleKey.WALLET_INSUFFICIENT_FUNDS); // 余额不不足
            }
        }
        // 提币数量小于最小提币数量
        if ( number.compareTo( entity.getLowerLimitOut()) < 0 ) {
            throw ExUtils.error( LocaleKey.WALLET_INSUFFICIENT_FUNDS);
        }
    }

    /**
     * 是否支持划转
     * @param dto
     * @param coin
     * @param wallet
     * @return
     */
    public static final void allowTransfer(TransferDTO dto, CoinEntity coin, UserWalletEntity wallet){
        //判断划转目的账户类型
        switch (dto.getAccountTo()){
            //  法币账户
            case BizConst.WalletConst.WALLET_TYPE_OTC :
                if(!StrUtil.equals( AppConst.FIELD_Y, coin.getIsOtc())){
                    throw ExUtils.error( LocaleKey.TRANSFER_TO_WALLET_ERROR);
                }
                break;
            //合约账户
            case BizConst.WalletConst.WALLET_TYPE_T :
                if(!StrUtil.equals( AppConst.FIELD_Y, coin.getIsT())){
                    throw ExUtils.error( LocaleKey.TRANSFER_TO_WALLET_ERROR);
                }
                break;
        }
        //判断用户钱包余额是否足够
        //可用余额
        BigDecimal usable = BigDecimal.ZERO;
        switch ( dto.getAccountFrom()){
            //法币账户
            case BizConst.WalletConst.WALLET_TYPE_OTC :
                usable = BigDecimalUtils.subtr( wallet.getBalanceOtc(), wallet.getFrozenOtc());
                break;
            //合约账户
            case BizConst.WalletConst.WALLET_TYPE_T :
                usable = BigDecimalUtils.subtr( wallet.getBalanceT(), wallet.getFrozenT());
                break;
            //币币账户
            case BizConst.WalletConst.WALLET_TYPE_TRADE :
                usable = BigDecimalUtils.subtr( wallet.getBalance(), wallet.getFrozenBalance());
                break;
        }
        //划转金额大于可用余额
        if( BigDecimalUtils.compareTo( dto.getAmount(), usable)){
            throw ExUtils.error( LocaleKey.WALLET_INSUFFICIENT_FUNDS); // 余额不不足
        }
    }

    /**
     * 计算日期到现在位置是否有三个月
     * @param time
     */
    public static final Boolean exceed( Date time){
        //格式化日期
        time = DateUtil.parse( DateUtil.format( time, DatePattern.NORM_DATE_FORMAT));
        //格式化当前日期 yyyy-MM-dd
        Date now = DateUtil.parse( DateUtil.format( new Date(), DatePattern.NORM_DATE_PATTERN));
        //比较时间
        return IntUtils.compare( IntUtils.toInt( DateUtil.betweenMonth( now, time, Boolean.FALSE)), 3) ? Boolean.TRUE : Boolean.FALSE ;
    }
    
    /**
     * 获取最近一次不为0的数据
     * @param list
     * @param idx
     * @return
     */
    public static final KlineVO getNotZeroKline(List<KlineVO> list, Integer idx){
        KlineVO vo = null;
        for (int i = idx ; i >= 0; i -- ) {
            vo = list.get( i);
            if ( !BigDecimalUtils.isZero( vo.getO())) {
                break;
            }
        }
        return vo;
    }

    /**
     * 排行榜排序
     * @param type
     * @param list
     * @return
     */
    public static final List<RankingVO> sort(String type,List<RankingVO> list){
        switch ( type){
            //涨
            case BizConst.Ranking.RANKING_RISE :
                CollUtil.sort(list, new Comparator<RankingVO>() {
                    @Override
                    public int compare(RankingVO o1, RankingVO o2) {
                        return o2.getChangeRange().compareTo( o1.getChangeRange()) == 1 ? 1 : -1;
                    }
                });
                break;
            //跌
            case BizConst.Ranking.RANKING_FALL :
                CollUtil.sort(list, new Comparator<RankingVO>() {
                    @Override
                    public int compare(RankingVO o1, RankingVO o2) {
                        return o1.getChangeRange().compareTo( o2.getChangeRange())  == 1 ? 1 : -1;
                    }
                });
                break;
            //新币
            case BizConst.Ranking.RANKING_NEW :
                //新币
                List<RankingVO> coin = CollUtil.newArrayList();
                for(int idx = 0; idx < list.size(); idx ++){
                    //是新币
                    if( StrUtil.equals( list.get( idx).getIsNew(), AppConst.FIELD_Y)){
                        coin.add( list.get( idx));
                    }
                }
                list = coin;
                CollUtil.sort(list, new Comparator<RankingVO>() {
                    @Override
                    public int compare(RankingVO o1, RankingVO o2) {
                        return o2.getSn().compareTo( o1.getSn());
                    }
                });
                break;
        }
        return CollUtil.newArrayList( list);
    }
    
    /**
     * 申请锁仓数量是否正确
     * @param configLockNum
     * @param lockNum
     * @return
     */
    public static final Boolean okLockNum(String configLockNum,BigDecimal lockNum){
    	
    	boolean okLockNum = false;
    	
    	String[] LockNums = configLockNum.split(",");
    	for(String LockNum : LockNums) {
    		BigDecimal bd = new BigDecimal(LockNum);
    		if(bd.compareTo(lockNum) == 0) {
    			okLockNum = true;
    		}
    	}
    	
    	
    	
    	
    	return okLockNum;
        
    }

    /**
     * 处理用户的抽奖次数
     * @param vo
     * @return
     */
    public static final PrizeVO draw(PrizeVO vo, LuckDrawEntity entity){
        //计算可抽奖次数
        Integer num = NumberUtil.sub( NumberUtil.add( IntUtils.floor( vo.getKyc(), entity.getKycNum()),
                            IntUtils.floor( vo.getContract(), entity.getContractNum())),
                vo.getDraw()).intValue();
        //返回数据
        PrizeVO prize = PrizeVO.builder()
                .num( num)
                .kyc( vo.getKyc() % entity.getKycNum())
                .contract( vo.getContract() % entity.getContractNum()).build();
        return prize;
    }

    /**
     * 判断用户的支付方式是否满足广告
     * @param userPay
     * @param advertPay
     */
    public static final void allowPay(List<UserPaymentVO> userPay, List<String> advertPay){
        if( CollUtil.isEmpty( userPay) || CollUtil.isEmpty( advertPay)){
            throw ExUtils.error( LocaleKey.OTC_ADVERT_NOT_ALLOW_PAYMENT);
        }
        Boolean bool = false;
        w:for( int idx = 0; idx < userPay.size(); idx ++){
            for( int index = 0; index < advertPay.size(); index ++){
                // 判断是否相同
                if( StrUtil.equals(userPay.get( idx).getPayType(), advertPay.get( index))){
                    bool = true;
                    break w;
                }
            }
        }
        if(!bool){
            throw ExUtils.error( LocaleKey.OTC_ADVERT_NOT_ALLOW_PAYMENT);
        }
    }

    /**
     * 判断是邮箱
     * @param username
     * @return
     */
    public static Boolean isEmail(String username) {
        //邮箱正则
        String email = AppConst.EMAIL;
        //开始匹配邮箱
        Matcher m = Pattern.compile(email).matcher( username);
        //判断
        if( m.matches()) {
            //为邮箱格式
            return  Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 判断是电话号码
     * @param username
     * @return
     */
    public static Boolean isMobile(String username){
        //手机号码正则
        String phone = AppConst.PHONE;
        //开始匹配手机号码
        Matcher m = Pattern.compile( phone).matcher( username);
        //判断
        if( m.matches()){
            //为电话格式
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 是否为商户
     * @param dto
     * @return
     */
    public static boolean isMerchant(UserDTO dto) {
        return StrUtil.equals(AppConst.FIELD_Y, dto.getIsMerchant());
    }

    /**
     * 抽奖概率
     * @param map
     * @return
     */
    public static Map<Integer, Double> probability(Map<String, LuckDrawConfigVO> map){
        //返回结果
        Map<Integer, Double> data = MapUtil.newHashMap();
        //遍历map
        for( Map.Entry<String, LuckDrawConfigVO> entry : map.entrySet()){
            data.put( IntUtils.toInt( entry.getKey()), entry.getValue().getProbability());
        }
        return data;
    }

    public static String formatName(String name){
        //判断用户名是手机还是邮箱
        if( BizUtils.isMobile( name)){
            name =  name.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }else if( BizUtils.isEmail( name)){
            name = name.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
        }
        return name;
    }

    /**
     * 是否需要计算降级
     * @param entity
     * @return
     */
    public static Boolean isDowngrade(UserUpgradeFlowEntity entity) {

        Boolean down = Boolean.FALSE;

        // 计算时间
        String cal = DateUtil.format( DateUtil.offsetMonth( entity.getCreateTime(), IntUtils.INT_THREE), DatePattern.PURE_DATE_PATTERN);

        String now = DateUtil.format( new Date(), DatePattern.PURE_DATE_PATTERN);

        if ( StrUtil.equals( cal, now)) {
            down = true;
        }
        return down;
    }

    /**
     * 获取用户实际等级序号
     * @param lv
     * @return
     */
    public static String getRankValue(String lv) {
        int idx = 1;
        switch ( lv) {
            case UpgradeConst.GRADE_RC: // 团长
                idx = 2;
                break;
            case UpgradeConst.GRADE_DC: // 师长
                idx = 3;
                break;
            case UpgradeConst.GRADE_AC: // 军长
                idx = 4;
                break;
            default:
                idx = 1;
        }
        return StrUtil.toString( idx);
    }


    public static void main(String[] args) {

        String bs  = "530788461@qq.com".replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");

        System.out.println( "执行结果" + bs);
    }
}
