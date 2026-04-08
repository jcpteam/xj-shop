package com.javaboot.framework.config;

import com.javaboot.framework.config.properties.CaptchaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置
 *
 * @author javaboot
 */
@Configuration
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaConfig {


}
