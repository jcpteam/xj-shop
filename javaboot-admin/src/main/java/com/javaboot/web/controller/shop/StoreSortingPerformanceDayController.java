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
import com.javaboot.shop.domain.StoreSortingPerformanceDay;
import com.javaboot.shop.service.IStoreSortingPerformanceDayService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 分拣绩效每日Controller
 * 
 * @author javaboot
 * @date 2021-07-20
 */
@Controller
@RequestMapping("/shop/performanceDay")
public class StoreSortingPerformanceDayController extends BaseController {
    private String prefix = "shop/performanceDay";

    @Autowired
    private IStoreSortingPerformanceDayService storeSortingPerformanceDayService;

    @RequiresPermissions("shop:performanceDay:view")
    @GetMapping()
    public String performanceDay() {
        return prefix + "/performanceDay";
    }

//    @RequiresPermissions("shop:performanceDay:view")
    @GetMapping("/detail/{monthId}")
    public String detail(@PathVariable("monthId") Long monthId, ModelMap mmap) {
        mmap.put("monthId",monthId);
        return prefix + "/performanceDay";
    }

    /**
     * 查询分拣绩效每日列表
     */
    @RequiresPermissions("shop:performanceDay:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreSortingPerformanceDay storeSortingPerformanceDay) {
        startPage();
        List<StoreSortingPerformanceDay> list = storeSortingPerformanceDayService.selectStoreSortingPerformanceDayList(storeSortingPerformanceDay);
        return getDataTable(list);
    }

    /**
     * 查询分拣绩效每日列表
     */
//    @RequiresPermissions("shop:performanceDay:list")
    @PostMapping("/list/{monthId}")
    @ResponseBody
    public TableDataInfo listWithMonthId(@PathVariable("monthId") Long monthId,StoreSortingPerformanceDay storeSortingPerformanceDay) {
        startPage();
        storeSortingPerformanceDay.setMonthId(monthId);
        List<StoreSortingPerformanceDay> list = storeSortingPerformanceDayService.selectStoreSortingPerformanceDayList(storeSortingPerformanceDay);
        return getDataTable(list);
    }

    /**
     * 导出分拣绩效每日列表
     */
    @RequiresPermissions("shop:performanceDay:export")
    @Log(title = "分拣绩效每日", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreSortingPerformanceDay storeSortingPerformanceDay) {
        List<StoreSortingPerformanceDay> list = storeSortingPerformanceDayService.selectStoreSortingPerformanceDayList(storeSortingPerformanceDay);
        ExcelUtil<StoreSortingPerformanceDay> util = new ExcelUtil<StoreSortingPerformanceDay>(StoreSortingPerformanceDay.class);
        return util.exportExcel(list, "performanceDay");
    }

    /**
     * 新增分拣绩效每日
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存分拣绩效每日
     */
    @RequiresPermissions("shop:performanceDay:add")
    @Log(title = "分拣绩效每日", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreSortingPerformanceDay storeSortingPerformanceDay) {
        return toAjax(storeSortingPerformanceDayService.insertStoreSortingPerformanceDay(storeSortingPerformanceDay));
    }

    /**
     * 修改分拣绩效每日
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreSortingPerformanceDay storeSortingPerformanceDay = storeSortingPerformanceDayService.selectStoreSortingPerformanceDayById(id);
        mmap.put("storeSortingPerformanceDay", storeSortingPerformanceDay);
        return prefix + "/edit";
    }

    /**
     * 修改保存分拣绩效每日
     */
    @RequiresPermissions("shop:performanceDay:edit")
    @Log(title = "分拣绩效每日", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreSortingPerformanceDay storeSortingPerformanceDay) {
        return toAjax(storeSortingPerformanceDayService.updateStoreSortingPerformanceDay(storeSortingPerformanceDay));
    }

    /**
     * 删除分拣绩效每日
     */
    @RequiresPermissions("shop:performanceDay:remove")
    @Log(title = "分拣绩效每日", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeSortingPerformanceDayService.deleteStoreSortingPerformanceDayByIds(ids));
    }
}
