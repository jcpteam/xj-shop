package com.javaboot.shop.service.impl;

import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.OrderLogType;
import com.javaboot.common.enums.OrderSource;
import com.javaboot.common.enums.OrderStatus;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreGoodsOrderItem;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.domain.StoreSortingDetail;
import com.javaboot.shop.domain.StoreSortingPerformanceStand;
import com.javaboot.shop.dto.StoreGoodsOrderDTO;
import com.javaboot.shop.mapper.StoreGoodsOrderMapper;
import com.javaboot.shop.mapper.StoreSortingDetailMapper;
import com.javaboot.shop.service.*;
import com.javaboot.shop.vo.StoreGoodsOrderItemVO;
import com.javaboot.shop.vo.StoreGoodsOrderSortingCountVO;
import com.javaboot.shop.vo.StoreGoodsOrderVO;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 分拣记录详情Service业务层处理
 * 
 * @author javaboot
 * @date 2021-07-05
 */
@Service
public class StoreSortingDetailServiceImpl implements IStoreSortingDetailService {
    @Autowired
    private StoreSortingDetailMapper storeSortingDetailMapper;

    @Autowired
    private IStoreGoodsOrderItemService storeGoodsOrderItemService;

    @Autowired
    private IStoreGoodsOrderService storeGoodsOrderService;

    @Autowired
    private StoreGoodsOrderMapper storeGoodsOrderMapper;

    @Autowired
    private IStoreReceiptService storeReceiptService;

    @Autowired
    private IStoreSortingPerformanceStandService storeSortingPerformanceStandService;

    @Autowired
    private IStoreMemberService storeMemberService;

    /**
     * 查询分拣记录详情
     * 
     * @param id 分拣记录详情ID
     * @return 分拣记录详情
     */
    @Override
    public StoreSortingDetail selectStoreSortingDetailById(Long id) {
        return storeSortingDetailMapper.selectStoreSortingDetailById(id);
    }

    /**
     * 查询分拣记录详情列表
     * 
     * @param storeSortingDetail 分拣记录详情
     * @return 分拣记录详情
     */
    @Override
    public List<StoreSortingDetail> selectStoreSortingDetailList(StoreSortingDetail storeSortingDetail) {
        return storeSortingDetailMapper.selectStoreSortingDetailList(storeSortingDetail);
    }

    @Override
    public List<StoreSortingDetail> selectStoreSortingDetailListForDeptMonth(StoreSortingDetail storeSortingDetail) {
        return storeSortingDetailMapper.selectStoreSortingDetailListForDeptMonth(storeSortingDetail);
    }

    /**
     * 新增分拣记录详情
     * 
     * @param storeSortingDetail 分拣记录详情
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreSortingDetail(StoreSortingDetail storeSortingDetail) {
        storeSortingDetail.setCreateTime(DateUtils.getNowDate());
        return storeSortingDetailMapper.insertStoreSortingDetail(storeSortingDetail);
    }

    /**
     * 修改分拣记录详情
     * 
     * @param storeSortingDetail 分拣记录详情
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreSortingDetail(StoreSortingDetail storeSortingDetail) {
        return storeSortingDetailMapper.updateStoreSortingDetail(storeSortingDetail);
    }

    /**
     * 删除分拣记录详情对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreSortingDetailByIds(String ids) {
        return storeSortingDetailMapper.deleteStoreSortingDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除分拣记录详情信息
     * 
     * @param id 分拣记录详情ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreSortingDetailById(Long id) {
        return storeSortingDetailMapper.deleteStoreSortingDetailById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult saveStoreSortingDetail(StoreSortingDetail storeSortingDetail) {
        // 先查询当前商品订单是否已经有分拣记录，有需要软删除
        StoreSortingDetail where = new StoreSortingDetail();
        where.setOrderItemId(storeSortingDetail.getOrderItemId());
        where.setStatus("1");
        List<StoreSortingDetail> exsitList = selectStoreSortingDetailList(where);

        // 保存分拣记录成功，需要更新订单商品状态为已分拣
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        storeSortingDetail.setSortingUserId(user.getUserId());

        //查询订单信息，获取交货日期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        StoreGoodsOrderVO orderVo = storeGoodsOrderMapper.selectStoreGoodsOrderById(storeSortingDetail.getOrderId());
        if(orderVo != null && orderVo.getDeliveryDate() != null){
            try {
                storeSortingDetail.setSortingDay(df.format(orderVo.getDeliveryDate()));
            }catch (Exception e){
                storeSortingDetail.setSortingDay(df.format(new Date()));
            }
        }else{
            storeSortingDetail.setSortingDay(df.format(new Date()));
        }

        // 判断是否有分拣重量，如果没有值则自动填充为分拣数量值
        if (storeSortingDetail.getSortingWeight() == null || storeSortingDetail.getSortingWeight() <= 0) {
            if (storeSortingDetail.getSortingQuantity() != null && storeSortingDetail.getSortingQuantity() > 0) {
                storeSortingDetail.setSortingWeight(storeSortingDetail.getSortingQuantity());
            }
        }
        // 计算当次分拣的分拣绩效值
        storeSortingDetail.setPerformanceResult(calculateSortingPerformanceResult(storeSortingDetail));
        int ret = insertStoreSortingDetail(storeSortingDetail);
        if (ret > 0) {
            // 删除之前的分拣记录
            if (CollectionUtils.isNotEmpty(exsitList)) {
                for (StoreSortingDetail record : exsitList) {
                    StoreSortingDetail update = new StoreSortingDetail();
                    update.setId(record.getId());
                    update.setStatus("0");
                    updateStoreSortingDetail(update);
                }
            }
            // 更新订单的商品分拣状态为已分拣
            StoreGoodsOrderItem storeGoodsOrderItem = new StoreGoodsOrderItem();
            storeGoodsOrderItem.setItemId(storeSortingDetail.getOrderItemId());
            storeGoodsOrderItem.setSortingStatus("1");
            ret = storeGoodsOrderItemService.updateStoreGoodsOrderItem(storeGoodsOrderItem);
        }

        StoreGoodsOrderSortingCountVO countInfo = null;
        if (ret > 0 && storeSortingDetail.getOrderId() != null) {
            //更新订单商品以及订单的实际分拣金额
            StoreGoodsOrderItem query = new StoreGoodsOrderItem();
            query.setOrderId(storeSortingDetail.getOrderId());
            List<StoreGoodsOrderItemVO> itemList = storeGoodsOrderItemService.selectStoreGoodsOrderItemList(query);
            double totalPrice = 0d;
            if(CollectionUtils.isNotEmpty(itemList)){
                for (StoreGoodsOrderItemVO item : itemList){
                    //判断当前商品是否已经分拣，如果已经分拣则需要计算一下实际分拣的金额
                    double sortingPrice = 0;
                    if("1".equals(item.getSortingStatus()) && item.getPrice() != null && item.getSortingWeight() != null){
                        if(item.getDiscount() != null){
                            //计算分拣金额时，需要根据折扣计算一下
//                            sortingPrice = item.getPrice() * item.getSortingWeight() * item.getDiscount();
                            sortingPrice = mul(mul(item.getPrice(),item.getSortingWeight()),item.getDiscount());
                        }else{
//                            sortingPrice = item.getPrice() * item.getSortingWeight();
                            sortingPrice = mul(item.getPrice() , item.getSortingWeight());
                        }
                        //更新商品分拣的金额
                        StoreGoodsOrderItem u = new StoreGoodsOrderItem();
                        u.setItemId(item.getItemId());
                        u.setSortingPrice(sortingPrice);
                        u.setSrcSortingQuantity(item.getSortingQuantity());
                        u.setSrcSortingWeight(item.getSortingWeight());
                        u.setOldSortingWeight(item.getSortingWeight());
                        storeGoodsOrderItemService.updateStoreGoodsOrderItem(u);
                    }else{
                        //没有分拣则还是使用订单金额计算总金额
                        if(item.getAmount() != null){
                            sortingPrice = item.getAmount();
                        }
                    }
//                    totalPrice += sortingPrice;
                    totalPrice = add(totalPrice,sortingPrice);
                }

                //判断double失真，计算保留两位小数四舍五入
                try {
                    String t = "" + totalPrice;
                    if(t.indexOf(".") != -1){
                        String[] tt = t.split(".");
                        if(tt != null && tt.length == 2 && tt[1].length() > 2){
                            BigDecimal b = new BigDecimal(totalPrice);
                            totalPrice = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                StoreGoodsOrderDTO update = new StoreGoodsOrderDTO();
                update.setOrderId(storeSortingDetail.getOrderId());
                update.setSortingPrice(totalPrice);
                storeGoodsOrderMapper.updateStoreGoodsOrder(update);
            }

            // 检查当前订单是否都分拣完成，分拣完成则修改订单状态为已发货
            List<StoreGoodsOrderSortingCountVO> records =
                    storeGoodsOrderService.getStoreGoodsOrderSortingCountByIds(storeSortingDetail.getOrderId() + "");
            if (CollectionUtils.isNotEmpty(records)) {
                StoreGoodsOrderSortingCountVO vo = records.get(0);
                if (vo.getOrderGoodCount() != null && vo.getOrderGoodSortCount() != null
                        && vo.getOrderGoodCount() == vo.getOrderGoodSortCount()) {
                    // 商品数量和已分拣数量一样，说明全部分拣完成，设置订单状态为已发货
                    StoreGoodsOrderDTO update = new StoreGoodsOrderDTO();
                    update.setOrderId(storeSortingDetail.getOrderId());
                    update.setStatus(OrderStatus.SENDED.getCode());
                    storeGoodsOrderMapper.updateStoreGoodsOrder(update);
//                    CompletableFuture.runAsync(() -> {
                        StoreGoodsOrderVO orderVO =
                                storeGoodsOrderService.selectStoreGoodsOrderById(storeSortingDetail.getOrderId());
                        storeGoodsOrderService.addLogNoGoods(orderVO, OrderLogType.COMPLETE.getCode(), user, "订单已分拣");

                        boolean autoFinanc = false;
                        //判断当前订单是否是分拣订单，如果是，则分拣完成就修改为财务审核通过，并生成付款单
                        if(OrderSource.SORTING_ORDER.getCode().equals(orderVO.getSource())){
                            autoFinanc = true;
                        }else{
                            //查询当前用户是否是自提客户，自提客户自动审核
                            try {
                                StoreMember storeMember = storeMemberService.selectStoreMemberById(Long.valueOf(orderVO.getMerchantId()));
                                if(storeMember != null && "2".equals(storeMember.getDeliveryType())){
                                    autoFinanc = true;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        if(autoFinanc){
                            //直接自动财务审核订单
                            int financialRet = storeGoodsOrderService.financialExamineStoreGoodsOrderByIds(storeSortingDetail.getOrderId() + "");
                            if(financialRet > 0){
                                //财务审核通过，自动生成对账单
                                storeReceiptService.batchSaveStoreReceipt(storeSortingDetail.getOrderId() + "");
                            }
                        }
//                    });

                }
                countInfo = vo;
            }
        }

        return new AjaxResult(AjaxResult.Type.SUCCESS, "操作成功", countInfo);
    }

    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public Double add(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.add(b2).doubleValue();
    }

    public Double sub(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.subtract(b2).doubleValue();
    }

    public Double mul(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSortDetailForItem(StoreSortingDetail storeSortingDetail) {
        // 先查询当前商品订单是否已经有分拣记录，有需要软删除
        StoreSortingDetail where = new StoreSortingDetail();
        where.setOrderItemId(storeSortingDetail.getOrderItemId());
        where.setStatus("1");
        List<StoreSortingDetail> exsitList = storeSortingDetailMapper.selectStoreSortingDetailList(where);

        // 保存分拣记录成功，需要更新订单商品状态为已分拣
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        storeSortingDetail.setSortingUserId(user.getUserId());
        //查询订单信息，获取交货日期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        StoreGoodsOrderVO orderVo = storeGoodsOrderMapper.selectStoreGoodsOrderById(storeSortingDetail.getOrderId());
        if(orderVo != null && orderVo.getDeliveryDate() != null){
            try {
                storeSortingDetail.setSortingDay(df.format(orderVo.getDeliveryDate()));
            }catch (Exception e){
                storeSortingDetail.setSortingDay(df.format(new Date()));
            }
        }else{
            storeSortingDetail.setSortingDay(df.format(new Date()));
        }

        // 判断是否有分拣重量，如果没有值则自动填充为分拣数量值
        if (storeSortingDetail.getSortingWeight() == null || storeSortingDetail.getSortingWeight() <= 0) {
            if (storeSortingDetail.getSortingQuantity() != null && storeSortingDetail.getSortingQuantity() > 0) {
                storeSortingDetail.setSortingWeight(storeSortingDetail.getSortingQuantity());
            }
        }

        // 计算当次分拣的分拣绩效值
        storeSortingDetail.setPerformanceResult(calculateSortingPerformanceResult(storeSortingDetail));
        int ret = insertStoreSortingDetail(storeSortingDetail);
        if (ret > 0) {
            // 删除之前的分拣记录
            if (CollectionUtils.isNotEmpty(exsitList)) {
                for (StoreSortingDetail record : exsitList) {
                    StoreSortingDetail update = new StoreSortingDetail();
                    update.setId(record.getId());
                    update.setStatus("0");
                    updateStoreSortingDetail(update);
                }
            }
            // 更新订单的商品分拣状态为已分拣
            StoreGoodsOrderItem storeGoodsOrderItem = new StoreGoodsOrderItem();
            storeGoodsOrderItem.setItemId(storeSortingDetail.getOrderItemId());
            storeGoodsOrderItem.setSortingStatus("1");
            ret = storeGoodsOrderItemService.updateStoreGoodsOrderItem(storeGoodsOrderItem);

            //更新订单商品以及订单的实际分拣金额
            StoreGoodsOrderItem query = new StoreGoodsOrderItem();
            query.setOrderId(storeSortingDetail.getOrderId());
            List<StoreGoodsOrderItemVO> itemList = storeGoodsOrderItemService.selectStoreGoodsOrderItemList(query);
            double totalPrice = 0d;
            if(CollectionUtils.isNotEmpty(itemList)){
                for (StoreGoodsOrderItemVO item : itemList){
                    //判断当前商品是否已经分拣，如果已经分拣则需要计算一下实际分拣的金额
                    double sortingPrice = 0;
                    if("1".equals(item.getSortingStatus()) && item.getPrice() != null && item.getSortingWeight() != null){
                        if(item.getDiscount() != null){
                            //计算分拣金额时，需要根据折扣计算一下
                            sortingPrice = item.getPrice() * item.getSortingWeight() * item.getDiscount();
                        }else{
                            sortingPrice = item.getPrice() * item.getSortingWeight();
                        }
                        //更新商品分拣的金额
                        StoreGoodsOrderItem u = new StoreGoodsOrderItem();
                        u.setItemId(item.getItemId());
                        u.setSortingPrice(sortingPrice);
                        u.setOldSortingWeight(item.getSortingWeight());
                        u.setSrcSortingQuantity(item.getSortingQuantity());
                        storeGoodsOrderItemService.updateStoreGoodsOrderItem(u);
                    }else{
                        //没有分拣则还是使用订单金额计算总金额
                        if(item.getAmount() != null){
                            sortingPrice = item.getAmount();
                        }
                    }
                    totalPrice += sortingPrice;
                }
                StoreGoodsOrderDTO update = new StoreGoodsOrderDTO();
                update.setOrderId(storeSortingDetail.getOrderId());
                update.setSortingPrice(new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                storeGoodsOrderMapper.updateStoreGoodsOrder(update);
            }
        }
    }

    /**
     * 计算当前分拣商品的分拣绩效
     *
     * @param storeSortingDetail
     * @return
     */
    private Double calculateSortingPerformanceResult(StoreSortingDetail storeSortingDetail) {
        if (storeSortingDetail.getSortingWeight() == null || storeSortingDetail.getSortingWeight() <= 0) {
            return 0d;
        }

        // 1、根据商品spu和客户类型，查询到对应的分拣绩效标准。
        StoreSortingPerformanceStand where = new StoreSortingPerformanceStand();
        where.setOrderItemId(storeSortingDetail.getOrderItemId());
        List<StoreSortingPerformanceStand> standList =
                storeSortingPerformanceStandService.selectStoreSortingPerformanceStandWithOrderItemId(where);
        if (CollectionUtils.isEmpty(standList)) {
            // 没有分拣绩效标准，绩效值为0
            return 0d;
        }
        // 正常情况下只有一条记录
        StoreSortingPerformanceStand standInfo = standList.get(0);

        // 2、根据当前分拣的重量(公斤)，以及绩效标准，计算当前分拣的绩效值。计算方式：分拣重量 * 单位转换比例 * 分拣权重 = 绩效结果
        // TODO 正常情况下，需要先按照公斤转换为绩效单位值再计算分拣绩效。 但现在没有公斤对应各个单位的转换关系，所以先默认全部以公斤值来计算分拣绩效
        if (standInfo.getSortingWeightRatio() == null || standInfo.getUnitRatio() == null || standInfo.getUnitRatio() <= 0) {
            return 0d;
        }
        //默认按照主单位计算
        double result =1.0d * storeSortingDetail.getSortingWeight() / standInfo.getUnitRatio() * standInfo.getSortingWeightRatio();
//        if(standInfo.getGoodUnitId() == 2){
//            //如果设置的副单位，则按照分拣数量计算
//            if(storeSortingDetail.getSortingQuantity() != null){
//                result =1.0d * storeSortingDetail.getSortingQuantity() / standInfo.getUnitRatio() * standInfo.getSortingWeightRatio();
//            }else{
//                result = 0d;
//            }
//        }
        return result;
    }
}
