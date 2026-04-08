package com.javaboot.wechat.controller;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 公共测试方法和参数.
 *
 * @author Binary Wang
 * @date 2019-06-14
 */
public abstract class BaseControllerTest {
	private static final String ROOT_URL = "http://127.0.0.1:9999/";

	@BeforeTest
	public void setup() {
		RestAssured.baseURI = ROOT_URL;
	}

}
