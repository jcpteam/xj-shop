package com.javaboot.web.controller.shop;

import java.util.List;

import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StorePriceSetting;
import com.javaboot.shop.domain.StorePriceSettingDetail;
import com.javaboot.shop.service.IStorePriceSettingDetailService;
import com.javaboot.shop.service.IStorePriceSettingService;
import com.javaboot.system.domain.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 控价Controller
 * 
 * @author javaboot
 * @date 2021-06-24
 */
@Controller
@RequestMapping("/shop/price")
public class StorePriceSettingController extends BaseController {
    private String prefix = "shop/price";

    @Autowired
    private IStorePriceSettingService storePriceSettingService;

    @Autowired
    private IStorePriceSettingDetailService storePriceSettingDetailService;

    @RequiresPermissions("shop:price:view")
    @GetMapping()
    public String setting() {
        return prefix + "/price";
    }

    /**
     * 查询控价列表
     */
    @RequiresPermissions("shop:price:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StorePriceSetting storePriceSetting) {
        startPage();
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(StringUtils.isEmpty(storePriceSetting.getDeptId())){
            storePriceSetting.setDeptId(user.isAdmin()?null:user.getDeptId());
        }
        List<StorePriceSetting> list = storePriceSettingService.selectStorePriceSettingList(storePriceSetting);
        return getDataTable(list);
    }


    /**
     * 导出控价列表
     */
    @RequiresPermissions("shop:price:export")
    @Log(title = "控价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StorePriceSetting storePriceSetting) {
        List<StorePriceSetting> list = storePriceSettingService.selectStorePriceSettingList(storePriceSetting);
        ExcelUtil<StorePriceSetting> util = new ExcelUtil<StorePriceSetting>(StorePriceSetting.class);
        return util.exportExcel(list, "setting");
    }

    /**
     * 新增控价
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存控价
     */
    @RequiresPermissions("shop:price:add")
    @Log(title = "控价", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StorePriceSetting storePriceSetting) {
        if(StringUtils.isEmpty(storePriceSetting.getDeptId())){
            error("部门不能为空");
        }

        if(StringUtils.isEmpty(storePriceSetting.getOptDate())){
            error("控价日期不能为空");
        }
        StorePriceSetting  storePriceSetting1 = storePriceSettingService.selectStorePriceSettingByDeptId(storePriceSetting.getDeptId());
        if(storePriceSetting1 == null
                || storePriceSetting1.getAdjustCount()<=0
                ||  storePriceSetting1.getGoodsCount()<=0){
            return error("该部门没有报价单活销售商品，无法控价");
        }
        //todo:需要根据角色判定是0还是1
        storePriceSetting.setStatus("0");
        Integer  result = storePriceSettingService.insertStorePriceSetting(storePriceSetting);
        Long  settingId = storePriceSetting.getSettingId();
        String deptId = storePriceSetting.getDeptId();
        List<StorePriceSettingDetail> storePriceSettingDetails = storePriceSettingDetailService.selectStorePriceSettingDetailsByDeptId(deptId);
        for(StorePriceSettingDetail storePriceSettingDetail:storePriceSettingDetails){
            storePriceSettingDetail.setPriceId(settingId);
            storePriceSettingDetail.setSettingDate(storePriceSetting.getOptDate());
            storePriceSettingDetail.setStatus("1");
        }
        storePriceSettingDetailService.updateStorePriceSettingDetailByDeptId(deptId,"-1");
        int result2 = storePriceSettingDetailService.insertStorePriceSettingDetailBatch(storePriceSettingDetails);

        return toAjax(result >0 && result2>0);
    }

    @RequiresPermissions("shop:price:detail")
    @GetMapping("/detail/{settingId}")
    public String edit(@PathVariable("settingId") String settingId, ModelMap mmap) {
        StorePriceSetting storePriceSetting = storePriceSettingService.selectStorePriceSettingById(Long.valueOf(settingId));
        mmap.put("storePriceSetting",storePriceSetting);
        mmap.put("priceId", Integer.valueOf(settingId));
        return prefix + "/detail";
    }


    /**
     * 修改控价
     */
    @GetMapping("/edit/{settingId}")
    public String edit(@PathVariable("settingId") Long settingId, ModelMap mmap) {
        StorePriceSetting storePriceSetting = storePriceSettingService.selectStorePriceSettingById(settingId);
        mmap.put("storePriceSetting", storePriceSetting);
        return prefix + "/edit";
    }

    /**
     * 修改保存控价
     */
    @RequiresPermissions("shop:price:audit")
    @Log(title = "控价", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    @ResponseBody
    public AjaxResult auditSave(StorePriceSetting storePriceSetting) {
        //先失效所有的控价数据，在生效当前的控价
        StorePriceSetting storePriceSettingOld = new StorePriceSetting();
        storePriceSettingOld.setDeptId(storePriceSetting.getDeptId());
        storePriceSettingOld.setStatus("0");
        storePriceSettingService.updateStorePriceSetting(storePriceSettingOld);
        storePriceSetting.setStatus("2");
        return toAjax(storePriceSettingService.updateStorePriceSetting(storePriceSetting));
    }



    /**
     * 修改保存控价
     */
    @RequiresPermissions("shop:price:audit")
    @Log(title = "控价", businessType = BusinessType.UPDATE)
    @PostMapping("/auditBatch")
    @ResponseBody
    public AjaxResult auditBatch(@RequestBody String jsonObject) {
        //先失效所有的控价数据，在生效当前的控价
        return toAjax(storePriceSettingService.updateStorePriceSettingBatch(jsonObject));
    }


    /**
     * 修改保存控价
     */
    @RequiresPermissions("shop:price:edit")
    @Log(title = "控价", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StorePriceSetting storePriceSetting) {
        return toAjax(storePriceSettingService.updateStorePriceSetting(storePriceSetting));
    }

    /**
     * 删除控价
     */
    @RequiresPermissions("shop:price:remove")
    @Log(title = "控价", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storePriceSettingService.deleteStorePriceSettingByIds(ids));
    }
}
