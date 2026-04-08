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
import com.javaboot.wechat.service.WxAccountService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 微信账户管理
 * @author Cracker
 */
@AllArgsConstructor
@Controller
@RequestMapping("/wechat/account")
public class WxAccountController extends BaseController {

    private WxMpService wxService;
    private WxAccountService wxAccountService;
    private static final String prefix = "wechat/account" ;

    /**
     * 微信账户列表
     */
    @GetMapping("/list")
    public String user() {
        return prefix + "/list" ;
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WxAccount wxAccount) {
        startPage();
        QueryWrapper<WxAccount> queryWrapper = new QueryWrapper<>();
        if(wxAccount.getDeptId() != null){
            queryWrapper.lambda().eq(WxAccount::getDeptId, wxAccount.getDeptId());
        }
        if(StrUtil.isNotBlank(wxAccount.getName())){
            queryWrapper.lambda().like(WxAccount::getName, wxAccount.getName());
        }
        if(StrUtil.isNotBlank(wxAccount.getAppid())){
            queryWrapper.lambda().like(WxAccount::getAppid, wxAccount.getAppid());
        }
        List<WxAccount> list = wxAccountService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 新增公众号
     */
    @GetMapping("/add")
    public String add(String deptId, ModelMap mmap) {
        mmap.put("deptId", deptId);
        return prefix + "/add" ;
    }

    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated WxAccount wxAccount) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }

        String loginName = ShiroUtils.getLoginName();
        wxAccount.setUpdateBy(loginName);
        wxAccount.setUpdateTime(new Date());
        wxAccount.setCreateBy(loginName);
        wxAccount.setCreateTime(new Date());
        //添加wxmp动态配置
        wxAccountService.addConfigStorage(wxAccount);
        //保存到数据库
        boolean success = wxAccountService.saveOrUpdate(wxAccount);
        AjaxResult result = null;
        if(success){
            result = AjaxResult.success("添加成功!");
        }else {
            result = AjaxResult.error("添加失败, 请重试!");
        }
        return result;
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
