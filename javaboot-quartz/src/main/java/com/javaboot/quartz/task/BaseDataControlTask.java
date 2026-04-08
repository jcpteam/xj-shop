package com.javaboot.quartz.task;

import com.javaboot.quartz.schedule.IGoodsPriceService;
import com.javaboot.quartz.util.CmdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author javaboot
 */
@Component("baseDataTask")
public class BaseDataControlTask {
    private static final Logger log = LoggerFactory.getLogger(BaseDataControlTask.class);

    public void syncBaseData(String viewName) {
        log.info("------------------执行定时任务------------BaseDataControlTask.syncBaseData");
        CmdUtils.callScript(viewName);
        log.info("------------------结束定时任务------------BaseDataControlTask.syncBaseData");
    }



}
