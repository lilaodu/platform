package com.blockchain.platform.task;

import com.blockchain.platform.mapper.OTCOrderMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.OTCOrderEntity;
import com.blockchain.platform.service.IOTCOrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class AutoCancelOtcTask {

    /**
     * otc 数据库接口
     */
    @Resource
    OTCOrderMapper otcOrderMapper;

    @Resource
    IOTCOrderService iotcOrderService;

    /**
     * 自动取消OTC订单
     * @throws Exception
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void autoCancel() throws Exception {
        //获取当前时间十五分钟前的数据
        Date dateBefore15Min = new Date(System.currentTimeMillis() - 15*60*1000);
        //获取订单
        List<OTCOrderEntity> list = otcOrderMapper.queryUncompleted( BaseDTO.builder()
                                                                .time(dateBefore15Min).build());
        //遍历，自动取消
        for(int idx = 0; idx < list.size(); idx ++){
            try {
                //取消
                iotcOrderService.cancelOtcOrder( list.get( idx));
            }catch (Exception ex){
                //
            }
        }
    }


}
