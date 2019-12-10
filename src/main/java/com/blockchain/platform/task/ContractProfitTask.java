package com.blockchain.platform.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.constant.UpgradeConst;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.TaskDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.entity.UserLockProfitEntity;
import com.blockchain.platform.pojo.entity.UserTradeProfitEntity;
import com.blockchain.platform.pojo.entity.UserUpgradeEntity;
import com.blockchain.platform.pojo.vo.ContractProfitVO;
import com.blockchain.platform.pojo.vo.UnLockProfitVO;
import com.blockchain.platform.service.ITaskService;
import com.blockchain.platform.service.IUserService;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 合约收益定时任务
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-09 2:01 PM
 **/
@Component
public class ContractProfitTask {


    /**
     * 缓存记录
     */
    @Resource
    private RedisPlugin redisPlugin;

    /**
     * 合约交易服务
     */
    @Resource
    private ITaskService taskService;

    /**
     * 用户对象
     */
    @Resource
    private IUserService userService;

    /**
     * 解锁合约交易收益
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void profit() {

        TaskDTO dto = TaskDTO.builder().build();
        // 昨日数据
        dto.setTime( DateUtil.format( DateUtil.yesterday(), DatePattern.PURE_DATE_PATTERN));

        List<ContractProfitVO> list = taskService.findContractProfitData( dto);

        if (CollUtil.isNotEmpty( list)) {

            // 获取秒合约配置
            Map<String, UserUpgradeEntity> config = redisPlugin.hget(RedisConst.PLATFORM_UPGRADE_CONFIG,
                                                        BizConst.UpgradeConst.UPGRADE_TYPE_CONTRACT);

            /**
             * 最大收益
             */
            BigDecimal maxTeamProfit = getMaxTeamProfit( config);

            for ( int idx = 0;idx < list.size();idx ++ ) {

                ContractProfitVO vo = list.get( idx);


                UserEntity entity = userService.findUserByCondition(UserDTO.builder().state( BizConst.BIZ_STATUS_VALID).id( vo.getUserId()).build());

                // 开始计算
                if ( ObjectUtil.isNotEmpty( entity)) {
                    recursion( entity, vo, vo.getAmount(), StrUtil.EMPTY, config, maxTeamProfit);
                }
            }
        }
    }

    /**
     * 计算最高团队收益
     * @param config
     * @return
     */
    protected static BigDecimal getMaxTeamProfit(Map<String, UserUpgradeEntity> config) {
        BigDecimal profit = BigDecimal.ZERO;

        for (Map.Entry<String, UserUpgradeEntity> entry : config.entrySet()) {

            if ( BigDecimalUtils.compareTo( entry.getValue().getTeamProfit(), profit)) {
                profit = entry.getValue().getTeamProfit();
            }
        }
        return profit;
    }


    /**
     * 开始递归
     * @param entity
     * @param vo
     * @param profit
     * @param lastRank
     * @param config
     */
    public void recursion(UserEntity entity,
                          ContractProfitVO vo,
                          BigDecimal profit,
                          String lastRank,
                          Map<String, UserUpgradeEntity> config,
                          BigDecimal maxTeamProfit) {

        // 当前收益配置
        UserUpgradeEntity upgrade = config.get( BizUtils.getRankValue( entity.getRank()));

        // 本次总收益
        BigDecimal total = BigDecimal.ZERO;

        if ( ObjectUtil.isNotEmpty( upgrade)) {

            // 本次额度
            BigDecimal amount = vo.getAmount();


            // 计算团队收益
            // 自己也算自己的团队
            BigDecimal ownTotal = BigDecimal.ZERO;

            BigDecimal ownProfit = upgrade.getTeamProfit();

            if ( IntUtils.equals( entity.getId(), vo.getUserId())
                    && BigDecimalUtils.compareTo(ownProfit, BigDecimal.ZERO)) {
                ownTotal = BigDecimalUtils.multi( ownProfit, amount);

                // 已分的部分奖励
                vo.setGradationProfit( ownProfit);
            }


            // 计算级差奖励
            BigDecimal equalityTotal = BigDecimal.ZERO;
            BigDecimal sameProfit = upgrade.getSameProfit();
            if ( StrUtil.equals( lastRank, entity.getRank())
                    && !IntUtils.equals( entity.getId(), vo.getUserId())
                    && BigDecimalUtils.compareTo( sameProfit, BigDecimal.ZERO)
                    && BigDecimalUtils.compareTo( profit, BigDecimal.ZERO)) {

                // 平级奖励 解锁 * 团队 * 平级
                equalityTotal = BigDecimalUtils.multi(profit, sameProfit);
            }


            // 计算直推奖励
            BigDecimal directTotal = BigDecimal.ZERO;

            // 计算团队奖励
            BigDecimal teamTotal = BigDecimal.ZERO;


            if ( !IntUtils.equals( entity.getId(), vo.getUserId())) {

                BigDecimal directProfit = upgrade.getDirectProfit();

                if ( IntUtils.equals( entity.getId(), vo.getParentId())
                        && BigDecimalUtils.compareTo( directProfit, BigDecimal.ZERO)) {
                    directTotal = BigDecimalUtils.multi( directProfit, amount);
                }


                BigDecimal teamProfit = upgrade.getTeamProfit();

                // 是否有级差判断
                Boolean gradation = !IntUtils.isZero( upgrade.getGradation());
                if ( gradation) {

                    // 当前收益等级
                    int keep = IntUtils.toInt( BizUtils.getRankValue( entity.getRank()));

                    int last = IntUtils.toInt( BizUtils.getRankValue( lastRank));

                    if ( keep > last) {

                        BigDecimal gradationProfit = vo.getGradationProfit();
                        if ( BigDecimalUtils.isZero( gradationProfit)) {
                            gradationProfit = teamProfit;
                        } else {
                            gradationProfit = BigDecimalUtils.subtr( teamProfit, gradationProfit);
                        }
                        vo.setGradationProfit( teamProfit);
                        // 上限
                        if ( BigDecimalUtils.compareTo( gradationProfit, BigDecimal.ZERO)) {
                            teamTotal = BigDecimalUtils.multi( gradationProfit, amount);
                        }
                    }
                } else {
                    teamTotal = BigDecimalUtils.multi( teamProfit, amount);
                }
            }
            // 总收益
            total = BigDecimalUtils.sCale(NumberUtil.add( ownTotal, equalityTotal, directTotal, teamTotal),
                                            AppConst.ORDER_NUMBER_LENGTH);
        }

        if ( BigDecimalUtils.compareTo( total, BigDecimal.ZERO)) {
            // 记录数据
            UserTradeProfitEntity tradeProfitEntity = new UserTradeProfitEntity();

            tradeProfitEntity.setAmount( total);

            tradeProfitEntity.setUserId( entity.getId());

            tradeProfitEntity.setProductUserId( vo.getUserId());

            tradeProfitEntity.setSymbol( vo.getSymbol());

            tradeProfitEntity.setType( BizConst.ProfitConst.TRADE_PROFIT_1);

            taskService.addTradeProfitEntity(  tradeProfitEntity);
        }

        // 递归收益
        if (IntUtils.greaterThanZero( entity.getParentId())) {

            UserEntity parent = findValidUser( entity.getParentId());
            recursion( parent, vo, total, entity.getRank(), config, maxTeamProfit);
        }
    }


    /**
     * 查找有效用户
     * @param id
     * @return
     */
    public UserEntity findValidUser(Integer id) {

        UserEntity entity = userService.findUserByCondition( UserDTO.builder().state( -1).id( id).build());

        if ( IntUtils.equals( entity.getState(), BizConst.BIZ_STATUS_VALID)) {
            return entity;
        }
        return findValidUser( entity.getParentId());
    }

}
