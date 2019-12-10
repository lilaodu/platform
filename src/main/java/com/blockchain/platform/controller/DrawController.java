package com.blockchain.platform.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.annotation.DuplicateVerify;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.enums.Method;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.IDrawService;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 抽奖控制器
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-13 6:11 PM
 **/
@RestController
@RequestMapping("/draw")
public class DrawController extends BaseController {

    /**
     * 抽奖服务接口
     */
    @Resource
    private IDrawService drawService;

    /**
     * 获取抽奖列表
     * @return
     */
    @RequestMapping("/query")
    public ResponseData query(){
        ResponseData data = ResponseData.ok();
        try {
            //获取所有有效的抽奖活动
            List<LuckDrawVO> list = drawService.query( BaseDTO.builder()
                                                .state(BizConst.BIZ_STATUS_VALID).build());
            //返回数据
            data.setData( list);
        }catch (Exception ex){
            ExUtils.error( ex ,LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 获取抽奖活动的具体奖项
     * @param dto
     * @return
     */
    @RequestMapping("/list")
    public ResponseData list(@RequestBody BaseDTO dto){
        ResponseData data = ResponseData.ok();
        try {
            //有效的奖项
            dto.setState( BizConst.BIZ_STATUS_VALID);
            //查询
            List<LuckDrawConfigVO> list = drawService.list( dto);
            if( CollUtil.isEmpty( list)){
                throw ExUtils.error( LocaleKey.DRAW_NOT_FIND);
            }
            //返回数据
            data.setData( list);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 获取抽奖活动的抽奖记录(抽中)
     * @param dto
     * @return
     */
    @RequestMapping("/log")
    public ResponseData log(@RequestBody PageDTO dto){
        ResponseData data = ResponseData.ok();
        try {
            //获取抽中的记录
            dto.setType(AppConst.FIELD_Y);
            //分页对象（不分页）
            Page<LuckDrawLogVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //获取活动的抽奖记录
            List<LuckDrawLogVO> list = drawService.log( dto);
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
     * 获取抽奖活动的抽奖记录(抽中)
     * @param dto
     * @return
     */
    @RequestMapping("/history")
    public ResponseData history(@RequestBody DrawWsDTO dto){
        ResponseData data = ResponseData.ok();
        try {
            List<LuckDrawLogVO> list = drawService.history( BaseDTO.builder()
                    .id( dto.getDrawId())   //抽奖id
                    .type( AppConst.FIELD_Y)   //类型（只查询中奖记录）
                    .limit( AppConst.DEFAULT_PAGE_SIZE).build());  //只查询多少条
            //返回数据
            WinningVO vo = new WinningVO();
            //是否中奖
            vo.setPrize( AppConst.FIELD_N);
            //是否是自己的记录
            vo.setOwn( Boolean.FALSE);
            //抽奖记录
            List< List<Object>> vos = new ArrayList<>();
            for(LuckDrawLogVO luckDrawLogVO : list){
                //抽奖记录
                LinkedList<Object> bullet = CollUtil.newLinkedList();
                // 手机号码
                bullet.add( luckDrawLogVO.getUsername());
                //中奖数量
                bullet.add( luckDrawLogVO.getNum());
                //中奖代币
                bullet.add( luckDrawLogVO.getSymbol());
                //中奖时间
                bullet.add( DateUtil.format( luckDrawLogVO.getTime(), DatePattern.NORM_DATETIME_FORMAT));
                vos.add( bullet);
            }
            vo.setBullet( vos);
            data.setData( vo);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 我的奖品
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/own")
    public ResponseData own(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //抽中的记录
            dto.setType( AppConst.FIELD_Y);
            //开始分页
            Page<LuckDrawLogVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //列表
            List<LuckDrawLogVO> list = drawService.log( dto);
            //返回数据
            data.setData( PageVO.builder()
                            .list( list)
                            .total( page.getTotal()).build());
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 获取抽奖次数，kyc次数，秒合约次数
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/info")
    public ResponseData info(@RequestBody BaseDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //统计用户抽奖次数
            PrizeVO vo = drawService.count( dto);
            //返回数据
            data.setData( vo);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }


    /**
     * 抽奖
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/prize")
    @DuplicateVerify(method = Method.DRAW_PRIZE)
    public ResponseData prize(@RequestBody BaseDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //有效的活动
            dto.setState( BizConst.BIZ_STATUS_VALID);
            //获取抽奖活动详情
            List<LuckDrawVO> list = drawService.query( dto);
            if(CollUtil.isEmpty( list)){
                throw ExUtils.error( LocaleKey.DRAW_NOT_FIND);
            }
            //设置活动单日总量
            dto.setNum( list.get(IntUtils.INT_ZERO).getDayAmount());
            //用户名称
            dto.setName( user.getUsername());
            //判断用户是否有抽奖次数
            PrizeVO prizeVO = drawService.count( dto);
            if( IntUtils.compare( IntUtils.INT_ZERO, prizeVO.getNum())){
                throw ExUtils.error( LocaleKey.DRAW_NUMBER_IS_ZERO);
            }
            //开始抽奖
            DrawDTO draw = new DrawDTO();
            draw.setId( dto.getId());
            draw.setUserId( user.getId());
            draw.setName( StrUtil.isEmpty( user.getMobile()) ? user.getEmail() : user.getMobile());
            WinningVO vo=  drawService.doPrize(draw);
            data.setData(vo);
            //返回数据
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
}
