package platform;

import com.blockchain.platform.PlatformApplication;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.mapper.OTCOrderMapper;
import com.blockchain.platform.mapper.UserMapper;
import com.blockchain.platform.mapper.UserWalletMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.UserWalletDTO;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.service.IDictionaryService;
import com.blockchain.platform.service.IOTCAdvertService;
import com.blockchain.platform.service.IOTCOrderService;
import com.blockchain.platform.service.IUserWalletService;
import com.blockchain.platform.service.impl.UnlockWarehouseFlowServiceimpl;
import com.blockchain.platform.task.UnLockProfitTask;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatformApplication.class)
public class ControllerWithRunningServer {


    @Autowired
    UnlockWarehouseFlowServiceimpl unlockWarehouseFlowServiceimpl;
    @Autowired
    UnLockProfitTask unLockProfitTask;


    @Test
    public void getAllCitiesTest() {

        unLockProfitTask.profit();












    }
}
