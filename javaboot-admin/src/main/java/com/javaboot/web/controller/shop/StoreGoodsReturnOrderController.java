package com.javaboot.web.controller.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.enums.OrderPayStatus;
import com.javaboot.common.enums.ReturnOrderStatus;
import com.javaboot.common.enums.ReturnOrderType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreGoodsReturnOrder;
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.dto.StoreGoodsReturnOrderDTO;
import com.javaboot.shop.dto.StoreGoodsReturnOrderExamineDTO;
import com.javaboot.shop.dto.StoreGoodsReturnOrderQueryDTO;
import com.javaboot.shop.service.IStoreGoodsReturnOrderService;
import com.javaboot.shop.service.IStoreHouseService;
import com.javaboot.shop.service.IStoreMemberService;
import com.javaboot.shop.vo.StoreGoodsReturnOrderVO;
import com.javaboot.system.domain.SysDictData;
import com.javaboot.system.domain.SysUser;

/**
 * 退货单信息主Controller
 * 
 * @author lqh
 * @date 2021-06-26
 */
@Controller
@RequestMapping("/shop/returnOrder")
public class StoreGoodsReturnOrderController extends BaseController {
    private String prefix = "shop/returnOrder";

    @Autowired
    private IStoreGoodsReturnOrderService storeGoodsReturnOrderService;
    @Autowired
    private IStoreMemberService storeMemberService;
    @Autowired
    private IStoreHouseService storeHouseService;
    @RequiresPermissions("shop:returnOrder:view")
    @GetMapping()
    public String returnOrder(ModelMap mmap) {

        List<SysDictData> statusList = new ArrayList<>(3);
        statusList.add(getDic(ReturnOrderStatus.WAITING_REVIEW.getCode(), ReturnOrderStatus.WAITING_REVIEW.getDesc()));
        statusList.add(getDic(ReturnOrderStatus.PASS.getCode(), ReturnOrderStatus.PASS.getDesc()));
        statusList.add(getDic(ReturnOrderStatus.NO_PASS.getCode(), ReturnOrderStatus.NO_PASS.getDesc()));

        mmap.put("statusList", statusList);
        mmap.put("typeList", getTypeList());

        return prefix + "/returnOrder";
    }

    private SysDictData getDic(String code,String desc){
        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictValue(code);
        sysDictData.setDictLabel(desc);
        return sysDictData;
    }
    /**
     * 查询退货单信息主列表
     */
    @RequiresPermissions("shop:returnOrder:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodsReturnOrderQueryDTO storeGoodsReturnOrder) {
        startPage();
        List<StoreGoodsReturnOrderVO> list = storeGoodsReturnOrderService.selectStoreGoodsReturnOrderList(storeGoodsReturnOrder);
        if(CollectionUtils.isNotEmpty(list)){
            StoreMember query=new StoreMember();
            query.setIds(list.stream().map(StoreGoodsReturnOrder::getMerchantId).collect(Collectors.toList()));
            List<StoreMember> memberList=storeMemberService.selectStoreMemberList(query);
            if(CollectionUtils.isNotEmpty(memberList)){
                memberList.forEach(m->{
                    list.forEach(v->{
                        if(v.getMerchantId().equals(m.getId().toString())){
                            v.setMerchantName(m.getNickname());
                        }
                    });
                });
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出退货单信息主列表
     */
    @RequiresPermissions("shop:returnOrder:export")
    @Log(title = "退货单信息主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodsReturnOrderQueryDTO storeGoodsReturnOrder) {
        List<StoreGoodsReturnOrderVO> list = storeGoodsReturnOrderService.selectStoreGoodsReturnOrderList(storeGoodsReturnOrder);
        ExcelUtil<StoreGoodsReturnOrderVO> util = new ExcelUtil<StoreGoodsReturnOrderVO>(StoreGoodsReturnOrderVO.class);
        return util.exportExcel(list, "returnOrder");
    }

    private List<SysDictData> getTypeList(){
        List<SysDictData> typeList = new ArrayList<>(3);
        typeList.add(getDic(ReturnOrderType.NO_DELIVERY.getCode(), ReturnOrderType.NO_DELIVERY.getDesc()));
        typeList.add(getDic(ReturnOrderType.LOSS.getCode(), ReturnOrderType.LOSS.getDesc()));
        typeList.add(getDic(ReturnOrderType.WAREHOUSING.getCode(), ReturnOrderType.WAREHOUSING.getDesc()));
        return typeList;
    }

    private List<SysDictData> getPayStatusList(){
        List<SysDictData> payStatusList = new ArrayList<>(3);
        payStatusList.add(getDic(OrderPayStatus.NOT_PAY.getCode(), OrderPayStatus.NOT_PAY.getDesc()));
        payStatusList.add(getDic(OrderPayStatus.PAYED.getCode(), OrderPayStatus.PAYED.getDesc()));
        return payStatusList;
    }
    /**
     * 新增退货单信息
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        List<StoreMember> storeMemberList= storeMemberService.selectStoreMemberList(null);
        mmap.put("payStatusList", getPayStatusList());
        mmap.put("typeList", getTypeList());
        mmap.put("houseList",   getHouseList());
        mmap.put("storeMemberList",storeMemberList);
        return prefix + "/add";
    }
    private List<StoreHouse> getHouseList(){
        StoreHouse storeHouse=new StoreHouse();
        storeHouse.setStatus(Constants.NORMAL);
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(!user.isAdmin()){
            storeHouse.setDeptId(user.getDeptId());
        }
        return storeHouseService.selectStoreHouseList(storeHouse);
    }
    /**
     * 新增保存退货单信息
     */
    @RequiresPermissions("shop:returnOrder:add")
    @Log(title = "新增保存退货单信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreGoodsReturnOrderDTO storeGoodsReturnOrder) {
        return toAjax(storeGoodsReturnOrderService.insertStoreGoodsReturnOrder(storeGoodsReturnOrder));
    }

    /**
     * 修改退货单信息
     */
    @GetMapping("/edit/{returnOrderId}")
    public String edit(@PathVariable("returnOrderId") Long returnOrderId, ModelMap mmap) {
        StoreGoodsReturnOrder storeGoodsReturnOrder = storeGoodsReturnOrderService.selectStoreGoodsReturnOrderById(returnOrderId);
        mmap.put("storeGoodsReturnOrder", storeGoodsReturnOrder);
        List<StoreMember> storeMemberList= storeMemberService.selectStoreMemberList(null);
        mmap.put("houseList",  getHouseList());
        mmap.put("typeList", getTypeList());
        mmap.put("payStatusList", getPayStatusList());
        mmap.put("storeMemberList",storeMemberList);
        return prefix + "/edit";
    }

    /**
     * 修改保存退货单信息
     */
    @RequiresPermissions("shop:returnOrder:edit")
    @Log(title = "修改保存退货单信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreGoodsReturnOrderDTO storeGoodsReturnOrder) {
        return toAjax(storeGoodsReturnOrderService.updateStoreGoodsReturnOrder(storeGoodsReturnOrder));
    }

    /**
     * 删除退货单信息
     */
    @RequiresPermissions("shop:returnOrder:remove")
    @Log(title = "删除退货单信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeGoodsReturnOrderService.deleteStoreGoodsReturnOrderByIds(ids));
    }

    /**
     * 审核订单信息
     */
    @RequiresPermissions("shop:order:edit")
    @Log(title = "审核订单信息", businessType = BusinessType.UPDATE)
    @PostMapping("/examine")
    @ResponseBody
    public AjaxResult examine(StoreGoodsReturnOrderExamineDTO examine) {
        int result=storeGoodsReturnOrderService.examine(examine);
        return toAjax(result>0);
    }
}
