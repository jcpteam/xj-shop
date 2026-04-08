package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.annotation.RepeatSubmit;
import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.enums.OrderPayStatus;
import com.javaboot.common.enums.OrderSource;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.domain.StorePaymentFlow;
import com.javaboot.shop.domain.StoreReceipt;
import com.javaboot.shop.dto.StoreGoodsOrderQueryDTO;
import com.javaboot.shop.dto.StoreGoodsOrderSortingQueryReq;
import com.javaboot.shop.dto.StoreReceiptQueryDTO;
import com.javaboot.shop.service.IStoreGoodsOrderService;
import com.javaboot.shop.service.IStoreMemberService;
import com.javaboot.shop.service.IStorePaymentFlowService;
import com.javaboot.shop.service.IStoreReceiptService;
import com.javaboot.shop.vo.StoreGoodsOrderSortingItemVO;
import com.javaboot.shop.vo.StoreGoodsOrderVO;
import com.javaboot.shop.vo.StoreReceiptVO;
import com.javaboot.system.domain.SysDictData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 收款单Controller
 *
 * @author lqh
 * @date 2021-07-05
 */
@Controller
@RequestMapping("/shop/receipt")
public class StoreReceiptController extends BaseController {
    private String prefix = "shop/receipt";

    @Autowired
    private IStoreReceiptService storeReceiptService;

    @Autowired
    private IStorePaymentFlowService storePaymentFlowService ;

    @Autowired
    private IStoreMemberService storeMemberService;

    @Autowired
    private IStoreGoodsOrderService storeGoodsOrderService;

    @RequiresPermissions("shop:receipt:view")
    @GetMapping()
    public String receipt(ModelMap mmap) {
        List<SysDictData> payStatusList = new ArrayList<>(2);
        payStatusList.add(getDic(OrderPayStatus.NOT_PAY.getCode(), OrderPayStatus.NOT_PAY.getDesc()));
        payStatusList.add(getDic(OrderPayStatus.PAYED.getCode(), OrderPayStatus.PAYED.getDesc()));
        mmap.put("payStatusList", payStatusList);
        return prefix + "/receipt";
    }

    private SysDictData getDic(String c,String d){
        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictValue(c);
        sysDictData.setDictLabel(d);
        return sysDictData;
    }
    /**
     * 查询收款单列表
     */
    @RequiresPermissions("shop:receipt:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreReceiptQueryDTO storeReceipt) {
        startPage();
        List<StoreReceiptVO> list = storeReceiptService.selectStoreReceiptList(storeReceipt, false);
        return getDataTable(list);
    }

    /**
     * 查询收款单列表
     */
    @PostMapping("/appList")
    @ResponseBody
    public TableDataInfo appList(StoreReceiptQueryDTO storeReceipt) {
        if(StringUtils.isNotEmpty(storeReceipt.getOrderIds()) && storeReceipt.getOrderIdList() == null){
            storeReceipt.setOrderIdList(Arrays.asList(storeReceipt.getOrderIds()));
        }
        return list(storeReceipt);
    }

    /**
     * 导出收款单列表
     */
    @RequiresPermissions("shop:receipt:export")
    @Log(title = "收款单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreReceiptQueryDTO storeReceipt) {
        List<StoreReceiptVO> list = storeReceiptService.selectStoreReceiptList(storeReceipt, true);
        if(CollectionUtils.isNotEmpty(list)) {
            list.stream().forEach(obj->{
                if(OrderPayStatus.NOT_PAY.getCode().equals(obj.getPayStatus())) {
                    obj.setLastModifyTime(null);
                }
            });
        }
        ExcelUtil<StoreReceiptVO> util = new ExcelUtil<StoreReceiptVO>(StoreReceiptVO.class);
        return util.exportExcel(list, "结算列表");
    }

    /**
     * 新增收款单
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存收款单
     */
    @RequiresPermissions("shop:receipt:add")
    @Log(title = "收款单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreReceipt storeReceipt) {
        return toAjax(storeReceiptService.insertStoreReceipt(storeReceipt));
    }

    /**
     * 新增保存收款单
     */
    @RequiresPermissions("shop:receipt:add")
    @Log(title = "收款单", businessType = BusinessType.INSERT)
    @PostMapping("/batchSave")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult batchSave(String orderIds) {
        return toAjax(storeReceiptService.batchSaveStoreReceipt(orderIds));
    }
    /**
     * 修改收款单
     */
    @GetMapping("/edit/{receiptId}")
    public String edit(@PathVariable("receiptId") Long receiptId, ModelMap mmap) {
        StoreReceipt storeReceipt = storeReceiptService.selectStoreReceiptById(receiptId);
        StorePaymentFlow queryObj = new StorePaymentFlow();
        queryObj.setReceiptId(receiptId);
        queryObj.setStatus(Constants.NORMAL);
        StorePaymentFlow rsltObj = new StorePaymentFlow();
        List<StorePaymentFlow> flowList = storePaymentFlowService.selectStorePaymentFlowList(queryObj);
        if(CollectionUtils.isNotEmpty(flowList)) {
            rsltObj = flowList.get(0);
        }
        rsltObj.setMerchantName(storeMemberService.selectStoreMemberById(Long.parseLong(storeReceipt.getMerchantId())).getNickname());
        mmap.put("storeReceipt", storeReceipt);
        mmap.put("storePaymentFlow", rsltObj);

        List<StoreGoodsOrderSortingItemVO> goosList = new ArrayList<>();
        mmap.put("goodsList", goosList);
        //查询一下订单数据，判断订单是否是分拣下单,如果是分拣下单则返回订单数据
        try {
            if (storeReceipt.getOrderIds() != null && storeReceipt.getOrderIds().indexOf(",") == -1) {
                StoreGoodsOrderQueryDTO orderReq = new StoreGoodsOrderQueryDTO();
                orderReq.setOrderId(Long.valueOf(storeReceipt.getOrderIds()));
                List<StoreGoodsOrderVO> list = storeGoodsOrderService.selectStoreGoodsOrderListForSorting(orderReq);
                if (CollectionUtils.isNotEmpty(list) && OrderSource.SORTING_ORDER.getCode().equals(list.get(0).getSource())) {
                    StoreGoodsOrderSortingQueryReq goodsReq = new StoreGoodsOrderSortingQueryReq();
                    goodsReq.setOrderId(list.get(0).getOrderId());
                    goosList = storeGoodsOrderService.selectStoreGoodsOrderByIdForSorting(goodsReq);
                    if (CollectionUtils.isNotEmpty(goosList)) {
                        mmap.put("goodsList", goosList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prefix + "/edit";
    }


    /**
     * 修改收款单
     */
    @GetMapping("/certificate/{receiptId}")
    public String certificate(@PathVariable("receiptId") Long receiptId, ModelMap mmap) {
        StoreReceipt storeReceipt = storeReceiptService.selectStoreReceiptById(receiptId);
        storeReceipt.setPayStatus(OrderPayStatus.PAYED.getCode());
        mmap.put("storeReceipt", storeReceipt);

        return prefix + "/certificate";
    }

    /**
     * 修改保存收款单
     */
    @RequiresPermissions("shop:receipt:edit")
    @Log(title = "收款单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreReceipt storeReceipt) {
        return toAjax(storeReceiptService.updateStoreReceipt(storeReceipt));
    }

    /**
     * 上传凭证修改
     */
    @RequiresPermissions("shop:receipt:edit")
    @Log(title = "上传凭证修改", businessType = BusinessType.UPDATE)
    @PostMapping("/certificate")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult editCertificate(StoreReceipt storeReceipt) {
        return toAjax(storeReceiptService.updateCertificate(storeReceipt));
    }

    /**
     * 删除凭证
     */
    @RequiresPermissions("shop:receipt:edit")
    @Log(title = "删除凭证", businessType = BusinessType.UPDATE)
    @PostMapping("/del/certificate")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult delCertificate(StoreReceipt storeReceipt) {
        return toAjax(storeReceiptService.delCertificate(storeReceipt));
    }

    /**
     * 删除收款单
     */
    @RequiresPermissions("shop:receipt:remove")
    @Log(title = "收款单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(storeReceiptService.deleteStoreReceiptByIds(ids));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改收款单
     */
    @GetMapping("/order/list/{receiptId}")
    public String orderList(@PathVariable("receiptId") Long receiptId, ModelMap mmap) {
        StoreReceipt storeReceipt = storeReceiptService.selectStoreReceiptById(receiptId);
        mmap.put("orderIds", storeReceipt.getOrderIds());
        return prefix + "/orderlist";
    }

    @Log(title = "导入对账单" , businessType = BusinessType.OTHER)
    @RequestMapping("/import")
    @ResponseBody
    public AjaxResult importExcel(MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {

        ExcelUtil<StoreReceipt> util = new ExcelUtil<StoreReceipt>(StoreReceipt.class);
        List<StoreReceipt> receiptList = util.importExcel(file.getInputStream());
        if(CollectionUtils.isNotEmpty(receiptList)) {
            for (StoreReceipt storeReceipt: receiptList)
            {
                StoreMember storeMember = new StoreMember();
                storeMember.setCustomerNo(storeReceipt.getMerchantId());
                List<StoreMember> storeMemberList = storeMemberService.selectStoreMemberList(storeMember);
                storeReceipt.setMerchantId(storeMemberList.get(0).getId()+"");
                storeReceipt.setDeptId(storeMemberList.get(0).getCustomerArea());
                storeReceipt.setCode(CodeConstants.RECEIPT + DateUtils.getRandom());
                storeReceipt.setOrderIds("-1");
                storeReceipt.setPayStatus(OrderPayStatus.NOT_PAY.getCode());
                storeReceiptService.insertStoreReceipt(storeReceipt);
            }
        }

        return AjaxResult.success("导入成功!");
    }
}
