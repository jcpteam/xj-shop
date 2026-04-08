package com.javaboot.web.controller.shop;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.javaboot.common.annotation.RepeatSubmit;
import com.javaboot.common.enums.OrderLogType;
import com.javaboot.common.enums.OrderSource;
import com.javaboot.common.enums.OrderStatus;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.shop.domain.StoreGoodsOrderItem;
import com.javaboot.shop.domain.StoreSortingPerformanceStand;
import com.javaboot.shop.dto.StoreGoodsOrderDTO;
import com.javaboot.shop.service.*;
import com.javaboot.shop.vo.StoreGoodsOrderItemVO;
import com.javaboot.shop.vo.StoreGoodsOrderSortingCountVO;
import com.javaboot.shop.vo.StoreGoodsOrderVO;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
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
import com.javaboot.shop.domain.StoreSortingDetail;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 分拣记录详情Controller
 * 
 * @author javaboot
 * @date 2021-07-05
 */
@Controller
@RequestMapping("/shop/sortingRecordDetail")
public class StoreSortingDetailController extends BaseController {
    private String prefix = "shop/sortingRecordDetail";

    @Autowired
    private IStoreSortingDetailService storeSortingDetailService;

    @RequiresPermissions("shop:sortingRecordDetail:view")
    @GetMapping()
    public String sortingRecordDetail() {
        return prefix + "/sortingRecordDetail";
    }

    /**
     * 查询分拣记录详情列表
     */
    @RequiresPermissions("shop:sortingRecordDetail:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreSortingDetail storeSortingDetail) {
        startPage();
        List<StoreSortingDetail> list = storeSortingDetailService.selectStoreSortingDetailList(storeSortingDetail);
        return getDataTable(list);
    }

    /**
     * 导出分拣记录详情列表
     */
    @RequiresPermissions("shop:sortingRecordDetail:export")
    @Log(title = "分拣记录详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreSortingDetail storeSortingDetail) {
        List<StoreSortingDetail> list = storeSortingDetailService.selectStoreSortingDetailList(storeSortingDetail);
        ExcelUtil<StoreSortingDetail> util = new ExcelUtil<StoreSortingDetail>(StoreSortingDetail.class);
        return util.exportExcel(list, "sortingRecordDetail");
    }

    /**
     * 新增分拣记录详情
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存分拣记录详情
     */
    @Log(title = "分拣记录详情", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(StoreSortingDetail storeSortingDetail) {
        return storeSortingDetailService.saveStoreSortingDetail(storeSortingDetail);
    }



    /**
     * 修改分拣记录详情
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreSortingDetail storeSortingDetail = storeSortingDetailService.selectStoreSortingDetailById(id);
        mmap.put("storeSortingDetail", storeSortingDetail);
        return prefix + "/edit";
    }

    /**
     * 修改保存分拣记录详情
     */
    @RequiresPermissions("shop:sortingRecordDetail:edit")
    @Log(title = "分拣记录详情", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreSortingDetail storeSortingDetail) {
        return toAjax(storeSortingDetailService.updateStoreSortingDetail(storeSortingDetail));
    }

    /**
     * 删除分拣记录详情
     */
    @RequiresPermissions("shop:sortingRecordDetail:remove")
    @Log(title = "分拣记录详情", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeSortingDetailService.deleteStoreSortingDetailByIds(ids));
    }
}
