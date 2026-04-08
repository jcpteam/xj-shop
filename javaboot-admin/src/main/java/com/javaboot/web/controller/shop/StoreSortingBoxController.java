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
import com.javaboot.shop.domain.StoreSortingBox;
import com.javaboot.shop.service.IStoreSortingBoxService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 分拣框信息Controller
 * 
 * @author javaboot
 * @date 2021-06-11
 */
@Controller
@RequestMapping("/shop/sortingBox")
public class StoreSortingBoxController extends BaseController {
    private String prefix = "shop/sortingBox";

    @Autowired
    private IStoreSortingBoxService storeSortingBoxService;

    @RequiresPermissions("shop:sortingBox:view")
    @GetMapping()
    public String sortingBox() {
        return prefix + "/sortingBox";
    }

    /**
     * 查询分拣框信息列表
     */
    @RequiresPermissions("shop:sortingBox:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreSortingBox storeSortingBox) {
        startPage();
        List<StoreSortingBox> list = storeSortingBoxService.selectStoreSortingBoxList(storeSortingBox);
        return getDataTable(list);
    }

    /**
     * 导出分拣框信息列表
     */
    @RequiresPermissions("shop:sortingBox:export")
    @Log(title = "分拣框信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreSortingBox storeSortingBox) {
        List<StoreSortingBox> list = storeSortingBoxService.selectStoreSortingBoxList(storeSortingBox);
        ExcelUtil<StoreSortingBox> util = new ExcelUtil<StoreSortingBox>(StoreSortingBox.class);
        return util.exportExcel(list, "sortingBox");
    }

    /**
     * 新增分拣框信息
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存分拣框信息
     */
    @RequiresPermissions("shop:sortingBox:add")
    @Log(title = "分拣框信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreSortingBox storeSortingBox) {
        return toAjax(storeSortingBoxService.insertStoreSortingBox(storeSortingBox));
    }

    /**
     * 修改分拣框信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreSortingBox storeSortingBox = storeSortingBoxService.selectStoreSortingBoxById(id);
        mmap.put("storeSortingBox", storeSortingBox);
        return prefix + "/edit";
    }

    /**
     * 修改保存分拣框信息
     */
    @RequiresPermissions("shop:sortingBox:edit")
    @Log(title = "分拣框信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreSortingBox storeSortingBox) {
        return toAjax(storeSortingBoxService.updateStoreSortingBox(storeSortingBox));
    }

    /**
     * 删除分拣框信息
     */
    @RequiresPermissions("shop:sortingBox:remove")
    @Log(title = "分拣框信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeSortingBoxService.deleteStoreSortingBoxByIds(ids));
    }
}
