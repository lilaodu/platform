package com.blockchain.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.QueryConst;
import com.blockchain.platform.mapper.UserWalletMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.DrawMoneyFlowEntity;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.TransferDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.dto.UserWalletDTO;
import com.blockchain.platform.pojo.entity.UnlockAssetsEntity;
import com.blockchain.platform.pojo.entity.UserAssetsFlowEntity;
import com.blockchain.platform.pojo.entity.UserWalletEntity;
import com.blockchain.platform.pojo.vo.CapitalVO;
import com.blockchain.platform.pojo.vo.DigVO;
import com.blockchain.platform.service.IUserWalletService;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.IntUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 用户钱包接口实现类
 */
@Service
public class UserWalletServiceImpl  extends ServiceImpl<UserWalletMapper, UserWalletEntity> implements IUserWalletService {
    @Resource
    private UserWalletMapper mapper;
    

    @Override
    public Boolean modify(UserWalletEntity entity) {
        return null;
    }

    public List<CapitalVO> getAssets(BaseDTO dto) {
        return mapper.queryCapital( dto);
    }
    
    public List<CapitalVO> getAssetsAll(BaseDTO dto) {
        return mapper.queryCapitalAll( dto);
    }
    

	public CapitalVO transferPending(UserDTO userDTO) {

        List<UserAssetsFlowEntity> flows = mapper.pendingFlow(userDTO);

        BigDecimal pending = BigDecimal.ZERO;
        for (UserAssetsFlowEntity userAssets:flows){
            //划入otc则增加冻结资金，划出则减少冻结资金
            if(StrUtil.equals(userAssets.getAddressTo(),BizConst.WalletConst.WALLET_TYPE_OTC)){
                pending = BigDecimalUtils.add(pending,userAssets.getAmount());
            }else {
                pending = BigDecimalUtils.subtr(pending,userAssets.getAmount());
            }
            //如果为负数，则当前冻结资金为0
            if(BigDecimalUtils.compare(BigDecimal.ZERO,pending)){
                pending = BigDecimal.ZERO;
            }
        }
        return CapitalVO.builder().frozenOtc(pending).build();
    }

	@Override
	public UserWalletEntity validateBalance(UserWalletDTO userWalletDTO) {
		
		return null;
	}

	@Override
	public List<UserAssetsFlowEntity> getAssetsFlow(PageDTO dto) {
		return mapper.queryAssetsFlow(dto);
	}

	@Override
	public List<UnlockAssetsEntity> queryUnlockTxFlow() {
		
		return null;
	}
	
	public UserWalletEntity queryUserWallet(UserWalletDTO walletDTO) {
		return mapper.validateBalance(walletDTO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean transferAssets(TransferDTO dto) {
		UserWalletDTO walletDTO = UserWalletDTO.builder()
                                            .userId(dto.getUserId())
                                            .symbol(dto.getSymbol()).build();
        //余额
        walletDTO.setBalance( dto.getAmount());
        //冻结金额
        walletDTO.setFrozenBalance(BigDecimal.ZERO);
        int num = 0;
        //判断划转源账户类型
        switch (dto.getAccountFrom()){
            //法币账户
            case BizConst.WalletConst.WALLET_TYPE_T :
                //直接扣除钱包账户余额
                num = mapper.updateTAssets(walletDTO);
                break;
            //合约账户
            case BizConst.WalletConst.WALLET_TYPE_OTC :
                //直接扣除OTC账户余额
                num = mapper.updateOTCAssets(walletDTO);
                break;
            //币币账户
            case BizConst.WalletConst.WALLET_TYPE_TRADE :
                //直接扣除币币账户余额
                num = mapper.updateAssets(walletDTO);
                break;
        }
        if (num > 0 ){
            walletDTO.setBalance(dto.getAmount().negate());
            walletDTO.setFrozenBalance(BigDecimal.ZERO);
            //判断划转目标账户类型
            switch (dto.getAccountTo()){
                //法币账户
                case BizConst.WalletConst.WALLET_TYPE_T :
                    //直接更新钱包账户余额
                    num = mapper.updateTAssets(walletDTO);
                    break;
                //合约账户
                case BizConst.WalletConst.WALLET_TYPE_OTC :
                    //直接更新OTC账户余额
                    num = mapper.updateOTCAssets(walletDTO);
                    break;
                //币币账户
                case BizConst.WalletConst.WALLET_TYPE_TRADE :
                    //直接更新币币账户余额
                    num =  mapper.updateAssets(walletDTO);
                    break;
            }
        }
        if (num >  0) {
            //插入流水
            num = mapper.addAssetsFlow( UserAssetsFlowEntity.builder()
                    .userId( dto.getUserId())  //用户id
                    .addressFrom(dto.getAccountFrom())  //划转源账户类型
                    .addressTo(dto.getAccountTo())  //划转目标类型
                    .amount(dto.getAmount())   //划转金额
                    .symbol(dto.getSymbol())   //划转货币
                    .type( BizConst.AssetsConst.ASSETS_FLOW_TYPE_TRANSFER)  //类型
                    .state( BizConst.AssetsConst.STATE_COMPLETE)  //状态
                    .build());
        }
		return num > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addAssetsFlow(UserAssetsFlowEntity entity, DrawMoneyFlowEntity flowEntity) {
		int k = 0;
        Map<String, Object> map = CollUtil.newHashMap();
        // 冻结资产
        if(StrUtil.equals(BizConst.AssetsConst.ASSETS_FLOW_TYPE_OUTS, entity.getType())) {
            // 两种情况 symbol = feeSymbol
            if(StrUtil.equals( entity.getFeeSymbol(), entity.getSymbol())) {
                map.put(QueryConst.QUERY_NUM, entity.getAmount());
                map.put(QueryConst.QUERY_USER_ID, entity.getUserId());
                map.put(QueryConst.QUERY_SYMBOL, entity.getSymbol());
                map.put(QueryConst.QUERY_TYPE, entity.getAddressFrom());
            } else {
                // 冻结基础转出货币
                map.put(QueryConst.QUERY_NUM, entity.getAmount());
                map.put(QueryConst.QUERY_USER_ID, entity.getUserId());
                map.put(QueryConst.QUERY_SYMBOL, entity.getSymbol());
                map.put(QueryConst.QUERY_TYPE, entity.getAddressFrom());
                mapper.updateWalletByOuts(map);

                // 冻结手续费
                map.clear();
                map.put(QueryConst.QUERY_NUM, entity.getOutFee());
                map.put(QueryConst.QUERY_USER_ID, entity.getUserId());
                map.put(QueryConst.QUERY_SYMBOL, entity.getFeeSymbol());
                map.put(QueryConst.QUERY_TYPE, entity.getAddressFrom());
            }
            k = mapper.updateWalletByOuts(map);
        }
        // 添加申请流水
        k = mapper.addAssetsFlow(entity);
        if( k > 0){
            // 添加工作流水
            flowEntity.setFlowId( entity.getId());
            k = mapper.addWorkFlow(flowEntity);
        }
        return k > 0;
	}

	@Override
	public DigVO dig(UserDTO userDTO) {
		return mapper.dig(userDTO);
	}

    @Override
    public UserWalletEntity findByCondition(BaseDTO dto) {
        return mapper.findByCondition( dto);
    }

    @Override
    public Boolean updateUserWallet(UserWalletDTO dto) {
        return mapper.updateUserWallet( dto) > 0;
    }

}
