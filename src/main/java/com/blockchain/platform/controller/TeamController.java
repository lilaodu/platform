package com.blockchain.platform.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.ITeamService;
import com.blockchain.platform.service.IUserService;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 团队控制器
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-05 8:17 PM
 **/
@RestController
@RequestMapping("/team")
public class TeamController extends BaseController {

    /**
     * 用户服务接口
     */
    @Resource
    private IUserService userService;

    /**
     * 团队服务接口
     */
    @Resource
    private ITeamService teamService;

    /**
     * 团队个人推广
     * @return
     */
    @RequestMapping("/extension")
    public ResponseData extension(HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户详情
            UserEntity entity = userService.findUserByCondition( user);
            //获取团队信息
            ExtensionVO vo = teamService.extension( BaseDTO.builder()
                                            .userId( user.getId())
                                            .authority( entity.getAuthority()).build());
            //获取团队直推信息
            ExtensionVO voZT = teamService.extensionZT( BaseDTO.builder()
                                            .userId( user.getId())
                                            .authority( entity.getAuthority()).build());
            //团队总交易额
            ExtensionVO tradeQuota = teamService.tradeQuota(BaseDTO.builder()
                    .userId( user.getId())
                    .authority( entity.getAuthority()).build());
            
            //昨日总业绩
            ExtensionVO teamLockNum = teamService.teamLockNum(BaseDTO.builder()
                    .userId( user.getId())
                    .authority( entity.getAuthority()).build());
            
            vo.setRcSum(voZT.getRcSum());
            vo.setDcSum(voZT.getDcSum());
            vo.setAcSum(voZT.getAcSum());
            vo.setCjSum(voZT.getCjSum());
            vo.setZjSum(voZT.getZjSum());
            vo.setGjSum(voZT.getGjSum());
            vo.setZcSum(voZT.getZcSum());
            vo.setTradeQuota(tradeQuota.getTradeQuota());
            vo.setTeamLockNum(teamLockNum.getTeamLockNum());
            
            //返回数据
            data.setData( vo);
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 秒合约昨日收益
     * @return
     */
    @RequestMapping("/contract")
    public ResponseData contract(HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //秒合约昨日收益
            List<ProfitVO> list = teamService.contract( BaseDTO.builder()
                                                .userId( user.getId())
                                                .state( BizConst.BIZ_STATUS_VALID).build());
            //返回数据
            data.setData( list);
        } catch (Exception ex) {

        }
        return data;
    }

    /**
     * 星球计划昨日收益
     * @return
     */
    @RequestMapping("/profit")
    public ResponseData profit(HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //星球计划昨日收益
            List<ProfitVO> vos = teamService.profit( BaseDTO.builder()
                                        .userId( user.getId())
                                        .state(BizConst.BIZ_STATUS_VALID).build());
            //返回数据
            data.setData( vos);
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 秒合约收益明细
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/contracts")
    public ResponseData contracts(@RequestBody PageDTO dto, HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //开始分页
            Page<RewardsVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //列表
            List<RewardsVO> list = teamService.contracts( dto);
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
     * 星球计划在在收益明细
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/profits")
    public ResponseData profits(@RequestBody PageDTO dto,HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
            //登录用户
            UserDTO user = getLoginUser( request);
            //用户id
            dto.setUserId( user.getId());
            //开始分页
            Page<RewardsVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //列表
            List<RewardsVO> list = teamService.profits( dto);
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
     * 我的团队
     * 二级
     * @param dto
     * @return
     */
    @RequestMapping("/stage")
    public ResponseData team(@RequestBody PageDTO dto,HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            UserDTO user = getLoginUser( request);
            if (ObjectUtil.isEmpty( user)) {
                throw ExUtils.error( LocaleKey.LOGON_HAS_EXPIRED);
            }
            dto.setUserId( user.getId());

            List<TeamDetailVO> array = CollUtil.newArrayList();

            List<TeamDetailVO> list = teamService.stage( dto);

            // 数组长度
//            Long size = IntUtils.toLong( list.size() > 0 ? (list.size() - 1) : 0);
            Long size = IntUtils.toLong( list.size() > 0 ? list.size() : 0);

            if ( CollUtil.isNotEmpty( list)) {

                int start = (dto.getPageNum() - 1) * dto.getPageSize();

                if ( start < size) {

                    int end = dto.getPageSize() * dto.getPageNum();
                    if ( end >= size) {
                        end = IntUtils.toInt( size) ;
                    }
                    array = list.subList( start, end);
                }
            }
            data.setData( PageVO.builder().total( size).list( array).build());
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }
}
