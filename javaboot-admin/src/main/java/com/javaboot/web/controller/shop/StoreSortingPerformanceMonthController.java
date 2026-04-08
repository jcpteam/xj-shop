package com.javaboot.web.controller.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.shop.domain.StoreSortingDailyWork;
import com.javaboot.shop.domain.StoreSortingDetail;
import com.javaboot.shop.domain.StoreSortingPerformanceDay;
import com.javaboot.shop.service.IStoreSortingDailyWorkService;
import com.javaboot.shop.service.IStoreSortingDetailService;
import com.javaboot.shop.service.IStoreSortingPerformanceDayService;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
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
import com.javaboot.shop.domain.StoreSortingPerformanceMonth;
import com.javaboot.shop.service.IStoreSortingPerformanceMonthService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 分拣绩效月份Controller
 *
 * @author javaboot
 * @date 2021-07-20
 */
@Controller
@RequestMapping("/shop/performanceMonth")
public class StoreSortingPerformanceMonthController extends BaseController {
    private String prefix = "shop/performanceMonth";

    @Autowired
    private IStoreSortingPerformanceMonthService storeSortingPerformanceMonthService;

    @RequiresPermissions("shop:performanceMonth:view")
    @GetMapping()
    public String performanceMonth() {
        return prefix + "/performanceMonth";
    }

    /**
     * 查询分拣绩效月份列表
     */
    @RequiresPermissions("shop:performanceMonth:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreSortingPerformanceMonth storeSortingPerformanceMonth) {
        startPage();
        List<StoreSortingPerformanceMonth> list = storeSortingPerformanceMonthService.selectStoreSortingPerformanceMonthList(storeSortingPerformanceMonth);
        return getDataTable(list);
    }

    /**
     * 导出分拣绩效月份列表
     */
    @RequiresPermissions("shop:performanceMonth:export")
    @Log(title = "分拣绩效月份", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreSortingPerformanceMonth storeSortingPerformanceMonth) {
        List<StoreSortingPerformanceMonth> list = storeSortingPerformanceMonthService.selectStoreSortingPerformanceMonthList(storeSortingPerformanceMonth);
        ExcelUtil<StoreSortingPerformanceMonth> util = new ExcelUtil<StoreSortingPerformanceMonth>(StoreSortingPerformanceMonth.class);
        return util.exportExcel(list, "分拣员绩效");
    }

    /**
     * 新增分拣绩效月份
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存分拣绩效月份
     */
    @RequiresPermissions("shop:performanceMonth:add")
    @Log(title = "分拣绩效月份", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreSortingPerformanceMonth storeSortingPerformanceMonth) {
        return toAjax(storeSortingPerformanceMonthService.insertStoreSortingPerformanceMonth(storeSortingPerformanceMonth));
    }

    /**
     * 修改分拣绩效月份
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreSortingPerformanceMonth storeSortingPerformanceMonth = storeSortingPerformanceMonthService.selectStoreSortingPerformanceMonthById(id);
        mmap.put("storeSortingPerformanceMonth", storeSortingPerformanceMonth);
        return prefix + "/edit";
    }

    /**
     * 修改保存分拣绩效月份
     */
    @RequiresPermissions("shop:performanceMonth:edit")
    @Log(title = "分拣绩效月份", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreSortingPerformanceMonth storeSortingPerformanceMonth) {
        return toAjax(storeSortingPerformanceMonthService.updateStoreSortingPerformanceMonth(storeSortingPerformanceMonth));
    }

    /**
     * 删除分拣绩效月份
     */
    @RequiresPermissions("shop:performanceMonth:remove")
    @Log(title = "分拣绩效月份", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeSortingPerformanceMonthService.deleteStoreSortingPerformanceMonthByIds(ids));
    }

    /**
     * 分拣绩效月份核算
     */
    @Log(title = "分拣绩效月份核算", businessType = BusinessType.INSERT)
    @PostMapping("/calculatePerformanceMonth")
    @ResponseBody
    public AjaxResult calculatePerformanceMonth(StoreSortingPerformanceMonth storeSortingPerformanceMonth) {
        return storeSortingPerformanceMonthService.calculatePerformanceMonth(storeSortingPerformanceMonth);
    }
}
