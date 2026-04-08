package com.javaboot.web.controller.shop;

import com.github.pagehelper.PageHelper;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.annotation.RepeatSubmit;
import com.javaboot.common.constant.PermissionConstants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.enums.OrderPayStatus;
import com.javaboot.common.enums.OrderStatus;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.dto.*;
import com.javaboot.shop.mapper.StoreGoodsOrderItemMapper;
import com.javaboot.shop.mapper.StoreMemberMapper;
import com.javaboot.shop.service.IStoreGoodsOrderService;
import com.javaboot.shop.service.IStoreGoodsQuotationGoodsService;
import com.javaboot.shop.service.IStoreMemberService;
import com.javaboot.shop.vo.*;
import com.javaboot.system.domain.SysDictData;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.impl.SysUserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 订单信息Controller
 *
 * @author lqh
 * @date 2021-05-30
 */
@Api("订单信息")
@Controller
@RequestMapping("/shop/order")
public class StoreGoodsOrderController extends BaseController {
    private String prefix = "shop/order";

    @Autowired
    private IStoreGoodsOrderService storeGoodsOrderService;
    @Autowired
    private StoreMemberMapper storeMemberMapper;

    @Autowired
    private StoreGoodsOrderItemMapper storeGoodsOrderItemMapper;


    @Autowired
    private IStoreMemberService storeMemberService;

    @Autowired
    private IStoreGoodsQuotationGoodsService storeGoodsQuotationGoodsService;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @RequiresPermissions("shop:order:view")
    @GetMapping(value = {"", "/finance", "/afterSales"})
    public String order(ModelMap mmap, HttpServletRequest request) {
        BiFunction<String, String, SysDictData> getDic = (c, d) -> {
            SysDictData sysDictData = new SysDictData();
            sysDictData.setDictValue(c);
            sysDictData.setDictLabel(d);
            return sysDictData;
        };
        List<SysDictData> statusList = new ArrayList<>(4);
        statusList.add(getDic.apply(OrderStatus.WAITING_REVIEW.getCode(), OrderStatus.WAITING_REVIEW.getDesc()));
        statusList.add(getDic.apply(OrderStatus.WAITING_SORTING.getCode(), OrderStatus.WAITING_SORTING.getDesc()));
        statusList.add(getDic.apply(OrderStatus.SENDED.getCode(), OrderStatus.SENDED.getDesc()));
        statusList.add(getDic.apply(OrderStatus.FINISH.getCode(), OrderStatus.FINISH.getDesc()));
        statusList.add(getDic.apply(OrderStatus.CANCEL.getCode(), OrderStatus.CANCEL.getDesc()));
        List<SysDictData> payStatusList = new ArrayList<>(2);
        payStatusList.add(getDic.apply(OrderPayStatus.NOT_PAY.getCode(), OrderPayStatus.NOT_PAY.getDesc()));
        payStatusList.add(getDic.apply(OrderPayStatus.PAYED.getCode(), OrderPayStatus.PAYED.getDesc()));
        mmap.put("statusList", statusList);
        mmap.put("payStatusList", payStatusList);
        String url = request.getRequestURI();
        mmap.put("type", url.substring(url.lastIndexOf("/") + 1, url.length()));
        return prefix + "/order";
    }

    /**
     * 查询订单信息列表
     */
    @RequiresPermissions("shop:order:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodsOrderQueryDTO storeGoodsOrder) {
        return processGetGoodsOrderList(storeGoodsOrder,true);
    }

    private TableDataInfo processGetGoodsOrderList(StoreGoodsOrderQueryDTO storeGoodsOrder,boolean needQueryMember){
        startPage();
        if(StringUtils.isNotEmpty(storeGoodsOrder.getOrderIds())) {
            storeGoodsOrder.setOrderIdList(Arrays.asList(storeGoodsOrder.getOrderIds().split(",")));
        }
        if(StringUtils.isNotEmpty(storeGoodsOrder.getStatus())) {
            storeGoodsOrder.setStatusList(Arrays.asList(storeGoodsOrder.getStatus().split(",")));
            storeGoodsOrder.setStatus("");
        } else {
            storeGoodsOrder.setStatusList(Arrays.asList("1,2,3,4".split(",")));
        }
        List<StoreGoodsOrderVO> list = storeGoodsOrderService.selectStoreGoodsOrderList(storeGoodsOrder);
        if (needQueryMember && CollectionUtils.isNotEmpty(list)) {
            StoreMember query = new StoreMember();
            query.setIds(list.stream().map(StoreGoodsOrder::getMerchantId).collect(Collectors.toList()));

            List<StoreMember> memberList = storeMemberMapper.selectStoreMemberList(query);
            if (CollectionUtils.isNotEmpty(memberList)) {

                Set<String> superNos = new HashSet<>();

                memberList.forEach(m->{
                    m.setSuperCustomerName(m.getNickname());
                    if("2".equals(m.getSettlementType())) {
                        superNos.add(m.getSuperCustomer());
                    }
                });
                // 填充结算单位
                if(CollectionUtils.isNotEmpty(superNos)) {
                    StoreMember storeMemberQry = new StoreMember();
                    storeMemberQry.setCustomerNOs(new ArrayList<>(superNos));
                    List<StoreMember> superList = storeMemberMapper.selectStoreMemberList(storeMemberQry);
                    memberList.forEach(m -> {
                        m.setSuperCustomerName(m.getNickname());
                        superList.forEach(s -> {
                            if (s.getCustomerNo().equals(m.getSuperCustomer())) {
                                m.setSuperCustomerName(s.getNickname());
                            }
                        });
                    });
                }

                memberList.forEach(m -> {
                    list.forEach(v -> {
                        if (v.getMerchantId().equals(m.getId().toString()))
                        {
                            v.setMerchantName(m.getNickname());
                            v.setSuperMerchantName(m.getSuperCustomerName());
                        }
                    });
                });
            }
        }
        return getDataTable(list);
    }

    /**
     * 查询当天是否有订单
     */
    @PostMapping("/checkTodayOrder")
    @ResponseBody
    public TableDataInfo checkTodayOrder(StoreGoodsCheckTodayOrder checkTodayOrder) {
        return getDataTable(storeGoodsOrderService.checkTodayOrder(checkTodayOrder));
    }

    /**
     * 查询订单信息列表
     */
    @PostMapping("/appList")
    @ResponseBody
    public TableDataInfo appList(StoreGoodsOrderQueryDTO storeGoodsOrder) {
        return processAppList(storeGoodsOrder);
    }

    private TableDataInfo processAppList(StoreGoodsOrderQueryDTO storeGoodsOrder){
        //1、先查分页查询订单列表
        TableDataInfo resultList = processGetGoodsOrderList(storeGoodsOrder,false);
        if(resultList == null || resultList.getTotal() <= 0){
            return resultList;
        }
        List<StoreGoodsOrderVO> orderList = (List<StoreGoodsOrderVO>) resultList.getRows();
        //获取所有的订单id
        List<Long> orderIds = orderList.stream().map(StoreGoodsOrderVO::getOrderId).collect(Collectors.toList());

        //查询订单id对应的商品列表
        storeGoodsOrder.setOrderIdListLong(orderIds);
        List<StoreGoodsOrderVO> orderIdToItemList = storeGoodsOrderService.selectStoreGoodsOrderListForApp(storeGoodsOrder);

        if (CollectionUtils.isNotEmpty(orderIdToItemList)) {
            StoreMember query = new StoreMember();
            query.setIds(orderIdToItemList.stream().map(StoreGoodsOrder::getMerchantId).collect(Collectors.toList()));

            List<StoreMember> memberList = storeMemberMapper.selectStoreMemberList(query);
            if (CollectionUtils.isNotEmpty(memberList)) {
                memberList.forEach(m -> {
                    orderIdToItemList.forEach(v -> {
                        if (v.getMerchantId().equals(m.getId().toString()))
                        {
                            v.setMerchantName(m.getNickname());
                            v.setSuperMerchantName(m.getSuperCustomerName());
                            v.setContactPhone(m.getTelephone());
                            v.setContactAddress(m.getAddress());
                        }
                    });
                });
            }
        }

        resultList.setRows(orderIdToItemList);
        return resultList;
    }

    /**
     * 查询订单信息列表,钉钉端单独包一个接口，与商城区分权限
     */
    @PostMapping("/appListForDingding")
    @ResponseBody
    public TableDataInfo appListForDingding(StoreGoodsOrderQueryDTO storeGoodsOrder) {
        return processAppList(storeGoodsOrder);
    }

    /**
     * 导出订单信息列表
     */
    @RequiresPermissions("shop:order:export")
    @Log(title = "导出订单信息列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodsOrderQueryDTO storeGoodsOrder) {
        if(StringUtils.isEmpty(storeGoodsOrder.getCreateBeginDate()) && StringUtils.isEmpty(storeGoodsOrder.getCreateEndDate())) {
            String today = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
            storeGoodsOrder.setCreateBeginDate(today.concat(" 00:00:00"));
            storeGoodsOrder.setCreateEndDate(today.concat(" 23:59:59"));
        }
        if(StringUtils.isNotEmpty(storeGoodsOrder.getCreateBeginDate()) && StringUtils.isNotEmpty(storeGoodsOrder.getCreateEndDate())) {
            PageHelper.startPage(1, 10000);
        }
        if(StringUtils.isNotEmpty(storeGoodsOrder.getStatus())) {
            storeGoodsOrder.setStatusList(Arrays.asList(storeGoodsOrder.getStatus().split(",")));
            storeGoodsOrder.setStatus("");
        } else {
            storeGoodsOrder.setStatusList(Arrays.asList("1,2,3,4".split(",")));
        }
        List<StoreGoodsOrderExportDTO> list = storeGoodsOrderItemMapper.selectExportGoodsOrderItemList(storeGoodsOrder);
        TableDataInfo tableDataInfo = getDataTable(list);
        if(tableDataInfo.getTotal() > 10000) {
            return AjaxResult.error("导出记录不能超过10000条");
        }
        ExcelUtil<StoreGoodsOrderExportDTO> util = new ExcelUtil<>(StoreGoodsOrderExportDTO.class);
        return util.exportExcel(list, "订货单");
    }

    /**
     * 新增订单信息
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = user.isAdmin() ? null : user.getDeptId();
        StoreMember storeMember = new StoreMember();
        storeMember.setCustomerArea(deptId);
        List<StoreMember> storeMemberList = storeMemberService.selectStoreMemberList(storeMember);
        mmap.put("storeMemberList", storeMemberList);
        mmap.put("checkOrderPasswd", "0");
        if(PermissionConstants.ORDER_PASSWORD_ROLE_LIST.contains(user.getRoles().get(0).getRoleKey())){
            mmap.put("checkOrderPasswd", "1");
        }
        return prefix + "/add";
    }

    /**
     * 新增分拣订单信息
     */
    @GetMapping("/addSortingOrder")
    public String addSortingOrder(ModelMap mmap) {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = user.isAdmin() ? null : user.getDeptId();
        StoreMember storeMember = new StoreMember();
        storeMember.setCustomerArea(deptId);
        storeMember.setDeliveryType("2");   //固定查询自提客户 分拣客户类型
        List<StoreMember> storeMemberList = storeMemberService.selectStoreMemberList(storeMember);
        mmap.put("storeMemberList", storeMemberList);
        mmap.put("checkOrderPasswd", "0");
        if(PermissionConstants.ORDER_PASSWORD_ROLE_LIST.contains(user.getRoles().get(0).getRoleKey())){
            mmap.put("checkOrderPasswd", "1");
        }
        mmap.put("fromSorting","1");   //从分拣下单标志
        return prefix + "/add";
    }

    /**
     * 新增保存订单信息
     */
    @RequiresPermissions("shop:order:add")
    @Log(title = "新增保存订单信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(StoreGoodsOrderDTO storeGoodsOrder) {
        try {
            return toAjax(storeGoodsOrderService.insertStoreGoodsOrder(storeGoodsOrder,false));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }


    /**
     * 新增保存订单信息
     */
    @Log(title = "新增保存订单信息", businessType = BusinessType.INSERT)
    @PostMapping("/appAdd")
    @ResponseBody
    public AjaxResult appAddSave(@RequestBody StoreGoodsOrderDTO storeGoodsOrder) {
        try {
            return toAjax(storeGoodsOrderService.insertStoreGoodsOrder(storeGoodsOrder,true));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改订单信息
     */
    @GetMapping("/edit/{orderId}/{type}")
    public String edit(@PathVariable("orderId") Long orderId,@PathVariable("type")String type, ModelMap mmap) {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        StoreGoodsOrderVO storeGoodsOrder = storeGoodsOrderService.selectStoreGoodsOrderById(orderId);
        mmap.put("storeGoodsOrder", storeGoodsOrder);
        StoreMember query  = new StoreMember();
        query.setId(Long.parseLong(storeGoodsOrder.getMerchantId()));
        List<StoreMember> storeMemberList = storeMemberMapper.selectStoreMemberList(query);
        mmap.put("storeMemberList", storeMemberList);
        mmap.put("type", type);
        mmap.put("checkOrderPasswd", "0");
        if(PermissionConstants.ORDER_PASSWORD_ROLE_LIST.contains(user.getRoles().get(0).getRoleKey())){
            mmap.put("checkOrderPasswd", "1");
        }
        return prefix + "/edit";
    }

    /**
     * 修改订单信息
     */
    @PostMapping("/detail/{orderId}")
    @ResponseBody
    @ApiOperation("订单详情")
    public StoreGoodsOrderForApp detail(@PathVariable("orderId") Long orderId) {

        StoreGoodsOrderForApp rsp = new StoreGoodsOrderForApp();
        StoreGoodsOrderVO storeGoodsOrder = storeGoodsOrderService.selectStoreGoodsOrderById(orderId);

        // 设置商户信息
        StoreMember storeMember = storeMemberMapper.selectStoreMemberById(Long.parseLong(storeGoodsOrder.getMerchantId()));

        // 设置商品信息
        List<Long> goodIds = new ArrayList<>();
        storeGoodsOrder.getItemList().forEach(o->goodIds.add(o.getGoodsId()));
        StoreGoodsQuotationGoodsQueryDTO goodsDto = new StoreGoodsQuotationGoodsQueryDTO();
        goodsDto.setGoodsIdList(goodIds);
        List<StoreGoodsQuotationGoodsVO> goodsList = storeGoodsQuotationGoodsService.selectStoreGoodsQuotationGoodsList(goodsDto);
        storeGoodsOrder.getItemList().forEach(o->{
            goodsList.forEach(goods->{
                if(o.getGoodsId().equals(goods.getGoodsId())) {
                    o.setGoodName(goods.getPropertyName());
                    o.setUnitName(goods.getMainUnitId().equals(o.getUnitId()) ? goods.getMainUnitName() : goods.getSubUnitName());
                }
            });
        });
        rsp.setStoreGoodsOrder(storeGoodsOrder);
        rsp.setStoreMember(storeMember);
        rsp.setGoodsList(goodsList);
        return rsp;
    }

    /**
     * 修改保存订单信息
     */
    @RequiresPermissions("shop:order:edit")
    @Log(title = "修改保存订单信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult editSave(StoreGoodsOrderDTO storeGoodsOrder) {
        return toAjax(storeGoodsOrderService.updateStoreGoodsOrder(storeGoodsOrder,false));
    }

    /**
     * 修改保存订单信息
     */
    @Log(title = "修改保存订单信息", businessType = BusinessType.UPDATE)
    @PostMapping("/appEdit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult appEditSave(@RequestBody StoreGoodsOrderDTO storeGoodsOrder) {
        return toAjax(storeGoodsOrderService.updateStoreGoodsOrder(storeGoodsOrder,true));
    }

    /**
     * 删除订单信息
     */
    @RequiresPermissions("shop:order:remove")
    @Log(title = "作废订单信息", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeGoodsOrderService.deleteStoreGoodsOrderByIdsCheckIfNeedRestore(ids,true));
    }

    /**
     * 删除订单信息
     */
    //    @RequiresPermissions("shop:order:remove")
    @Log(title = "作废订单信息", businessType = BusinessType.DELETE)
    @PostMapping("/appRemove")
    @ResponseBody
    public AjaxResult appRemove(String ids) {
        return toAjax(storeGoodsOrderService.deleteStoreGoodsOrderByIdsCheckIfNeedRestore(ids,false));
    }


    /**
     * 湘佳要求物理删除订单信息，这里订单状态改成0
     */
    @RequiresPermissions("shop:order:remove")
    @Log(title = "作废订单信息", businessType = BusinessType.DELETE)
    @PostMapping("/phyRemove")
    @ResponseBody
    public AjaxResult phyRemove(Long ids) {
        return toAjax(storeGoodsOrderService.deleteStoreGoodsOrderById(ids));
    }

    /**
     * 还原订单信息
     */
    @RequiresPermissions("shop:order:remove")
    @Log(title = "还原订单信息", businessType = BusinessType.DELETE)
    @PostMapping("/revert")
    @ResponseBody
    public AjaxResult revert(String ids) {
        try {
            return toAjax(storeGoodsOrderService.revertStoreGoodsOrderByIds(ids));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * 审核订单信息
     */
    @RequiresPermissions("shop:order:edit")
    @Log(title = "审核订单信息", businessType = BusinessType.UPDATE)
    @PostMapping("/examine")
    @ResponseBody
    public AjaxResult examine(String ids) {
        return toAjax(storeGoodsOrderService.examineStoreGoodsOrderByIds(ids));
    }

    /**
     * 财务审核订单信息
     */
    @RequiresPermissions("shop:order:edit")
    @Log(title = "财务审核订单信息", businessType = BusinessType.UPDATE)
    @PostMapping("/financial/examine")
    @ResponseBody
    public AjaxResult financialExamine(String ids) {
        return toAjax(storeGoodsOrderService.financialExamineStoreGoodsOrderByIds(ids));
    }

    /**
     * 财务审核订单信息
     */
    @RequiresPermissions("shop:order:edit")
    @Log(title = "财务审核订单信息", businessType = BusinessType.UPDATE)
    @PostMapping("/financial/cancel")
    @ResponseBody
    public AjaxResult financialCancel(String ids) {
        try {
            return toAjax(storeGoodsOrderService.financialCancelStoreGoodsOrderByIds(ids));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }


    /**
     * 查询分拣订单信息列表
     */
    @PostMapping("/listForSorting")
    @ResponseBody
    public TableDataInfo listForSorting(StoreGoodsOrderQueryDTO storeGoodsOrder) {
        List<StoreGoodsOrderVO> list = processQueryListForSorting(storeGoodsOrder);
        return getDataTable(list);
    }

    private List<StoreGoodsOrderVO> processQueryListForSorting(StoreGoodsOrderQueryDTO storeGoodsOrder){
        startPage();

        List<String> statusList = new ArrayList<>();
        if ("1".equals(storeGoodsOrder.getSortingStatus())) {
            // 待分拣
            statusList.add(OrderStatus.WAITING_SORTING.getCode());
        } else if ("2".equals(storeGoodsOrder.getSortingStatus())) {
            // 已分拣
            statusList.add(OrderStatus.SENDED.getCode());
            statusList.add(OrderStatus.FINISH.getCode());
        } else {
            // 全部
            statusList.add(OrderStatus.WAITING_SORTING.getCode());
            statusList.add(OrderStatus.SENDED.getCode());
            statusList.add(OrderStatus.FINISH.getCode());
        }
        storeGoodsOrder.setStatusList(statusList);

        List<StoreGoodsOrderVO> list = storeGoodsOrderService.selectStoreGoodsOrderListForSorting(storeGoodsOrder);
        if (CollectionUtils.isNotEmpty(list)) {
            StoreMember query = new StoreMember();
            query.setIds(list.stream().map(StoreGoodsOrder::getMerchantId).collect(Collectors.toList()));
            List<StoreMember> memberList = storeMemberService.selectStoreMemberList(query);
            if (CollectionUtils.isNotEmpty(memberList)) {
                List<String> opmanagerIds = memberList.stream().map(StoreMember::getOpmanagerId).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(opmanagerIds)){
                    List<SysUser> opmanagerUserList = sysUserService.selectUserByIds(opmanagerIds);
                    if(CollectionUtils.isNotEmpty(opmanagerUserList)){
                        memberList.forEach(m -> {
                            if(StringUtils.isNotEmpty(m.getOpmanagerId())){
                                opmanagerUserList.forEach(o -> {
                                    if (o.getUserId().equals(m.getOpmanagerId())) {
                                        m.setOpmanagerName(o.getUserName());
                                        m.setOpmanagerPhone(o.getPhonenumber());
                                    }
                                });
                            }
                        });
                    }
                }

                memberList.forEach(m -> {
                    list.forEach(v -> {
                        if (v.getMerchantId().equals(m.getId().toString())) {
                            v.setMerchantName(m.getNickname());
                            v.setPrintType(m.getPrintType());
                            v.setDeliveryType(m.getDeliveryType());
                            v.setContactName(m.getContactName());
                            v.setContactPhone(m.getTelephone());
                            v.setOpmanagerName(m.getOpmanagerName());
                            v.setOpmanagerPhone(m.getOpmanagerPhone());
                        }
                    });
                });
            }

            // 查询订单已分拣的商品总数
            String orderIds = "";
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    orderIds += list.get(i).getOrderId();
                } else {
                    orderIds += list.get(i).getOrderId() + ",";
                }
            }
            List<StoreGoodsOrderSortingCountVO> sortingCountList =
                    storeGoodsOrderService.getStoreGoodsOrderSortingCountByIds(orderIds);
            if (CollectionUtils.isNotEmpty(sortingCountList)) {
                sortingCountList.forEach(m -> {
                    list.forEach(v -> {
                        if (v.getOrderId().toString().equals(m.getOrderId().toString())) {
                            v.setOrderGoodCount(m.getOrderGoodCount());
                            v.setOrderGoodSortCount(m.getOrderGoodSortCount());
                        }
                    });
                });
            }
        }

        return list;
    }

    /**
     * 查询分拣订单商品详情信息列表
     */
    @PostMapping("/goodListForSorting")
    @ResponseBody
    public TableDataInfo goodListForSorting(StoreGoodsOrderSortingQueryReq req) {
        startPage();
        List<StoreGoodsOrderSortingItemVO> list = storeGoodsOrderService.selectStoreGoodsOrderByIdForSorting(req);
        return getDataTable(list);
    }

    /**
     * 查询分拣订单信息列表
     */
    @PostMapping("/listForSortingMaterial")
    @ResponseBody
    public TableDataInfo listForSortingMaterial(StoreGoodsOrderQueryDTO storeGoodsOrder) {
        if(StringUtils.isNotEmpty(storeGoodsOrder.getOrderIds())) {
            storeGoodsOrder.setOrderIdList(Arrays.asList(storeGoodsOrder.getOrderIds().split(",")));
        }
        List<StoreGoodsOrderVO> list = processQueryListForSorting(storeGoodsOrder);

        if(CollectionUtils.isNotEmpty(list)){
            List<String> orderIds = new ArrayList<>();
            for(StoreGoodsOrderVO orderInfo : list){
                StoreGoodsOrderSortingQueryReq req = new StoreGoodsOrderSortingQueryReq();
                req.setOrderId(orderInfo.getOrderId());
                req.setDeptId(storeGoodsOrder.getLoginUserDeptId());
                List<StoreGoodsOrderSortingItemVO> itemList = storeGoodsOrderService.selectStoreGoodsOrderByIdForSorting(req);
                orderInfo.setSortingMaterialItemVOS(itemList);

                orderIds.add(orderInfo.getOrderId() + "");
            }

            try {
                if("1".equals(storeGoodsOrder.getPrintUpdateType())){
                    //更新订单配货单的状态为已打印
                    storeGoodsOrderService.updateStoreGoodsOrderPrintByIds(orderIds);
                }else if("2".equals(storeGoodsOrder.getPrintUpdateType())){
                    //更新订单出库单的状态为已打印
                    storeGoodsOrderService.updateStoreGoodsOrderOdoPrintByIds(orderIds);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return getDataTable(list);
    }

    /**
     * 查询分拣订单搜索条件数据
     */
    @PostMapping("/getSortingQueryParams")
    @ResponseBody
    public StoreSortingQueryParamsVO getSortingQueryParams() {
        StoreSortingQueryParamsVO ret = new StoreSortingQueryParamsVO();
        // 查询订单所有的用户
        List<SortingQueryMerchantInfo> merchantInfoList = storeGoodsOrderService.getSortingMerchantInfo();
        ret.setMerchantInfoList(merchantInfoList);
        // 查询订单所有的线路
        List<StoreDeliverRoute> routeList = storeGoodsOrderService.getSortingRouteInfo();
        ret.setRouteList(routeList);
        // 查询订单所有的报价单
        List<StoreGoodsQuotation> quotationList = storeGoodsOrderService.getSortingQuotationInfo();
        ret.setQuotationList(quotationList);
        return ret;
    }

    /**
     * 查询分拣订单搜索条件数据
     */
    @GetMapping("/getItemComment")
    @ResponseBody
    public TableDataInfo getItemComment(StoreGoodsOrderItem item) {
        return getDataTable(storeGoodsOrderItemMapper.selectOrderItemComments(item));
    }

    @PostMapping("/updateStoreGoodsOrderOdoPrintByIds")
    @ResponseBody
    public AjaxResult updateStoreGoodsOrderOdoPrintByIds(StoreGoodsOrderQueryDTO storeGoodsOrder) {
        if(storeGoodsOrder != null && storeGoodsOrder.getOrderId() != null){
            List<String> orderIds = new ArrayList<>();
            orderIds.add(storeGoodsOrder.getOrderId() + "");
            return toAjax(storeGoodsOrderService.updateStoreGoodsOrderOdoPrintByIds(orderIds));
        }else{
            return AjaxResult.error("参数为空");
        }
    }

}
