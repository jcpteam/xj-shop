package com.javaboot.web.controller.shop;

import java.util.Arrays;
import java.util.List;

import com.javaboot.common.constant.Constants;
import com.javaboot.shop.domain.StoreGoodsSpuUnitConversion;
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.service.IStoreGoodsSalesUnitService;
import com.javaboot.shop.service.IStoreGoodsSpuUnitConversionService;
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
import com.javaboot.shop.domain.StoreGoodsSpuUnit;
import com.javaboot.shop.service.IStoreGoodsSpuUnitService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * spu单位Controller
 * 
 * @author lqh
 * @date 2021-06-20
 */
@Controller
@RequestMapping("/shop/spuUnit")
public class StoreGoodsSpuUnitController extends BaseController {
    private String prefix = "shop/spuUnit";

    @Autowired
    private IStoreGoodsSpuUnitService storeGoodsSpuUnitService;
    @Autowired
    private IStoreGoodsSpuUnitConversionService storeGoodsSpuUnitConversionService;
    @Autowired
    private IStoreGoodsSalesUnitService storeGoodsSalesUnitService;
    @RequiresPermissions("shop:unit:view")
    @GetMapping()
    public String unit() {
        return prefix + "/unit";
    }

    /**
     * 查询spu单位列表
     */
    @RequiresPermissions("shop:unit:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodsSpuUnit storeGoodsSpuUnit) {
        startPage();
        List<StoreGoodsSpuUnit> list = storeGoodsSpuUnitService.selectStoreGoodsSpuUnitList(storeGoodsSpuUnit);
        return getDataTable(list);
    }

    /**
     * 导出spu单位列表
     */
    @RequiresPermissions("shop:unit:export")
    @Log(title = "spu单位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodsSpuUnit storeGoodsSpuUnit) {
        List<StoreGoodsSpuUnit> list = storeGoodsSpuUnitService.selectStoreGoodsSpuUnitList(storeGoodsSpuUnit);
        ExcelUtil<StoreGoodsSpuUnit> util = new ExcelUtil<StoreGoodsSpuUnit>(StoreGoodsSpuUnit.class);
        return util.exportExcel(list, "unit");
    }

    /**
     * 新增spu单位
     */
    @GetMapping("/add/{spuNo}")
    public String add(@PathVariable("spuNo") String spuNos,ModelMap mmap) {
        StoreGoodsSpuUnit storeGoodsSpuUnit = new StoreGoodsSpuUnit();
        storeGoodsSpuUnit.setSpuNos(spuNos);
        mmap.put("storeGoodsSpuUnit", storeGoodsSpuUnit);
        mmap.put("unitIdList",  storeGoodsSalesUnitService.getNormalSpecificationsList());
        return prefix + "/add";
    }
    /**
     * 新增保存spu单位
     */
    @RequiresPermissions("shop:unit:add")
    @Log(title = "spu单位", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreGoodsSpuUnit storeGoodsSpuUnit) {
        StoreGoodsSpuUnitConversion query=new StoreGoodsSpuUnitConversion();
        query.setSpuNoList(Arrays.asList(storeGoodsSpuUnit.getSpuNos().split(",")));
        List<StoreGoodsSpuUnitConversion> list= storeGoodsSpuUnitConversionService.selectStoreGoodsSpuUnitConversionList(query);
        if(CollectionUtils.isNotEmpty(list)){
            return AjaxResult.error("已有转换关系不能新增");
        }
        storeGoodsSpuUnit.setSpuNoList(query.getSpuNoList());
        return toAjax(storeGoodsSpuUnitService.insertStoreGoodsSpuUnit(storeGoodsSpuUnit));
    }

    /**
     * 修改spu单位
     */
    @GetMapping("/edit/{spuNo}")
    public String edit(@PathVariable("spuNo") String spuNo, ModelMap mmap) {
        StoreGoodsSpuUnit storeGoodsSpuUnit = storeGoodsSpuUnitService.selectStoreGoodsSpuUnitById(spuNo);
        mmap.put("unitIdList",  storeGoodsSalesUnitService.getNormalSpecificationsList());
        mmap.put("storeGoodsSpuUnit", storeGoodsSpuUnit);
        return prefix + "/edit";
    }

    /**
     * 修改保存spu单位
     */
    @RequiresPermissions("shop:unit:edit")
    @Log(title = "spu单位", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreGoodsSpuUnit storeGoodsSpuUnit) {
        StoreGoodsSpuUnitConversion query=new StoreGoodsSpuUnitConversion();
        query.setSpuNo(storeGoodsSpuUnit.getSpuNo());
//        List<StoreGoodsSpuUnitConversion> list= storeGoodsSpuUnitConversionService.selectStoreGoodsSpuUnitConversionList(query);
//        if(CollectionUtils.isNotEmpty(list)){
//            return AjaxResult.error("已有转换关系不能修改");
//        }
        return toAjax(storeGoodsSpuUnitService.updateStoreGoodsSpuUnit(storeGoodsSpuUnit));
    }

    /**
     * 删除spu单位
     */
    @RequiresPermissions("shop:unit:remove")
    @Log(title = "spu单位", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        StoreGoodsSpuUnitConversion query=new StoreGoodsSpuUnitConversion();
        query.setSpuNoList(Arrays.asList(ids.split(",")));
        List<StoreGoodsSpuUnitConversion> list= storeGoodsSpuUnitConversionService.selectStoreGoodsSpuUnitConversionList(query);
        if(CollectionUtils.isNotEmpty(list)){
            return AjaxResult.error("已有转换关系不能删除");
        }
        return toAjax(storeGoodsSpuUnitService.deleteStoreGoodsSpuUnitByIds(ids));
    }
}
