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
import com.javaboot.shop.domain.StoreDriverInfo;
import com.javaboot.shop.service.IStoreDriverInfoService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 司机信息Controller
 * 
 * @author javaboot
 * @date 2021-05-25
 */
@Controller
@RequestMapping("/shop/driver")
public class StoreDriverInfoController extends BaseController {
    private String prefix = "shop/driver";

    @Autowired
    private IStoreDriverInfoService storeDriverInfoService;

    @RequiresPermissions("shop:driver:view")
    @GetMapping()
    public String driver() {
        return prefix + "/driver";
    }

    /**
     * 查询司机信息列表
     */
    @RequiresPermissions("shop:driver:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreDriverInfo storeDriverInfo) {
        startPage();
        List<StoreDriverInfo> list = storeDriverInfoService.selectStoreDriverInfoList(storeDriverInfo);
        return getDataTable(list);
    }

    /**
     * 导出司机信息列表
     */
    @RequiresPermissions("shop:driver:export")
    @Log(title = "司机信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreDriverInfo storeDriverInfo) {
        List<StoreDriverInfo> list = storeDriverInfoService.selectStoreDriverInfoList(storeDriverInfo);
        ExcelUtil<StoreDriverInfo> util = new ExcelUtil<StoreDriverInfo>(StoreDriverInfo.class);
        return util.exportExcel(list, "driver");
    }

    /**
     * 新增司机信息
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存司机信息
     */
    @RequiresPermissions("shop:driver:add")
    @Log(title = "司机信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreDriverInfo storeDriverInfo) {
        return toAjax(storeDriverInfoService.insertStoreDriverInfo(storeDriverInfo));
    }

    /**
     * 修改司机信息
     */
    @GetMapping("/edit/{driverId}")
    public String edit(@PathVariable("driverId") Long driverId, ModelMap mmap) {
        StoreDriverInfo storeDriverInfo = storeDriverInfoService.selectStoreDriverInfoById(driverId);
        mmap.put("storeDriverInfo", storeDriverInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存司机信息
     */
    @RequiresPermissions("shop:driver:edit")
    @Log(title = "司机信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreDriverInfo storeDriverInfo) {
        return toAjax(storeDriverInfoService.updateStoreDriverInfo(storeDriverInfo));
    }

    /**
     * 删除司机信息
     */
    @RequiresPermissions("shop:driver:remove")
    @Log(title = "司机信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeDriverInfoService.deleteStoreDriverInfoByIds(ids));
    }
}
