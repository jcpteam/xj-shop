package com.javaboot.shop.task;

import com.javaboot.common.constant.Constants;
import com.javaboot.shop.domain.StorePaymentFlow;
import com.javaboot.shop.service.IStorePaymentFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component("payQryTask")
public class PayQryTask
{

    private static final Logger logger = LoggerFactory.getLogger(PayQryTask.class);

    private static ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>();

    @Autowired
    private IStorePaymentFlowService storePaymentFlowService;

    /**
     * 5s查询一次流水记录
     */
    public void query(){
        StorePaymentFlow flow = new StorePaymentFlow();
        flow.setStatus(Constants.NORMAL);
        List<StorePaymentFlow> list = storePaymentFlowService.selectStorePaymentFlowList(flow);
        list.forEach(obj->{
            if(!queue.contains(obj)) {
                queue.add(obj);
                try {
                    storePaymentFlowService.payQuery(obj);
                } catch (Exception e) {
                    logger.error("聚合支付查询异常了",e);
                } finally {
                    queue.poll();
                }
            }
        });

    }
}
