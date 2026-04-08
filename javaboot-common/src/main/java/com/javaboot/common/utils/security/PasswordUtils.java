/*
 * Copyright (c) 2016-2019 http://www.javaboot.cn. All rights reserved.
 * This file is part of the javaboot development framework.
 * Do not make the source code available without the author's permission.
 * This copyright must be retained when used in commercial products.
 * If you have any questions, please email admin@javaboot.cn
 */

package com.javaboot.common.utils.security;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import com.javaboot.common.utils.codec.Sha1Utils;

/**
 * @author javaboot
 */
public class PasswordUtils {

	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = RandomUtil.randomBytes(SALT_SIZE);
		byte[] hashPassword = Sha1Utils.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return HexUtil.encodeHexStr(salt) + HexUtil.encodeHexStr(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = HexUtil.decodeHex(password.substring(0,16));
		byte[] hashPassword = Sha1Utils.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(HexUtil.encodeHexStr(salt) + HexUtil.encodeHexStr(hashPassword));
	}

	public static void main(String[] args) {
		System.out.println(entryptPassword("admin"));
		System.out.println(validatePassword("admin", "1d522bbf049ddbcd0c489a9e4795017fdf27f0b85f7ef432b5da9686"));
	}
}
