package com.javaboot.web.controller.shop;

import java.util.List;

import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.system.domain.SysUser;
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
import com.javaboot.shop.domain.StoreHomepageAds;
import com.javaboot.shop.service.IStoreHomepageAdsService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 商城首页广告信息Controller
 * 
 * @author javaboot
 * @date 2021-06-05
 */
@Controller
@RequestMapping("/shop/homepageAd")
public class StoreHomepageAdsController extends BaseController {
    private String prefix = "shop/homepageAd";

    @Autowired
    private IStoreHomepageAdsService storeHomepageAdsService;

    @RequiresPermissions("shop:homepageAd:view")
    @GetMapping()
    public String homepageAd() {
        return prefix + "/homepageAd";
    }

    /**
     * 查询商城首页广告信息列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreHomepageAds storeHomepageAds) {
        startPage();
        List<StoreHomepageAds> list = storeHomepageAdsService.selectStoreHomepageAdsList(storeHomepageAds);
        return getDataTable(list);
    }

    /**
     * 导出商城首页广告信息列表
     */
    @RequiresPermissions("shop:homepageAd:export")
    @Log(title = "商城首页广告信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreHomepageAds storeHomepageAds) {
        List<StoreHomepageAds> list = storeHomepageAdsService.selectStoreHomepageAdsList(storeHomepageAds);
        ExcelUtil<StoreHomepageAds> util = new ExcelUtil<StoreHomepageAds>(StoreHomepageAds.class);
        return util.exportExcel(list, "homepageAd");
    }

    /**
     * 新增商城首页广告信息
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存商城首页广告信息
     */
    @RequiresPermissions("shop:homepageAd:add")
    @Log(title = "商城首页广告信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreHomepageAds storeHomepageAds) {
        SysUser sysUser = ShiroUtils.getSysUser();
        storeHomepageAds.setCreatorId(sysUser.getUserId());
        return toAjax(storeHomepageAdsService.insertStoreHomepageAds(storeHomepageAds));
    }

    /**
     * 修改商城首页广告信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreHomepageAds storeHomepageAds = storeHomepageAdsService.selectStoreHomepageAdsById(id);
        mmap.put("storeHomepageAds", storeHomepageAds);
        return prefix + "/edit";
    }

    /**
     * 修改保存商城首页广告信息
     */
    @RequiresPermissions("shop:homepageAd:edit")
    @Log(title = "商城首页广告信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreHomepageAds storeHomepageAds) {
        return toAjax(storeHomepageAdsService.updateStoreHomepageAds(storeHomepageAds));
    }

    /**
     * 删除商城首页广告信息
     */
    @RequiresPermissions("shop:homepageAd:remove")
    @Log(title = "商城首页广告信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeHomepageAdsService.deleteStoreHomepageAdsByIds(ids));
    }
}
