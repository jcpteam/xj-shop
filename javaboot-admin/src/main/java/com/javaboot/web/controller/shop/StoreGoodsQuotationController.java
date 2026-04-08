package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.enums.GoodsQuotationStatus;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreGoodsQuotation;
import com.javaboot.shop.dto.StoreGoodsQuotationQueryDTO;
import com.javaboot.shop.service.IStoreGoodsQuotationService;
import com.javaboot.shop.vo.StoreGoodsQuotationVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * 报价信息Controller
 *
 * @author lqh
 * @date 2021-05-18
 */
@Controller
@RequestMapping("/shop/quotation")
public class StoreGoodsQuotationController extends BaseController {
    private String prefix = "shop/quotation";

    @Autowired
    private IStoreGoodsQuotationService storeGoodsQuotationService;

    @RequiresPermissions("shop:quotation:list")
    @GetMapping()
    public String quotation() {

        return prefix + "/quotation";
    }


    /**
     * 查询报价信息列表
     */
    @RequiresPermissions("shop:quotation:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodsQuotationQueryDTO storeGoodsQuotation) {
        if(!storeGoodsQuotation.isQueryAll()) {
            startPage();
        }
        List<StoreGoodsQuotation> list = storeGoodsQuotationService.selectStoreGoodsQuotationList(storeGoodsQuotation);
        return getDataTable(list);
    }

    /**
     * 导出报价信息列表
     */
    @RequiresPermissions("shop:quotation:export")
    @Log(title = "报价信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodsQuotationQueryDTO storeGoodsQuotation) {
        List<StoreGoodsQuotation> list = storeGoodsQuotationService.selectStoreGoodsQuotationList(storeGoodsQuotation);
        ExcelUtil<StoreGoodsQuotation> util = new ExcelUtil<StoreGoodsQuotation>(StoreGoodsQuotation.class);
        return util.exportExcel(list, "quotation");
    }

    /**
     * 新增报价信息
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存报价信息
     */
    @RequiresPermissions("shop:quotation:add")
    @Log(title = "报价信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreGoodsQuotation storeGoodsQuotation) {
        caseStatus(true, storeGoodsQuotation);
        return toAjax(storeGoodsQuotationService.insertStoreGoodsQuotation(storeGoodsQuotation));
    }

    /**
     * 新增保存报价信息
     */
    @RequiresPermissions("shop:quotation:add")
    @Log(title = "复制报价信息", businessType = BusinessType.INSERT)
    @PostMapping("/copy")
    @ResponseBody
    public AjaxResult copy(StoreGoodsQuotation storeGoodsQuotation) {
        return toAjax(storeGoodsQuotationService.copyStoreGoodsQuotation(storeGoodsQuotation));
    }

    /**
     * 修改报价信息
     */
    @GetMapping("/edit/{quotationid}")
    public String edit(@PathVariable("quotationid") Long quotationid, ModelMap mmap) {
        StoreGoodsQuotation storeGoodsQuotation = storeGoodsQuotationService.selectStoreGoodsQuotationById(quotationid);
        caseStatus(false, storeGoodsQuotation);
        StoreGoodsQuotationVO vo = new StoreGoodsQuotationVO();
        BeanUtils.copyProperties(storeGoodsQuotation, vo);
        mmap.put("storeGoodsQuotation", vo);
        return prefix + "/edit";
    }

    /**
     * 修改保存报价信息
     */
    @RequiresPermissions("shop:quotation:edit")
    @Log(title = "报价信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreGoodsQuotationVO storeGoodsQuotation) {
        try {
            caseStatus(true, storeGoodsQuotation);
            return toAjax(storeGoodsQuotationService.updateStoreGoodsQuotation(storeGoodsQuotation));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除报价信息
     */
    @RequiresPermissions("shop:quotation:remove")
    @Log(title = "报价信息", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(storeGoodsQuotationService.removeStoreGoodsQuotationByIds(ids));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 转换状态
     *
     * @param save
     * @param storeGoodsQuotation
     */
    private void caseStatus(boolean save, StoreGoodsQuotation storeGoodsQuotation) {
        String value;
        if (save) {
            value = GoodsQuotationStatus.ON.getCode().equals(storeGoodsQuotation.getStatus()) ? GoodsQuotationStatus.ON.getValue() : GoodsQuotationStatus.OFF.getValue();
        } else {
            value = GoodsQuotationStatus.ON.getValue().equals(storeGoodsQuotation.getStatus()) ? GoodsQuotationStatus.ON.getCode() : GoodsQuotationStatus.OFF.getCode();

        }
        storeGoodsQuotation.setStatus(value);
    }


    /**
     * 根据登陆用户或报价单查询
     */
    @RequiresPermissions("shop:quotation:list")
    @PostMapping("/selectStoreGoodsQuotationListOfUserOrQuotation")
    @ResponseBody
    public TableDataInfo selectStoreGoodsQuotationListOfUserOrQuotation(StoreGoodsQuotationQueryDTO storeGoodsQuotation) {
        List<StoreGoodsQuotation> list = storeGoodsQuotationService.selectStoreGoodsQuotationListOfUserOrQuotation(storeGoodsQuotation);
        return getDataTable(list);
    }
}
