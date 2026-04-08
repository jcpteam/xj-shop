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
import com.javaboot.shop.domain.StoreGoodsSpuConversionNew;
import com.javaboot.shop.service.IStoreGoodsSpuConversionNewService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * spu转换比例Controller
 * 
 * @author javaboot
 * @date 2022-03-05
 */
@Controller
@RequestMapping("/shop/StoreGoodsSpuConversionNew")
public class StoreGoodsSpuConversionNewController extends BaseController {
    private String prefix = "shop/StoreGoodsSpuConversionNew";

    @Autowired
    private IStoreGoodsSpuConversionNewService storeGoodsSpuConversionNewService;

    @RequiresPermissions("shop:StoreGoodsSpuConversionNew:view")
    @GetMapping()
    public String StoreGoodsSpuConversionNew() {
        return prefix + "/StoreGoodsSpuConversionNew";
    }

    /**
     * 查询spu转换比例列表
     */
    @RequiresPermissions("shop:StoreGoodsSpuConversionNew:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodsSpuConversionNew storeGoodsSpuConversionNew) {
        startPage();
        List<StoreGoodsSpuConversionNew> list = storeGoodsSpuConversionNewService.selectStoreGoodsSpuConversionNewList(storeGoodsSpuConversionNew);
        return getDataTable(list);
    }

    /**
     * 导出spu转换比例列表
     */
    @RequiresPermissions("shop:StoreGoodsSpuConversionNew:export")
    @Log(title = "spu转换比例", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodsSpuConversionNew storeGoodsSpuConversionNew) {
        List<StoreGoodsSpuConversionNew> list = storeGoodsSpuConversionNewService.selectStoreGoodsSpuConversionNewList(storeGoodsSpuConversionNew);
        ExcelUtil<StoreGoodsSpuConversionNew> util = new ExcelUtil<StoreGoodsSpuConversionNew>(StoreGoodsSpuConversionNew.class);
        return util.exportExcel(list, "StoreGoodsSpuConversionNew");
    }

    /**
     * 新增spu转换比例
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存spu转换比例
     */
    @RequiresPermissions("shop:StoreGoodsSpuConversionNew:add")
    @Log(title = "spu转换比例", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreGoodsSpuConversionNew storeGoodsSpuConversionNew) {
        return toAjax(storeGoodsSpuConversionNewService.insertStoreGoodsSpuConversionNew(storeGoodsSpuConversionNew));
    }

    /**
     * 修改spu转换比例
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreGoodsSpuConversionNew storeGoodsSpuConversionNew = storeGoodsSpuConversionNewService.selectStoreGoodsSpuConversionNewById(id);
        mmap.put("storeGoodsSpuConversionNew", storeGoodsSpuConversionNew);
        return prefix + "/edit";
    }

    /**
     * 修改保存spu转换比例
     */
    @RequiresPermissions("shop:StoreGoodsSpuConversionNew:edit")
    @Log(title = "spu转换比例", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreGoodsSpuConversionNew storeGoodsSpuConversionNew) {
        return toAjax(storeGoodsSpuConversionNewService.updateStoreGoodsSpuConversionNew(storeGoodsSpuConversionNew));
    }

    /**
     * 删除spu转换比例
     */
    @RequiresPermissions("shop:StoreGoodsSpuConversionNew:remove")
    @Log(title = "spu转换比例", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeGoodsSpuConversionNewService.deleteStoreGoodsSpuConversionNewByIds(ids));
    }
}
