//package com.blockchain.platform.websocket;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSON;
//import cn.hutool.json.JSONUtil;
//import com.blockchain.platform.constant.AppConst;
//import com.blockchain.platform.constant.BizConst;
//import com.blockchain.platform.constant.RedisConst;
//import com.blockchain.platform.constant.SubscribeConst;
//import com.blockchain.platform.plugins.redis.RedisPlugin;
//import com.blockchain.platform.plugins.spring.SpringPlugin;
//import com.blockchain.platform.pojo.dto.BaseDTO;
//import com.blockchain.platform.pojo.dto.ChatDTO;
//import com.blockchain.platform.pojo.dto.UserDTO;
//import com.blockchain.platform.pojo.entity.OTCChatEntity;
//import com.blockchain.platform.pojo.entity.OTCOrderEntity;
//import com.blockchain.platform.pojo.vo.ChatVO;
//import com.blockchain.platform.pojo.vo.OTCAdvertVO;
//import com.blockchain.platform.pojo.vo.OTCChatVO;
//import com.blockchain.platform.service.IOTCChatService;
//import com.blockchain.platform.service.IOTCOrderService;
//import com.blockchain.platform.utils.IntUtils;
//import io.netty.handler.codec.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.yeauty.annotation.*;
//import org.yeauty.pojo.ParameterMap;
//import org.yeauty.pojo.Session;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
//
///**
// * 聊天
// *
// * @author David.Li
// * @version 1.0
// * @create 2019-08-30 4:13 PM
// **/
//@Component
//@ServerEndpoint(value = "/chat/socket.io", port = 7369)
//public class NettyChatWebSocket {
//
//    /**
//     * 锁
//     */
//    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
//
//    /**
//     * 当前容器
//     */
//    @Resource
//    private SpringPlugin springPlugin;
//
//
//    /**
//     * 缓存数据
//     */
//    private static RedisPlugin redisPlugin;
//
//    /**
//     * otc 订单服务接口
//     */
//    private static IOTCOrderService orderService;
//
//    /**
//     * otc 聊天服务接口
//     */
//    private static IOTCChatService chatService;
//
//    /**
//     * 缓存记录
//     * @return
//     */
//    private RedisPlugin getRedisPlugin() {
//        if ( ObjectUtil.isEmpty( redisPlugin)) {
//            redisPlugin = springPlugin.getBean( RedisPlugin.class);
//        }
//        return redisPlugin;
//    }
//
//    /**
//     * 获取广告信息
//     * @param advertNo
//     * @return
//     */
//    public OTCAdvertVO getMerchantVO(String advertNo) {
//        if (ObjectUtil.isEmpty(redisPlugin)) {
//            redisPlugin = SpringPlugin.getBean(RedisPlugin.class);
//        }
//        Map<String, OTCAdvertVO> map = redisPlugin.get( RedisConst.PLATFORM_OTC_ADVERT);
//        if (MapUtil.isNotEmpty( map)) {
//            return map.get( advertNo);
//        }
//        return null;
//    }
//
//    /**
//     * 商户信息
//     * @return
//     */
//    public List<String> getMerchantList() {
//        return getRedisPlugin().get( RedisConst.PLATFORM_OTC_MERCHANTS);
//    }
//
//    /**
//     * 获取用户数据
//     * @param token
//     * @return
//     */
//    public UserDTO getUserData(String token) {
//        if (ObjectUtil.isEmpty(redisPlugin)) {
//            redisPlugin = SpringPlugin.getBean(RedisPlugin.class);
//        }
//        UserDTO dto = null;
//        if (StrUtil.isNotEmpty(token)) {
//            dto = redisPlugin.hget(RedisConst.PLATFORM_USER_DATA, token);
//        }
//        return dto;
//    }
//
//    public IOTCOrderService getOrder(){
//        if( ObjectUtil.isEmpty( orderService)){
//            orderService = SpringPlugin.getBean( IOTCOrderService.class);
//        }
//        return orderService;
//    }
//
//    public IOTCChatService getChat(){
//        if( ObjectUtil.isEmpty( chatService)){
//            chatService = SpringPlugin.getBean( IOTCChatService.class);
//        }
//        return chatService;
//    }
//
//
//
//    /**
//     * 开启连接
//     * @param session
//     * @param headers
//     * @param parameterMap
//     * @throws IOException
//     */
//    @OnOpen
//    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {
//    }
//
//    /**
//     * 关闭链接
//     * @param session
//     * @throws IOException
//     */
//    @OnClose
//    public void onClose(Session session) throws IOException {
//        removeUser( session);
//    }
//
//    /**
//     * 连接异常
//     * @param session
//     * @param throwable
//     */
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        removeUser( session);
//    }
//
//    /**
//     * 收到消息
//     * @param session
//     * @param message
//     */
//    @OnMessage
//    public void onMessage(Session session, String message) {
//        // 收到消息
//        ChatDTO dto = BeanUtil.toBean( JSONUtil.parse( message), ChatDTO.class);
//        //登录用户
//        UserDTO user = getUserData( dto.getToken());
//        //保存用户
//        saveUser( session, dto, user);
//        //订单服务接口
//        switch ( dto.getTopic()) {
//            //请求
//            case SubscribeConst.TOPIC_CHAT_REQ:
//                // 获取 历史记录 db
//                List<OTCChatVO> vos = getChat().query( BaseDTO.builder()
//                                                .advertNumber( dto.getAdvertNo()) //广告编号
//                                                .orderNumber( dto.getOrderNo())  //订单编号
//                                                .userId( user.getId()).build());  //用户id
//                //返回信息
//                ChatVO vo = BeanUtil.toBean( dto, ChatVO.class);
//                //聊天记录
//                vo.setData( vos);
//                vo.setType( BizConst.ChatConst.ADVICE_TYPE_HISTORY);
//                //发送
//                sendTextOwn( session, vo);
//                break;
//            //发送
//            case SubscribeConst.TOPIC_CHAT_ENTER:
//                //分发消息
//                //设置用户头像
//                dto.setHeadImage( user.getHeadImage());
//                dispatch( dto);
//                break;
//            //监听
//            case SubscribeConst.TOPIC_CHAT_MONITOR:
//                Integer unRead = getChat().countUnRead( BaseDTO.builder().userId( user.getId()).build());
//
//                ChatVO urVO = new ChatVO();
//                urVO.setType( BizConst.ChatConst.ADVICE_TYPE_NUM);
//                urVO.setData( unRead);
//                sendTextOwn( session, urVO);
//                break;
//            default:
//        }
//        // 存储数据(db)
//        if ( StrUtil.equals( SubscribeConst.TOPIC_CHAT_ENTER, dto.getTopic())){
//            //添加消息 db
//            OTCChatEntity entity = OTCChatEntity.builder()
//                                .advertNumber( dto.getAdvertNo())  ///广告编号
//                                .orderNumber( dto.getOrderNo())   //订单编号
//                                .userId(user.getId())   //用户id
//                                .content(dto.getContent()).build();
//            //判断当前登录用户订单用户还是商户
//            OTCAdvertVO vo = getMerchantVO( dto.getAdvertNo());
//            if( IntUtils.equals( vo.getUserId(), user.getId())){
//                //商户
//                entity.setState( BizConst.ChatConst.STATE_USER_UNREAD);
//            }else{
//                entity.setState( BizConst.ChatConst.STATE_MERCHANT_UNREAD);
//            }
//            getChat().send( entity);  //发送内容
//        }
//    }
//
//    /**
//     * 分发消息
//     * @param dto
//     */
//    public void dispatch( ChatDTO dto) {
//        UserDTO user = getUserData( dto.getToken());
//        //聊天消息
//        ChatVO vo = BeanUtil.toBean( dto, ChatVO.class);
//        // 聊天内容
//        OTCChatVO chat = OTCChatVO.builder()
//                                .content( dto.getContent())
//                                .own(AppConst.FIELD_Y)
//                                .time( new Date()).build();
//        List<OTCChatVO> content = CollUtil.newArrayList();
//        content.add( chat);
//        vo.setData( content);
//        //广告详情
//        OTCAdvertVO otc = getMerchantVO( dto.getAdvertNo());
//        //订单用户
//        if ( !IntUtils.equals( user.getId(), otc.getUserId())) {
//            // 获取订单 db
//            OTCOrderEntity order = getOrder().findByCondition( BaseDTO.builder()
//                                                .orderNumber( dto.getOrderNo()).build());
//            sendTextUser( order.getUserId(), vo);
//        } else {
//            //商户
//            sendTextMerchant( otc.getUserId(), vo);
//        }
//    }
//
//    /**
//     * 存储用户信息
//     * @param session
//     * @param dto
//     */
//    public void saveUser(Session session, ChatDTO dto, UserDTO user) {
//
//        try {
//            rwLock.writeLock().lock();
//            //广告
//            List<String> merchantList = getMerchantList();
//            if ( ObjectUtil.isNotEmpty( user) ) {
//                ChatChannelUtils.user.put( user.getId(), session);
//                // 是否商户
//                if ( merchantList.contains( StrUtil.toString( user.getId()))) {
//                    //商户
//                    ChatChannelUtils.merchant.put( user.getId(), session);
//
//                    Map<Integer, String> online = getRedisPlugin().get( RedisConst.PLATFORM_MERCHANT_ONLINE);
//
//                    if ( MapUtil.isEmpty( online)){
//                        online = MapUtil.newHashMap();
//                    }
//                    // 在线商户
//                    online.put( user.getId(), session.id().asShortText());
//
//                    getRedisPlugin().set( RedisConst.PLATFORM_MERCHANT_ONLINE, online);
//                }
//                ChatChannelUtils.values.put( session.id(), user.getId());
//            }
//        } finally {
//            rwLock.writeLock().unlock();
//        }
//    }
//
//    /**
//     * 移除用户
//     * @param session
//     * @param dto
//     */
//    public void removeUser(Session session, ChatDTO dto) {
//        try {
//            rwLock.writeLock().lock();
//            //登录用户
//            UserDTO user = getUserData( dto.getToken());
//            if ( ObjectUtil.isNotEmpty( user) ) {
//                //广告
//                List<String> merchantList = getMerchantList();
//                if ( merchantList.contains( StrUtil.toString( user.getId()))) {
//                    //商户
//                    ChatChannelUtils.merchant.remove( user.getId());
//
//                    Map<Integer, String> online = getRedisPlugin().get( RedisConst.PLATFORM_MERCHANT_ONLINE);
//                    if ( MapUtil.isNotEmpty( online)) {
//                        online.remove( user.getId());
//                    }
//                }
//                //订单用户
//                ChatChannelUtils.user.remove( user.getId());
//            } else {
//                // 验证是否存在用户
//
//                int key = ChatChannelUtils.values.get( session.id());
//
//                if ( ObjectUtil.isNotEmpty( key)) {
//
//                    ChatChannelUtils.merchant.remove( key);
//
//                    ChatChannelUtils.user.remove( key);
//                }
//            }
//            ChatChannelUtils.values.remove( session.id());
//        } finally {
//            rwLock.writeLock().unlock();
//        }
//    }
//
//    /**
//     * 移除用户
//     * @param session
//     */
//    public void removeUser(Session session) {
//        try {
//            rwLock.writeLock().lock();
//
//            int key = ChatChannelUtils.values.get( session.id());
//
//            if ( ObjectUtil.isNotEmpty( key)) {
//
//                ChatChannelUtils.merchant.remove( key);
//
//                ChatChannelUtils.user.remove( key);
//
//                Map<Integer, String> online = getRedisPlugin().get( RedisConst.PLATFORM_MERCHANT_ONLINE);
//                if ( MapUtil.isNotEmpty( online)
//                        && online.containsKey( key)) {
//                    online.remove( key);
//                }
//                getRedisPlugin().set( RedisConst.PLATFORM_MERCHANT_ONLINE, online);
//            }
//
//            ChatChannelUtils.values.remove( session.id());
//        } finally {
//            rwLock.writeLock().unlock();
//        }
//    }
//
//    /**
//     * 发送消息 to 商户
//     * @param userId
//     * @param vo
//     */
//    public void sendTextMerchant(int userId, ChatVO vo) {
//        try {
//            rwLock.readLock().lock();
//            Session session = ChatChannelUtils.merchant.get( userId);
//
//            session.sendText( JSONUtil.toJsonStr( vo));
//        } finally {
//            rwLock.readLock().unlock();
//        }
//    }
//
//
//    /**
//     * 发送消息 to 用户
//     * @param userId
//     * @param vo
//     */
//    public void sendTextUser(int userId, ChatVO vo) {
//        try {
//            rwLock.readLock().lock();
//            Session session = ChatChannelUtils.user.get( userId);
//            session.sendText( JSONUtil.toJsonStr( vo));
//        } finally {
//            rwLock.readLock().unlock();
//        }
//    }
//
//    /**
//     * 个人消息发送
//     * @param session
//     * @param vo
//     */
//    public void sendTextOwn(Session session, ChatVO vo) {
//        try {
//            rwLock.readLock().lock();
//            session.sendText( JSONUtil.toJsonStr( vo));
//        } finally {
//            rwLock.readLock().unlock();
//        }
//    }
//}
