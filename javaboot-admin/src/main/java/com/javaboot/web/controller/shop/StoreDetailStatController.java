package com.javaboot.web.controller.shop;

import com.github.pagehelper.PageHelper;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.AsGoodsQuotation;
import com.javaboot.shop.domain.AsLossStock;
import com.javaboot.shop.domain.AsOrderStock;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import com.javaboot.shop.dto.AsOrderStockDTO;
import com.javaboot.shop.dto.StoreStockExportDTO;
import com.javaboot.shop.dto.StoreWarehouseStockQueryDTO;
import com.javaboot.shop.mapper.AsOrderStockMapper;
import com.javaboot.shop.service.IAsLossStockService;
import com.javaboot.shop.service.IAsOrderStockService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shop/detail/stat")
public class StoreDetailStatController extends BaseController
{

    @Autowired
    private IAsOrderStockService asOrderStockService;

    @Autowired
    private IAsLossStockService asLossStockService;

    @Autowired
    private AsOrderStockMapper asOrderStockMapper;

    private String prefix = "/shop/stat";

    /**
     * 统计页面按照编号(1-10)
     */
    @GetMapping("/{pageName}")
    public String add(@PathVariable("pageName") String pageName, ModelMap mmap) {

        return prefix + "/" + pageName;
    }

    /**
     * 经营指标统计
     */
    @PostMapping("/report1")
    @ResponseBody
    public TableDataInfo ajaxReport1(AsOrderStockDTO asOrderStockDTO) {
        startPage();
        List<AsOrderStockDTO> list = asOrderStockService.deptDaySaleTotal(asOrderStockDTO);
        return getDataTable(list);
    }

    /**
     * 销售计划统计
     */
    @PostMapping("/report2")
    @ResponseBody
    public TableDataInfo ajaxReport2(AsOrderStockDTO asOrderStockDTO) {
        startPage();
        List<AsOrderStockDTO> list = asOrderStockService.deptMonthSaleTotal(asOrderStockDTO);
        return getDataTable(list);
    }

    /**
     * 物料销售统计-表头
     */
    @PostMapping("/report3/head")
    @ResponseBody
    public TableDataInfo ajaxReport3Head(AsOrderStockDTO asOrderStockDTO) {
        return getDataTable(asOrderStockMapper.selectTableHeader(asOrderStockDTO));
    }

    /**
     * 物料销售统计
     */
    @PostMapping("/report3")
    @ResponseBody
    public TableDataInfo ajaxReport3(AsOrderStockDTO asOrderStockDTO) {
        startPage();
        List<Map<String, String>> list = asOrderStockService.spuDaySaleTotal(asOrderStockDTO);
        return getDataTable(list);
    }

    /**
     * 客户销售累计
     */
    @PostMapping("/report5")
    @ResponseBody
    public TableDataInfo ajaxReport5(AsOrderStockDTO asOrderStockDTO) {
        List<AsOrderStockDTO> list = asOrderStockService.mechantSaleCalc(asOrderStockDTO);
        return getDataTable(list);
    }

    /**
     * 物料统计
     */
    @PostMapping("/report6")
    @ResponseBody
    public TableDataInfo ajaxReport6(AsOrderStockDTO asOrderStockDTO) {
        startPage();
        List<AsOrderStockDTO> list = asOrderStockService.deptSaleCalc(asOrderStockDTO);
        return getDataTable(list);
    }

    /**
     * 物料统计
     */
    @PostMapping("/report7")
    @ResponseBody
    public TableDataInfo ajaxReport7(AsOrderStockDTO asOrderStockDTO) {
        List<AsGoodsQuotation> list = asOrderStockService.calcAsGoodsQuotation(asOrderStockDTO);
        return getDataTable(list);
    }

    /**
     * 物料统计
     */
    @PostMapping("/report8/1")
    @ResponseBody
    public AsGoodsQuotation ajaxReport81(AsOrderStockDTO asOrderStockDTO) {
        return asOrderStockService.orderPayStat(asOrderStockDTO);
    }

    /**
     * 物料统计
     */
    @PostMapping("/report8/2")
    @ResponseBody
    public AsGoodsQuotation ajaxReport82(AsOrderStockDTO asOrderStockDTO) {
        return asOrderStockService.orderStatusStat(asOrderStockDTO);
    }

    /**
     * 经营指标统计
     */
    @PostMapping("/report9")
    @ResponseBody
    public TableDataInfo ajaxReport9(AsOrderStock orderStock) {
        // 默认排序
        Map<String, Object> params = new HashMap<>();
        params.put("orderByColumn","stat_date");
        params.put("isAsc","desc");
        orderStock.setParams(params);
        startPage();
        List<AsOrderStockDTO> list = asOrderStockService.selectAsOrderStockList(orderStock);
        return getDataTable(list);
    }

    /**
     * 统计中间表导出
     */
    @Log(title = "统计中间表导出", businessType = BusinessType.EXPORT)
    @PostMapping("/report9/export")
    @ResponseBody
    public AjaxResult export(AsOrderStock orderStock) {

        // 默认排序
        PageHelper.startPage(1, 10000, "dept_id");
        List<AsOrderStockDTO> list = asOrderStockService.selectAsOrderStockList(orderStock);

        ExcelUtil<AsOrderStockDTO> util = new ExcelUtil<>(AsOrderStockDTO.class);
        return util.exportExcel(list, "统计中间表");
    }

    /**
     * 经营指标统计
     */
    @PostMapping("/report10")
    @ResponseBody
    public TableDataInfo ajaxReport10(AsLossStock asLossStock) {
        // 默认排序
        Map<String, Object> params = new HashMap<>();
        params.put("orderByColumn","stat_date");
        params.put("isAsc","desc");
        asLossStock.setParams(params);
        startPage();
        List<AsLossStock> list = asLossStockService.selectAsLossStockList(asLossStock);
        return getDataTable(list);
    }
}
