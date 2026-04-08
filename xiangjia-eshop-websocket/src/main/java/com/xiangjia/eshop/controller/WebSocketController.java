package com.xiangjia.eshop.controller;

import com.xiangjia.eshop.server.WebsocketServer;
import com.xiangjia.eshop.util.StringUtils;
import com.xiangjia.eshop.util.WebSocketUtil;
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
public class WebSocketController {

    private String prefix = "websocket" ;

    @Autowired
    private WebsocketServer websocketServer;

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
    @PostMapping("/notice")
    @ResponseBody
    public AjaxResult notice(String msg) throws UnsupportedEncodingException {
        WebSocketUtil.sendNotificationMsg(msg, websocketServer.getOnlineUsers());
        return AjaxResult.success("消息发送成功");
    }
}
