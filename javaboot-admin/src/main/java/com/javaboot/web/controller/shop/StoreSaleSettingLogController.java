package com.javaboot.web.controller.shop;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.shop.domain.StoreSaleSettingLog;
import com.javaboot.shop.service.IStoreSaleSettingLogService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 商品上数日志Controller
 * 
 * @author lqh
 * @date 2021-06-01
 */
@Controller
@RequestMapping("/shop/salelog")
public class StoreSaleSettingLogController extends BaseController {
    private String prefix = "shop/salelog";

    @Autowired
    private IStoreSaleSettingLogService storeSaleSettingLogService;

    @RequiresPermissions("shop:salelog:view")
    @GetMapping()
    public String salelog() {
        return prefix + "/salelog";
    }

    /**
     * 查询商品上数日志列表
     */
    @RequiresPermissions("shop:salelog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreSaleSettingLog storeSaleSettingLog) {
        startPage();
        List<StoreSaleSettingLog> list = storeSaleSettingLogService.selectStoreSaleSettingLogList(storeSaleSettingLog);
        return getDataTable(list);
    }

    /**
     * 导出商品上数日志列表
     */
    @RequiresPermissions("shop:salelog:export")
    @Log(title = "商品上数日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreSaleSettingLog storeSaleSettingLog) {
        List<StoreSaleSettingLog> list = storeSaleSettingLogService.selectStoreSaleSettingLogList(storeSaleSettingLog);
        ExcelUtil<StoreSaleSettingLog> util = new ExcelUtil<StoreSaleSettingLog>(StoreSaleSettingLog.class);
        return util.exportExcel(list, "salelog");
    }

    /**
     * 新增商品上数日志
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存商品上数日志
     */
    @RequiresPermissions("shop:salelog:add")
    @Log(title = "商品上数日志", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreSaleSettingLog storeSaleSettingLog) {
        return toAjax(storeSaleSettingLogService.insertStoreSaleSettingLog(storeSaleSettingLog));
    }

    /**
     * 修改商品上数日志
     */
    @GetMapping("/edit/{logId}")
    public String edit(@PathVariable("logId") Long logId, ModelMap mmap) {
        StoreSaleSettingLog storeSaleSettingLog = storeSaleSettingLogService.selectStoreSaleSettingLogById(logId);
        mmap.put("storeSaleSettingLog", storeSaleSettingLog);
        return prefix + "/edit";
    }

    /**
     * 修改保存商品上数日志
     */
    @RequiresPermissions("shop:salelog:edit")
    @Log(title = "商品上数日志", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreSaleSettingLog storeSaleSettingLog) {
        return toAjax(storeSaleSettingLogService.updateStoreSaleSettingLog(storeSaleSettingLog));
    }

    /**
     * 删除商品上数日志
     */
    @RequiresPermissions("shop:salelog:remove")
    @Log(title = "商品上数日志", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeSaleSettingLogService.deleteStoreSaleSettingLogByIds(ids));
    }
}
