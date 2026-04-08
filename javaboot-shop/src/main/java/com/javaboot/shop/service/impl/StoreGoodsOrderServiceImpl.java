package com.javaboot.shop.service.impl;

import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.constant.PermissionConstants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.*;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.dto.*;
import com.javaboot.shop.mapper.*;
import com.javaboot.shop.service.*;
import com.javaboot.shop.vo.*;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 订单信息主Service业务层处理
 *
 * @author lqh
 * @date 2021-05-30
 */
@Service
public class StoreGoodsOrderServiceImpl implements IStoreGoodsOrderService {
    @Autowired
    private StoreGoodsOrderMapper storeGoodsOrderMapper;
    @Autowired
    private StoreGoodsOrderItemMapper storeGoodsOrderItemMapper;
    @Autowired
    private IStoreGoodsOrderItemService itemService;
    @Autowired
    private IStoreGoodsQuotationGoodsService quotationGoodsService;
    @Autowired
    private IStoreGoodsOrderLogService storeGoodsOrderLogService;
    @Autowired
    private IStorePaymentAccountService storePaymentAccountService;
    @Autowired
    private IStoreBlackWhiteService blackWhiteService;
    @Autowired
    private IStoreMemberService memberService;

    @Autowired
    private StoreOrderPasswordMapper storeOrderPasswordMapper;

    @Autowired
    private IStoreGoodsCartItemService storeGoodsCartItemService;


    @Autowired
    private IStoreSortingDetailService storeSortingDetailService;

    @Autowired
    private StoreGoodsSpuMapper storeGoodsSpuMapper;
    @Autowired
    private IStoreGoodsQuotationGoodsService storeGoodsQuotationGoodsService;

    @Autowired
    private IStoreMemberCommentService storeMemberCommentService;

    @Autowired
    private StoreGoodsQuotationGoodsMapper storeGoodsQuotationGoodsMapper;

    @Autowired
    private StoreMemberMapper storeMemberMapper;

    /**
     * 查询订单信息主
     *
     * @param orderId
     *            订单信息主ID
     * @return 订单信息主
     */
    @Override
    public StoreGoodsOrderVO selectStoreGoodsOrderById(Long orderId) {
        StoreGoodsOrderVO vo = storeGoodsOrderMapper.selectStoreGoodsOrderById(orderId);
        vo.setDeliveryDateText(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, vo.getDeliveryDate()));
        StoreGoodsOrderItem query = new StoreGoodsOrderItem();
        query.setOrderId(orderId);
        List<StoreGoodsOrderItemVO> itemList = itemService.selectStoreGoodsOrderItemList(query);
        vo.setItemList(itemList);
        return vo;
    }

    /**
     * 查询订单信息主列表
     *
     * @param storeGoodsOrder
     *            订单信息主
     * @return 订单信息主
     */
    @Override
    public List<StoreGoodsOrderVO> selectStoreGoodsOrderList(StoreGoodsOrderQueryDTO storeGoodsOrder) {
        List<StoreGoodsOrderVO> list = storeGoodsOrderMapper.selectStoreGoodsOrderList(storeGoodsOrder);
        setObjNames(list);
        return list;
    }

    /**
     * 查询订单信息主列表
     *
     * @param storeGoodsOrder
     *            订单信息主
     * @return 订单信息主
     */
    @Override
    public List<StoreGoodsOrderVO> selectStoreGoodsOrderListForApp(StoreGoodsOrderQueryDTO storeGoodsOrder) {
        List<StoreGoodsOrderVO> list = storeGoodsOrderMapper.selectStoreGoodsOrderListForApp(storeGoodsOrder);
        setObjNames(list);
        return list;
    }

    private void setObjNames(List<StoreGoodsOrderVO> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(o -> {
                o.setPayStatusName(OrderPayStatus.getDescValue(o.getPayStatus()));
                o.setSourceName(OrderSource.getDescValue(o.getSource()));
//                o.setPrintStatusName(OrderPrintStatus.getDescValue(o.getPrintStatus()));
                o.setPrintStatusName(OrderPrintStatus.getDescValue(o.getOdoPrintStatus()));
                o.setStatusName(OrderStatus.getDescValue(o.getStatus()));
                if(o.getSortingPrice() == null) {
                    o.setSortingPrice(0.0);
                }
            });
        }
    }

    /**
     * 新增订单信息主
     *
     * @param storeGoodsOrder
     *            订单信息主
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreGoodsOrder(StoreGoodsOrderDTO storeGoodsOrder,boolean isApp) throws BusinessException {

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        Date deliveryDate = storeGoodsOrder.getDeliveryDate();
        if(hour >= 9 && DateUtils.getDate().equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, deliveryDate))) {
            storeGoodsOrder.setDeliveryDate(DateUtils.addDays(deliveryDate, 1));
        }
        Map<String, String> map = getStartAndEndTime(new Date());
        String startTime = map.get("startTime");
        String endTime = map.get("endTime");
        // 查询商户信息：如果是子商户则使用父id查支付账号
        StoreMember member = memberService.selectStoreMemberById(Long.parseLong(storeGoodsOrder.getMerchantId()));
        if (member == null) {
            throw new BusinessException("未查到商户信息");
        }

        // 检查订单商品是否存在于报价单中
        checkOrderInQuotation(storeGoodsOrder, member.getQuotationId());

        List<String> memberIdList = new ArrayList<>(2);
        memberIdList.add(member.getId().toString());
        if (StringUtils.isNotEmpty(member.getSuperCustomer())) {
            memberIdList.add(member.getSuperCustomer());
        }
        //校验订单密码
        if(!isApp){
            checkOrderPwd(storeGoodsOrder);
        }
        // 验证是否有支付账号：无不能下单
        StorePaymentAccountQueryDTO queryAccount = new StorePaymentAccountQueryDTO();
        queryAccount.setMemberIdList(memberIdList);
        queryAccount.setStatus(PaymentAccountStatus.ENABLE.getCode());
        List<StorePaymentAccountVO> list = storePaymentAccountService.selectStorePaymentAccountList(queryAccount);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException("商户未绑定支付账号或者支付账号未启用，不能下单");
        }
        StorePaymentAccountVO accountVO = list.get(0);
        if (list.size() > 1) {
            // 如果出现多个账户：父商户和子商户账号同时存在，则优先使用自己的账户
            accountVO = list.stream().filter(e -> e.getMemberId().equals(member.getId().toString())).findFirst().get();
        }
        // 验证支付账号是否为黑名单：黑名单余额不足不能下单
        StoreBlackWhite queryBlackWhite = new StoreBlackWhite();
        queryBlackWhite.setAccountId(accountVO.getAccountId());
        queryBlackWhite.setType(BlackWhiteType.Black.getCode());
        queryBlackWhite.setStatus(Constants.NORMAL);
        List<StoreBlackWhiteVO> blackWhiteVOS = blackWhiteService.selectStoreBlackWhiteList(queryBlackWhite);
        if (CollectionUtils.isNotEmpty(blackWhiteVOS) && (accountVO.getCash() == null || accountVO.getCash() < storeGoodsOrder.getPrice())) {
            throw new BusinessException(
                "您存在未支付订单，请先支付再下单！商户号【" + accountVO.getAccountNumber() + "】，订单金额" + storeGoodsOrder.getPrice() + "，不能下单");
        }
        storeGoodsOrder.setCode(CodeConstants.ORDER + DateUtils.getRandom());
        if (StringUtils.isEmpty(storeGoodsOrder.getSource())) {
            if(isApp){
                if("1".equals(storeGoodsOrder.getFromDingding())){
                    //钉钉端下单设置代下单类型
                    storeGoodsOrder.setSource(OrderSource.REPLACE_ORDER.getCode());
                }else{
                    storeGoodsOrder.setSource(OrderSource.WX.getCode());
                }
            }else{
                if(StringUtils.isNotEmpty(storeGoodsOrder.getFromSorting()) && storeGoodsOrder.getFromSorting().equals("1")){
                    storeGoodsOrder.setSource(OrderSource.SORTING_ORDER.getCode());
                }else{
                    storeGoodsOrder.setSource(OrderSource.REPLACE_ORDER.getCode());
                }
            }
        }
        //分拣订单直接审核通过
//        if(StringUtils.isNotEmpty(storeGoodsOrder.getFromSorting()) && storeGoodsOrder.getFromSorting().equals("1")){
//            storeGoodsOrder.setStatus(OrderStatus.WAITING_SORTING.getCode());
//        }
        if(!isApp){ // 代下单和分拣下单直接是审核通过状态
            storeGoodsOrder.setStatus(OrderStatus.WAITING_SORTING.getCode());
        }
        if (StringUtils.isEmpty(storeGoodsOrder.getPayStatus())) {
            storeGoodsOrder.setPayStatus(OrderPayStatus.NOT_PAY.getCode());
        }
        if (StringUtils.isEmpty(storeGoodsOrder.getPrintStatus())) {
            storeGoodsOrder.setPrintStatus(OrderPrintStatus.NOT_PRINT.getCode());
        }
        if (StringUtils.isEmpty(storeGoodsOrder.getOdoPrintStatus())) {
            storeGoodsOrder.setOdoPrintStatus(OrderPrintStatus.NOT_PRINT.getCode());
        }

        //设置订单创建id
        try
        {
            SysUser user1 = (SysUser)SecurityUtils.getSubject().getPrincipal();
            if(user1 != null){
                storeGoodsOrder.setCreatorId(user1.getUserId());
            }else{
                if(isApp && storeGoodsOrder.getUserId() != null){
                    storeGoodsOrder.setCreatorId(storeGoodsOrder.getUserId());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        int result = storeGoodsOrderMapper.insertStoreGoodsOrder(storeGoodsOrder);
        if (result == 1) {
            List<StoreGoodsQuotationGoodsSaleNumDTO> dtoList = new ArrayList<>(storeGoodsOrder.getItemList().size());

            storeGoodsOrder.getItemList().forEach(i -> {
                i.setOrderId(storeGoodsOrder.getOrderId());
                itemService.insertStoreGoodsOrderItem(i);
                StoreGoodsQuotationGoodsSaleNumDTO dto = StoreGoodsQuotationGoodsSaleNumDTO.builder()
                    .spuNo(i.getSpuNo()).isAdd(false).buyNum(i.getQuantity()).startTime(startTime).endTime(endTime).deptId(i.getDeptId()).build();
                // 扣库存
                if (dto.getBuyNum() == 0) {
                    throw new BusinessException("商品【" + dto.getGoodsId() + "】购买数量不能为0");
                }
                int size = quotationGoodsService.updateSaleNum(dto);
                if (size != 1) {
                    throw new BusinessException("商品【" + storeGoodsSpuMapper.selectStoreGoodsSpuById(dto.getSpuNo()).getName() + "】库存不足无法新增订单");
                }

            });
            SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
            CompletableFuture.runAsync(() -> {
                try {
                    StoreGoodsOrderVO orderVO = new StoreGoodsOrderVO();
                    BeanUtils.copyProperties(storeGoodsOrder, orderVO);
                    if(StringUtils.isNotEmpty(storeGoodsOrder.getFromSorting()) && storeGoodsOrder.getFromSorting().equals("1")){
                        addLogNoGoods(orderVO, OrderLogType.CREATE.getCode(), user, "已提交分拣订单，直接审核通过");
                    }else{
                        addLogNoGoods(orderVO, OrderLogType.CREATE.getCode(), user, "已提交订单，等待审核");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

            // 修改备注
            storeMemberCommentService.addOrUpdateCommnet(storeGoodsOrder);

            if(isApp){
                if("1".equals(storeGoodsOrder.getFromDingding())){
                }else{
                    //如果是微信下单， 下单成功后，需要更新购物车数据
                    StoreGoodsCartItem cartUpdate = new StoreGoodsCartItem();
                    cartUpdate.setUserId(storeGoodsOrder.getMerchantId());
                    cartUpdate.setStatus("0");
                    storeGoodsCartItemService.delCartGoodsWithUserId(cartUpdate);
                }
            }
        }
        return result;
    }

    /**
     * 修改订单信息主
     *
     * @param storeGoodsOrder
     *            订单信息主
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreGoodsOrder(StoreGoodsOrderDTO storeGoodsOrder,boolean isApp) {
        Map<String, String> map = getStartAndEndTime(new Date());
        String startTime = map.get("startTime");
        String endTime = map.get("endTime");
        if(!isApp){
            //校验订单密码
            checkOrderPwd(storeGoodsOrder);
        }



        // 检查订单商品是否存在于报价单中
        if(StringUtils.isNotBlank(storeGoodsOrder.getMerchantId())) {
            StoreMember member = storeMemberMapper.selectStoreMemberById(Long.parseLong(storeGoodsOrder.getMerchantId()));
            if (member == null) {
                throw new BusinessException("未查到商户信息");
            }
            if(OrderStatus.WAITING_REVIEW.getCode().equals(storeGoodsOrder.getStatus())
                || OrderStatus.WAITING_SORTING.getCode().equals(storeGoodsOrder.getStatus()) ) {
                checkOrderInQuotation(storeGoodsOrder, member.getQuotationId());
            }
        }

        int result = storeGoodsOrderMapper.updateStoreGoodsOrder(storeGoodsOrder);
        if (OrderStatus.CANCEL.getCode().equals(storeGoodsOrder.getStatus())) {
            SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
            CompletableFuture.runAsync(() -> {
                StoreGoodsOrderVO vo = selectStoreGoodsOrderById(storeGoodsOrder.getOrderId());
                addLogNoGoods(vo, OrderLogType.CANCEL.getCode(), user, "已作废");
            });

        }
        if (result == 1 && CollectionUtils.isNotEmpty(storeGoodsOrder.getItemList())) {
            StoreGoodsOrderItem query = new StoreGoodsOrderItem();
            query.setOrderId(storeGoodsOrder.getOrderId());
            List<StoreGoodsOrderItemVO> oldItemList = itemService.selectStoreGoodsOrderItemList(query);
            List<StoreGoodsOrderItemVO> addList = new ArrayList<>(storeGoodsOrder.getItemList().size());
            List<StoreGoodsOrderItemVO> updateList = new ArrayList<>(storeGoodsOrder.getItemList().size());
            List<StoreSortingDetail> updateSortList = new ArrayList<>(storeGoodsOrder.getItemList().size());
            List<StoreGoodsOrderItemVO> deleteList = new ArrayList<>();
            deleteList.addAll(oldItemList);
            List<Long> oldIds = oldItemList.stream().map(StoreGoodsOrderItem::getGoodsId).collect(Collectors.toList());
            List<Long> newIds = storeGoodsOrder.getItemList().stream().map(StoreGoodsOrderItem::getGoodsId).collect(Collectors.toList());
            for(Iterator<StoreGoodsOrderItemVO> it = deleteList.iterator();it.hasNext();){
                StoreGoodsOrderItem tempVo = it.next();
                if(newIds.contains(tempVo.getGoodsId())) {
                    it.remove();
                }
            }
            storeGoodsOrder.getItemList().forEach(i -> {
                StoreGoodsOrderItemVO vo = new StoreGoodsOrderItemVO();
                BeanUtils.copyProperties(i, vo);
                vo.setOrderId(storeGoodsOrder.getOrderId());
                if (!oldIds.contains(i.getGoodsId())) {

                    addList.add(vo);
                } else if (oldIds.contains(i.getGoodsId())) {
                    StoreGoodsOrderItem old =
                        oldItemList.stream().filter(e -> e.getGoodsId().equals(i.getGoodsId())).findFirst().get();
                    vo.setItemId(old.getItemId());
                    vo.setOldQuantity(old.getQuantity());
                    vo.setOldPirce(old.getPrice());
                    if((old.getSortingWeight() != null && !old.getSortingWeight().equals(i.getSortingWeight()))
                        || (old.getDiscount() != null && i.getDiscount() != null && old.getDiscount().equals(i.getDiscount()))
                        || old.getPrice() != i.getPrice()) {
                        StoreSortingDetail storeSortingDetail = new StoreSortingDetail();
                        storeSortingDetail.setOrderItemId(i.getItemId());
                        storeSortingDetail.setSortingWeight(i.getSortingWeight());
                        updateSortList.add(storeSortingDetail);
                        vo.setOldQuantity(old.getSortingWeight() != null ? old.getSortingWeight() : old.getQuantity());
                        // 设置原来的分拣数量
                        if(OrderStatus.SENDED.getCode().equals(storeGoodsOrder.getStatus())) {
                            vo.setOldSortingWeight(i.getSortingWeight());
                        }
                    }
                    updateList.add(vo);
                    //deleteList.remove(old);
                }
            });
            // 新增并减库存
            if (CollectionUtils.isNotEmpty(addList)) {
                addList.forEach(i -> {
                    StoreGoodsQuotationGoodsSaleNumDTO dto = StoreGoodsQuotationGoodsSaleNumDTO.builder()
                        .spuNo(i.getSpuNo()).isAdd(false).buyNum(i.getQuantity()).startTime(startTime).endTime(endTime).deptId(i.getDeptId()).build();
                    itemService.insertStoreGoodsOrderItem(i);
                    if (dto.getBuyNum() == 0) {
                        throw new BusinessException("商品【" + dto.getGoodsId() + "】购买数量不能为0");
                    }
                    int size = quotationGoodsService.updateSaleNum(dto);
                    if (size != 1) {
                        throw new BusinessException("商品【" + storeGoodsSpuMapper.selectStoreGoodsSpuById(dto.getSpuNo()).getName() + "】库存不足无法新增订单");
                    }
                });
            }
            // 删除并加库存
            if (CollectionUtils.isNotEmpty(deleteList)) {

                deleteList.forEach(i -> {
                    StoreGoodsQuotationGoodsSaleNumDTO dto = StoreGoodsQuotationGoodsSaleNumDTO.builder().isAdd(true)
                        .spuNo(i.getSpuNo()).buyNum(i.getQuantity()).startTime(startTime).endTime(endTime).deptId(storeGoodsOrder.getDeptId()).build();
                    itemService.deleteStoreGoodsOrderItemById(i.getItemId());
                    quotationGoodsService.updateSaleNum(dto);
                });
            }
            // 更新，根据新老的数量加减库存
            if (CollectionUtils.isNotEmpty(updateList)) {
                BiFunction<Double, Double, Double> sub = (x, y) -> BigDecimal.valueOf(x).subtract(BigDecimal.valueOf(y))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                updateList.forEach(i -> {
                    if (i.getQuantity() > i.getOldQuantity()) {
                        StoreGoodsQuotationGoodsSaleNumDTO dto =
                            StoreGoodsQuotationGoodsSaleNumDTO.builder().spuNo(i.getSpuNo()).isAdd(false)
                                .buyNum(sub.apply(i.getQuantity(), i.getOldQuantity())).startTime(startTime).endTime(endTime).deptId(i.getDeptId()).build();
                        int size = quotationGoodsService.updateSaleNum(dto);
                        if (dto.getBuyNum() == 0) {
                            throw new BusinessException("商品【" + dto.getGoodsId() + "】购买数量不能为0");
                        }
                        if (size != 1) {
                            throw new BusinessException("商品【" + storeGoodsSpuMapper.selectStoreGoodsSpuById(dto.getSpuNo()).getName() + "】库存不足无法新增订单");
                        }
                    } else if (i.getQuantity() < i.getOldQuantity()) {
                        StoreGoodsQuotationGoodsSaleNumDTO dto =
                            StoreGoodsQuotationGoodsSaleNumDTO.builder().spuNo(i.getSpuNo()).isAdd(true)
                                .buyNum(sub.apply(i.getOldQuantity(), i.getQuantity())).deptId(storeGoodsOrder.getDeptId()).build();
                        quotationGoodsService.updateSaleNum(dto);
                    }
                    itemService.updateStoreGoodsOrderItem(i);
                });
            }

            // 更新分拣的结算数量
            if (CollectionUtils.isNotEmpty(updateSortList)) {
                updateSortList.forEach(i -> {

                    // 查询原来的商品分拣记录
                    StoreSortingDetail where = new StoreSortingDetail();
                    where.setOrderItemId(i.getOrderItemId());
                    where.setStatus(Constants.NORMAL);
                    List<StoreSortingDetail> exsitList = storeSortingDetailService.selectStoreSortingDetailList(where);

                    // 更新商品的分拣记录
                    if(CollectionUtils.isNotEmpty(exsitList)) {
                        StoreSortingDetail detailNew = new StoreSortingDetail();
                        detailNew.setOrderId(storeGoodsOrder.getOrderId());
                        detailNew.setOrderItemId(exsitList.get(0).getOrderItemId());
                        detailNew.setSortingQuantity(exsitList.get(0).getSortingQuantity());
                        detailNew.setSortingQuantityUnit(exsitList.get(0).getSortingQuantityUnit());
                        detailNew.setOutStockStatus(exsitList.get(0).getOutStockStatus());
                        detailNew.setSortingWeight(i.getSortingWeight());
                        storeSortingDetailService.updateSortDetailForItem(detailNew);
                    }
                });
            }
            addUpdateOrderLog(storeGoodsOrder, addList, updateList, deleteList);
        }

        // 修改备注
        storeMemberCommentService.addOrUpdateCommnet(storeGoodsOrder);

        return result;
    }

    /**
     * 新增订单修改日志
     *
     * @param storeGoodsOrder
     * @param addList
     * @param updateList
     * @param deleteList
     */
    private void addUpdateOrderLog(StoreGoodsOrderDTO storeGoodsOrder, List<StoreGoodsOrderItemVO> addList,
        List<StoreGoodsOrderItemVO> updateList, List<StoreGoodsOrderItemVO> deleteList) {
        SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
        String addText = "添加商品->名称：%s,商品编号：%s,价格：%s,数量：%s。", update = "商品->名称：%s,商品编号：%s,价格：%s改为%s,数量：%s改为%s。",
            remove = "移除商品->名称：%s,商品编号：%s。", updatePrice = "名称：%s,商品编号：%s,原价格：%s，修改后：%s";
        CompletableFuture.runAsync(() -> {

            if (CollectionUtils.isNotEmpty(addList)) {
                for (StoreGoodsOrderItemVO v : addList) {
                    StoreGoodsQuotationGoodsVO goodsVO =
                        quotationGoodsService.selectStoreGoodsQuotationGoodsById(v.getGoodsId());
                    String optionLog = String.format(addText, goodsVO.getPropertyName(), goodsVO.getSpuNo(),
                        v.getPrice(), v.getQuantity());
                    saveLog(storeGoodsOrder, OrderLogType.UPDATE.getCode(), user, v, optionLog, goodsVO.getSpuNo());
                }

            }
            if (CollectionUtils.isNotEmpty(updateList)) {

                for (StoreGoodsOrderItemVO v : updateList) {
                    StoreGoodsQuotationGoodsVO goodsVO =
                        quotationGoodsService.selectStoreGoodsQuotationGoodsById(v.getGoodsId());
                    if (!v.getPrice().equals(v.getOldPirce())) {
                        String optionPriceLog = String.format(updatePrice, goodsVO.getPropertyName(),
                            goodsVO.getSpuNo(), v.getOldPirce(), v.getPrice());
                        saveLog(storeGoodsOrder, OrderLogType.UPDATE_PRICE.getCode(), user, v, optionPriceLog,
                            goodsVO.getSpuNo());
                    }
                    String optionLog = String.format(update, goodsVO.getPropertyName(), goodsVO.getSpuNo(),
                        v.getOldPirce(), v.getPrice(), v.getOldQuantity(), v.getQuantity());
                    saveLog(storeGoodsOrder, OrderLogType.UPDATE.getCode(), user, v, optionLog, goodsVO.getSpuNo());
                }

            }
            if (CollectionUtils.isNotEmpty(deleteList)) {
                for (StoreGoodsOrderItemVO v : deleteList) {
                    StoreGoodsQuotationGoodsVO goodsVO =
                        quotationGoodsService.selectStoreGoodsQuotationGoodsById(v.getGoodsId());
                    String optionLog = String.format(remove, goodsVO.getPropertyName(), goodsVO.getSpuNo());
                    saveLog(storeGoodsOrder, OrderLogType.UPDATE.getCode(), user, v, optionLog, goodsVO.getSpuNo());
                }
            }

        });
    }

    /**
     * 单格商品日志
     * 
     * @param order
     * @param type
     * @param user
     * @param i
     * @param operationLog
     */
    private void saveLog(StoreGoodsOrderDTO order, String type, SysUser user, StoreGoodsOrderItemVO i,
        String operationLog, String spu) {
        storeGoodsOrderLogService
            .insertStoreGoodsOrderLog(StoreGoodsOrderLog.builder().type(type).code(order.getCode()).spuNo(spu)
                .orderId(order.getOrderId()).itemId(i.getItemId()).goodId(i.getGoodsId()).quantity(i.getQuantity())
                .amount(i.getAmount()).operateUserId(user.getUserId()).operationLog(operationLog).build());
    }

    /**
     * 订单作废
     *
     * @param ids 作废的订单id
     *
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsOrderByIdsCheckIfNeedRestore(String ids,boolean restoreCart) {
        //1、先查询需要删除的订单信息，判断是否需要还原购物车
        List<StoreGoodsOrderVO> needRestoreOrderList = new ArrayList<>();
        String[] idsArr = Convert.toStrArray(ids);
        for (int i = 0; i < idsArr.length; i++) {
            StoreGoodsOrderVO vo = selectStoreGoodsOrderById(Long.valueOf(idsArr[i]));
            Map<String, String> map = getStartAndEndTime(vo.getCreateTime());
            String startTime = map.get("startTime");
            String endTime = map.get("endTime");
            vo.getItemList().forEach(item->{
                StoreGoodsQuotationGoodsSaleNumDTO dto =
                    StoreGoodsQuotationGoodsSaleNumDTO.builder().spuNo(item.getSpuNo()).isAdd(true)
                        .buyNum(item.getQuantity()).startTime(startTime).endTime(endTime).deptId(vo.getDeptId()).build();
                int size = quotationGoodsService.updateSaleNum(dto);
            });

            //如果是微信下单，状态为未审核则需要还原购物车数据
            if(restoreCart && OrderSource.WX.getCode().equals(vo.getSource()) && OrderStatus.WAITING_REVIEW.getCode().equals(vo.getStatus())){
                needRestoreOrderList.add(vo);
            }
        }

        //2、删除订单数据
        int delRet = deleteStoreGoodsOrderByIds(ids);
        if(delRet > 0){
            //3、删除成功，还原订单到购物车
            if(restoreCart && CollectionUtils.isNotEmpty(needRestoreOrderList)){
                for (StoreGoodsOrderVO info: needRestoreOrderList){
                    if(CollectionUtils.isNotEmpty(info.getItemList())){
                        for (StoreGoodsOrderItemVO item : info.getItemList()){
                            StoreGoodsCartItem cartItem = new StoreGoodsCartItem();
                            cartItem.setUserId(info.getMerchantId());
                            cartItem.setGoodsId(item.getGoodsId() + "");
                            cartItem.setSpuNo(item.getSpuNo());
                            cartItem.setQuantity(item.getQuantity());
                            cartItem.setAmount(item.getAmount());
                            cartItem.setPrice(item.getPrice());
                            cartItem.setComment(item.getComment());
                            cartItem.setStatus("1");
                            if(item.getUnitId() != null){
                                cartItem.setUnitId(Double.valueOf(item.getUnitId()));
                            }
                            storeGoodsCartItemService.insertStoreGoodsCartItem(cartItem);
                        }
                    }
                }
            }
        }
        return delRet;
    }

    /**
     * 订单作废
     *
     * @param ids 作废的订单id
     *
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsOrderByIds(String ids) {
        String[] arr = Convert.toStrArray(ids);
        storeGoodsOrderMapper.updateOldOfStatus(Arrays.asList(arr));
        int result = storeGoodsOrderMapper.deleteStoreGoodsOrderByIds(arr);
        SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
        CompletableFuture.runAsync(() -> {
            String[] idsArr = Convert.toStrArray(ids);
            for (int i = 0; i < idsArr.length; i++) {
                StoreGoodsOrderVO vo = selectStoreGoodsOrderById(Long.valueOf(idsArr[i]));
                if (vo != null) {
                    addLogNoGoods(vo, OrderLogType.CANCEL.getCode(), user, "订单已作废");
                }
            }
        });
        return result;
    }

    /**
     * 订单作废还原
     *
     * @param ids
     *            订单作废还原数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int revertStoreGoodsOrderByIds(String ids) {
        String[] arr = Convert.toStrArray(ids);
        int result = storeGoodsOrderMapper.updateStatusOfOld(Arrays.asList(arr));
        for (int i = 0; i < arr.length; i++) {
            StoreGoodsOrderVO vo = selectStoreGoodsOrderById(Long.valueOf(arr[i]));
            if (vo != null) {
                Map<String, String> map = getStartAndEndTime(vo.getCreateTime());
                String startTime = map.get("startTime");
                String endTime = map.get("endTime");
                vo.getItemList().forEach(item->{
                    StoreGoodsQuotationGoodsSaleNumDTO dto =
                        StoreGoodsQuotationGoodsSaleNumDTO.builder().spuNo(item.getSpuNo()).isAdd(false)
                            .buyNum(item.getQuantity()).startTime(startTime).endTime(endTime).deptId(vo.getDeptId()).build();
                    int size = quotationGoodsService.updateSaleNum(dto);
                    if (size != 1) {
                        throw new BusinessException("商品【" + storeGoodsSpuMapper.selectStoreGoodsSpuById(dto.getSpuNo()).getName() + "】库存不足无法还原订单");
                    }
                });
            }
        }
        SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
        CompletableFuture.runAsync(() -> {
            String[] idsArr = Convert.toStrArray(ids);
            for (int i = 0; i < idsArr.length; i++) {
                StoreGoodsOrderVO vo = selectStoreGoodsOrderById(Long.valueOf(idsArr[i]));
                if (vo != null) {
                    addLogNoGoods(vo, OrderLogType.CANCEL_REVERT.getCode(), user, "订单取消作废");
                }
            }
        });
        return result;
    }

    /**
     * 审核订单
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int examineStoreGoodsOrderByIds(String ids) {
        int result = storeGoodsOrderMapper.examineStoreGoodsOrderByIds(Convert.toStrArray(ids));
        // 异步添加审核日志
        SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
        CompletableFuture.runAsync(() -> {
            String[] idsArr = Convert.toStrArray(ids);
            StoreGoodsOrderQueryDTO query = new StoreGoodsOrderQueryDTO();
            for (int i = 0; i < idsArr.length; i++) {
                query.setOrderId(Long.valueOf(idsArr[i]));
                List<StoreGoodsOrderVO> list = storeGoodsOrderMapper.selectStoreGoodsOrderList(query);
                if (CollectionUtils.isNotEmpty(list)) {
                    addLogNoGoods(list.get(i), OrderLogType.EXAMINE.getCode(), user, "订单已通过审核");
                }
            }
        });
        return result;
    }

    @Override
    public int financialExamineStoreGoodsOrderByIds(String ids) {
        int result = storeGoodsOrderMapper.financialExamineStoreGoodsOrderByIds(Convert.toStrArray(ids));
        // 异步添加审核日志
        SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
        CompletableFuture.runAsync(() -> {
            String[] idsArr = Convert.toStrArray(ids);
            StoreGoodsOrderQueryDTO query = new StoreGoodsOrderQueryDTO();
            for (int i = 0; i < idsArr.length; i++) {
                query.setOrderId(Long.valueOf(idsArr[i]));
                List<StoreGoodsOrderVO> list = storeGoodsOrderMapper.selectStoreGoodsOrderList(query);
                if (CollectionUtils.isNotEmpty(list)) {
                    addLogNoGoods(list.get(i), OrderLogType.FINANCE_EXAMINE.getCode(), user, "订单财务审核通过");
                }
            }
        });
        return result;
    }

    @Override
    public int financialCancelStoreGoodsOrderByIds(String ids) {
        int result = storeGoodsOrderMapper.financialCancelStoreGoodsOrderByIds(Convert.toStrArray(ids));
        if (result == 0) {
            throw new BusinessException("订单已经支付，不能取消审核！");
        }
        // 异步添加审核日志
        SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
        CompletableFuture.runAsync(() -> {
            String[] idsArr = Convert.toStrArray(ids);
            StoreGoodsOrderQueryDTO query = new StoreGoodsOrderQueryDTO();
            for (int i = 0; i < idsArr.length; i++) {
                query.setOrderId(Long.valueOf(idsArr[i]));
                List<StoreGoodsOrderVO> list = storeGoodsOrderMapper.selectStoreGoodsOrderList(query);
                if (CollectionUtils.isNotEmpty(list)) {
                    addLogNoGoods(list.get(i), OrderLogType.FINANCE_EXAMINE.getCode(), user, "订单财务取消审核");
                }
            }
        });
        return result;
    }

    /**
     * 添加日志:与商品无关
     *
     * @param order
     * @param type
     * @param user
     */
    @Override
    public void addLogNoGoods(StoreGoodsOrderVO order, String type, SysUser user, String operationLog) {
        storeGoodsOrderLogService.insertStoreGoodsOrderLog(StoreGoodsOrderLog.builder().type(type).code(order.getCode())
            .orderId(order.getOrderId()).operateUserId(user.getUserId()).operationLog(operationLog).build());
    }

    /**
     * 删除订单信息主信息
     *
     * @param orderId
     *            订单信息主ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsOrderById(Long orderId) {
        int result = storeGoodsOrderMapper.deleteStoreGoodsOrderById(orderId);
        if (result == 1) {
            CompletableFuture.runAsync(() -> {
                StoreGoodsOrderQueryDTO query = new StoreGoodsOrderQueryDTO();
                query.setOrderId(orderId);
                List<StoreGoodsOrderVO> list = storeGoodsOrderMapper.selectStoreGoodsOrderList(query);
                if (CollectionUtils.isNotEmpty(list)) {
                    SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
                    addLogNoGoods(list.get(0), OrderLogType.DELETE.getCode(), user, "删除订单");
                }
            });
        }
        return result;
    }

    /**
     * 查询订单信息主列表
     *
     * @param storeGoodsOrder
     *            订单信息主
     * @return 订单信息主
     */
    @Override
    public List<StoreGoodsOrderVO> selectStoreGoodsOrderListForSorting(StoreGoodsOrderQueryDTO storeGoodsOrder) {
        List<StoreGoodsOrderVO> list = storeGoodsOrderMapper.selectStoreGoodsOrderListForSorting(storeGoodsOrder);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(o -> {
                o.setPayStatusName(OrderPayStatus.getDescValue(o.getPayStatus()));
                o.setSourceName(OrderSource.getDescValue(o.getSource()));
                o.setPrintStatusName(OrderPrintStatus.getDescValue(o.getPrintStatus()));
                o.setStatusName(OrderStatus.getDescValue(o.getStatus()));
            });
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StoreGoodsOrderSortingCountVO> getStoreGoodsOrderSortingCountByIds(String ids) {
        return storeGoodsOrderMapper.getStoreGoodsOrderSortingCountByIds(Convert.toStrArray(ids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SortingQueryMerchantInfo> getSortingMerchantInfo() {
        String loginUserDeptId = null;
        SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
        if (!user.isAdmin()) {
            loginUserDeptId = user.getDept().getDeptId();
        }
        return storeGoodsOrderMapper.getSortingMerchantInfo(loginUserDeptId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StoreDeliverRoute> getSortingRouteInfo() {
        String loginUserDeptId = null;
        SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
        if (!user.isAdmin()) {
            loginUserDeptId = user.getDept().getDeptId();
        }
        return storeGoodsOrderMapper.getSortingRouteInfo(loginUserDeptId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StoreGoodsQuotation> getSortingQuotationInfo() {
        String loginUserDeptId = null;
        SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
        if (!user.isAdmin()) {
            loginUserDeptId = user.getDept().getDeptId();
        }
        return storeGoodsOrderMapper.getSortingQuotationInfo(loginUserDeptId);
    }

    /**
     * 查询订单信息主
     *
     * @param req
     *            订单信息主ID
     * @return 订单信息主
     */
    @Override
    public List<StoreGoodsOrderSortingItemVO> selectStoreGoodsOrderByIdForSorting(StoreGoodsOrderSortingQueryReq req) {
        StoreGoodsOrderItem query = new StoreGoodsOrderItem();
        query.setOrderId(req.getOrderId());
        query.setDeptId(req.getDeptId());
        List<StoreGoodsOrderSortingItemVO> itemList = itemService.selectStoreGoodsOrderItemListForSorting(query);
        return itemList;
    }

    @Override
    public int updateStatusOfOld(List<String> orderIdList) {
        return storeGoodsOrderMapper.updateStatusOfOld(orderIdList);
    }

    @Override
    public int updateOldOfStatus(List<String> orderIdList) {
        return storeGoodsOrderMapper.updateOldOfStatus(orderIdList);
    }

    /**
     * 检查订单商品是否存在于报价单中
     * @param storeGoodsOrder
     * @param quotationId
     */
    private void checkOrderInQuotation(StoreGoodsOrderDTO storeGoodsOrder, Long quotationId) {
        if (quotationId == null) {
            throw new BusinessException("商户未关联报价单");
        }
        StoreGoodsQuotationGoodsQueryDTO storeGoodsQuotationGoodsQueryDto = new StoreGoodsQuotationGoodsQueryDTO();
        storeGoodsQuotationGoodsQueryDto.setQuotationId(quotationId);
        storeGoodsQuotationGoodsQueryDto.setStatus(GoodsQuotationStatus.ON.getValue());
        List<StoreGoodsQuotationGoodsVO> goodsQuotationGoodsVOList = storeGoodsQuotationGoodsMapper.selectStoreGoodsQuotationGoodsList(storeGoodsQuotationGoodsQueryDto);
        if (CollectionUtils.isEmpty(goodsQuotationGoodsVOList)) {
            throw new BusinessException("商户关联的报价单下无商品信息！");
        }
        List<Long> goodsIdList = goodsQuotationGoodsVOList.stream().map(StoreGoodsQuotationGoodsVO::getGoodsId).collect(Collectors.toList());
        boolean exist = true;
        for (StoreGoodsOrderItem obj : storeGoodsOrder.getItemList())
        {
            if (!goodsIdList.contains(obj.getGoodsId()))
            {
                exist = false;
                break;
            }
        }

        if(!exist) {
            throw new BusinessException("订单中的商品信息，在报价单中不存在！");
        }
    }

    private void checkOrderPwd(StoreGoodsOrderDTO storeGoodsOrder) {

        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(user != null && user.getRoles() != null && !user.getRoles().isEmpty()
                && PermissionConstants.ORDER_PASSWORD_ROLE_LIST.contains(user.getRoles().get(0).getRoleKey())){
            return;
        }

        //如果没有商品列表，直接返回
        if(storeGoodsOrder.getItemList() == null){
            return;
        }
//        List<Long> goodsIdList = new ArrayList<>();
//        for (StoreGoodsOrderItem item : storeGoodsOrder.getItemList()){
//            goodsIdList.add(item.getGoodsId());
//        }
//        StoreGoodsQuotationGoodsQueryDTO storeGoodsQuotationGoods = new StoreGoodsQuotationGoodsQueryDTO();
//        storeGoodsQuotationGoods.setGoodsIdList(goodsIdList);
//        List<StoreGoodsQuotationGoodsVO> list = storeGoodsQuotationGoodsService.selectStoreGoodsQuotationGoodsList(storeGoodsQuotationGoods);
//
//        // 订单商品存在折扣不为1的需要校验
//        boolean isCheck = false;
//        for (StoreGoodsOrderItem item : storeGoodsOrder.getItemList()){
//            for (StoreGoodsQuotationGoodsVO goodsInfo : list) {
//                if(item.getGoodsId().equals(goodsInfo.getGoodsId()) && !item.getPrice().equals(goodsInfo.getSettingPrice() != null ? goodsInfo.getSettingPrice() : goodsInfo.getPrice())) {
//                    isCheck = true;
//                    break;
//                }
//            }
////            if(item.getDiscount() != null && item.getDiscount() < 1.0){
////                isCheck = true;
////            }
//        }
        boolean isCheck = isCheckFlag(storeGoodsOrder);

        if(isCheck) {
            // 获取订单对应区域的密码
            StoreOrderPassword reqPwd = new StoreOrderPassword();
            reqPwd.setDeptId(storeGoodsOrder.getDeptId());
            List<StoreOrderPassword> pwdList = storeOrderPasswordMapper.selectStoreOrderPasswordList(reqPwd);
            //校验密码
            boolean isCorrect = false;
            for (StoreOrderPassword o : pwdList)
            {
                if(o.getPassword().equals(storeGoodsOrder.getOrderPassword())){
                    isCorrect = true;
                }
            }
            if (!isCorrect) {
                throw new BusinessException("订单密码错误，请联系管理员或区域经理！");
            }
        }
    }

    private boolean isCheckFlag(StoreGoodsOrderDTO storeGoodsOrder) {
        List<StoreGoodsOrderItem> insertList = new ArrayList<>();
        List<StoreGoodsOrderItem> updateList = new ArrayList<>();

        if(storeGoodsOrder.getOrderId() != null) {

            storeGoodsOrder.getItemList().forEach(obj->{
                if(obj.getItemId() != null) {
                    updateList.add(obj);
                } else {
                    insertList.add(obj);
                }
            });

        } else {
            insertList.addAll(storeGoodsOrder.getItemList());
        }


        // 新增的订单商品和报价单中价格比较，价格不一致需要校验密码
        if(CollectionUtils.isNotEmpty(insertList)) {
            List<Long> goodsIdList = new ArrayList<>();
            for (StoreGoodsOrderItem item : insertList){
                goodsIdList.add(item.getGoodsId());
            }
            StoreGoodsQuotationGoodsQueryDTO storeGoodsQuotationGoods = new StoreGoodsQuotationGoodsQueryDTO();
            storeGoodsQuotationGoods.setGoodsIdList(goodsIdList);
            List<StoreGoodsQuotationGoodsVO> list = storeGoodsQuotationGoodsService.selectStoreGoodsQuotationGoodsList(storeGoodsQuotationGoods);

            for (StoreGoodsOrderItem item : insertList){
                for (StoreGoodsQuotationGoodsVO goodsInfo : list) {
                    if(item.getGoodsId().equals(goodsInfo.getGoodsId()) && !item.getPrice().equals(goodsInfo.getSettingPrice() != null ? goodsInfo.getSettingPrice() : goodsInfo.getPrice())) {
                        return true;
                    }
                }
            }
        }

        // 修改的订单商品和原订单中价格比较，价格不一致需要校验密码
        if(CollectionUtils.isNotEmpty(updateList)) {
            StoreGoodsOrderItem item = new StoreGoodsOrderItem();
            item.setOrderId(storeGoodsOrder.getOrderId());
            List<StoreGoodsOrderItemVO> itemDbList = storeGoodsOrderItemMapper.selectStoreGoodsOrderItemList(item);

            for (StoreGoodsOrderItem itemReq : updateList){
                for (StoreGoodsOrderItemVO itemDb : itemDbList) {
                    if(itemReq.getItemId().equals(itemDb.getItemId()) && !itemReq.getPrice().equals(itemDb.getPrice())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int updateStoreGoodsOrderPrintByIds(List<String> orderIdList) {
        return storeGoodsOrderMapper.updateStoreGoodsOrderPrintByIds(orderIdList);
    }

    @Override
    public int updateStoreGoodsOrderOdoPrintByIds(List<String> orderIdList) {
        return storeGoodsOrderMapper.updateStoreGoodsOrderOdoPrintByIds(orderIdList);
    }

    private Map<String, String> getStartAndEndTime(Date d) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, d);
        if(hour < 9) {
            today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(d, -1));
        }
        String startTime = today.concat(" 00:00:00");
        String endTime = today.concat(" 23:59:59");
        Map<String, String> map = new HashMap<>();
        map.put("startTime",  startTime);
        map.put("endTime",  endTime);
        return map;
    }

    @Override
    public List<Long> checkTodayOrder(StoreGoodsCheckTodayOrder checkTodayOrder){
        List<Long> retList = storeGoodsOrderMapper.checkTodayOrder(checkTodayOrder);
        if(retList == null){
            retList = new ArrayList<>();
        }
        return retList;
    }

    @Override
    public List<StoreGoodsOrderVO> queryUnpayOrderList() {
        return storeGoodsOrderMapper.queryUnpayOrderList();
    }
}
