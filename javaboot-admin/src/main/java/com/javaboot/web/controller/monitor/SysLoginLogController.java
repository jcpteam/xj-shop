package com.javaboot.web.controller.monitor;

import java.util.List;

import com.javaboot.framework.shiro.service.SysPasswordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.system.domain.SysLoginLog;
import com.javaboot.system.service.ISysLoginLogService;

/**
 * 系统访问记录
 *
 * @author javaboot
 */
@Controller
@RequestMapping("/monitor/loginlog")
public class SysLoginLogController extends BaseController {
    private String prefix = "monitor/loginlog" ;

    @Autowired
    private ISysLoginLogService loginLogService;

    @Autowired
    private SysPasswordService passwordService;

    @RequiresPermissions("monitor:loginlog:view")
    @GetMapping()
    public String loginlog() {
        return prefix + "/loginlog" ;
    }

    @RequiresPermissions("monitor:loginlog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysLoginLog loginlog) {
        startPage();
        List<SysLoginLog> list = loginLogService.selectLoginLogList(loginlog);
        return getDataTable(list);
    }

    @Log(title = "登陆日志" , businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:loginlog:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysLoginLog loginlog) {
        List<SysLoginLog> list = loginLogService.selectLoginLogList(loginlog);
        ExcelUtil<SysLoginLog> util = new ExcelUtil<SysLoginLog>(SysLoginLog.class);
        return util.exportExcel(list, "登陆日志");
    }

    @RequiresPermissions("monitor:loginlog:remove")
    @Log(title = "登陆日志" , businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(loginLogService.deleteLoginLogByIds(ids));
    }

    @RequiresPermissions("monitor:loginlog:remove")
    @Log(title = "登陆日志" , businessType = BusinessType.CLEAN)
    @PostMapping("/clean")
    @ResponseBody
    public AjaxResult clean() {
        loginLogService.cleanLoginLog();
        return success();
    }

    @RequiresPermissions("monitor:loginlog:unlock")
    @Log(title = "账户解锁" , businessType = BusinessType.OTHER)
    @PostMapping("/unlock")
    @ResponseBody
    public AjaxResult unlock(String loginName) {
        passwordService.unlock(loginName);
        return success();
    }
}
