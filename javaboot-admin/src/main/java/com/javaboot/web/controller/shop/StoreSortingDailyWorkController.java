package com.javaboot.web.controller.shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreSortingDailyUser;
import com.javaboot.shop.service.IStoreSortingDailyUserService;
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
import com.javaboot.shop.domain.StoreSortingDailyWork;
import com.javaboot.shop.service.IStoreSortingDailyWorkService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 分拣每日考勤Controller
 * 
 * @author javaboot
 * @date 2021-06-11
 */
@Controller
@RequestMapping("/shop/sortingDailyWork")
public class StoreSortingDailyWorkController extends BaseController {
    private String prefix = "shop/sortingDailyWork";

    @Autowired
    private IStoreSortingDailyWorkService storeSortingDailyWorkService;

    @RequiresPermissions("shop:sortingDailyWork:view")
    @GetMapping()
    public String sortingDailyWork() {
        return prefix + "/sortingDailyWork";
    }

    /**
     * 查询分拣每日考勤列表
     */
    @RequiresPermissions("shop:sortingDailyWork:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreSortingDailyWork storeSortingDailyWork) {
        startPage();
        List<StoreSortingDailyWork> list = storeSortingDailyWorkService.selectStoreSortingDailyWorkList(storeSortingDailyWork);
        return getDataTable(list);
    }

    /**
     * 导出分拣每日考勤列表
     */
    @RequiresPermissions("shop:sortingDailyWork:export")
    @Log(title = "分拣每日考勤", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreSortingDailyWork storeSortingDailyWork) {
        List<StoreSortingDailyWork> list = storeSortingDailyWorkService.selectStoreSortingDailyWorkList(storeSortingDailyWork);
        ExcelUtil<StoreSortingDailyWork> util = new ExcelUtil<StoreSortingDailyWork>(StoreSortingDailyWork.class);
        return util.exportExcel(list, "sortingDailyWork");
    }

    /**
     * 新增分拣每日考勤
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存分拣每日考勤
     */
    @RequiresPermissions("shop:sortingDailyWork:add")
    @Log(title = "分拣每日考勤", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreSortingDailyWork storeSortingDailyWork) {
        return storeSortingDailyWorkService.saveStoreSortingDailyWork(storeSortingDailyWork);
    }



    /**
     * 修改分拣每日考勤
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreSortingDailyWork storeSortingDailyWork = storeSortingDailyWorkService.selectStoreSortingDailyWorkById(id);
        mmap.put("storeSortingDailyWork", storeSortingDailyWork);
        return prefix + "/edit";
    }

    /**
     * 修改保存分拣每日考勤
     */
    @RequiresPermissions("shop:sortingDailyWork:edit")
    @Log(title = "分拣每日考勤", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreSortingDailyWork storeSortingDailyWork) {
        StoreSortingDailyWork where = new StoreSortingDailyWork();
        where.setDeptId(storeSortingDailyWork.getDeptId());
        where.setWorkDay(storeSortingDailyWork.getWorkDay());
        List<StoreSortingDailyWork> existList = storeSortingDailyWorkService.selectStoreSortingDailyWorkList(where);
        if(CollectionUtils.isNotEmpty(existList)){
            return AjaxResult.error("已经存在该区域相同的考勤日期");
        }
        return toAjax(storeSortingDailyWorkService.updateStoreSortingDailyWork(storeSortingDailyWork));
    }

    /**
     * 删除分拣每日考勤
     */
    @RequiresPermissions("shop:sortingDailyWork:remove")
    @Log(title = "分拣每日考勤", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeSortingDailyWorkService.deleteStoreSortingDailyWorkByIds(ids));
    }
}
