package com.blockchain.platform.task;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.TaskDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.blockchain.platform.pojo.entity.UserLockProfitEntity;
import com.blockchain.platform.pojo.entity.UserUpgradeEntity;
import com.blockchain.platform.pojo.vo.UnLockProfitVO;
import com.blockchain.platform.service.ITaskService;
import com.blockchain.platform.service.IUserService;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 解锁收益任务
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-07 11:19 AM
 **/
@Component
public class UnLockProfitTask {

    /**
     * 缓存引用
     */
    @Resource
    private RedisPlugin redisPlugin;

    /**
     * 代币任务接口
     */
    @Resource
    private ITaskService taskService;

    @Resource
    private IUserService userService;

    /**
     * 解锁星球计划收益
     */
    @Scheduled(cron = "* * 0 * * ?")
    public void profit() {

        // 升级收益配置信息
        Map<String, UserUpgradeEntity> config = redisPlugin.hget( RedisConst.PLATFORM_UPGRADE_CONFIG,
                                                BizConst.UpgradeConst.UPGRADE_TYPE_LOCK);

        if ( ObjectUtil.isNotEmpty( config)) {

            // 获取昨日解锁用户收益
            TaskDTO dto = TaskDTO.builder().build();
            // 昨日数据
            dto.setTime( DateUtil.format( DateUtil.yesterday(), DatePattern.PURE_DATE_PATTERN));

            List<UnLockProfitVO> list = taskService.findUnLockProfitData(dto);

            for (int idx = 0;idx < list.size();idx ++) {
                UnLockProfitVO vo = list.get( idx);

                UserEntity entity = userService.findUserByCondition(UserDTO.builder().id( vo.getUserId()).build());

                // 开始计算收益
                // 第一 上次未处理
                recursion( entity, vo, vo.getUnLockTotal(), -1, config);
            }
        }

    }

    /**
     * 递归计算当前用户收益
     * @param config
     * @param vo 实际产生余额的用户
     * @param profit 本次解锁 or 收益
     * @param entity 实际产生收益用户
     */
    public void recursion(UserEntity entity,
                          UnLockProfitVO vo,
                          BigDecimal profit,
                          Integer lastLv,
                          Map<String, UserUpgradeEntity> config) {

        // consume 收益等级
        Integer consume = entity.getLockLv();

        // 收益配置
        UserUpgradeEntity upgradeEntity = config.get( StrUtil.toString( consume));

        // 开始计算当前用户收益
        if ( ObjectUtil.isNotEmpty( upgradeEntity)) {

            // 本次解锁数量
            BigDecimal unLockTotal = vo.getUnLockTotal();

            /**
             * 计算自己的收益
             */
            BigDecimal teamProfit = upgradeEntity.getTeamProfit();

            BigDecimal ownTotal = BigDecimal.ZERO;
            if ( IntUtils.equals(entity.getId(), vo.getUserId())
                    && BigDecimalUtils.compareTo( teamProfit, BigDecimal.ZERO)) {
                ownTotal = BigDecimalUtils.multi( unLockTotal, teamProfit);
                vo.setGradationRate( teamProfit);
            }

            // 计算平级奖励
            // 平级奖励只针对直推
            // 收益 * 比例
            BigDecimal equalityTotal = BigDecimal.ZERO;
            if ( IntUtils.equals( vo.getLockLv(), consume)
                    && IntUtils.equals( entity.getLockLv(), lastLv)
                    && BigDecimalUtils.compareTo( teamProfit, BigDecimal.ZERO)) {

                BigDecimal sameProfit = upgradeEntity.getSameProfit();
                // 平级奖励 解锁 * 团队 * 平级
                equalityTotal = BigDecimalUtils.multi(profit, sameProfit);
            }

            /**
             * 收益对象 <> 产生对象
             */
            // 团队收益
            BigDecimal teamTotal = BigDecimal.ZERO;

            // 直推收益
            BigDecimal directTotal = BigDecimal.ZERO;

            if ( !IntUtils.equals( entity.getId(), vo.getUserId())) {
                // 直推比例
                BigDecimal directProfit = upgradeEntity.getDirectProfit();

                // 享受代数
                int layer = upgradeEntity.getDirectLayer();

                int curLayer = getCurUserLayer( vo.getAuthority(), entity.getId());

                // 当前代数 必须为下级
                // 当前代数 必须在直推访问内
                // layer < 0 说明均可享受
                if ( curLayer > 0 && ( layer < 0 || layer >= curLayer)
                        && BigDecimalUtils.compareTo(directProfit, BigDecimal.ZERO)) {
                    directTotal = BigDecimalUtils.multi( directProfit, unLockTotal);
                }

                // 是否有级差判断
                Boolean gradation = !IntUtils.isZero( upgradeEntity.getGradation());

                if (gradation) {

                    /**
                     * 产生等级 <= 收益等级
                     * 生产对应收益
                     */
                    if ( vo.getLockLv() < consume) {

                        BigDecimal gradationProfit = vo.getGradationRate();
                        if ( BigDecimalUtils.isZero( gradationProfit)) {
                            gradationProfit = teamProfit;
                        } else {
                            gradationProfit = BigDecimalUtils.subtr( upgradeEntity.getTeamProfit(), gradationProfit);
                        }
                        vo.setGradationRate( teamProfit);
                        // 上限
                        if ( BigDecimalUtils.compareTo( gradationProfit, BigDecimal.ZERO)) {
                            teamTotal = BigDecimalUtils.multi( gradationProfit, unLockTotal);
                        }
                    }
                } else {
                    // 不开启级差 直接计算
                    teamTotal = BigDecimalUtils.multi( teamProfit, unLockTotal);
                }

                // 总收益
                BigDecimal total = BigDecimalUtils.sCale( NumberUtil.add(equalityTotal, teamTotal, directTotal, ownTotal),
                        BizConst.TradeConst.BIGDECIMAL_MAX_LENGTH);

                if ( BigDecimalUtils.compareTo( total, BigDecimal.ZERO)) {
                    // 记录数据
                    UserLockProfitEntity lockProfitEntity = new UserLockProfitEntity();

                    lockProfitEntity.setAmount( total);

                    lockProfitEntity.setUserId( entity.getId());

                    lockProfitEntity.setProductUserId( vo.getUserId());

                    // 产生数据
                    lockProfitEntity.setProductId( vo.getProductId());

                    lockProfitEntity.setSymbol( BizConst.BASE_TOKEN);

                    taskService.addLockProfitEntity(  lockProfitEntity);
                }
                UserEntity parent = userService.findUserByCondition( UserDTO.builder().id( entity.getParentId()).build());
                if ( ObjectUtil.isNotEmpty( parent)) {
                    recursion( parent, vo, total, entity.getLockLv(),config);
                }
            }
        }
    }

    /**
     * 获取当前用户是第几代推广
     * @param authority
     * @param id
     * @return
     */
    private static int getCurUserLayer(String authority, Integer id) {
        // 再次验证是否为本代用户
        String ca = StrUtil.concat(Boolean.FALSE, StrUtil.toString(id), StrUtil.UNDERLINE);
        if ( StrUtil.containsAny( authority, ca)) {
            return -1;
        }
        // 分级
        List<String> step = StrUtil.split( authority, StrUtil.C_UNDERLINE);
        return step.size() - 1;
    }

}
