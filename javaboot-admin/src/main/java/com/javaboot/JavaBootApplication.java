package com.javaboot;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

/**
 * 启动程序
 *
 * @author javaboot
 */

@MapperScan("com.javaboot.quartz.schedule")
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    org.activiti.spring.boot.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class JavaBootApplication {

    public final static Logger log = LoggerFactory.getLogger(JavaBootApplication.class);

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(JavaBootApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");

        log.info("\n----------------------------------------------------------\n\t" +
            "Application  is running! Access URLs:\n\t" +
            "Local: \t\thttp://localhost:" + port + path + "\n\t" +
            "External: \thttp://" + ip + ":" + port + path + "\n\t" +
            "----------------------------------------------------------");
    }


    @Bean
    public FilterRegistrationBean  corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOrigin("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        config.addExposedHeader("Access-Token, uid");
        // 对接口配置跨域设置
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        // 设置监听器的优先级
        bean.setOrder(0);

        return bean;
    }
}