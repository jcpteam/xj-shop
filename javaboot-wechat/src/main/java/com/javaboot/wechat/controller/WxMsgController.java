package com.javaboot.wechat.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import java.sql.Struct;
import java.util.List;

/**
 * 用户消息
 * @author Cracker
 */
@AllArgsConstructor
@Controller
@RequestMapping("/wechat/msg")
public class WxMsgController extends BaseController {

    private WxMpService wxMpService;
    private WxAccountService wxAccountService;
    private WxUserService wxUserService;
    private WxMsgService wxMsgService;
    private static final String prefix = "wechat/msg" ;

    /**
     * 用户消息列表
     */
    @GetMapping("/list")
    public String user() {
        return prefix + "/list" ;
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WxMsg wxMsg) {
        startPage();
        QueryWrapper<WxMsg> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(wxMsg.getRepType())) {
            queryWrapper.lambda().eq(WxMsg::getRepType, wxMsg.getRepType());
        }
        if(StrUtil.isNotBlank(wxMsg.getReadFlag())){
            queryWrapper.lambda().eq(WxMsg::getReadFlag, wxMsg.getReadFlag());
        }
        List<WxMsg> list = wxMsgService.list(queryWrapper);
        return getDataTable(list);
    }

}
