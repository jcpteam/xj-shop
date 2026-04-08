package com.javaboot.shop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.enums.*;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.dto.*;
import com.javaboot.shop.service.*;
import com.javaboot.shop.vo.*;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreGoodsReturnOrderMapper;
import com.javaboot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 退货单信息主Service业务层处理
 * 
 * @author lqh
 * @date 2021-06-26
 */
@Service
public class StoreGoodsReturnOrderServiceImpl implements IStoreGoodsReturnOrderService {
    @Autowired
    private StoreGoodsReturnOrderMapper storeGoodsReturnOrderMapper;
    @Autowired
    private IStoreGoodsReturnOrderItemService itemService;
    @Autowired
    private IStoreHouseService storeHouseService;
    @Autowired
    private IStoreGoodsSpuUnitService storeGoodsSpuUnitService;
    @Autowired
    private IStoreGoodsSpuUnitConversionService storeGoodsSpuUnitConversionService;
    @Autowired
    private IStoreWarehouseStockService storeWarehouseStockService;
    /**
     * 查询退货单信息主
     * 
     * @param returnOrderId
     *            退货单信息主ID
     * @return 退货单信息主
     */
    @Override
    public StoreGoodsReturnOrderVO selectStoreGoodsReturnOrderById(Long returnOrderId) {
        StoreGoodsReturnOrderVO vo = storeGoodsReturnOrderMapper.selectStoreGoodsReturnOrderById(returnOrderId);
        StoreGoodsReturnOrderItem query = new StoreGoodsReturnOrderItem();
        query.setReturnOrderId(returnOrderId);
        List<StoreGoodsReturnOrderItemVO> itemList = itemService.selectStoreGoodsReturnOrderItemList(query);
        vo.setItemList(itemList);
        return vo;
    }

    /**
     * 查询退货单信息主列表
     * 
     * @param storeGoodsReturnOrder 退货单信息主
     * @return 退货单信息主
     */
    @Override
    public List<StoreGoodsReturnOrderVO> selectStoreGoodsReturnOrderList(StoreGoodsReturnOrderQueryDTO storeGoodsReturnOrder) {
        List<StoreGoodsReturnOrderVO> list=storeGoodsReturnOrderMapper.selectStoreGoodsReturnOrderList(storeGoodsReturnOrder);
        if(CollectionUtils.isNotEmpty(list)) {
            List<StoreHouse> houseList = storeHouseService.selectStoreHouseListByIds(list.stream().map(StoreGoodsReturnOrderVO::getWarehouseId).collect(Collectors.toList()));
            list.forEach(o -> {
                StoreHouse warehouse = CollectionUtils.isEmpty(houseList) ? null : houseList.stream().filter(h -> h.getStoreId().equals(o.getWarehouseId())).findFirst().orElse(null);
                if (warehouse != null) {
                    o.setWarehouseName(warehouse.getStoreName());
                    o.setStoreType(warehouse.getStoreType());
                }
                o.setPayStatusName(OrderPayStatus.getDescValue(o.getPayStatus()));
                o.setStatusName(ReturnOrderStatus.getDescValue(o.getStatus()));
                o.setTypeName(ReturnOrderType.getDescValue(o.getReturnType()));
            });
        }
        return list;
    }

    /**
     * 新增退货单信息主
     * 
     * @param storeGoodsReturnOrder
     *            退货单信息主
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreGoodsReturnOrder(StoreGoodsReturnOrderDTO storeGoodsReturnOrder) {
        storeGoodsReturnOrder.setCode(CodeConstants.RETURN_ORDER+DateUtils.getRandom());
        int result = storeGoodsReturnOrderMapper.insertStoreGoodsReturnOrder(storeGoodsReturnOrder);
        if (result == 1) {
            storeGoodsReturnOrder.getItemList().forEach(i -> {
                i.setReturnOrderId(storeGoodsReturnOrder.getReturnOrderId());
                itemService.insertStoreGoodsReturnOrderItem(i);
            });
        }
        return result;
    }

    /**
     * 修改退货单信息主
     * 
     * @param storeGoodsReturnOrder
     *            退货单信息主
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreGoodsReturnOrder(StoreGoodsReturnOrderDTO storeGoodsReturnOrder) {
        int result =  storeGoodsReturnOrderMapper.updateStoreGoodsReturnOrder(storeGoodsReturnOrder);
        if(result==1){
            itemService.deleteStoreGoodsReturnOrderItemByReturnOrderId(storeGoodsReturnOrder.getReturnOrderId());
            storeGoodsReturnOrder.getItemList().forEach(i -> {
                i.setReturnOrderId(storeGoodsReturnOrder.getReturnOrderId());
                itemService.insertStoreGoodsReturnOrderItem(i);
            });
        }
        return result;
    }

    /**
     * 删除退货单信息主对象
     * 
     * @param ids
     *            需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsReturnOrderByIds(String ids) {
        return storeGoodsReturnOrderMapper.deleteStoreGoodsReturnOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除退货单信息主信息
     * 
     * @param returnOrderId
     *            退货单信息主ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsReturnOrderById(Long returnOrderId) {
        return storeGoodsReturnOrderMapper.deleteStoreGoodsReturnOrderById(returnOrderId);
    }


    /**
     * 审核退货单
     * @param examine
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int examine(StoreGoodsReturnOrderExamineDTO examine) {
        int result= storeGoodsReturnOrderMapper.examine(examine);
        if(ReturnOrderStatus.PASS.getCode().equals(examine.getStatus())){
            StoreGoodsReturnOrderQueryDTO query=new StoreGoodsReturnOrderQueryDTO();
            query.setReturnOrderIds(examine.getIdList());
            List<StoreGoodsReturnOrderVO> list=selectStoreGoodsReturnOrderList(query);
            list.forEach(l->{
                if(ReturnOrderType.WAREHOUSING.getCode().equals(l.getReturnType())){
                    StoreWarehouseStockDTO dto=new StoreWarehouseStockDTO();
                    dto.setDeptId(l.getDeptId());
                    dto.setStatus(Constants.NORMAL);
                    dto.setWarehouseId(l.getWarehouseId());
                    dto.setStockType(l.getStoreType());
                    dto.setStockNo(l.getCode());
                    dto.setCategory(StockCategory.RETURN_ORDER.getCode());
                    StoreGoodsReturnOrderItem queryItem = new StoreGoodsReturnOrderItem();
                    queryItem.setReturnOrderId(l.getReturnOrderId());
                    List<StoreGoodsReturnOrderItemVO> returnItemList = itemService.selectStoreGoodsReturnOrderItemList(queryItem);
                    List<StoreWarehouseStockItem> itemList=new ArrayList<>(returnItemList.size());
                    //查询单位
                    StoreGoodsSpuUnit unitQuery=new StoreGoodsSpuUnit();
                    unitQuery.setSpuNoList(itemList.stream().map(StoreWarehouseStockItem::getSpuNo).collect(Collectors.toList()));
                    List<StoreGoodsSpuUnit> unitList=storeGoodsSpuUnitService.selectStoreGoodsSpuUnitList(unitQuery);
                    //查询换算关系
                    StoreGoodsSpuUnitConversion queryCov=new StoreGoodsSpuUnitConversion();
                    queryCov.setSpuNoList(unitQuery.getSpuNoList());
                    List<StoreGoodsSpuUnitConversion> listCov= storeGoodsSpuUnitConversionService.selectStoreGoodsSpuUnitConversionList(queryCov);
                    returnItemList.forEach(i->{
                        StoreWarehouseStockItem item=new StoreWarehouseStockItem();
                        item.setSpuNo(i.getSpuNo());
                        item.setWarehouseId(l.getWarehouseId());
                        item.setDeptId(l.getDeptId());
                        item.setAmount(i.getAmount());
                        item.setQuantity(i.getQuantity());
                        //默认公斤
                        item.setQuantityUnit("1");
                        item.setWeightUnit("1");
                        item.setQuantity(i.getQuantity());
                        item.setStocksNumber(i.getQuantity());
                        if(CollectionUtils.isNotEmpty(unitList)){
                            StoreGoodsSpuUnit unit=  unitList.stream().filter(u->u.getSpuNo().equals(i.getSpuNo())).findFirst().orElse(null);
                            if(unit!=null){
                                item.setQuantityUnit(unit.getSubUnitId().toString());
                                item.setWeightUnit(unit.getMainUnitId().toString());
                                if(CollectionUtils.isNotEmpty(listCov)){
                                    StoreGoodsSpuUnitConversion cov=  listCov.stream().filter(u->u.getSpuNo().equals(i.getSpuNo())).findFirst().orElse(null);
                                    if(cov!=null){
                                        if(i.getUnitId().equals(unit.getMainUnitId())){
                                            item.setQuantity(i.getQuantity());
                                            item.setStocksNumber(BigDecimal.valueOf(cov.getMainCaseSub()*i.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                                        }else{
                                            item.setQuantity(BigDecimal.valueOf(cov.getSubCaseMain()*i.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                                            item.setStocksNumber(i.getQuantity());
                                        }
                                    }
                                }
                            }
                        }
                        itemList.add(item);
                    });
                    dto.setItemList(itemList);
                    storeWarehouseStockService.insertStoreWarehouseStock(dto);
                }
            });
        }
        return result;
    }
}
