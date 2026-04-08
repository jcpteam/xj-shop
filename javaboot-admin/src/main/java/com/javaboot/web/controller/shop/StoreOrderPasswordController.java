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
import com.javaboot.shop.domain.StoreOrderPassword;
import com.javaboot.shop.service.IStoreOrderPasswordService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 订单密码Controller
 * 
 * @author javaboot
 * @date 2021-08-07
 */
@Controller
@RequestMapping("/shop/orderpassword")
public class StoreOrderPasswordController extends BaseController {
    private String prefix = "shop/orderpassword";

    @Autowired
    private IStoreOrderPasswordService storeOrderPasswordService;

    @RequiresPermissions("shop:orderpassword:list")
    @GetMapping()
    public String orderpassword() {
        return prefix + "/orderpassword";
    }

    /**
     * 查询订单密码列表
     */
    @RequiresPermissions("shop:orderpassword:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreOrderPassword storeOrderPassword) {
        startPage();
        List<StoreOrderPassword> list = storeOrderPasswordService.selectStoreOrderPasswordList(storeOrderPassword);
        return getDataTable(list);
    }

    /**
     * 导出订单密码列表
     */
    @RequiresPermissions("shop:orderpassword:export")
    @Log(title = "订单密码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreOrderPassword storeOrderPassword) {
        List<StoreOrderPassword> list = storeOrderPasswordService.selectStoreOrderPasswordList(storeOrderPassword);
        ExcelUtil<StoreOrderPassword> util = new ExcelUtil<StoreOrderPassword>(StoreOrderPassword.class);
        return util.exportExcel(list, "orderpassword");
    }

    /**
     * 新增订单密码
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存订单密码
     */
    @RequiresPermissions("shop:orderpassword:add")
    @Log(title = "订单密码", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreOrderPassword storeOrderPassword) {
        return toAjax(storeOrderPasswordService.insertStoreOrderPassword(storeOrderPassword));
    }

    /**
     * 修改订单密码
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreOrderPassword storeOrderPassword = storeOrderPasswordService.selectStoreOrderPasswordById(id);
        mmap.put("storeOrderPassword", storeOrderPassword);
        return prefix + "/edit";
    }

    /**
     * 修改保存订单密码
     */
    @RequiresPermissions("shop:orderpassword:edit")
    @Log(title = "订单密码", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreOrderPassword storeOrderPassword) {
        return toAjax(storeOrderPasswordService.updateStoreOrderPassword(storeOrderPassword));
    }

    /**
     * 删除订单密码
     */
    @RequiresPermissions("shop:orderpassword:remove")
    @Log(title = "订单密码", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeOrderPasswordService.deleteStoreOrderPasswordByIds(ids));
    }
}
