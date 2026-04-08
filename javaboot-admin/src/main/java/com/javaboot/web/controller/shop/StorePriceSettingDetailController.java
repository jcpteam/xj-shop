package com.javaboot.web.controller.shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.enums.OrderPayStatus;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.service.IStorePriceSettingDetailService;
import com.javaboot.shop.service.IStorePriceSettingService;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 控价详情Controller
 * 
 * @author javaboot
 * @date 2021-07-04
 */
@Controller
@RequestMapping("/price/detail")
public class StorePriceSettingDetailController extends BaseController {
    private String prefix = "/price/detail";

    @Autowired
    private IStorePriceSettingDetailService storePriceSettingDetailService;

    @Autowired
    private IStorePriceSettingService storePriceSettingService;

    @RequiresPermissions("price:detail:view")
    @GetMapping()
    public String detail() {
        return prefix + "/detail";
    }

    /**
     * 查询控价详情列表
     */
    @RequiresPermissions("price:detail:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StorePriceSettingDetail storePriceSettingDetail) {
        startPage();
//        List<StorePriceSettingDetail> list = storePriceSettingDetailService.selectStorePriceSettingDetailList(storePriceSettingDetail);
        if(StringUtils.isNotEmpty(storePriceSettingDetail.getQuotationIds())){
            storePriceSettingDetail.setQuotationIdList(Arrays.asList(storePriceSettingDetail.getQuotationIds().split(",")));
        }
        if(StringUtils.isNotEmpty(storePriceSettingDetail.getSpuNos())){
            storePriceSettingDetail.setSpuNoList(Arrays.asList(storePriceSettingDetail.getSpuNos().split(",")));
        }
        List<StorePriceSettingDetail> list = storePriceSettingDetailService.selectStorePriceSettingDetailsByPriceId(storePriceSettingDetail);
        return getDataTable(list);
    }

    /**
     * 查询控价详情列表
     */
//    @RequiresPermissions("price:detail:list")
    @PostMapping("/listForParams")
    @ResponseBody
    public TableDataInfo listForParams(StorePriceSettingDetail storePriceSettingDetail) {
        List<StorePriceSettingDetail> list = storePriceSettingDetailService.selectStorePriceSettingDetailsByPriceIdForParams(storePriceSettingDetail);
        return getDataTable(list);
    }

    /**
     * 导出控价详情列表
     */
    @RequiresPermissions("price:detail:export")
    @Log(title = "控价详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StorePriceSettingDetail storePriceSettingDetail) {
        if(StringUtils.isNotEmpty(storePriceSettingDetail.getQuotationIds())){
            storePriceSettingDetail.setQuotationIdList(Arrays.asList(storePriceSettingDetail.getQuotationIds().split(",")));
        }
        if(StringUtils.isNotEmpty(storePriceSettingDetail.getSpuNos())){
            storePriceSettingDetail.setSpuNoList(Arrays.asList(storePriceSettingDetail.getSpuNos().split(",")));
        }
        List<StorePriceSettingDetail> list = storePriceSettingDetailService.selectStorePriceSettingDetailsByPriceId(storePriceSettingDetail);
        ExcelUtil<StorePriceSettingDetail> util = new ExcelUtil<StorePriceSettingDetail>(StorePriceSettingDetail.class);
        return util.exportExcel(list, "控价单详情");
    }


    @Log(title = "导入报价单" , businessType = BusinessType.OTHER)
    @RequestMapping("/import")
    @ResponseBody
    public AjaxResult importExcel(MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {

        ExcelUtil<StorePriceSettingDetail> util = new ExcelUtil<StorePriceSettingDetail>(StorePriceSettingDetail.class);
        List<StorePriceSettingDetail> receiptList = util.importExcel(file.getInputStream());
        if(CollectionUtils.isNotEmpty(receiptList)) {
            List<StorePriceSettingDetail> storePriceSettingDetails = new ArrayList<>();
            for (StorePriceSettingDetail storeReceipt: receiptList)
            {
                StorePriceSettingDetail storePriceSettingDetail = new StorePriceSettingDetail();
                storePriceSettingDetail.setGoodsId(storeReceipt.getGoodsId());
                storePriceSettingDetail.setPrice(storeReceipt.getPrice());
                storePriceSettingDetail.setSettingDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,new Date()));
//                storePriceSettingDetailService.updateStorePriceSettingDetail(storeReceipt);
                storePriceSettingDetails.add(storePriceSettingDetail);
            }
            storePriceSettingDetailService.updateStorePriceSettingDetailBatch(storePriceSettingDetails);
        }

        return AjaxResult.success("导入成功!");
    }

    /**
     * 新增控价详情
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存控价详情
     */
    @RequiresPermissions("price:detail:add")
    @Log(title = "控价详情", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(String  deptId,String optDate) {
        return toAjax(storePriceSettingDetailService.insertStorePriceSettingDetailByDeptId(deptId,optDate));
    }

    /**
     * 修改控价详情
     */
    @GetMapping("/edit/{settingId}")
    public String edit(@PathVariable("settingId") Integer settingId, ModelMap mmap) {
        StorePriceSettingDetail storePriceSettingDetail = storePriceSettingDetailService.selectStorePriceSettingDetailById(settingId);
        mmap.put("storePriceSettingDetail", storePriceSettingDetail);
        return prefix + "/edit";
    }

    /**
     * 修改保存控价详情
     */
    @RequiresPermissions("price:detail:edit")
    @Log(title = "控价详情", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody StorePriceSettingDetail storePriceSettingDetail) {
        StorePriceSetting storePriceSetting = storePriceSettingService.selectStorePriceSettingById(storePriceSettingDetail.getPriceId());
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(user.isAdmin()){
            return toAjax(storePriceSettingDetailService.updateStorePriceSettingDetail(storePriceSettingDetail));
        }else{
            if(storePriceSetting.getStatus().equals("2")){
                return AjaxResult.error("已审核无法修改提交");
            }else{
//                storePriceSetting.setSubmitStatus("1");
                storePriceSettingService.updateStorePriceSetting(storePriceSetting);
                return toAjax(storePriceSettingDetailService.updateStorePriceSettingDetail(storePriceSettingDetail));
            }
        }
    }


    /**
     * 修改保存控价详情
     */
    @RequiresPermissions("price:detail:edit")
    @Log(title = "控价详情", businessType = BusinessType.UPDATE)
    @PostMapping("/editStatus")
    @ResponseBody
    public AjaxResult editStatus(@RequestBody StorePriceSettingDetail storePriceSettingDetail) {
        StorePriceSetting storePriceSetting = storePriceSettingService.selectStorePriceSettingById(storePriceSettingDetail.getPriceId());
        if(storePriceSetting.getStatus().equals("2")){
            return AjaxResult.error("已审核无法提交");
        }
//        storePriceSetting.setSettingId(storePriceSettingDetail.getPriceId());
        storePriceSetting.setSubmitStatus("1");
        return toAjax(storePriceSettingService.updateStorePriceSetting(storePriceSetting));
    }

    /**
     * 删除控价详情
     */
    @RequiresPermissions("price:detail:remove")
    @Log(title = "控价详情", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {

        return toAjax(storePriceSettingDetailService.deleteStorePriceSettingDetailByIds(ids));
    }
}
