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
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.service.IStoreHouseService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 仓库信息Controller
 * 
 * @author javaboot
 * @date 2021-05-25
 */
@Controller
@RequestMapping("/shop/house")
public class StoreHouseController extends BaseController {
    private String prefix = "shop/house";

    @Autowired
    private IStoreHouseService storeHouseService;

    @RequiresPermissions("shop:house:view")
    @GetMapping()
    public String house() {
        return prefix + "/house";
    }

    /**
     * 查询仓库信息列表
     */
    @RequiresPermissions("shop:house:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreHouse storeHouse) {
        startPage();
        List<StoreHouse> list = storeHouseService.selectStoreHouseList(storeHouse);
        return getDataTable(list);
    }

    /**
     * 导出仓库信息列表
     */
    @RequiresPermissions("shop:house:export")
    @Log(title = "仓库信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreHouse storeHouse) {
        List<StoreHouse> list = storeHouseService.selectStoreHouseList(storeHouse);
        ExcelUtil<StoreHouse> util = new ExcelUtil<StoreHouse>(StoreHouse.class);
        return util.exportExcel(list, "house");
    }

    /**
     * 新增仓库信息
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存仓库信息
     */
    @RequiresPermissions("shop:house:add")
    @Log(title = "仓库信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreHouse storeHouse) {
        return toAjax(storeHouseService.insertStoreHouse(storeHouse));
    }

    /**
     * 修改仓库信息
     */
    @GetMapping("/edit/{storeId}")
    public String edit(@PathVariable("storeId") Long storeId, ModelMap mmap) {
        StoreHouse storeHouse = storeHouseService.selectStoreHouseById(storeId);
        mmap.put("storeHouse", storeHouse);
        return prefix + "/edit";
    }

    /**
     * 修改保存仓库信息
     */
    @RequiresPermissions("shop:house:edit")
    @Log(title = "仓库信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreHouse storeHouse) {
        return toAjax(storeHouseService.updateStoreHouse(storeHouse));
    }

    /**
     * 删除仓库信息
     */
    @RequiresPermissions("shop:house:remove")
    @Log(title = "仓库信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeHouseService.deleteStoreHouseByIds(ids));
    }
}
