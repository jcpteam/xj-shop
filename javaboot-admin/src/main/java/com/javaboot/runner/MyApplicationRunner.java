package com.javaboot.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 程序启动后通过ApplicationRunner处理一些事务
 *
 * @author javaboot
 */

@Component
public class MyApplicationRunner implements ApplicationRunner {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(ApplicationArguments applicationArguments) {

        System.out.println("*********************系统启动成功*********************");

    }
}
