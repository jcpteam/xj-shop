package com.javaboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author javaboot
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.javaboot.**.mapper")
public class JavaBootAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavaBootAppApplication.class, args);
        System.out.println("API 启动成功...");
    }
}