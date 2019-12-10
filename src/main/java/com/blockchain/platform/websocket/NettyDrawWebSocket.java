//package com.blockchain.platform.websocket;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.date.DatePattern;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONUtil;
//import com.blockchain.platform.constant.AppConst;
//import com.blockchain.platform.constant.RedisConst;
//import com.blockchain.platform.constant.SubscribeConst;
//import com.blockchain.platform.plugins.redis.RedisPlugin;
//import com.blockchain.platform.plugins.spring.SpringPlugin;
//import com.blockchain.platform.pojo.dto.BaseDTO;
//import com.blockchain.platform.pojo.dto.DrawWsDTO;
//import com.blockchain.platform.pojo.dto.PageDTO;
//import com.blockchain.platform.pojo.dto.UserDTO;
//import com.blockchain.platform.pojo.vo.LuckDrawLogVO;
//import com.blockchain.platform.pojo.vo.WinningVO;
//import com.blockchain.platform.service.IDrawService;
//import io.netty.channel.ChannelId;
//import io.netty.handler.codec.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.yeauty.annotation.*;
//import org.yeauty.pojo.ParameterMap;
//import org.yeauty.pojo.Session;
//
//import java.io.IOException;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
//
///**
// * 抽奖通知
// *
// * @author David.Li
// * @version 1.0
// * @create 2019-09-05 1:45 PM
// **/
//@Component
//@ServerEndpoint(value = "/draw/socket.io", port = 7359)
//public class NettyDrawWebSocket {
//
//
//    /**
//     * 锁
//     */
//    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
//
//
//    /**
//     * 容器
//     */
//    private static ConcurrentHashMap<String, Session> holder = new ConcurrentHashMap<>();
//
//
//    /**
//     * 数据
//     */
//    private static ConcurrentHashMap<ChannelId, String> hash = new ConcurrentHashMap<>();
//
//
//    /**
//     * 缓存对象
//     */
//    private static RedisPlugin redisPlugin;
//
//    /**
//     * 抽奖服务
//     */
//    private static IDrawService drawService;
//
//
//    /**
//     * 缓存记录
//     * @return
//     */
//    private RedisPlugin getRedisPlugin() {
//        if ( ObjectUtil.isEmpty( redisPlugin)) {
//            redisPlugin = SpringPlugin.getBean( RedisPlugin.class);
//        }
//        return redisPlugin;
//    }
//
//    private IDrawService getDrawService(){
//        if ( ObjectUtil.isEmpty( drawService)) {
//            drawService = SpringPlugin.getBean( IDrawService.class);
//        }
//        return drawService;
//    }
//
//    /**
//     * 获取用户数据
//     * @param token
//     * @return
//     */
//    public UserDTO getUserData(String token) {
//        UserDTO dto = null;
//        if (StrUtil.isNotEmpty(token)) {
//            dto = getRedisPlugin().hget(RedisConst.PLATFORM_USER_DATA, token);
//        }
//        return dto;
//    }
//
//
//    /**
//     * 开启连接
//     *
//     * @param session
//     * @param headers
//     * @param parameterMap
//     * @throws IOException
//     */
//    @OnOpen
//    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {
//        // 打开通道
//    }
//
//    /**
//     * 关闭当前连接
//     *
//     * @param session
//     * @throws IOException
//     */
//    @OnClose
//    public void onClose(Session session) throws IOException {
//        quit( session);
//    }
//
//    /**
//     * 连接异常
//     *
//     * @param session
//     * @param throwable
//     */
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        quit(session);
//    }
//
//    /**
//     * 收到消息
//     *
//     * @param session
//     * @param message
//     */
//    @OnMessage
//    public void onMessage(Session session, String message) {
//
//        DrawWsDTO dto = BeanUtil.toBean( JSONUtil.parse( message), DrawWsDTO.class);
//        drawService = getDrawService();
//        switch ( dto.getTopic()) {
//            //历史记录
//            case SubscribeConst.TOPIC_DRAW_HISTORY:
//                access( session, dto.getToken());
//                //获取当前抽奖历史中奖记录
//                List<LuckDrawLogVO> list = drawService.history( BaseDTO.builder()
//                                                    .id( dto.getDrawId())   //抽奖id
//                                                    .type( AppConst.FIELD_Y)   //类型（只查询中奖记录）
//                                                    .limit( AppConst.DEFAULT_PAGE_SIZE).build());  //只查询多少条
//                //返回数据
//                WinningVO vo = new WinningVO();
//                //是否中奖
//                vo.setPrize( AppConst.FIELD_N);
//                //是否是自己的记录
//                vo.setOwn( Boolean.FALSE);
//                //抽奖记录
//                List< List<Object>> vos = new ArrayList<>();
//                for(int idx = 0; idx < list.size(); idx ++){
//                    //抽奖记录
//                    LinkedList<Object> bullet = CollUtil.newLinkedList();
//                    // 手机号码
//                    bullet.add( list.get( idx).getUsername());
//                    //中奖数量
//                    bullet.add( list.get( idx).getNum());
//                    //中奖代币
//                    bullet.add( list.get(idx).getSymbol());
//                    //中奖时间
//                    bullet.add( DateUtil.format( list.get( idx).getTime(), DatePattern.NORM_DATETIME_FORMAT));
//                    vos.add( bullet);
//                }
//                vo.setBullet( vos);
//                //发送
//                session.sendText( JSONUtil.toJsonStr( vo));
//                break;
//            case SubscribeConst.TOPIC_DRAW_RECORD:
//                access( session, dto.getToken());
//                break;
//            case SubscribeConst.UN_TOPIC_DRAW_HISTORY:
//                quit( session);
//                break;
//            case SubscribeConst.UN_TOPIC_DRAW_RECORD:
//                quit( session);
//                break;
//                default:
//                    break;
//        }
//
//    }
//
//    /**
//     * 加入频道
//     * @param session
//     * @param token
//     */
//    protected void access( Session session, String token) {
//        // 用户信息
//        if ( StrUtil.isEmpty( token)) {
//            // 为了移除数据
//            hash.put( session.id(), session.id().asShortText());
//
//            holder.put( session.id().asShortText(), session);
//
//        } else {
//            UserDTO dto = getUserData( token);
//
//            hash.put( session.id(), StrUtil.toString( dto.getId()));
//
//            holder.put( StrUtil.toString( dto.getId()), session);
//        }
//    }
//
//    /**
//     * 退出频道
//     * @param session
//     */
//    protected void quit(Session session){
//        if( hash.containsKey( session.id())) {
//            holder.remove( hash.get( session.id()));
//        }
//    }
//
//    /**
//     * 发送消息
//     * @param key
//     * @param vo
//     */
//    public void sendTextAll(String key, WinningVO vo) {
//        if( StrUtil.isNotEmpty( key)){
//            for (Map.Entry<String, Session> entry : holder.entrySet()) {
//                Session session = entry.getValue();
//                if ( session.isActive() && session.isOpen()) {
//                    if ( StrUtil.equals( key, entry.getKey())) {
//                        WinningVO own = BeanUtil.toBean( vo, WinningVO.class);
//                        own.setOwn( true);
//                        session.sendText( JSONUtil.toJsonStr( own));
//                    } else {
//                        if ( StrUtil.equals(AppConst.FIELD_Y, vo.getPrize())) {
//                            session.sendText( JSONUtil.toJsonStr( vo));
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
