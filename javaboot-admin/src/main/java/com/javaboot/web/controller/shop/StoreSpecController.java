package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreGoodsType;
import com.javaboot.shop.domain.StoreSpec;
import com.javaboot.shop.service.IStoreGoodsTypeService;
import com.javaboot.shop.service.IStoreSpecService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品规格(独立)Controller
 *
 * @author javaboot
 * @date 2019-08-25
 */
@Controller
@RequestMapping("/shop/spec")
public class StoreSpecController extends BaseController {
    private String prefix = "shop/spec";

    @Autowired
    private IStoreSpecService storeSpecService;
    @Autowired
    private IStoreGoodsTypeService storeGoodsTypeService;

    @RequiresPermissions("shop:spec:view")
    @GetMapping()
    public String spec() {
        return prefix + "/spec";
    }

    /**
     * 查询商品规格(独立)列表
     */
    @RequiresPermissions("shop:spec:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreSpec storeSpec) {
        startPage();
        List<StoreSpec> list = storeSpecService.selectStoreSpecList(storeSpec);
        for (StoreSpec spec: list) {
            Integer typeId = spec.getTypeId();
            StoreGoodsType storeGoodsType = storeGoodsTypeService.selectStoreGoodsTypeById(typeId.intValue());
            spec.setTypeName(storeGoodsType.getName());
        }
        return getDataTable(list);
    }

    /**
     * 导出商品规格(独立)列表
     */
    @RequiresPermissions("shop:spec:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreSpec storeSpec) {
        List<StoreSpec> list = storeSpecService.selectStoreSpecList(storeSpec);
        ExcelUtil<StoreSpec> util = new ExcelUtil<StoreSpec>(StoreSpec.class);
        return util.exportExcel(list, "spec");
    }

    /**
     * 新增商品规格(独立)
     */
    @GetMapping("/add")
    public String add(Model model) {
        List<StoreGoodsType> list = storeGoodsTypeService.selectStoreGoodsTypeList(new StoreGoodsType());
        model.addAttribute("types", list);
        return prefix + "/add";
    }

    /**
     * 新增保存商品规格(独立)
     */
    @RequiresPermissions("shop:spec:add")
    @Log(title = "商品规格(独立)", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreSpec storeSpec) {
        return toAjax(storeSpecService.insertStoreSpec(storeSpec));
    }

    /**
     * 修改商品规格(独立)
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        StoreSpec storeSpec = storeSpecService.selectStoreSpecById(id);
        mmap.put("storeSpec", storeSpec);
        List<StoreGoodsType> list = storeGoodsTypeService.selectStoreGoodsTypeList(new StoreGoodsType());
        mmap.put("types", list);

        return prefix + "/edit";
    }

    /**
     * 修改保存商品规格(独立)
     */
    @RequiresPermissions("shop:spec:edit")
    @Log(title = "商品规格(独立)", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreSpec storeSpec) {
        return toAjax(storeSpecService.updateStoreSpec(storeSpec));
    }

    /**
     * 删除商品规格(独立)
     */
    @RequiresPermissions("shop:spec:remove")
    @Log(title = "商品规格(独立)", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeSpecService.deleteStoreSpecByIds(ids));
    }
}
