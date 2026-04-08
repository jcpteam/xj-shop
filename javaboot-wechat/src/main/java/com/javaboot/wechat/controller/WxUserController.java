package com.javaboot.wechat.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaboot.common.config.Global;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.wechat.entity.WxAccount;
import com.javaboot.wechat.entity.WxUser;
import com.javaboot.wechat.service.WxAccountService;
import com.javaboot.wechat.service.WxUserService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Struct;
import java.util.Date;
import java.util.List;

/**
 * 微信账户管理
 * @author Cracker
 */
@AllArgsConstructor
@Controller
@RequestMapping("/wechat/user")
public class WxUserController extends BaseController {

    private WxMpService wxMpService;
    private WxAccountService wxAccountService;
    private WxUserService wxUserService;
    private static final String prefix = "wechat/user" ;

    /**
     * 微信账户列表
     */
    @GetMapping("/list")
    public String user() {
        return prefix + "/list" ;
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WxUser wxUser) {
        startPage();
        QueryWrapper<WxUser> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(wxUser.getNickName())){
            queryWrapper.lambda().like(WxUser::getNickName, wxUser.getNickName());
        }
        if(StrUtil.isNotBlank(wxUser.getOpenId())){
            queryWrapper.lambda().like(WxUser::getOpenId, wxUser.getOpenId());
        }
        if(wxUser.getAppid() != null){
            queryWrapper.lambda().eq(WxUser::getAppid, wxUser.getAppid());
        }
        List<WxUser> list = wxUserService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 同步公众号用户
     * @param wxAccount
     * @return
     * @throws WxErrorException
     */
    @PostMapping("/sync")
    @ResponseBody
    public AjaxResult sync(@Validated WxAccount wxAccount) throws WxErrorException {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }

        String appid = wxAccount.getAppid();
        wxMpService.switchover(appid);
        wxUserService.synchroWxUser(appid);

        return AjaxResult.success("微信用户同步成功!");
    }

    /**
     * 编辑公众号
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        mmap.put("account", wxAccountService.getById(id));
        return prefix + "/edit" ;
    }

    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated WxAccount wxAccount) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }

        String loginName = ShiroUtils.getLoginName();
        wxAccount.setUpdateBy(loginName);
        wxAccount.setUpdateTime(new Date());
        boolean success = wxAccountService.saveOrUpdate(wxAccount);
        AjaxResult result = null;
        if(success){
            result = AjaxResult.success("编辑成功!");
        }else {
            result = AjaxResult.error("编辑失败, 请重试!");
        }
        return result;
    }

    /**
     * 删除公众号
     */
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(wxAccountService.removeByIds(ListUtil.toList(ids.split(","))));
    }

}
