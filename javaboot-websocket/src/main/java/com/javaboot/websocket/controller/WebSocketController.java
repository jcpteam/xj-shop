package com.javaboot.websocket.controller;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.websocket.server.WebsocketServer;
import com.javaboot.websocket.util.WebSocketUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

/**
 * @author javaboot
 */
@Controller
public class WebSocketController extends BaseController {

    private String prefix = "websocket" ;

    @Autowired
    private WebsocketServer websocketServer;

    @RequiresPermissions("notice")
    @GetMapping("/notice")
    public String notice(Model model) {
        model.addAttribute("online" , websocketServer.getOnlineUserCount());
        return prefix + "/notification" ;
    }

    /**
     * 发送消息通知
     *
     * @return
     */
    @RequiresPermissions("notice")
    @PostMapping("/notice")
    @Log(title = "通过websocket向前台用户发送通知" , businessType = BusinessType.OTHER)
    @ResponseBody
    public AjaxResult notice(String msg) throws UnsupportedEncodingException {
        WebSocketUtil.sendNotificationMsg(msg, websocketServer.getOnlineUsers());
        return AjaxResult.success("消息发送成功");
    }
}
