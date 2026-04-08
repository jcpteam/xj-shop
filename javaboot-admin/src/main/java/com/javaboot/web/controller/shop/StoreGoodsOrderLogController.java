package com.javaboot.web.controller.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.javaboot.common.enums.OrderLogType;
import com.javaboot.common.enums.OrderStatus;
import com.javaboot.shop.dto.StoreGoodsOrderLogQueryDTO;
import com.javaboot.shop.vo.StoreGoodsOrderLogVO;
import com.javaboot.system.domain.SysDictData;
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
import com.javaboot.shop.domain.StoreGoodsOrderLog;
import com.javaboot.shop.service.IStoreGoodsOrderLogService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 记录调整时候的退货或者补货Controller
 * 
 * @author lqh
 * @date 2021-06-03
 */
@Controller
@RequestMapping("/shop/orderLog")
public class StoreGoodsOrderLogController extends BaseController {
    private String prefix = "shop/orderLog";

    @Autowired
    private IStoreGoodsOrderLogService storeGoodsOrderLogService;

    @RequiresPermissions("shop:orderLog:view")
    @GetMapping()
    public String orderLog(ModelMap mmap) {
        BiFunction<String,String,SysDictData> getDic =(c,d)->{
            SysDictData sysDictData = new SysDictData();
            sysDictData.setDictValue(c);
            sysDictData.setDictLabel(d);
            return sysDictData;
        };
        List<SysDictData> typeList = new ArrayList<>(10);
        typeList.add(getDic.apply(OrderLogType.CREATE.getCode(), OrderLogType.CREATE.getDesc()));
        typeList.add(getDic.apply(OrderLogType.UPDATE.getCode(), OrderLogType.UPDATE.getDesc()));
        typeList.add(getDic.apply(OrderLogType.DELETE.getCode(), OrderLogType.DELETE.getDesc()));
        typeList.add(getDic.apply(OrderLogType.EXAMINE.getCode(), OrderLogType.EXAMINE.getDesc()));
        typeList.add(getDic.apply(OrderLogType.SORTING.getCode(), OrderLogType.SORTING.getDesc()));
        typeList.add(getDic.apply(OrderLogType.FINANCE_EXAMINE.getCode(), OrderLogType.FINANCE_EXAMINE.getDesc()));
        typeList.add(getDic.apply(OrderLogType.UPDATE_PRICE.getCode(), OrderLogType.UPDATE_PRICE.getDesc()));
        typeList.add(getDic.apply(OrderLogType.PAY_RECEIPT.getCode(), OrderLogType.PAY_RECEIPT.getDesc()));
        typeList.add(getDic.apply(OrderLogType.COMPLETE.getCode(), OrderLogType.COMPLETE.getDesc()));
        typeList.add(getDic.apply(OrderLogType.CANCEL.getCode(), OrderLogType.CANCEL.getDesc()));
        mmap.put("typeList", typeList);
        return prefix + "/orderLog";
    }

    /**
     * 查询记录调整时候的退货或者补货列表
     */
    @RequiresPermissions("shop:orderLog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodsOrderLogQueryDTO storeGoodsOrderLog) {
        startPage();
        List<StoreGoodsOrderLogVO> list = storeGoodsOrderLogService.selectStoreGoodsOrderLogList(storeGoodsOrderLog);
        return getDataTable(list);
    }

    /**
     * 导出记录调整时候的退货或者补货列表
     */
    @RequiresPermissions("shop:orderLog:export")
    @Log(title = "记录调整时候的退货或者补货", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodsOrderLogQueryDTO storeGoodsOrderLog) {
        List<StoreGoodsOrderLogVO> list = storeGoodsOrderLogService.selectStoreGoodsOrderLogList(storeGoodsOrderLog);
        ExcelUtil<StoreGoodsOrderLogVO> util = new ExcelUtil<StoreGoodsOrderLogVO>(StoreGoodsOrderLogVO.class);
        return util.exportExcel(list, "orderLog");
    }

    /**
     * 新增记录调整时候的退货或者补货
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存记录调整时候的退货或者补货
     */
    @RequiresPermissions("shop:orderLog:add")
    @Log(title = "记录调整时候的退货或者补货", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreGoodsOrderLog storeGoodsOrderLog) {
        return toAjax(storeGoodsOrderLogService.insertStoreGoodsOrderLog(storeGoodsOrderLog));
    }

    /**
     * 修改记录调整时候的退货或者补货
     */
    @GetMapping("/edit/{logId}")
    public String edit(@PathVariable("logId") Long logId, ModelMap mmap) {
        StoreGoodsOrderLog storeGoodsOrderLog = storeGoodsOrderLogService.selectStoreGoodsOrderLogById(logId);
        mmap.put("storeGoodsOrderLog", storeGoodsOrderLog);
        return prefix + "/edit";
    }

    /**
     * 修改保存记录调整时候的退货或者补货
     */
    @RequiresPermissions("shop:orderLog:edit")
    @Log(title = "记录调整时候的退货或者补货", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreGoodsOrderLog storeGoodsOrderLog) {
        return toAjax(storeGoodsOrderLogService.updateStoreGoodsOrderLog(storeGoodsOrderLog));
    }

    /**
     * 删除记录调整时候的退货或者补货
     */
    @RequiresPermissions("shop:orderLog:remove")
    @Log(title = "记录调整时候的退货或者补货", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeGoodsOrderLogService.deleteStoreGoodsOrderLogByIds(ids));
    }
}
