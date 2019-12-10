package com.blockchain.platform.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import cn.hutool.core.util.ObjectUtil;
import com.blockchain.platform.mapper.UserMapper;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.ExtensionVO;
import com.blockchain.platform.pojo.vo.PageVO;
import com.blockchain.platform.pojo.vo.UnlockWarehouseFlowVO;
import com.blockchain.platform.pojo.vo.UserAdviceVO;
import com.blockchain.platform.service.IKycService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.ExConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.dto.UserLockWarehouseDTO;
import com.blockchain.platform.pojo.dto.UserWalletDTO;
import com.blockchain.platform.service.IUnlockWarehouseFlowService;
import com.blockchain.platform.service.IUserLockWarehouseService;
import com.blockchain.platform.service.IUserWalletService;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import com.github.pagehelper.PageHelper;
import cn.hutool.core.bean.BeanUtil;

@RestController
@RequestMapping("/lockWarehouse")
public class LockWarehouseController extends BaseController{


	@Resource
	UserMapper userMapper;
	/**
     * 钱包接口
     */
    @Resource
    private IUserWalletService userWalletService;
	
    /**
     * 缓存对象
     */
    @Resource
    private RedisPlugin redisPlugin;
    
    @Resource
    IUnlockWarehouseFlowService unlockWarehouseFlowService;
    
    @Resource
    IUserLockWarehouseService userLockWarehouseService;

	/**
	 * kyc 认证
	 */
	@Resource
	private IKycService kycService;
	
	/**
	 * 用户锁仓
	 * @param dto
	 * @param valid
	 * @param request
	 * @return
	 * 
	 * 1.传入锁币数量
	 * 2.判断用户是否有资格
	 * 3.判断传入参数是否正确(锁币数量)
	 * 4.判断用户btb钱包数量够不够
	 * 5.冻结用户btb钱包里的锁币数量
	 * 6.用户btb钱包添加赠送数量
	 * 7.生成订单
	 * 
	 * 
	 */
	@RequestMapping("/lockCoin")
    public ResponseData lockCion(@RequestBody @Valid UserLockWarehouseDTO dto, BindingResult valid, HttpServletRequest request) {
		ResponseData data = ResponseData.ok();
		try {
			//登录用户
			UserDTO user = getLoginUser( request);
			UserEntity userEntity=userMapper.selectById(user.getId());
			if(userEntity.getState().equals(2)) throw ExUtils.error(LocaleKey.USER_LOCKED);
			//获取锁仓配置
			LockWarehouseConfigEntity lockWarehouseConfig = redisPlugin.get(RedisConst.LOCK_WAREHOUSE_CONFIG);
			//锁币数量
			BigDecimal lockNum = dto.getLockNum();
			lockNum = lockNum.abs();//取绝对值
			//获取用户实名认证详情
			UserKycEntity kyc = kycService.findByCondition( BaseDTO.builder()
													.userId( user.getId())
													.state( BizConst.BIZ_STATUS_FAILED).build());
			if(BeanUtil.isEmpty( kyc) || !IntUtils.compare( kyc.getState(), BizConst.KycConst.K1_OK)){
				throw ExUtils.error( LocaleKey.USER_C1_NOT_ACCESS);
			}
			/**
			 * 数量是否正确
			 * 1.前端传入是否过大
			 * 2.用户总量是否上限
			 */
			if(!BizUtils.okLockNum(lockWarehouseConfig.getLockNum(),lockNum)) {
				throw ExUtils.error(LocaleKey.SYS_PARAM_ERROR);
			}
			BigDecimal lockNumTotal = userLockWarehouseService.queryLockNumTotal(user.getId());
			if(lockNumTotal!=null && lockWarehouseConfig.getLockToplimit().compareTo(lockNumTotal.add(lockNum)) == -1) {
				throw ExUtils.error(LocaleKey.LOCK_WAREHOUSE_TOP);
			}
			// 通过symbol和userid获取用户特定钱包
			UserWalletEntity walletEntity = userWalletService.queryUserWallet(
					UserWalletDTO.builder().symbol(dto.getCoin()).userId( user.getId()).build()
			);
			// 验证余额是否足够
			BizUtils.verifyWallet(walletEntity,dto.getLockNum(),BizConst.WalletConst.WALLET_TYPE_TRADE);
			//构建订单
			UserLockWarehouseEntity userLockWarehouse = new UserLockWarehouseEntity();
			userLockWarehouse.setUserId(user.getId());
			userLockWarehouse.setLockNum(lockNum);
			userLockWarehouse.setGiveCoinNum(lockNum.multiply(lockWarehouseConfig.getGiftRatio()));
			BigDecimal zro = new BigDecimal(IntUtils.INT_ZERO);
			Date date = new Date();
			userLockWarehouse.setUnlockDayNum(IntUtils.INT_ZERO);
			userLockWarehouse.setLockDate(date);
			userLockWarehouse.setUnlockNum(zro);
			userLockWarehouse.setExpireNum(zro);
			userLockWarehouse.setCreateTime(date);
			userLockWarehouse.setSymbol(dto.getCoin());
			//进行锁仓
			boolean b = userLockWarehouseService.addLockWarehouseAndWallet(userLockWarehouse);
			if ( b) {
				userLockWarehouseService.upgrade( userLockWarehouse.getUserId());
			}
			data.setData(userLockWarehouse);
		}catch (Exception ex){
			ExUtils.error(ex, LocaleKey.WALLET_INSUFFICIENT_FUNDS);
		}
		return data;
    }
	
	/**
	 * 用户锁仓情况
	 * @param request
	 * @return
	 */
	@RequestMapping("/lockDetails")
    public ResponseData lockDetails( HttpServletRequest request){
		ResponseData data = ResponseData.ok();
		try {	
			QueryWrapper<UserLockWarehouseEntity> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq(BizConst.sql.userId,getLoginUser(request).getId());
	    	List<UserLockWarehouseEntity> entitys = userLockWarehouseService.list(queryWrapper);
	    	data.setData(entitys);
		} catch (Exception e) {
			throw ExUtils.error(LocaleKey.SYS_QUERY_FAILED);
		}
        return data;
		
	}
	
	/**
	 * 用户解锁流水列表(分页)
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping("/unlockFlow")
    public ResponseData unlockFlow(@RequestBody PageDTO pageDTO, HttpServletRequest request){
		ResponseData data = ResponseData.ok();
		try {
			IPage<UnlockWarehouseFlowEntity> unlockWarehouseFlow = null;
			if(IntUtils.equals(pageDTO.getTabIndex(), IntUtils.INT_ONE)) {
				//释放unlock_num>0
				QueryWrapper<UnlockWarehouseFlowEntity> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq(BizConst.sql.userId,getLoginUser(request).getId()).gt(BizConst.sql.UNLOCKNUM, IntUtils.INT_ZERO).orderByDesc(BizConst.sql.createTime);
				IPage<UnlockWarehouseFlowEntity> ipage = new Page<>(pageDTO.getPageNum(),pageDTO.getPageSize());
				unlockWarehouseFlow = unlockWarehouseFlowService.page(ipage, queryWrapper);
				data.setData(unlockWarehouseFlow);
				
			}else if(IntUtils.equals(pageDTO.getTabIndex(), IntUtils.INT_ZERO)) {
				//全部
//				QueryWrapper<UnlockWarehouseFlowEntity> queryWrapper = new QueryWrapper<>();
//				queryWrapper.eq(BizConst.sql.userId,getLoginUser(request).getId()).orderByDesc(BizConst.sql.createTime);
//				IPage<UnlockWarehouseFlowEntity> ipage = new Page<>(pageDTO.getPageNum(),pageDTO.getPageSize());
//				unlockWarehouseFlow = unlockWarehouseFlowService.page(ipage, queryWrapper);
//				data.setData(unlockWarehouseFlow);

				//开始分页
				com.github.pagehelper.Page<UnlockWarehouseFlowVO> page = PageHelper.startPage(pageDTO.getPageNum(),pageDTO.getPageSize());
	            //列表查询
	            List<UnlockWarehouseFlowVO> list = unlockWarehouseFlowService.allUnlock(getLoginUser(request).getId());
	            //返回数据
	            data.setData( PageVO.builder().total( page.getTotal()).list( list).build());
	            
			}else if(IntUtils.equals(pageDTO.getTabIndex(), IntUtils.INT_TWO)){
				//销毁,当天之前 unlock_num<=0
				
//				List<UnlockWarehouseFlowVO> unlockWarehouseFlowVO = unlockWarehouseFlowService.noUnlock(getLoginUser(request).getId());
				
				 //开始分页
				com.github.pagehelper.Page<UnlockWarehouseFlowVO> page = PageHelper.startPage(pageDTO.getPageNum(),pageDTO.getPageSize());
	            //列表查询
	            List<UnlockWarehouseFlowVO> list = unlockWarehouseFlowService.noUnlock(getLoginUser(request).getId());
	            //返回数据
	            data.setData( PageVO.builder().total( page.getTotal()).list( list).build());
	            
//				QueryWrapper<UnlockWarehouseFlowEntity> queryWrapper = new QueryWrapper<>();
//				
//				queryWrapper.eq(BizConst.sql.userId,getLoginUser(request).getId()).le(BizConst.sql.UNLOCKNUM, IntUtils.INT_ZERO).orderByDesc(BizConst.sql.createTime);
//				
//				IPage<UnlockWarehouseFlowEntity> ipage = new Page<>(pageDTO.getPageNum(),pageDTO.getPageSize());
//				unlockWarehouseFlow = unlockWarehouseFlowService.page(ipage, queryWrapper);

			}
			
			
		} catch (Exception e) {
			throw ExUtils.error(LocaleKey.SYS_QUERY_FAILED);
		}
		
		return data;
	}
	
	/**
	 * 锁仓配置
	 * @return
	 */
    @RequestMapping("/config")
    public ResponseData config() {
        ResponseData data = ResponseData.ok();
        //redis获取配置信息
        try {
        	LockWarehouseConfigEntity entity = redisPlugin.get(RedisConst.LOCK_WAREHOUSE_CONFIG);
        	data.setData(entity);
		} catch (Exception e) {
			throw ExUtils.error(LocaleKey.SYS_QUERY_FAILED);
		}
        return data;
    }
    
    /**
     * 获取当天任务有效次数
     * (如果完成一次秒合约后,加了仓那么这一次属于无效的,重新安总额计算)
     */
    @RequestMapping("/dayTaskNum")
    public ResponseData dayTaskNum(HttpServletRequest request) {
    	ResponseData data = ResponseData.ok();
    	
    	//登录用户
        UserDTO user = getLoginUser( request);
    	if(BeanUtil.isEmpty(user)) {
        	throw ExUtils.error(  LocaleKey.USER_NOT_FIND);
        }
    	try {	 
	    	 Integer num = unlockWarehouseFlowService.dayTaskNum(user.getId());
	    	 data.setData(num);
	    } catch (Exception e) {
			throw ExUtils.error(LocaleKey.SYS_QUERY_FAILED);
		}
		return data;
    }
    
	
	
}
