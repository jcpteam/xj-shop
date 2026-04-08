package com.javaboot.wechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.wechat.entity.WxAccount;
import com.javaboot.wechat.mapper.WxAccountMapper;
import com.javaboot.wechat.service.WxAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Service;

/**
 * 微信用户
 *
 * @author javaboot
 * @date 2019-03-25 15:39:39
 */
@Slf4j
@Service
@AllArgsConstructor
public class WxAccountServiceImpl extends ServiceImpl<WxAccountMapper, WxAccount> implements WxAccountService {

	private final WxMpService wxService;

	@Override
	public boolean saveOrUpdate(WxAccount wxAccount) {
		return super.saveOrUpdate(wxAccount);
	}

	@Override
	public void addConfigStorage(WxAccount wxAccount) {
		WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
		configStorage.setAppId(wxAccount.getAppid());
		configStorage.setSecret(wxAccount.getSecret());
		configStorage.setToken(wxAccount.getToken());
		configStorage.setAesKey(wxAccount.getAeskey());
		wxService.addConfigStorage(wxAccount.getAppid(), configStorage);
	}
}
