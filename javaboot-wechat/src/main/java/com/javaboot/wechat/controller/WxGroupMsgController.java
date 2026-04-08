package com.javaboot.wechat.controller;

import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.wechat.entity.WxMsg;
import com.javaboot.wechat.service.WxAccountService;
import com.javaboot.wechat.service.WxMsgService;
import com.javaboot.wechat.service.WxUserService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 消息群发
 * @author Cracker
 */
@AllArgsConstructor
@Controller
@RequestMapping("/wechat/groupmsg")
public class WxGroupMsgController extends BaseController {

    private WxMpService wxMpService;
    private WxAccountService wxAccountService;
    private WxUserService wxUserService;
    private WxMsgService wxMsgService;
    private static final String prefix = "wechat/groupmsg" ;

    /**
     * 用户消息列表
     */
    @GetMapping("/list")
    public String user() {
        return prefix + "/list" ;
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WxMsg wxUser) {
        startPage();
        List<WxMsg> list = wxMsgService.list();
        return getDataTable(list);
    }

}
