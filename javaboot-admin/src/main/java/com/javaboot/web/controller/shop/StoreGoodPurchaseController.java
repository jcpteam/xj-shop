package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.annotation.RepeatSubmit;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.dto.StoreGoodPurchaseDTO;
import com.javaboot.shop.dto.StoreGoodPurchaseQueryDTO;
import com.javaboot.shop.dto.StoreGoodsPurchaseExportDTO;
import com.javaboot.shop.mapper.StoreGoodPurchaseItemMapper;
import com.javaboot.shop.service.IStoreGoodPurchaseService;
import com.javaboot.shop.vo.StoreGoodPurchaseVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品采购Controller
 *
 * @author lqh
 * @date 2021-05-23
 */
@Controller
@RequestMapping("/shop/purchase")
public class StoreGoodPurchaseController extends BaseController {
    private String prefix = "shop/purchase";

    @Autowired
    private IStoreGoodPurchaseService storeGoodPurchaseService;

    @Autowired
    private StoreGoodPurchaseItemMapper storeGoodPurchaseItemMapper;

    @RequiresPermissions("shop:purchase:view")
    @GetMapping()
    public String purchase() {
        return prefix + "/purchase";
    }

    /**
     * 查询商品采购列表
     */
    @RequiresPermissions("shop:purchase:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodPurchaseQueryDTO storeGoodPurchase) {
        startPage();
        List<StoreGoodPurchaseVO> list = storeGoodPurchaseService.selectStoreGoodPurchaseList(storeGoodPurchase);
        return getDataTable(list);
    }

    /**
     * 导出商品采购列表
     */
    @RequiresPermissions("shop:purchase:export")
    @Log(title = "商品采购", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodPurchaseQueryDTO storeGoodPurchase) {
        List<StoreGoodsPurchaseExportDTO> list = storeGoodPurchaseItemMapper.selectExportGoodsPurchaseItemList(storeGoodPurchase);
        ExcelUtil<StoreGoodsPurchaseExportDTO> util = new ExcelUtil<>(StoreGoodsPurchaseExportDTO.class);
        return util.exportExcel(list, "采购单");
    }

    /**
     * 新增商品采购
     */
    @GetMapping("/add")
    public String add() {

        return prefix + "/add";
    }

    /**
     * 新增保存商品采购
     */
    @RequiresPermissions("shop:purchase:add")
    @Log(title = "商品采购", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(StoreGoodPurchaseDTO storeGoodPurchase) {
        return toAjax(storeGoodPurchaseService.insertStoreGoodPurchase(storeGoodPurchase));
    }

    /**
     * 修改商品采购
     */
    @GetMapping("/edit/{purchaseId}")
    public String edit(@PathVariable("purchaseId") Long purchaseId, ModelMap mmap) {
        StoreGoodPurchaseVO storeGoodPurchase = storeGoodPurchaseService.selectStoreGoodPurchaseById(purchaseId);
        mmap.put("storeGoodPurchase", storeGoodPurchase);
        return prefix + "/edit";
    }

    /**
     * 修改保存商品采购
     */
    @RequiresPermissions("shop:purchase:edit")
    @Log(title = "商品采购", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult editSave(StoreGoodPurchaseDTO storeGoodPurchase) {
        return toAjax(storeGoodPurchaseService.updateStoreGoodPurchase(storeGoodPurchase));
    }

    /**
     * 删除商品采购
     */
    @RequiresPermissions("shop:purchase:remove")
    @Log(title = "商品采购", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(storeGoodPurchaseService.deleteStoreGoodPurchaseByIds(ids));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 采购单生成入库单
     */
    @RequiresPermissions("shop:purchase:add")
    @Log(title = "采购单生成入库单", businessType = BusinessType.DELETE)
    @PostMapping("/purchase2stock")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult purchase2Stock(StoreGoodPurchaseDTO dto) {
        try {
            return toAjax(storeGoodPurchaseService.addStockByPurchase(dto));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
