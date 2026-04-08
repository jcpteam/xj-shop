package com.javaboot.web.controller.shop;

import java.util.List;

import com.javaboot.shop.dto.StoreCustomerDTO;
import com.javaboot.shop.service.IStoreDeliverRouteService;
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
import com.javaboot.shop.domain.StoreRouteShop;
import com.javaboot.shop.service.IStoreRouteShopService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 线路和商户关联Controller
 *
 * @author javaboot
 * @date 2021-06-05
 */
@Controller
@RequestMapping("/shop/routeCustomer")
public class StoreRouteShopController extends BaseController {
    private String prefix = "shop/routeCustomer";

    @Autowired
    private IStoreRouteShopService storeRouteShopService;

    @Autowired
    private IStoreDeliverRouteService routeService;

    @RequiresPermissions("shop:routeCustomer:view")
    @GetMapping()
    public String routeCustomer() {
        return prefix + "/routeCustomer";
    }

    /**
     * 查询线路和商户关联列表
     */
//    @RequiresPermissions("shop:routeCustomer:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreRouteShop storeRouteShop) {
        startPage();
        List<StoreRouteShop> list = storeRouteShopService.selectStoreRouteShopList(storeRouteShop);
        return getDataTable(list);
    }

    /**
     * 导出线路和商户关联列表
     */
    @RequiresPermissions("shop:routeCustomer:export")
    @Log(title = "线路和商户关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreRouteShop storeRouteShop) {
        List<StoreRouteShop> list = storeRouteShopService.selectStoreRouteShopList(storeRouteShop);
        ExcelUtil<StoreRouteShop> util = new ExcelUtil<StoreRouteShop>(StoreRouteShop.class);
        return util.exportExcel(list, "routeCustomer");
    }

    /**
     * 新增线路和商户关联
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    @GetMapping("/add/{routeId}")
    public String addWithRouteId(@PathVariable("routeId") Long routeId, ModelMap mmap) {
        //查询用户信息、及用户角色部门信息列表返回
        mmap.put("routeInfo", routeService.selectStoreDeliverRouteById(routeId));
        return prefix + "/add";
    }

    /**
     * 新增保存线路和商户关联
     */
    @RequiresPermissions("shop:routeCustomer:add")
    @Log(title = "线路和商户关联", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreRouteShop storeRouteShop) {
        StoreRouteShop where = new StoreRouteShop();
        where.setRouteId(storeRouteShop.getRouteId());
        where.setCustomerId(storeRouteShop.getCustomerId());
        List<StoreRouteShop> exist = storeRouteShopService.selectStoreRouteShopList(where);
        if (exist != null && exist.size() > 0) {
            return AjaxResult.error("已经存在相同商户");
        }
        return toAjax(storeRouteShopService.insertStoreRouteShop(storeRouteShop));
    }

    /**
     * 修改线路和商户关联
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreRouteShop storeRouteShop = storeRouteShopService.selectStoreRouteShopById(id);
        mmap.put("storeRouteShop", storeRouteShop);
        return prefix + "/edit";
    }

    /**
     * 修改保存线路和商户关联
     */
    @RequiresPermissions("shop:routeCustomer:edit")
    @Log(title = "线路和商户关联", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreRouteShop storeRouteShop) {
        return toAjax(storeRouteShopService.updateStoreRouteShop(storeRouteShop));
    }

    /**
     * 删除线路和商户关联
     */
    @RequiresPermissions("shop:routeCustomer:remove")
    @Log(title = "线路和商户关联", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeRouteShopService.deleteStoreRouteShopByIds(ids));
    }

    /**
     * 查询线路关联的商户详细
     */
//    @RequiresPermissions("shop:routeCustomer:view")
    @GetMapping("/detail/{routeId}")
    public String detail(@PathVariable("routeId") Long routeId, ModelMap mmap) {
        //查询用户信息、及用户角色部门信息列表返回
        mmap.put("routeInfo", routeService.selectStoreDeliverRouteById(routeId));
        StoreRouteShop where = new StoreRouteShop();
        where.setRouteId(routeId);
        mmap.put("routeCustomerList", storeRouteShopService.selectStoreRouteShopList(where));
        return prefix + "/routeCustomer";
    }

    /**
     * 查询线路和商户关联列表
     */
    @PostMapping("/getCustomerList")
    @ResponseBody
    public TableDataInfo getCustomerList() {
//        startPage();
        List<StoreCustomerDTO> list = storeRouteShopService.getCustomerList();
        return getDataTable(list);
    }
}
