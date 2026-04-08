package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreGoods;
import com.javaboot.shop.domain.StoreSource;
import com.javaboot.shop.domain.StoreSpec;
import com.javaboot.shop.domain.StoreSpecItem;
import com.javaboot.shop.service.*;
import com.javaboot.shop.service.IStoreSourceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 源码管理
 *
 * @author javaboot
 * @date 2019-08-29
 */
@Controller
@RequestMapping("/shop/source")
public class StoreSourceController extends BaseController {
    private String prefix = "shop/source";

    @Autowired
    private IStoreSourceService storeSourceService;
    @Autowired
    private IStoreGoodsService goodsService;
    @Autowired
    private IStoreSpecService specService;
    @Autowired
    private IStoreSpecItemService specItemService;


    @RequiresPermissions("shop:source:view")
    @GetMapping()
    public String source() {
        return prefix + "/source";
    }

    /**
     * 查询源码列表
     */
    @RequiresPermissions("shop:source:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreSource storeSource) {
        startPage();
        List<StoreSource> list = storeSourceService.selectStoreSourceList(storeSource);
        return getDataTable(list);
    }

    /**
     * 导出源码列表
     */
    @RequiresPermissions("shop:source:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreSource storeSource) {
        List<StoreSource> list = storeSourceService.selectStoreSourceList(storeSource);
        ExcelUtil<StoreSource> util = new ExcelUtil<StoreSource>(StoreSource.class);
        return util.exportExcel(list, "source");
    }

    /**
     * 新增源码
     */
    @GetMapping("/add")
    public String add(Model model) {
        //商品列表
        List<StoreGoods> goodsList = goodsService.selectStoreGoodsList(new StoreGoods());
        model.addAttribute("goodsList", goodsList);
        //规格
        List<StoreSpec> specList = specService.selectStoreSpecList(new StoreSpec());
        model.addAttribute("specList", specList);
        //规格项
        List<StoreSpecItem> specItemList = specItemService.selectStoreSpecItemList(new StoreSpecItem());
        model.addAttribute("specItemList", specItemList);

        return prefix + "/add";
    }

    /**
     * 新增保存源码
     */
    @RequiresPermissions("shop:source:add")
    @Log(title = "源码", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreSource storeSource) {
        return toAjax(storeSourceService.insertStoreSource(storeSource));
    }

    /**
     * 修改源码
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        StoreSource storeSource = storeSourceService.selectStoreSourceById(id);
        model.addAttribute("storeSource", storeSource);

        //商品列表
        List<StoreGoods> goodsList = goodsService.selectStoreGoodsList(new StoreGoods());
        model.addAttribute("goodsList", goodsList);
        //规格
        List<StoreSpec> specList = specService.selectStoreSpecList(new StoreSpec());
        model.addAttribute("specList", specList);
        //规格项
        List<StoreSpecItem> specItemList = specItemService.selectStoreSpecItemList(new StoreSpecItem());
        model.addAttribute("specItemList", specItemList);

        return prefix + "/edit";
    }

    /**
     * 修改保存源码
     */
    @RequiresPermissions("shop:source:edit")
    @Log(title = "源码", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreSource storeSource) {
        return toAjax(storeSourceService.updateStoreSource(storeSource));
    }

    /**
     * 删除源码
     */
    @RequiresPermissions("shop:source:remove")
    @Log(title = "源码", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeSourceService.deleteStoreSourceByIds(ids));
    }
}
