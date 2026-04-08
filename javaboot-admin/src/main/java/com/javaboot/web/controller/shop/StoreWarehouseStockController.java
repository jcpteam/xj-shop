package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.enums.StockCategory;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.dto.StoreStockExportDTO;
import com.javaboot.shop.dto.StoreWarehouseStockDTO;
import com.javaboot.shop.dto.StoreWarehouseStockQueryDTO;
import com.javaboot.shop.mapper.StoreGoodsSalesUnitMapper;
import com.javaboot.shop.mapper.StoreWarehouseStockItemMapper;
import com.javaboot.shop.service.IStoreHouseService;
import com.javaboot.shop.service.IStoreWarehouseStockService;
import com.javaboot.shop.vo.StoreStockForApp;
import com.javaboot.shop.vo.StoreWarehouseStockVO;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品入库Controller
 * 
 * @author lqh
 * @date 2021-05-20
 */
@Api("入库单信息")
@Controller
@RequestMapping("/shop/stock")
public class StoreWarehouseStockController extends BaseController {
    private String prefix = "shop/stock";

    @Autowired
    private IStoreWarehouseStockService storeWarehouseStockService;
    @Autowired
    private IStoreHouseService storeHouseService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private StoreWarehouseStockItemMapper storeWarehouseStockItemMapper;
    @Autowired
    private StoreGoodsSalesUnitMapper storeGoodsSalesUnitMapper;



    @RequiresPermissions("shop:stock:view")
    @GetMapping(value={"", "/finance"})
    public String stock() {
        return prefix + "/stock";
    }


    @RequiresPermissions("shop:stock:view")
    @GetMapping(value={"/stat"})
    public String stockStat() {
        return prefix + "/stockstat";
    }

    /**
     * 查询商品入库列表
     */
    @RequiresPermissions("shop:stock:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreWarehouseStockQueryDTO storeWarehouseStock) {
        // 获取权限菜单
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        Set<String> menus = menuService.selectPermsByUserId(user.getUserId());
        // 查询审核状态为1,2的
        if(CollectionUtils.isNotEmpty(menus) && menus.contains("shop:financeStock:list")) {
            storeWarehouseStock.setAuditStatus("1");
        }
        startPage();
        List<StoreWarehouseStockVO> list = storeWarehouseStockService.selectStoreWarehouseStockList(storeWarehouseStock);
        return getDataTable(list);
    }

    /**
     * 导出商品入库列表
     */
    @RequiresPermissions("shop:stock:export")
    @Log(title = "商品入库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreWarehouseStockQueryDTO storeWarehouseStock) {
//        if(StringUtils.isEmpty(storeWarehouseStock.getBeginDate()) && StringUtils.isEmpty(storeWarehouseStock.getEndDate())) {
//            String today = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
//            storeWarehouseStock.setBeginDate(today.concat(" 00:00:00"));
//            storeWarehouseStock.setEndDate(today.concat(" 23:59:59"));
//        }
        List<StoreStockExportDTO> list = storeWarehouseStockItemMapper.selectExportStockItemList(storeWarehouseStock);
        // 填充主副单位
        List<StoreGoodsSalesUnit> units = storeGoodsSalesUnitMapper.selectStoreGoodsSalesUnitList(new StoreGoodsSalesUnit());
        if(CollectionUtils.isNotEmpty(list)) {
            Map<String, StoreGoodsSalesUnit> spuUnitMap = units.stream().collect(Collectors.toMap(key->(key.getUnitId()+""), obj->obj));
            list.forEach(obj->{
                obj.setMainUnitName(obj.getMainUnit());
                StoreGoodsSalesUnit mainUnitObj = spuUnitMap.get(obj.getMainUnit());
                if(mainUnitObj != null) {
                    obj.setMainUnitName(mainUnitObj.getName());
                }
                obj.setSubUnitName(obj.getSubUnit());
                StoreGoodsSalesUnit subUnitObj = spuUnitMap.get(obj.getSubUnit());
                if(subUnitObj != null) {
                    obj.setSubUnitName(subUnitObj.getName());
                }
            });
        }
        ExcelUtil<StoreStockExportDTO> util = new ExcelUtil<>(StoreStockExportDTO.class);
        return util.exportExcel(list, "入库单");
    }

    /**
     * 新增商品入库
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("houseList",   getHouseList());
        return prefix + "/add";
    }
    private List<StoreHouse> getHouseList(){
        StoreHouse storeHouse=new StoreHouse();
        storeHouse.setStatus(Constants.NORMAL);
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(!user.isAdmin()){
            storeHouse.setDeptId(user.getDeptId());
        }
        return storeHouseService.selectStoreHouseList(storeHouse);
    }

    /**
     * 新增保存商品入库
     */
    @RequiresPermissions("shop:stock:add")
    @Log(title = "商品入库", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreWarehouseStockDTO dto) {
        dto.setCategory(StockCategory.NOMARL.getCode());
        return toAjax(storeWarehouseStockService.insertStoreWarehouseStock(dto));
    }

    /**
     * 入库单详情
     */
    @PostMapping("/detail/{stockId}")
    @ResponseBody
    @ApiOperation("入库单详情")
    public StoreStockForApp detail(@PathVariable("stockId") Long stockId) {

        StoreStockForApp stockForApp = new StoreStockForApp();
        StoreWarehouseStockVO storeWarehouseStock = storeWarehouseStockService.selectStoreWarehouseStockById(stockId);
        stockForApp.setHouseList(getHouseList());
        stockForApp.setStoreWarehouseStock(storeWarehouseStock);
        return stockForApp;
    }
    /**
     * 修改商品入库
     */
    @GetMapping("/edit/{stockId}")
    public String edit(@PathVariable("stockId") Long stockId, ModelMap mmap) {
        StoreWarehouseStockVO storeWarehouseStock = storeWarehouseStockService.selectStoreWarehouseStockById(stockId);
        mmap.put("houseList",  getHouseList());
        mmap.put("storeWarehouseStock", storeWarehouseStock);
        return prefix + "/edit";
    }

    /**
     * 修改保存商品入库
     */
    @RequiresPermissions("shop:stock:edit")
    @Log(title = "商品入库", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreWarehouseStockDTO dto) {
        try {
            return toAjax(storeWarehouseStockService.updateStoreWarehouseStock(dto, true));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除商品入库
     */
    @RequiresPermissions("shop:stock:remove")
    @Log(title = "商品入库", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeWarehouseStockService.deleteStoreWarehouseStockByIds(ids));
    }


    /**
     * 审核订单信息主
     */
    @RequiresPermissions("shop:order:edit")
    @Log(title = "审核入库单信息主", businessType = BusinessType.UPDATE)
    @PostMapping("/audit/stock")
    @ResponseBody
    public AjaxResult auditStock(StoreWarehouseStockDTO dto) {
        return toAjax(storeWarehouseStockService.auditStock(dto));
    }

}
