package com.javaboot.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboot.wechat.entity.WxAccount;

/**
 * WxAccountService
 *
 * @author javaboot.cn
 * @version 1.0.0
 * @date 2020/9/25 12:43
 */
public interface WxAccountService extends IService<WxAccount> {

    void addConfigStorage(WxAccount wxAccount);

}
