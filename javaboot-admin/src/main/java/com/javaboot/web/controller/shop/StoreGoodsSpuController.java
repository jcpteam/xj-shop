package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreGoodsSpu;
import com.javaboot.shop.dto.StoreGoodsSpuDTO;
import com.javaboot.shop.service.IStoreGoodsSpuService;
import com.javaboot.shop.vo.StoreGoodsSpuVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品SPU信息Controller
 *
 * @author lqh
 * @date 2021-05-23
 */
@Controller
@RequestMapping("/shop/spu")
public class StoreGoodsSpuController extends BaseController {
    private String prefix = "shop/spu";

    @Autowired
    private IStoreGoodsSpuService storeGoodsSpuService;

    @RequiresPermissions("shop:spu:view")
    @GetMapping()
    public String spu() {
        return prefix + "/spu";
    }

    /**
     * 查询商品SPU信息列表
     */
    @RequiresPermissions("shop:spu:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodsSpuDTO storeGoodsSpu) {
        if(!storeGoodsSpu.getIsQueryAll()){
            startPage();
        }
        List<StoreGoodsSpuVO> list = storeGoodsSpuService.selectStoreGoodsSpuList(storeGoodsSpu);
        return getDataTable(list);
    }


    /**
     * 导出商品SPU信息列表
     */
    @RequiresPermissions("shop:spu:export")
    @Log(title = "商品SPU信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodsSpuDTO storeGoodsSpu) {
        List<StoreGoodsSpuVO> list = storeGoodsSpuService.selectStoreGoodsSpuList(storeGoodsSpu);
        ExcelUtil<StoreGoodsSpuVO> util = new ExcelUtil<StoreGoodsSpuVO>(StoreGoodsSpuVO.class);
        return util.exportExcel(list, "spu");
    }

    /**
     * 新增商品SPU信息
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存商品SPU信息
     */
    @RequiresPermissions("shop:spu:add")
    @Log(title = "商品SPU信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreGoodsSpu storeGoodsSpu) {
        return toAjax(storeGoodsSpuService.insertStoreGoodsSpu(storeGoodsSpu));
    }

    /**
     * 修改商品SPU信息
     */
    @GetMapping("/edit/{spuNo}")
    public String edit(@PathVariable("spuNo") String spuNo, ModelMap mmap) {
        StoreGoodsSpuVO storeGoodsSpu = storeGoodsSpuService.selectStoreGoodsSpuById(spuNo);
        mmap.put("storeGoodsSpu", storeGoodsSpu);
        return prefix + "/edit";
    }

    /**
     * 修改保存商品SPU信息
     */
    @RequiresPermissions("shop:spu:edit")
    @Log(title = "商品SPU信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreGoodsSpu storeGoodsSpu) {
        return toAjax(storeGoodsSpuService.updateStoreGoodsSpu(storeGoodsSpu));
    }

    /**
     * 删除商品SPU信息
     */
    @RequiresPermissions("shop:spu:remove")
    @Log(title = "商品SPU信息", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeGoodsSpuService.deleteStoreGoodsSpuByIds(ids));
    }
}
