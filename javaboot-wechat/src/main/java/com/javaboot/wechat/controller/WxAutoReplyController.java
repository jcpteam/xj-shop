package com.javaboot.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.wechat.entity.WxAutoReply;
import com.javaboot.wechat.entity.WxMsg;
import com.javaboot.wechat.service.WxAccountService;
import com.javaboot.wechat.service.WxAutoReplyService;
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
 * 消息自动回复
 * @author Cracker
 */
@AllArgsConstructor
@Controller
@RequestMapping("/wechat/autoreply")
public class WxAutoReplyController extends BaseController {

    private WxMpService wxMpService;
    private WxAutoReplyService wxAutoReplyService;
    private static final String prefix = "wechat/autoreply" ;

    /**
     * 用户消息列表
     */
    @GetMapping("/list")
    public String user() {
        return prefix + "/list" ;
    }

    /**
     * 自动回复列表
     * @param wxAutoReply
     * @return
     */
    @PostMapping("/data")
    @ResponseBody
    public TableDataInfo list(WxAutoReply wxAutoReply) {
        startPage();
        List<WxAutoReply> list = wxAutoReplyService.list(new QueryWrapper<WxAutoReply>().lambda().eq(WxAutoReply::getType, wxAutoReply.getType()));
        return getDataTable(list);
    }

}
