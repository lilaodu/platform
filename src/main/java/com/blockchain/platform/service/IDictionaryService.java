package com.blockchain.platform.service;

import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.*;
import java.util.List;

/**
 * 字典接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 11:29 AM
 **/
public interface IDictionaryService {

	/**
     * 缓存数据
     * @param dto
     * @return
     */
    List<CoinEntity> token(BaseDTO dto);
    
    /**
     * 首页banner配置信息
     * @param dto
     * @return
     */
    List<BannerVO> banner(BaseDTO dto);

    /**
     * 通知信息
     * @param dto
     * @return
     */
    List<NoticeVO> notice(BaseDTO dto);

    /**
     * 文章信息
     * @param dto
     * @return
     */
    List<ArticleVO> article(BaseDTO dto);

    /**
     * 货币配置信息
     * @param dto
     * @return
     */
    List<CoinEntity> coin(BaseDTO dto);

    /**
     * 交易对信息
     * @param dto
     * @return
     */
    List<MarketCoinEntity> pairs(BaseDTO dto);

    /**
     * 初始化 交易排行
     * @param dto
     * @return
     */
    List<RankingVO> ranking(BaseDTO dto);
    
    /**
     * 参数信息
     * @param dto
     * @return
     */
    List<ParamsVO> params(BaseDTO dto);
    
    /**
     * 秒合约信息接口
     */
    List<SecondsContractEntity> secondsContract(BaseDTO dto);

    /**
     * 市场配置
     * @param dto
     * @return
     */
    List<MarketConfigEntity> market(BaseDTO dto);

    /**
     * 获取抽奖活动
     * @param dto
     * @return
     */
    List<LuckDrawVO> draw(BaseDTO dto);

    /**
     * 字典信息
     * @param dto
     * @return
     */
    List<String> dict(BaseDTO dto);

    /**
     * 广告列表
     * @param dto
     * @return
     */
    List<OTCAdvertVO> advert(BaseDTO dto);

    /**
     * 钱包配置
     * @param dto
     * @return
     */
    List<WalletConfigEntity> wallet(BaseDTO dto);

    /**
     * 商户列表
     * @param dto
     * @return
     */
    List<String> merchants(BaseDTO dto);


    /**
     * 升级
     * @return
     */
    List<UserUpgradeEntity> upgrade();
}
