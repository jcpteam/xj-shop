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
import com.javaboot.shop.domain.StoreDeliverRoute;
import com.javaboot.shop.service.IStoreDeliverRouteService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 线路信息Controller
 * 
 * @author javaboot
 * @date 2021-05-25
 */
@Controller
@RequestMapping("/shop/route")
public class StoreDeliverRouteController extends BaseController {
    private String prefix = "shop/route";

    @Autowired
    private IStoreDeliverRouteService storeDeliverRouteService;

    @RequiresPermissions("shop:route:view")
    @GetMapping()
    public String route() {
        return prefix + "/route";
    }

    /**
     * 查询线路信息列表
     */
    @RequiresPermissions("shop:route:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreDeliverRoute storeDeliverRoute) {
        startPage();
        List<StoreDeliverRoute> list = storeDeliverRouteService.selectStoreDeliverRouteList(storeDeliverRoute);
        return getDataTable(list);
    }

    /**
     * 导出线路信息列表
     */
//    @RequiresPermissions("shop:route:export")
    @Log(title = "线路信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreDeliverRoute storeDeliverRoute) {
        List<StoreDeliverRoute> list = storeDeliverRouteService.selectStoreDeliverRouteListForExport(storeDeliverRoute);
        ExcelUtil<StoreDeliverRoute> util = new ExcelUtil<StoreDeliverRoute>(StoreDeliverRoute.class);
        return util.exportExcel(list, "线路信息");
    }

    /**
     * 新增线路信息
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存线路信息
     */
    @RequiresPermissions("shop:route:add")
    @Log(title = "线路信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreDeliverRoute storeDeliverRoute) {
        return toAjax(storeDeliverRouteService.insertStoreDeliverRoute(storeDeliverRoute));
    }

    /**
     * 修改线路信息
     */
    @GetMapping("/edit/{routeId}")
    public String edit(@PathVariable("routeId") Long routeId, ModelMap mmap) {
        StoreDeliverRoute storeDeliverRoute = storeDeliverRouteService.selectStoreDeliverRouteById(routeId);
        mmap.put("storeDeliverRoute", storeDeliverRoute);
        return prefix + "/edit";
    }

    /**
     * 修改保存线路信息
     */
    @RequiresPermissions("shop:route:edit")
    @Log(title = "线路信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreDeliverRoute storeDeliverRoute) {
        return toAjax(storeDeliverRouteService.updateStoreDeliverRoute(storeDeliverRoute));
    }

    /**
     * 删除线路信息
     */
    @RequiresPermissions("shop:route:remove")
    @Log(title = "线路信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeDeliverRouteService.deleteStoreDeliverRouteByIds(ids));
    }
}
