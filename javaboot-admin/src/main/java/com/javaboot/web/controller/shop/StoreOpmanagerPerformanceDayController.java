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
import com.javaboot.shop.domain.StoreOpmanagerPerformanceDay;
import com.javaboot.shop.service.IStoreOpmanagerPerformanceDayService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 业务员每日绩效Controller
 * 
 * @author javaboot
 * @date 2021-12-21
 */
@Controller
@RequestMapping("/shop/opmanagerPerformancDay")
public class StoreOpmanagerPerformanceDayController extends BaseController {
    private String prefix = "shop/opmanagerPerformancDay";

    @Autowired
    private IStoreOpmanagerPerformanceDayService storeOpmanagerPerformanceDayService;

    @RequiresPermissions("shop:opmanagerPerformancDay:view")
    @GetMapping()
    public String opmanagerPerformancDay() {
        return prefix + "/opmanagerPerformancDay";
    }

    /**
     * 查询业务员每日绩效列表
     */
    @RequiresPermissions("shop:opmanagerPerformancDay:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay) {
        startPage();
        List<StoreOpmanagerPerformanceDay> list = storeOpmanagerPerformanceDayService.selectStoreOpmanagerPerformanceDayList(storeOpmanagerPerformanceDay);
        return getDataTable(list);
    }

    /**
     * 导出业务员每日绩效列表
     */
    @RequiresPermissions("shop:opmanagerPerformancDay:export")
    @Log(title = "业务员每日绩效", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay) {
        List<StoreOpmanagerPerformanceDay> list = storeOpmanagerPerformanceDayService.selectStoreOpmanagerPerformanceDayList(storeOpmanagerPerformanceDay);
        ExcelUtil<StoreOpmanagerPerformanceDay> util = new ExcelUtil<StoreOpmanagerPerformanceDay>(StoreOpmanagerPerformanceDay.class);
        return util.exportExcel(list, "opmanagerPerformancDay");
    }

    /**
     * 新增业务员每日绩效
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存业务员每日绩效
     */
    @RequiresPermissions("shop:opmanagerPerformancDay:add")
    @Log(title = "业务员每日绩效", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay) {
        return toAjax(storeOpmanagerPerformanceDayService.insertStoreOpmanagerPerformanceDay(storeOpmanagerPerformanceDay));
    }

    /**
     * 修改业务员每日绩效
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay = storeOpmanagerPerformanceDayService.selectStoreOpmanagerPerformanceDayById(id);
        mmap.put("storeOpmanagerPerformanceDay", storeOpmanagerPerformanceDay);
        return prefix + "/edit";
    }

    /**
     * 修改保存业务员每日绩效
     */
    @RequiresPermissions("shop:opmanagerPerformancDay:edit")
    @Log(title = "业务员每日绩效", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay) {
        return toAjax(storeOpmanagerPerformanceDayService.updateStoreOpmanagerPerformanceDay(storeOpmanagerPerformanceDay));
    }

    /**
     * 删除业务员每日绩效
     */
    @RequiresPermissions("shop:opmanagerPerformancDay:remove")
    @Log(title = "业务员每日绩效", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeOpmanagerPerformanceDayService.deleteStoreOpmanagerPerformanceDayByIds(ids));
    }
}
