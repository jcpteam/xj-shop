package com.javaboot.framework.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author javaboot
 */
@ConfigurationProperties(prefix = CaptchaProperties.PREFIX)
public class CaptchaProperties {

	public static final String PREFIX = "captcha";
	public static final long DEFAULT_CAPTCHA_TIMEOUT = 60 * 1000;

	/**
	 * 访问地址
	 */
	private String pattern = "/captcha";
	/**
	 * 验证码宽度
	 */
	private int width = 130;

	/**
	 * 验证码高度
	 */
	private int height = 48;

	/**
	 * 验证码位数
	 */
	private int len = 4;

	/**
	 * 验证码类型 1-默认字母数字混合 2-数字 3-字母
	 */
	private int charType = 1;

	/**
	 * 图片类型 SpecCaptcha, GifCaptcha, ChineseCaptcha, ChineseGifCaptcha, ArithmeticCaptcha
	 */
	private String captchaType = "SpecCaptcha";

	/**
	 * 验证码缓存的key
	 */
	private String captchaStoreKey = "captcha";
	/**
	 * 验证码创建时间缓存的key
	 */
	private String captchaDateStoreKey = "captchaDate";
	/**
	 * 验证码有效期；单位（毫秒），默认 60000
	 */
	private long captchaTimeout = DEFAULT_CAPTCHA_TIMEOUT;


	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getCaptchaStoreKey() {
		return captchaStoreKey;
	}

	public void setCaptchaStoreKey(String captchaStoreKey) {
		this.captchaStoreKey = captchaStoreKey;
	}

	public String getCaptchaDateStoreKey() {
		return captchaDateStoreKey;
	}

	public void setCaptchaDateStoreKey(String captchaDateStoreKey) {
		this.captchaDateStoreKey = captchaDateStoreKey;
	}

	public long getCaptchaTimeout() {
		return captchaTimeout;
	}

	public void setCaptchaTimeout(long captchaTimeout) {
		this.captchaTimeout = captchaTimeout;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public int getCharType() {
		return charType;
	}

	public void setCharType(int charType) {
		this.charType = charType;
	}

	public String getCaptchaType() {
		return captchaType;
	}

	public void setCaptchaType(String captchaType) {
		this.captchaType = captchaType;
	}
}
