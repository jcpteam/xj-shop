package com.javaboot.applicationEvent.config;


import com.javaboot.applicationEvent.ApplicationEventAspect;
import com.javaboot.applicationEvent.ApplicationEventManager;
import com.javaboot.applicationEvent.IApplicationEvent;
import com.javaboot.system.service.ISysEventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 系统事件配置
 *
 * @author javaboot
 */
@Configuration
public class ApplicationEventConfig {

    @Autowired
    List<IApplicationEvent> applicationEventList;//所有实现系统事件类
    @Autowired
    ISysEventLogService eventLogService;//系统事件日志记录

    /**
     * 系统事件管理器
     *
     * @return
     */
    @Bean("applicationEventManager")
    public ApplicationEventManager getApplicationEventManager() {
        ApplicationEventManager applicationEventManager = new ApplicationEventManager();
        applicationEventManager.setApplicationEventList(applicationEventList);
        applicationEventManager.setEventLogService(eventLogService);
        return applicationEventManager;
    }

    /**
     * 系统事件触发切面类
     */
    @Bean("applicationEventAspect")
    public ApplicationEventAspect getApplicationEventAspect() {
        ApplicationEventAspect applicationEventAspect = new ApplicationEventAspect();
        applicationEventAspect.setApplicationEventManager(getApplicationEventManager());
        return applicationEventAspect;
    }
}
