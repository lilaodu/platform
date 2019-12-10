package com.blockchain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典数据接口
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-18 1:12 PM
 **/
@Mapper
public interface DictionaryMapper {

	/**
     * 缓存数据
     * @param dto
     * @return
     */
    List<CoinEntity> token(BaseDTO dto);
	
    /**
     * banner配置
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
     * 参数信息
     * @param dto
     * @return
     */
    List<ParamsVO> params(BaseDTO dto);
    
    /**
     * 秒合约配置信息
     * @param dto
     * @return
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
     * 查询商户订单
     * @param dto
     * @return
     */
    List<String> merchants(BaseDTO dto);


    /**
     * 配置新
     * @param dto
     * @return
     */
    List<UserUpgradeEntity> upgrade(BaseDTO dto);
}
