package com.javaboot.web.controller.shop;

import java.util.ArrayList;
import java.util.List;

import com.javaboot.common.exception.BusinessException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.enums.PaymentAccountStatus;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StorePaymentAccount;
import com.javaboot.shop.dto.StorePaymentAccountQueryDTO;
import com.javaboot.shop.service.IStoreMemberService;
import com.javaboot.shop.service.IStorePaymentAccountService;
import com.javaboot.shop.vo.StorePaymentAccountVO;
import com.javaboot.system.domain.SysDictData;

/**
 * 支付账户Controller
 * 
 * @author lqh
 * @date 2021-07-09
 */
@Controller
@RequestMapping("/shop/account")
public class StorePaymentAccountController extends BaseController {
    private String prefix = "shop/account";

    @Autowired
    private IStorePaymentAccountService storePaymentAccountService;
    @Autowired
    private IStoreMemberService storeMemberServic;

    @RequiresPermissions("shop:account:view")
    @GetMapping()
    public String account() {
        return prefix + "/account";
    }

    /**
     * 查询支付账户列表
     */
    @RequiresPermissions("shop:account:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StorePaymentAccountQueryDTO storePaymentAccount) {
        startPage();
        List<StorePaymentAccountVO> list =
            storePaymentAccountService.selectStorePaymentAccountList(storePaymentAccount);
        return getDataTable(list);
    }

    /**
     * 查询支付账户列表
     */
    @PostMapping("/appList")
    @ResponseBody
    public TableDataInfo appList(StorePaymentAccountQueryDTO storePaymentAccount) {
        return list(storePaymentAccount);
    }

    /**
     * 导出支付账户列表
     */
    @RequiresPermissions("shop:account:export")
    @Log(title = "支付账户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StorePaymentAccountQueryDTO storePaymentAccount) {
        List<StorePaymentAccountVO> list =
            storePaymentAccountService.selectStorePaymentAccountList(storePaymentAccount);
        ExcelUtil<StorePaymentAccountVO> util = new ExcelUtil<StorePaymentAccountVO>(StorePaymentAccountVO.class);
        return util.exportExcel(list, "account");
    }

    /**
     * 新增支付账户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("memberList", storeMemberServic.selectStoreMemberListOfLoginUser());
        mmap.put("statusList", getStatusList());
        return prefix + "/add";
    }

    private List<SysDictData> getStatusList() {
        List<SysDictData> statusList = new ArrayList<>(2);
        statusList.add(new SysDictData(PaymentAccountStatus.ENABLE.getCode(), PaymentAccountStatus.ENABLE.getDesc()));
        statusList.add(new SysDictData(PaymentAccountStatus.DISABLE.getCode(), PaymentAccountStatus.DISABLE.getDesc()));
        return statusList;
    }

    /**
     * 新增保存支付账户
     */
    @RequiresPermissions("shop:account:add")
    @Log(title = "支付账户", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StorePaymentAccount storePaymentAccount) {
        StorePaymentAccount existInfo = storePaymentAccountService.selectStorePaymentAccountById(storePaymentAccount.getAccountId());
        if(existInfo != null){
            return error("新增支付账号'" + storePaymentAccount.getAccountId() + "'失败，账号ID已存在");
        }
        return toAjax(storePaymentAccountService.insertStorePaymentAccount(storePaymentAccount));
    }

    /**
     * 修改支付账户
     */
    @GetMapping("/edit/{accountId}/{page}")
    public String edit(@PathVariable("accountId") String accountId, @PathVariable("page") String page, ModelMap mmap) {
        StorePaymentAccount storePaymentAccount = storePaymentAccountService.selectStorePaymentAccountById(accountId);
        mmap.put("storePaymentAccount", storePaymentAccount);
        mmap.put("memberList", storeMemberServic.selectStoreMemberListOfLoginUser());
        mmap.put("statusList", getStatusList());

        return prefix + "/" + page;
    }

    /**
     * 修改保存支付账户
     */
    @RequiresPermissions("shop:account:edit")
    @Log(title = "支付账户", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StorePaymentAccount storePaymentAccount) {
        return toAjax(storePaymentAccountService.updateStorePaymentAccount(storePaymentAccount));
    }

    /**
     * 修改保存支付账户
     */
    @RequiresPermissions("shop:account:edit")
    @Log(title = "支付账户", businessType = BusinessType.UPDATE)
    @PostMapping("/charge")
    @ResponseBody
    public AjaxResult charge(StorePaymentAccount storePaymentAccount) {
        try {
            return toAjax(storePaymentAccountService.updateAccountNums(storePaymentAccount));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除支付账户
     */
    @RequiresPermissions("shop:account:remove")
    @Log(title = "支付账户", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storePaymentAccountService.deleteStorePaymentAccountByIds(ids));
    }
}
