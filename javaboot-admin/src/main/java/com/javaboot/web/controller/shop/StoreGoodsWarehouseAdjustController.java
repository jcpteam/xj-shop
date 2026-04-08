package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.annotation.RepeatSubmit;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreGoodsWarehouseAdjust;
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.dto.StoreGoodsAdjustExportDTO;
import com.javaboot.shop.dto.StoreGoodsWarehouseAdjustDTO;
import com.javaboot.shop.mapper.StoreGoodsWarehouseAdjustMapper;
import com.javaboot.shop.service.IStoreGoodsWarehouseAdjustService;
import com.javaboot.shop.service.IStoreHouseService;
import com.javaboot.shop.vo.StoreGoodsWarehouseAdjustVO;
import com.javaboot.system.domain.SysDept;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品调拨信息Controller
 * 
 * @author lqh
 * @date 2021-06-10
 */
@Controller
@RequestMapping("/shop/adjust")
public class StoreGoodsWarehouseAdjustController extends BaseController {
    private String prefix = "shop/adjust";

    @Autowired
    private IStoreGoodsWarehouseAdjustService storeGoodsWarehouseAdjustService;

    @Autowired
    private IStoreHouseService storeHouseService;

    @Autowired
    private StoreGoodsWarehouseAdjustMapper storeGoodsWarehouseAdjustMapper;

    @RequiresPermissions("shop:adjust:view")
    @GetMapping()
    public String adjust(ModelMap mmap) {
        StoreHouse storeHouse=new StoreHouse();
        storeHouse.setStatus(Constants.NORMAL);
        mmap.put("houseList",   storeHouseService.selectStoreHouseList(storeHouse));
        return prefix + "/adjust";
    }

    /**
     * 查询商品调拨信息列表
     */
    @RequiresPermissions("shop:adjust:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodsWarehouseAdjust storeGoodsWarehouseAdjust) {
        startPage();
        List<StoreGoodsWarehouseAdjustVO> list = storeGoodsWarehouseAdjustService.selectStoreGoodsWarehouseAdjustList(storeGoodsWarehouseAdjust);
        return getDataTable(list);
    }

    /**
     * 导出商品调拨信息列表
     */
    @RequiresPermissions("shop:adjust:export")
    @Log(title = "商品调拨信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodsWarehouseAdjust storeGoodsWarehouseAdjust) {
        List<StoreGoodsAdjustExportDTO> list = storeGoodsWarehouseAdjustMapper.selectExportGoodsAdjustItemList(storeGoodsWarehouseAdjust);
        StoreHouse storeHouse=new StoreHouse();
        storeHouse.setStatus(Constants.NORMAL);
        List<StoreHouse> houseList = storeHouseService.selectStoreHouseList(storeHouse);
        if(CollectionUtils.isNotEmpty(houseList)){
            Map<Long, StoreHouse> houseMap = houseList.stream()
                .collect(Collectors.toMap(StoreHouse::getStoreId, Function.identity(), (key1, key2) -> key2));
            list.forEach(s->{
                StoreHouse sourceWarehouse= houseList.stream().filter(h->(h.getStoreId()+"").equals(s.getSourceWarehouseId())).findFirst().orElse(null);
                StoreHouse targetWarehouse= houseList.stream().filter(h->(h.getStoreId()+"").equals(s.getTargetWarehouseId())).findFirst().orElse(null);
                if(sourceWarehouse!=null){
                    s.setSourceWarehouseName(sourceWarehouse.getStoreName());
                }
                if(targetWarehouse!=null){
                    s.setTargetWarehouseName(targetWarehouse.getStoreName());
                }
            });
        }
        ExcelUtil<StoreGoodsAdjustExportDTO> util = new ExcelUtil<StoreGoodsAdjustExportDTO>(StoreGoodsAdjustExportDTO.class);
        return util.exportExcel(list, "调拨单");
    }

    /**
     * 新增商品调拨信息
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        StoreHouse storeHouse=new StoreHouse();
        storeHouse.setStatus(Constants.NORMAL);
        mmap.put("houseList",   storeHouseService.selectStoreHouseList(storeHouse));
        return prefix + "/add";
    }

    /**
     * 新增保存商品调拨信息
     */
    @RequiresPermissions("shop:adjust:add")
    @Log(title = "商品调拨信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(StoreGoodsWarehouseAdjustDTO storeGoodsWarehouseAdjust) {
        return toAjax(storeGoodsWarehouseAdjustService.insertStoreGoodsWarehouseAdjust(storeGoodsWarehouseAdjust));
    }

    /**
     * 修改商品调拨信息
     */
    @GetMapping("/edit/{adjustId}")
    public String edit(@PathVariable("adjustId") Long adjustId, ModelMap mmap) {
        StoreGoodsWarehouseAdjust storeGoodsWarehouseAdjust = storeGoodsWarehouseAdjustService.selectStoreGoodsWarehouseAdjustById(adjustId);
        mmap.put("storeGoodsWarehouseAdjust", storeGoodsWarehouseAdjust);
        StoreHouse storeHouse=new StoreHouse();
        storeHouse.setStatus(Constants.NORMAL);
        mmap.put("houseList",   storeHouseService.selectStoreHouseList(storeHouse));
        return prefix + "/edit";
    }

    /**
     * 修改保存商品调拨信息
     */
    @RequiresPermissions("shop:adjust:edit")
    @Log(title = "商品调拨信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult editSave(StoreGoodsWarehouseAdjustDTO storeGoodsWarehouseAdjust) {
        return toAjax(storeGoodsWarehouseAdjustService.updateStoreGoodsWarehouseAdjust(storeGoodsWarehouseAdjust));
    }

    /**
     * 删除商品调拨信息
     */
    @RequiresPermissions("shop:adjust:remove")
    @Log(title = "商品调拨信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(storeGoodsWarehouseAdjustService.deleteStoreGoodsWarehouseAdjustByIds(ids));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }


    /**
     * 调拨单生成入库单
     */
    @RequiresPermissions("shop:adjust:add")
    @Log(title = "调拨单生成入库单", businessType = BusinessType.DELETE)
    @PostMapping("/adjust2stock")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult adjust2stock(StoreGoodsWarehouseAdjustDTO dto) {
        return toAjax(storeGoodsWarehouseAdjustService.addStockByAdjust(dto));
    }
}
