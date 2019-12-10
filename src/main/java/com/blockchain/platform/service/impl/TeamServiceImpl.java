package com.blockchain.platform.service.impl;

import com.blockchain.platform.mapper.TeamMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.ITeamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 团队服务实现类
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-06 11:21 AM
 **/
@Service
public class TeamServiceImpl implements ITeamService {

    @Resource
    private TeamMapper mapper;

    @Override
    public ExtensionVO extension(BaseDTO dto) {
        return mapper.extension( dto);
    }
    
    @Override
    public ExtensionVO extensionZT(BaseDTO dto) {
        return mapper.extensionZT( dto);
    }

    @Override
    public List<ProfitVO> contract(BaseDTO dto) {
        return mapper.contract( dto);
    }

    @Override
    public ExtensionVO tradeQuota(BaseDTO dto) {
        return mapper.tradeQuota( dto);
    }
    
    @Override
    public ExtensionVO teamLockNum(BaseDTO dto) {
        return mapper.teamLockNum( dto);
    }
    
    @Override
    public List<ProfitVO> profit(BaseDTO dto) {
        return mapper.profit( dto);
    }

    @Override
    public List<RewardsVO> contracts(PageDTO dto) {
        return mapper.contracts( dto);
    }

    @Override
    public List<RewardsVO> profits(PageDTO dto) {
        return mapper.profits( dto);
    }

    @Override
    public List<TeamDetailVO> stage(PageDTO dto){
        return mapper.stage(dto);
    }
}
