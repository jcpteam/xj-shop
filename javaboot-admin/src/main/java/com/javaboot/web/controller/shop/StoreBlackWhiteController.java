package com.javaboot.web.controller.shop;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BlackWhiteType;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreBlackWhite;
import com.javaboot.shop.dto.StorePaymentAccountQueryDTO;
import com.javaboot.shop.service.IStoreBlackWhiteService;
import com.javaboot.shop.service.IStorePaymentAccountService;
import com.javaboot.shop.vo.StoreBlackWhiteVO;
import com.javaboot.shop.vo.StorePaymentAccountVO;
import com.javaboot.system.domain.SysDictData;
import com.javaboot.system.domain.SysUser;

/**
 * 黑白名单Controller
 * 
 * @author lqh
 * @date 2021-07-08
 */
@Controller
@RequestMapping("/shop/blackWhite")
public class StoreBlackWhiteController extends BaseController {
    private String prefix = "shop/blackWhite";

    @Autowired
    private IStoreBlackWhiteService storeBlackWhiteService;


    @Autowired
    private IStorePaymentAccountService storePaymentAccountService;
    @RequiresPermissions("shop:blackWhite:view")
    @GetMapping()
    public String blackWhite(ModelMap mmap) {
        mmap.put("typeList", getTypeList());
        return prefix + "/blackWhite";
    }

    private List<SysDictData> getTypeList(){
        List<SysDictData> typeList = new ArrayList<>(3);
        typeList.add(new SysDictData(BlackWhiteType.White.getCode(), BlackWhiteType.White.getDesc()));
        typeList.add(new SysDictData(BlackWhiteType.Black.getCode(), BlackWhiteType.Black.getDesc()));
        typeList.add(new SysDictData(BlackWhiteType.WhiteForever.getCode(), BlackWhiteType.WhiteForever.getDesc()));
        return typeList;
    }
    /**
     * 查询黑白名单列表
     */
    @RequiresPermissions("shop:blackWhite:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreBlackWhite storeBlackWhite) {
        startPage();
        if(StringUtils.isEmpty(storeBlackWhite.getDeptId())){
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            if(!user.isAdmin()){
                storeBlackWhite.setDeptId(user.getDeptId());
            }
        }
        List<StoreBlackWhiteVO> list = storeBlackWhiteService.selectStoreBlackWhiteList(storeBlackWhite);
        return getDataTable(list);
    }

    /**
     * 导出黑白名单列表
     */
    @RequiresPermissions("shop:blackWhite:export")
    @Log(title = "黑白名单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreBlackWhite storeBlackWhite) {
        List<StoreBlackWhiteVO> list = storeBlackWhiteService.selectStoreBlackWhiteList(storeBlackWhite);
        ExcelUtil<StoreBlackWhiteVO> util = new ExcelUtil<StoreBlackWhiteVO>(StoreBlackWhiteVO.class);
        return util.exportExcel(list, "blackWhite");
    }

    /**
     * 新增黑白名单
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("typeList",  getTypeList());
        mmap.put("accountList",  getAccountList());
        return prefix + "/add";
    }

    private List<StorePaymentAccountVO> getAccountList(){
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        StorePaymentAccountQueryDTO query = new StorePaymentAccountQueryDTO();
        if(!user.isAdmin()){
            query.setDeptId(user.getDeptId());
        }
        return storePaymentAccountService.selectStorePaymentAccountList(query);
    }
    /**
     * 新增保存黑白名单
     */
    @RequiresPermissions("shop:blackWhite:add")
    @Log(title = "黑白名单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreBlackWhite storeBlackWhite) {
        return toAjax(storeBlackWhiteService.insertStoreBlackWhite(storeBlackWhite));
    }

    /**
     * 修改黑白名单
     */
    @GetMapping("/edit/{blackWhiteId}")
    public String edit(@PathVariable("blackWhiteId") Long blackWhiteId, ModelMap mmap) {
        StoreBlackWhite storeBlackWhite = storeBlackWhiteService.selectStoreBlackWhiteById(blackWhiteId);
        mmap.put("storeBlackWhite", storeBlackWhite);
        mmap.put("typeList", getTypeList());
        mmap.put("accountList",  getAccountList());
        return prefix + "/edit";
    }

    /**
     * 修改保存黑白名单
     */
    @RequiresPermissions("shop:blackWhite:edit")
    @Log(title = "黑白名单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreBlackWhite storeBlackWhite) {
        return toAjax(storeBlackWhiteService.updateStoreBlackWhite(storeBlackWhite));
    }

    /**
     * 删除黑白名单
     */
    @RequiresPermissions("shop:blackWhite:remove")
    @Log(title = "黑白名单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeBlackWhiteService.deleteStoreBlackWhiteByIds(ids));
    }

    /**
     * 删除黑白名单
     */
    @RequiresPermissions("shop:blackWhite:remove")
    @Log(title = "批量设置黑白名单", businessType = BusinessType.DELETE)
    @PostMapping( "/update/type")
    @ResponseBody
    public AjaxResult updatType(StoreBlackWhite storeBlackWhite) {
        return toAjax(storeBlackWhiteService.updateStoreBlackWhiteType(storeBlackWhite));
    }
}
