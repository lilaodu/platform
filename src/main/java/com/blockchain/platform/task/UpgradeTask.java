package com.blockchain.platform.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.constant.UpgradeConst;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.RecommendVO;
import com.blockchain.platform.pojo.vo.TeamVO;
import com.blockchain.platform.pojo.vo.UpgradeDirectVO;
import com.blockchain.platform.pojo.vo.UserVO;
import com.blockchain.platform.service.ITaskService;
import com.blockchain.platform.service.IUserService;
import com.blockchain.platform.service.IUserWalletService;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 升级机制 定时任务器
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-25 5:24 PM
 **/
@Component
public class UpgradeTask {

    /**
     * 定时任务服务接口
     */
    @Resource
    private ITaskService taskService;

    /**
     * 用户服务接口
     */
    @Resource
    private IUserService userService;

    /**
     * 钱包数据
     */
    @Resource
    private IUserWalletService userWalletService;

    /**
     * 缓存数据
     */
    @Resource
    private RedisPlugin redisPlugin;


    /**
     * 已经升级用户对象
     * 用于降低递归率
     */
    private static final ConcurrentHashMap<Integer, UserUpgradeFlowEntity> processed = new ConcurrentHashMap<>();


    /**
     * 升级机制，每天凌晨执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    private void upgrade(){
        Long s = System.currentTimeMillis();
        // 获取昨日交易用户
        TaskDTO dto = TaskDTO.builder().build();
        // 昨日数据
        dto.setTime( DateUtil.format( DateUtil.offsetDay( new Date(), -1), DatePattern.PURE_DATE_PATTERN));

        // 获取用户数据
        List<UserEntity> users = taskService.findUserByCondition( dto);

        Map<Integer, UserUpgradeEntity> config = redisPlugin.hget(RedisConst.PLATFORM_UPGRADE_CONFIG,
                                                                    BizConst.UpgradeConst.UPGRADE_TYPE_CONTRACT);

        for (int idx = 0;idx < users.size();idx ++ ) {
            UserEntity entity = users.get( idx);
            // 第一次递归直接升级
            recursion( entity, config, Boolean.TRUE);
        }

        // 线程执行完情况数据
        processed.clear();
        Long e = System.currentTimeMillis();

        System.out.println("执行完成时间：" + (e - s) + "ms");
        System.out.println("检查是否已经情况内容：" + JSONUtil.toJsonStr( processed));

    }

    /**
     * 开始进行用户升级
     * 递归处理
     * @param entity
     * @param config
     * @param isChildUp 下级是否升级
     */
    private void recursion(UserEntity entity, Map<Integer, UserUpgradeEntity> config,Boolean isChildUp) {

        // 是否升级
        Boolean isUpgrade = false;

        UserUpgradeFlowEntity history = processed.get( entity.getId());
        // 如果已经处理，或者直推下级 已升级 再次计算
        if ( ObjectUtil.isEmpty( history)
                || isChildUp) {
            // 获取用户等级
            int lv = IntUtils.toInt( BizUtils.getRankValue( entity.getRank()));

            // 用户当前保持条件
            UserUpgradeEntity keep = config.get( StrUtil.toString(lv ));

            // 获取是否有人工干预
            BaseDTO dto = new BaseDTO();
            dto.setUserId( entity.getId());

            // 最后一次升级记录
            UserUpgradeFlowEntity flowEntity = taskService.findLastUpgrade( dto);

            // 当前订单状态
            int state = 0;

            // 人工干预 则提示
            if ( ObjectUtil.isNotEmpty( flowEntity) &&
                    (IntUtils.equals(BizConst.UpgradeConst.UPGRADE_TYPE_4, flowEntity.getState())
                    || IntUtils.equals( BizConst.UpgradeConst.UPGRADE_TYPE_3, flowEntity.getState()))) {
                // 提示
                if ( BizUtils.isDowngrade( flowEntity)) {
                    isUpgrade = true;
                }
                state = BizConst.UpgradeConst.UPGRADE_TYPE_5;
            } else {
                if ( ObjectUtil.isNotEmpty( flowEntity) && BizUtils.isDowngrade( flowEntity)) {
                    isUpgrade = true;
                }
            }

            // 判断是否需要降级
            if ( isUpgrade ) {
                TaskDTO taskDTO = new TaskDTO();
                // 权限
                taskDTO.setAuthority( entity.getAuthority());
                // 上次升级时间
                taskDTO.setTime( DateUtil.format( flowEntity.getCreateTime(), DatePattern.PURE_DATETIME_FORMAT));

                // 距离时间
                taskDTO.setEnd( DateUtil.format( DateUtil.offsetMonth( flowEntity.getCreateTime(), IntUtils.INT_THREE), DatePattern.PURE_DATETIME_FORMAT));

                // 计算 三个月 内交易量
                BigDecimal volume = taskService.teamTradeVolume( taskDTO);

                // 如果 团队交易量
                if ( !BigDecimalUtils.compareTo( keep.getDirectTotal(), volume)) {
                    isUpgrade = false;
                }
            }

            // 是否需要降级
            UserUpgradeFlowEntity doFlowEntity = null;
            if ( isUpgrade ) {
                String dwgrade = StrUtil.toString( lv - 1);

                if ( config.containsKey( dwgrade)) {
                    // 开始降级
                    UserUpgradeEntity downgrade = config.get( lv --);

                    // 当前升级状态
                    doFlowEntity = new UserUpgradeFlowEntity();
                    doFlowEntity.setState( state > 0 ? state : BizConst.UpgradeConst.UPGRADE_TYPE_2);
                    doFlowEntity.setRankLevel( downgrade.getLv());
                    doFlowEntity.setUserId( entity.getId());
                    doFlowEntity.setType( BizConst.UpgradeConst.UPGRADE_TYPE_CONTRACT);
                    doFlowEntity.setHistoryLevel( entity.getRank());
                    doFlowEntity.setAdminUserId( -1);
                    doFlowEntity.setCreateTime(new Date());

                    // 已经降级
                    isUpgrade = false;
                }
            } else if( state == 0) { // 状态 不是人工干预过
                // 默认状态
                String upgradeLv = StrUtil.toString( lv + 1);

                if ( config.containsKey( upgradeLv)) {

                    UserUpgradeEntity upgrade = config.get( upgradeLv);
                    // 统计钱包
                    TaskDTO taskDTO = new TaskDTO();

                    taskDTO.setUserId( entity.getId());

                    // 只统计此钱包
                    taskDTO.setSymbol( CollUtil.newArrayList(BizConst.BASE_TOKEN, BizConst.USDT_TOKEN));

                    // 钱包数据
                    Map<Integer, UpgradeDirectVO> array = taskService.findDirectHold( taskDTO);

                    // 达标人数
                    int standard = 0;
                    for (Map.Entry<Integer, UpgradeDirectVO> entry : array.entrySet()) {
                        if ( !IntUtils.equals( entity.getId(), entry.getKey()) &&
                                BigDecimalUtils.compare( entry.getValue().getAmount(), upgrade.getOwnTotal())) {
                            standard ++;
                        }
                    }

                    Boolean checkOwnSign = Boolean.TRUE;
                    if ( BigDecimalUtils.compare( upgrade.getDirectOwnTotal(), BigDecimal.ZERO)) {
                        if ( array.containsKey( entity.getId())) {
                            UpgradeDirectVO ownWallet = array.get( entity.getId());
                            if ( BigDecimalUtils.compare( ownWallet.getAmount(), upgrade.getDirectOwnTotal())) {
                                checkOwnSign = true;
                            } else {
                                checkOwnSign = false;
                            }
                        } else {
                            checkOwnSign = false;
                        }
                    }

                    // 直推人数 用户持有量 自己持有量
                    if ( IntUtils.compare(  array.size(), upgrade.getDirectNum())
                            && IntUtils.compare( standard,upgrade.getDirectNum())
                            && checkOwnSign) {
                        isUpgrade = true; // 已经升级
                        // 开始升级
                        doFlowEntity = new UserUpgradeFlowEntity();
                        doFlowEntity.setAdminUserId( -1);
                        doFlowEntity.setState( BizConst.UpgradeConst.UPGRADE_TYPE_1);
                        doFlowEntity.setHistoryLevel( entity.getRank());
                        doFlowEntity.setType( BizConst.UpgradeConst.UPGRADE_TYPE_CONTRACT);
                        doFlowEntity.setRankLevel( upgrade.getLv());
                        doFlowEntity.setUserId( entity.getId());
                    }
                }
            }

            // 说明有升降级
            if ( ObjectUtil.isNotEmpty( doFlowEntity)) {

                Boolean execute = Boolean.TRUE;

                // 是否存在已处理
                if ( ObjectUtil.isNotEmpty( history)) {

                    int keepLv = IntUtils.toInt( BizUtils.getRankValue( doFlowEntity.getRankLevel()));

                    int historyLv = IntUtils.toInt( BizUtils.getRankValue( history.getRankLevel()));

                    execute = keepLv > historyLv;
                }
                // 再次升级
                if ( execute) {
                    taskService.addUserUpgradeFlow( doFlowEntity);
                    // 已经调整用户
                    processed.put( entity.getId(), doFlowEntity);
                }
            }
        }

        // 存在父级
        if ( IntUtils.greaterThanZero( entity.getParentId())) {
            UserEntity parent = userService.findUserByCondition( UserDTO.builder().state( -1).id( entity.getParentId()).build());
            recursion( parent, config, isUpgrade);
        }
    }

}
