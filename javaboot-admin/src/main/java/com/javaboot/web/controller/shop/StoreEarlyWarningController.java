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
import com.javaboot.shop.domain.StoreEarlyWarning;
import com.javaboot.shop.service.IStoreEarlyWarningService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 预警设置Controller
 * 
 * @author javaboot
 * @date 2021-11-15
 */
@Controller
@RequestMapping("/shop/earlyWarning")
public class StoreEarlyWarningController extends BaseController {
    private String prefix = "shop/earlyWarning";

    @Autowired
    private IStoreEarlyWarningService storeEarlyWarningService;

    @RequiresPermissions("shop:earlyWarning:view")
    @GetMapping()
    public String earlyWarning() {
        return prefix + "/earlyWarning";
    }

    /**
     * 查询预警设置列表
     */
//    @RequiresPermissions("shop:earlyWarning:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreEarlyWarning storeEarlyWarning) {
        startPage();
        List<StoreEarlyWarning> list = storeEarlyWarningService.selectStoreEarlyWarningList(storeEarlyWarning);
        return getDataTable(list);
    }

    /**
     * 导出预警设置列表
     */
    @RequiresPermissions("shop:earlyWarning:export")
    @Log(title = "预警设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreEarlyWarning storeEarlyWarning) {
        List<StoreEarlyWarning> list = storeEarlyWarningService.selectStoreEarlyWarningList(storeEarlyWarning);
        ExcelUtil<StoreEarlyWarning> util = new ExcelUtil<StoreEarlyWarning>(StoreEarlyWarning.class);
        return util.exportExcel(list, "earlyWarning");
    }

    /**
     * 新增预警设置
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存预警设置
     */
    @RequiresPermissions("shop:earlyWarning:add")
    @Log(title = "预警设置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreEarlyWarning storeEarlyWarning) {
        return toAjax(storeEarlyWarningService.insertStoreEarlyWarning(storeEarlyWarning));
    }

    /**
     * 修改预警设置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreEarlyWarning storeEarlyWarning = storeEarlyWarningService.selectStoreEarlyWarningById(id);
        mmap.put("storeEarlyWarning", storeEarlyWarning);
        return prefix + "/edit";
    }

    /**
     * 修改保存预警设置
     */
//    @RequiresPermissions("shop:earlyWarning:edit")
    @Log(title = "预警设置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreEarlyWarning storeEarlyWarning) {
        return toAjax(storeEarlyWarningService.updateStoreEarlyWarning(storeEarlyWarning));
    }

    /**
     * 删除预警设置
     */
    @RequiresPermissions("shop:earlyWarning:remove")
    @Log(title = "预警设置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeEarlyWarningService.deleteStoreEarlyWarningByIds(ids));
    }
}
