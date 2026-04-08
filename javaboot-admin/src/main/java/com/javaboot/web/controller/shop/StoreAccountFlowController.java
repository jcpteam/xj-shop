package com.javaboot.web.controller.shop;

import java.util.ArrayList;
import java.util.List;

import com.javaboot.common.enums.AccountFlowType;
import com.javaboot.common.enums.BlackWhiteType;
import com.javaboot.common.enums.PaymentAccountStatus;
import com.javaboot.shop.dto.StoreAccountFlowQueryDTO;
import com.javaboot.shop.vo.StoreAccountFlowVO;
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
import com.javaboot.shop.domain.StoreAccountFlow;
import com.javaboot.shop.service.IStoreAccountFlowService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 账户流水Controller
 * 
 * @author lqh
 * @date 2021-07-09
 */
@Controller
@RequestMapping("/shop/flow")
public class StoreAccountFlowController extends BaseController {
    private String prefix = "shop/flow";

    @Autowired
    private IStoreAccountFlowService storeAccountFlowService;

    @RequiresPermissions("shop:flow:view")
    @GetMapping()
    public String flow(ModelMap mmap) {
        List<SysDictData> typeList = new ArrayList<>(2);
        //typeList.add(new SysDictData(AccountFlowType.ADVANCE.getCode(), AccountFlowType.ADVANCE.getDesc()));
        typeList.add(new SysDictData(AccountFlowType.RECHARGE.getCode(), AccountFlowType.RECHARGE.getDesc()));
        typeList.add(new SysDictData(AccountFlowType.PAY.getCode(), AccountFlowType.PAY.getDesc()));
        mmap.put("typeList",typeList);
        return prefix + "/flow";
    }

    /**
     * 查询账户流水列表
     */
    @RequiresPermissions("shop:flow:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreAccountFlowQueryDTO storeAccountFlow) {
        startPage();
        List<StoreAccountFlowVO> list = storeAccountFlowService.selectStoreAccountFlowList(storeAccountFlow);
        return getDataTable(list);
    }

    /**
     * 导出账户流水列表
     */
    @RequiresPermissions("shop:flow:export")
    @Log(title = "账户流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreAccountFlowQueryDTO storeAccountFlow) {
        List<StoreAccountFlowVO> list = storeAccountFlowService.selectStoreAccountFlowList(storeAccountFlow);
        ExcelUtil<StoreAccountFlowVO> util = new ExcelUtil<StoreAccountFlowVO>(StoreAccountFlowVO.class);
        return util.exportExcel(list, "flow");
    }

    /**
     * 新增账户流水
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存账户流水
     */
    @RequiresPermissions("shop:flow:add")
    @Log(title = "账户流水", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreAccountFlow storeAccountFlow) {
        return toAjax(storeAccountFlowService.insertStoreAccountFlow(storeAccountFlow));
    }

    /**
     * 修改账户流水
     */
    @GetMapping("/edit/{flowId}")
    public String edit(@PathVariable("flowId") Long flowId, ModelMap mmap) {
        StoreAccountFlow storeAccountFlow = storeAccountFlowService.selectStoreAccountFlowById(flowId);
        mmap.put("storeAccountFlow", storeAccountFlow);
        return prefix + "/edit";
    }

    /**
     * 修改保存账户流水
     */
    @RequiresPermissions("shop:flow:edit")
    @Log(title = "账户流水", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreAccountFlow storeAccountFlow) {
        return toAjax(storeAccountFlowService.updateStoreAccountFlow(storeAccountFlow));
    }

    /**
     * 删除账户流水
     */
    @RequiresPermissions("shop:flow:remove")
    @Log(title = "账户流水", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeAccountFlowService.deleteStoreAccountFlowByIds(ids));
    }
}
