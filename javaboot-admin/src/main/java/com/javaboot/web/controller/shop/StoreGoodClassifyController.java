package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreGoodsClassify;
import com.javaboot.shop.service.IStoreGoodsClassifyService;
import com.javaboot.shop.vo.StoreGoodsClassifyTreeVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类Controller
 *
 * @author lqh
 * @date 2021-05-23
 */
@Controller
@RequestMapping("/shop/classify")
public class StoreGoodClassifyController extends BaseController {
    private String prefix = "shop/classify";

    @Autowired
    private IStoreGoodsClassifyService storeGoodClassifyService;

    @RequiresPermissions("shop:classify:view")
    @GetMapping()
    public String classify() {
        return prefix + "/classify";
    }

    /**
     * 查询商品分类列表
     */
    @RequiresPermissions("shop:classify:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodsClassify storeGoodClassify) {
        startPage();
        List<StoreGoodsClassify> list = storeGoodClassifyService.selectStoreGoodClassifyList(storeGoodClassify);
        return getDataTable(list);
    }

    /**
     * 查询商品分类列表
     */
    @PostMapping("/appList")
    @ResponseBody
    public TableDataInfo appList(StoreGoodsClassify storeGoodClassify) {
        return list(storeGoodClassify);
    }

    /**
     * 导出商品分类列表
     */
    @RequiresPermissions("shop:classify:export")
    @Log(title = "商品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodsClassify storeGoodClassify) {
        List<StoreGoodsClassify> list = storeGoodClassifyService.selectStoreGoodClassifyList(storeGoodClassify);
        ExcelUtil<StoreGoodsClassify> util = new ExcelUtil<StoreGoodsClassify>(StoreGoodsClassify.class);
        return util.exportExcel(list, "classify");
    }

    /**
     * 新增商品分类
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存商品分类
     */
    @RequiresPermissions("shop:classify:add")
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreGoodsClassify storeGoodClassify) {
        return toAjax(storeGoodClassifyService.insertStoreGoodClassify(storeGoodClassify));
    }

    /**
     * 修改商品分类
     */
    @GetMapping("/edit/{classifyId}")
    public String edit(@PathVariable("classifyId") String classifyId, ModelMap mmap) {
        StoreGoodsClassify storeGoodClassify = storeGoodClassifyService.selectStoreGoodClassifyById(classifyId);
        mmap.put("storeGoodClassify", storeGoodClassify);
        return prefix + "/edit";
    }

    /**
     * 修改保存商品分类
     */
    @RequiresPermissions("shop:classify:edit")
    @Log(title = "商品分类", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreGoodsClassify storeGoodClassify) {
        return toAjax(storeGoodClassifyService.updateStoreGoodClassify(storeGoodClassify));
    }

    /**
     * 删除商品分类
     */
    @RequiresPermissions("shop:classify:remove")
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeGoodClassifyService.deleteStoreGoodClassifyByIds(ids));
    }

    /**
     * 获取树结构分类
     */
    @RequiresPermissions("shop:classify:list")
    @PostMapping("/queryClassifyTree")
    @ResponseBody
    public TableDataInfo queryClassifyTree() {
        List<StoreGoodsClassifyTreeVO> list= storeGoodClassifyService.queryClassifyTree();
        return getDataTable(list);
    }
}
