package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.constant.PermissionConstants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreGoodsQuotation;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.dto.StorePaymentAccountQueryDTO;
import com.javaboot.shop.service.IStoreMemberService;
import com.javaboot.shop.service.IStorePaymentAccountService;
import com.javaboot.shop.vo.StorePaymentAccountVO;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysDeptService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商城会员信息Controller
 *
 * @author javaboot
 * @date 2019-08-30
 */
@Controller
@RequestMapping("/shop/member")
public class StoreMemberController extends BaseController {
    private String prefix = "shop/member";

    @Autowired
    private IStoreMemberService storeMemberService;

    @Autowired
    private IStorePaymentAccountService storePaymentAccountService;

    @Autowired
    private ISysDeptService deptService;

    @RequiresPermissions("shop:member:view")
    @GetMapping()
    public String member() {
        return prefix + "/member";
    }

    /**
     * 子账号列表
     */
    @GetMapping("/sub/list/{id}")
    public String subList(@PathVariable("id") Long id, ModelMap mmap) {
        mmap.put("storeMember", storeMemberService.selectStoreMemberById(id));
        return prefix + "/sub-member";
    }


    /**
     * 查询商城会员信息列表
     */
    @RequiresPermissions("shop:member:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreMember storeMember) {
        startPage();

        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(user != null && user.getRoles() != null && !user.getRoles().isEmpty()
            && PermissionConstants.EXTERNAL_SALER.equals(user.getRoles().get(0).getRoleKey())){
            storeMember.setOpmanagerId(user.getUserId());
        }

        // 根据结算单位的名称获取商户的id列表
        if(StringUtils.isNotBlank(storeMember.getSearchValue())) {
            StorePaymentAccountQueryDTO dto = new StorePaymentAccountQueryDTO();
            dto.setNickName(storeMember.getSearchValue());
            List<StorePaymentAccountVO> payAccountList = storePaymentAccountService.selectStorePaymentAccountList(dto);
            if(CollectionUtils.isNotEmpty(payAccountList)) {
                storeMember.setIds(payAccountList.stream().map(StorePaymentAccountVO::getMemberId).collect(Collectors.toList()));
            } else {
                return getDataTable(new ArrayList<>());
            }
        }
        if(StringUtils.isBlank(storeMember.getCustomerArea())) {
            String deptId = user.isAdmin() ? null : user.getDeptId();
            storeMember.setCustomerArea(deptId);
        }
        List<StoreMember> list = storeMemberService.selectStoreMemberList(storeMember);
        return getDataTable(list);
    }

    /**
     * 查询商户对应的报价单信息
     */
    @PostMapping("/quotationWithMemberId")
    @ResponseBody
    public AjaxResult quotationWithMemberId(StoreMember storeMember) {
        StoreGoodsQuotation storeGoodsQuotation = storeMemberService.quotationWithMemberId(storeMember);
        if (storeGoodsQuotation != null) {
            return AjaxResult.success(storeGoodsQuotation);
        } else {
            return AjaxResult.error("没有查询到报价单，请检查商户配置是否关联报价单");
        }
    }

    /**
     * 导出商城会员信息列表
     */
    @RequiresPermissions("shop:member:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreMember storeMember) {
        List<StoreMember> list = storeMemberService.selectStoreMemberList(storeMember);
        ExcelUtil<StoreMember> util = new ExcelUtil<StoreMember>(StoreMember.class);
        return util.exportExcel(list, "member");
    }

    /**
     * 新增商城会员信息
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存商城会员信息
     */
    @RequiresPermissions("shop:member:add")
    @Log(title = "商城会员信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreMember storeMember) {
        return toAjax(storeMemberService.insertStoreMember(storeMember));
    }

    /**
     * 修改商城会员信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreMember storeMember = storeMemberService.selectStoreMemberById(id);
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        mmap.put("storeMember", storeMember);
        mmap.put("deptList",deptService.selectDeptListById(user.isAdmin()?null:user.getDeptId()));
        StorePaymentAccountQueryDTO dto = new StorePaymentAccountQueryDTO();
        dto.setMemberIdList(new ArrayList<>());
        dto.getMemberIdList().add(storeMember.getId()+"");
        if(StringUtils.isNotBlank(storeMember.getSuperCustomer())){
            dto.getMemberIdList().add(storeMember.getSuperCustomer());
        }
        List<StorePaymentAccountVO> accountList = storePaymentAccountService.selectStorePaymentAccountList(dto);
        StorePaymentAccountVO vo = new StorePaymentAccountVO();
        if(CollectionUtils.isNotEmpty(accountList)) {
            for (StorePaymentAccountVO account : accountList)
            {
                if (account.getMemberId().equals(storeMember.getId() + ""))
                {
                    vo = account;
                    break;
                }
                else
                {
                    vo = account;
                }
            }
        }
        mmap.put("account", vo);
        return prefix + "/edit";
    }

    /**
     * 修改保存商城会员信息
     */
    @RequiresPermissions("shop:member:edit")
    @Log(title = "商城会员信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreMember storeMember) {
        return toAjax(storeMemberService.updateStoreMember(storeMember));
    }

    @Log(title = "商城会员信息", businessType = BusinessType.UPDATE)
    @PostMapping("/batchSaveOpmanagerId")
    @ResponseBody
    public AjaxResult batchSaveOpmanagerId(StoreMember storeMember) {
        return toAjax(storeMemberService.batchSaveOpmanagerId(storeMember));
    }

    /**
     * 删除商城会员信息
     */
    @RequiresPermissions("shop:member:remove")
    @Log(title = "商城会员信息", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeMemberService.deleteStoreMemberByIds(ids));
    }


    /**
     * 修改商户报价单信息
     */
    @RequiresPermissions("shop:member:edit")
    @Log(title = "修改商户报价单信息", businessType = BusinessType.UPDATE)
    @PostMapping("/update/quotation")
    @ResponseBody
    public AjaxResult updateQuotation(@RequestBody  StoreMember storeMember) {

        storeMember.setIds(Arrays.asList(Convert.toStrArray(storeMember.getMemberIds())));
        storeMember.getMemberIds().split(",");
        return toAjax(storeMemberService.updateQuotation(storeMember));
    }

}
