package com.javaboot.quartz.task;

import com.javaboot.quartz.schedule.IGoodsPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author javaboot
 */
@Component("goodsTask")
public class GoodsControlTask {
    private static final Logger log = LoggerFactory.getLogger(GoodsControlTask.class);

    @Autowired
    private IGoodsPriceService iGoodsPriceService;

    public void priceControl() {
        log.info("------------------执行定时任务------------jbTask.priceControl");
        int count = iGoodsPriceService.selectTodayPriceSetting();
        log.info("------------------执行定时任务------------{}",count);
        if(count==0){
            iGoodsPriceService.deleteTodayPriceSetting();
            iGoodsPriceService.deleteTodayPriceSettingDetail();
            iGoodsPriceService.insertTodayPriceSetting();
            iGoodsPriceService.insertTodayPriceSettingDetail();
        }


        log.info("------------------结束定时任务------------jbTask.priceControl");
    }



}
