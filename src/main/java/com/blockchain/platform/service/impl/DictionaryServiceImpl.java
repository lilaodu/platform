package com.blockchain.platform.service.impl;

import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.mapper.DictionaryMapper;
import com.blockchain.platform.mapper.OrderFlowMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.RankingDTO;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.IDictionaryService;
import cn.hutool.core.bean.BeanUtil;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理接口实现类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:51 AM
 **/
@Service
public class DictionaryServiceImpl  implements IDictionaryService {

    /**
     * 字典数据库接口
     */
    @Resource
    private DictionaryMapper mapper;
    
    /**
     * 订单数据接口
     */
    @Resource
    private OrderFlowMapper orderMapper;

    @Override
    public List<BannerVO> banner(BaseDTO dto) {
        return mapper.banner( dto);
    }

    @Override
    public List<NoticeVO> notice(BaseDTO dto) {
        return mapper.notice( dto);
    }

    @Override
    public List<ArticleVO> article(BaseDTO dto) {
        return mapper.article( dto);
    }

    @Override
    public List<CoinEntity> coin(BaseDTO dto) {
        return mapper.coin( dto);
    }

    @Override
    public List<MarketCoinEntity> pairs(BaseDTO dto) {
        return mapper.pairs( dto);
    }

    @Override
    public List<ParamsVO> params(BaseDTO dto) {
        return mapper.params( dto);
    }
    
    public List<SecondsContractEntity> secondsContract(BaseDTO dto) {
        return mapper.secondsContract( dto);
    }

    @Override
    public List<MarketConfigEntity> market(BaseDTO dto) {
        return mapper.market( dto);
    }

    @Override
    public List<LuckDrawVO> draw(BaseDTO dto) {
        return mapper.draw( dto);
    }

    @Override
    public List<String> dict(BaseDTO dto) {
        return mapper.dict( dto);
    }

    @Override
    public List<OTCAdvertVO> advert(BaseDTO dto) {
        return mapper.advert( dto);
    }

    @Override
    public List<WalletConfigEntity> wallet(BaseDTO dto) {
        return mapper.wallet( dto);
    }

    @Override
    public List<CoinEntity> token(BaseDTO dto) {
        return mapper.token( dto);
    }
    
    public List<RankingVO> ranking(BaseDTO dto){
        List<RankingVO> vos = new ArrayList<>();
        // 实际数据
        List<RankingDTO> ranking = orderMapper.queryTokenRanking( dto);
        // 格式化数据
        for (int idx = 0;idx < ranking.size(); idx ++) {
            RankingDTO rankingDTO = ranking.get( idx);
            // 创建
            RankingVO vo = BeanUtil.toBean( rankingDTO.getTick(), RankingVO.class);
            // 复制其他属性
            BeanUtil.copyProperties( rankingDTO, vo);
            vos.add( vo);
        }
        return vos;
    }

    @Override
    public List<String> merchants(BaseDTO dto) {
        return mapper.merchants(dto);
    }

    @Override
    public List<UserUpgradeEntity> upgrade() {
        BaseDTO dto = new BaseDTO();
        dto.setState(BizConst.BIZ_STATUS_VALID);
        return mapper.upgrade( dto);
    }
}
