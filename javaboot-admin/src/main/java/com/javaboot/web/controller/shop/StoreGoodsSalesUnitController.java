package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import com.javaboot.shop.service.IStoreGoodsSalesUnitService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 销售单位Controller
 * 
 * @author lqh
 * @date 2021-05-29
 */
@Controller
@RequestMapping("/shop/unit")
public class StoreGoodsSalesUnitController extends BaseController {
    private String prefix = "shop/unit";

    @Autowired
    private IStoreGoodsSalesUnitService storeGoodsSalesUnitService;

    @RequiresPermissions("shop:unit:view")
    @GetMapping()
    public String unit() {
        return prefix + "/unit";
    }

    /**
     * 查询销售单位列表
     */
    @RequiresPermissions("shop:unit:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodsSalesUnit storeGoodsSalesUnit) {
        startPage();
        List<StoreGoodsSalesUnit> list = storeGoodsSalesUnitService.selectStoreGoodsSalesUnitList(storeGoodsSalesUnit);
        return getDataTable(list);
    }
    /**
     * 查询SPU销售单位列表
     */
    @RequiresPermissions("shop:unit:list")
    @PostMapping("/getSpuUnit")
    @ResponseBody
    public TableDataInfo getSpuUnit(@RequestParam("spuNo") String spuNo) {
        List<StoreGoodsSalesUnit> list = storeGoodsSalesUnitService.getSpuUnit(spuNo);
        return getDataTable(list);
    }
    /**
     * 导出销售单位列表
     */
    @RequiresPermissions("shop:unit:export")
    @Log(title = "销售单位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodsSalesUnit storeGoodsSalesUnit) {
        List<StoreGoodsSalesUnit> list = storeGoodsSalesUnitService.selectStoreGoodsSalesUnitList(storeGoodsSalesUnit);
        ExcelUtil<StoreGoodsSalesUnit> util = new ExcelUtil<StoreGoodsSalesUnit>(StoreGoodsSalesUnit.class);
        return util.exportExcel(list, "unit");
    }

    /**
     * 新增销售单位
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存销售单位
     */
    @RequiresPermissions("shop:unit:add")
    @Log(title = "销售单位", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreGoodsSalesUnit storeGoodsSalesUnit) {
        return toAjax(storeGoodsSalesUnitService.insertStoreGoodsSalesUnit(storeGoodsSalesUnit));
    }

    /**
     * 修改销售单位
     */
    @GetMapping("/edit/{unitId}")
    public String edit(@PathVariable("unitId") Long unitId, ModelMap mmap) {
        StoreGoodsSalesUnit storeGoodsSalesUnit = storeGoodsSalesUnitService.selectStoreGoodsSalesUnitById(unitId);
        mmap.put("storeGoodsSalesUnit", storeGoodsSalesUnit);
        return prefix + "/edit";
    }

    /**
     * 修改保存销售单位
     */
    @RequiresPermissions("shop:unit:edit")
    @Log(title = "销售单位", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreGoodsSalesUnit storeGoodsSalesUnit) {
        return toAjax(storeGoodsSalesUnitService.updateStoreGoodsSalesUnit(storeGoodsSalesUnit));
    }

    /**
     * 删除销售单位
     */
    @RequiresPermissions("shop:unit:remove")
    @Log(title = "销售单位", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(storeGoodsSalesUnitService.deleteStoreGoodsSalesUnitByIds(ids));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
