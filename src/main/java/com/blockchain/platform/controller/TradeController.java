package com.blockchain.platform.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.dictionary.DictionaryFactory;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.HuobiKLineEntity;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.ISocketService;
import com.blockchain.platform.service.IUserWalletService;
import com.blockchain.platform.utils.BizUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.NotifyDTO;
import com.blockchain.platform.pojo.entity.SecondsContractOrderEntity;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.websocket.NettyWebSocket;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 交易界面控制器
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-11 3:43 PM
 **/
@RestController
@RequestMapping("/trade")
public class TradeController extends BaseController {

    /**
     * 配置信息
     */
    @Resource
    private RedisPlugin redisPlugin;


    @Resource
    private ISocketService socketService;

    /**
     * 币币交易
     * @param dto
     * @return
     */
    @RequestMapping("/history")
    public ResponseData trade(@RequestBody BaseDTO dto, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if (StrUtil.isEmpty( dto.getSymbol())) {
                throw ExUtils.error( LocaleKey.SYS_PARAM_ERROR);
            }

            // 通知数据
            WsVO vo = new WsVO();

            // ticker
            RankingVO ticker = socketService.findWsTickerByCondition( dto.getSymbol());
            vo.setTick( ticker);

            // 历史记录
            List<DealVO> deals = socketService.findWsHistoryByCondition( dto.getSymbol());
            vo.setHistory( deals);


            // 买卖盘数据
            List<Map<String, List<OrderVO>>> handicap = socketService.findWsKByCondition( dto.getSymbol());

            vo.setSells( handicap.get(1));

            // 买
            vo.setBuys(  handicap.get(0));


            UserDTO user = getLoginUser( request);
            if ( ObjectUtil.isNotEmpty( user)) {
                // 获取我的委托订单
                PageVO actives = socketService.findWsActivesByCondition(dto.getSymbol(), StrUtil.toString( user.getId()));
                vo.setActives( actives);


                // 获取我的钱包信息
                Map<String, CapitalVO> capital = socketService.findWsCapitalByCondition(dto.getSymbol(), StrUtil.toString( user.getId()));
                vo.setCapital( capital);
            }
            data.setData( vo);
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 合约kline数据
     * @param dto
     * @return
     */
    @RequestMapping("/sc/ticker")
    public ResponseData ticker(@RequestBody BaseDTO dto) {
        ResponseData data = ResponseData.ok();
        try {

            HuobiKLineEntity kline= (HuobiKLineEntity) DictionaryFactory.getMemoryDic(RedisConst.HUOBI_WS_KLINE,dto.getSymbol());
            data.setData(kline);
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }
}