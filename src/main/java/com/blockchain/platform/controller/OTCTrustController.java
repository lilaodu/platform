package com.blockchain.platform.controller;

import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.TrustDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.pojo.vo.OTCTrustVO;
import com.blockchain.platform.service.IOTCTrustService;
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


/**
 * OTC信任管理控制器
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-19 9:11 AM
 **/
@RestController
@RequestMapping("/otc/trust")
public class OTCTrustController extends BaseController {

    /**
     * 信任管理服务接口
     */
    @Resource
    private IOTCTrustService trustService;

    /**
     * 信任管理 - 信任我的/我信任的/我屏蔽的
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping("/query")
    public ResponseData query(@RequestBody PageDTO dto, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            //登录对象
            UserDTO user = getLoginUser(request);
            //设置用户id
            dto.setUserId(user.getId());
            //有效的数据
            dto.setState(BizConst.BIZ_STATUS_VALID);
            //开始分页
            Page<OTCTrustVO> page = PageHelper.startPage( dto.getPageNum(), dto.getPageSize());
            //列表信息
            List<OTCTrustVO> list = trustService.query(dto);
            //返回数据
            data.setData(PageVO.builder()
                    .total(page.getTotal())
                    .list(list).build());
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 添加 | 取消（信任/屏蔽用户 | 举报广告）
     *
     * @param dto
     * @return
     */
    @RequestMapping("/modify")
    public ResponseData modify(@RequestBody @Valid TrustDTO dto, BindingResult valid, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        try {
            if (valid.hasErrors()) {
                throw ExUtils.error(valid.getFieldError().getDefaultMessage());
            }
            //登录对象
            UserDTO user = getLoginUser(request);
            //设置用户id
            dto.setUserId(user.getId());
            //（信任/屏蔽）用户 | 举报广告
            Boolean bool = trustService.modify( dto);
            if (!bool) {
                throw ExUtils.error(LocaleKey.SYS_OPERATE_FAILED);
            }
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_OPERATE_FAILED);
        }
        return data;
    }
}
