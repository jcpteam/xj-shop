package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.StockCategory;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.dto.StoreWarehouseStockDTO;
import com.javaboot.shop.dto.StoreWarehouseStockQueryDTO;
import com.javaboot.shop.mapper.*;
import com.javaboot.shop.service.IStoreGoodsSpuUnitConversionService;
import com.javaboot.shop.service.IStoreHouseService;
import com.javaboot.shop.service.IStoreWarehouseStockItemService;
import com.javaboot.shop.service.IStoreWarehouseStockService;
import com.javaboot.shop.vo.StoreWarehouseStockVO;
import com.javaboot.system.domain.SysDept;
import com.javaboot.system.domain.SysDictData;
import com.javaboot.system.service.ISysDeptService;
import com.javaboot.system.service.ISysDictDataService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品入库Service业务层处理
 * 
 * @author lqh
 * @date 2021-05-20
 */
@Service
public class StoreWarehouseStockServiceImpl implements IStoreWarehouseStockService
{
    @Autowired
    private StoreWarehouseStockMapper storeWarehouseStockMapper;

    @Autowired
    private StoreWarehouseStockItemMapper storeWarehouseStockItemMapper;

    @Autowired
    private IStoreWarehouseStockItemService storeWarehouseStockItemService;

    @Autowired
    private StoreGoodsSpuUnitMapper storeGoodsSpuUnitMapper;

    @Autowired
    private StoreWarehouseInventoryMapper storeWarehouseInventoryMapper;

    @Autowired
    private StoreGoodPurchaseMapper storeGoodPurchaseMapper;

    @Autowired
    private StoreGoodsWarehouseAdjustMapper storeGoodsWarehouseAdjustMapper;

    @Autowired
    private IStoreHouseService storeHouseService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IStoreGoodsSpuUnitConversionService storeGoodsSpuUnitConversionService;

    @Autowired
    private StoreGoodsSpuUnitConversionMapper storeGoodsSpuUnitConversionMapper;

    @Autowired
    private StoreGoodPurchaseItemMapper storeGoodPurchaseItemMapper;

    @Autowired
    private StoreGoodsWarehouseAdjustItemMapper storeGoodsWarehouseAdjustItemMapper;

    /**
     * 查询商品入库
     * 
     * @param stockId 商品入库ID
     * @return 商品入库
     */
    @Override
    public StoreWarehouseStockVO selectStoreWarehouseStockById(Long stockId) {
        StoreWarehouseStockVO vo= storeWarehouseStockMapper.selectStoreWarehouseStockById(stockId);
        Map<String,Object> map=new HashMap<>(1);
        map.put("stock_id",stockId);
        map.put("status", Constants.NORMAL);
        vo.setItemList(storeWarehouseStockItemService.listByMap(map));
        return vo;
    }

    /**
     * 查询商品入库列表
     * 
     * @param storeWarehouseStock 商品入库
     * @return 商品入库
     */
    @Override
    public List<StoreWarehouseStockVO> selectStoreWarehouseStockList(StoreWarehouseStockQueryDTO storeWarehouseStock) {
        List<StoreWarehouseStockVO> list= storeWarehouseStockMapper.selectStoreWarehouseStockList(storeWarehouseStock);
        if(CollectionUtils.isNotEmpty(list)){
            List<StoreHouse> houseList= storeHouseService.selectStoreHouseListByIds(list.stream().map(StoreWarehouseStock::getWarehouseId).collect(Collectors.toList()));
            List<SysDictData>  sysDictDataList=    dictDataService.selectDictDataByType("store_house_type");
            SysDept deptQuery=new SysDept();
            deptQuery.setDeptIdList(list.stream().map(StoreWarehouseStock::getDeptId).collect(Collectors.toList()));
            List<SysDept> deptList=deptService.selectDeptList(deptQuery);
            if(CollectionUtils.isNotEmpty(houseList)){
                list.forEach(s->{
                    StoreHouse house= CollectionUtils.isEmpty(houseList)?null: houseList.stream().filter(h->h.getStoreId().equals(s.getWarehouseId())).findFirst().orElse(null);
                    SysDictData data=  CollectionUtils.isEmpty(sysDictDataList)?null:sysDictDataList.stream().filter(d->d.getDictValue().equals(s.getStockType())).findFirst().get();
                    SysDept dept= CollectionUtils.isEmpty(deptList)?null: deptList.stream().filter(d->d.getDeptId().equals(s.getDeptId())).findFirst().orElse(null);
                    if(house!=null){
                        s.setWarehouseName(house.getStoreName());
                    }
                    if(data!=null){
                        s.setStockTypeName(data.getDictLabel());
                    }
                    if(dept!=null){
                        s.setDeptName(dept.getDeptName());
                    }
                    s.setCategoryName(StockCategory.getDescValue(s.getCategory()));
                });
            }
        }
        return list;
    }

    /**
     * 新增商品入库
     * 
     * @param dto 商品入库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreWarehouseStock(StoreWarehouseStockDTO dto) {
        if(dto.getCreateTime() == null) {
            dto.setCreateTime(DateUtils.getNowDate());
        }
        dto.setStatus(Constants.NORMAL);
        dto.setStockNo(CodeConstants.STOCK+DateUtils.getRandom());
        int result=  storeWarehouseStockMapper.insertStoreWarehouseStock(dto);
        dto.getItemList().forEach(i->{
            i.setStockId(dto.getStockId());
            i.setWarehouseId(dto.getWarehouseId());
            i.setStatus(Constants.NORMAL);
        });
        storeWarehouseStockItemService.saveBatch(dto.getItemList(),dto.getItemList().size());
        calcUnitConversion(dto.getDeptId(), DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
        return result;
    }

    /**
     * EAS商品入库
     *
     * @param dto 商品入库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertEASStock(StoreWarehouseStockDTO dto) {
        // 判断入库单是否已经存在
        QueryWrapper query = new QueryWrapper<StoreWarehouseStock>();
        query.eq("stock_no",dto.getStockNo());
        List<StoreWarehouseStock> stockList = storeWarehouseStockMapper.selectList(query);
        if(CollectionUtils.isNotEmpty(stockList)) {
            return 0;
        }
        int result=  storeWarehouseStockMapper.insertStoreWarehouseStock(dto);
        dto.getItemList().forEach(i->{
            i.setStockId(dto.getStockId());
        });
        storeWarehouseStockItemService.saveBatch(dto.getItemList(),dto.getItemList().size());
        calcUnitConversion(dto.getDeptId(), DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
        return result;
    }

    /**
     * 修改商品入库
     * 
     * @param dto 商品入库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreWarehouseStock(StoreWarehouseStockDTO dto, boolean isReplace) {
        // 修改设置成未同步状态
        dto.setSynStatus("0");
        dto.setLastModifyTime(DateUtils.getNowDate());
        int result= storeWarehouseStockMapper.updateStoreWarehouseStock(dto);
        Map<String,Object> params=new HashMap<>();
        params.put("stock_id",dto.getStockId());
        params.put("status",Constants.NORMAL);
        List<StoreWarehouseStockItem> oldItem=storeWarehouseStockItemService.listByMap(params);
        oldItem.forEach(o->{
            o.setStatus(Constants.DELETE);
            o.setLastModifyTime(DateUtils.getNowDate());
        });

        StoreWarehouseStockVO stockDb = storeWarehouseStockMapper.selectStoreWarehouseStockById(dto.getStockId());

        if(StockCategory.ADJUST_IN_ORDER.getCode().equals(stockDb.getCategory())
            || StockCategory.ADJUST_OUT_ORDER.equals(stockDb.getCategory())) {
            StoreGoodsWarehouseAdjustItem qryObj = new StoreGoodsWarehouseAdjustItem();
            qryObj.setAdjustId(Long.parseLong(stockDb.getObjectId()));
            List<StoreGoodsWarehouseAdjustItem> storeGoodsWarehouseAdjustItemList = storeGoodsWarehouseAdjustItemMapper.selectStoreGoodsWarehouseAdjustItemList(qryObj);

            double adjustTotal = 0.0;
            double dtoTotal = 0.0;
            for (StoreGoodsWarehouseAdjustItem storeGoodsWarehouseAdjustItem : storeGoodsWarehouseAdjustItemList)
            {
                double one = new BigDecimal(storeGoodsWarehouseAdjustItem.getAmount() * storeGoodsWarehouseAdjustItem.getUnitNumber()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                adjustTotal += one;
            }

            for (StoreWarehouseStockItem dtoItem : dto.getItemList())
            {
                dtoTotal += dtoItem.getTotalPrice();
            }
            if(new BigDecimal(dtoTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() !=  new BigDecimal(adjustTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) {
                throw new BusinessException("入库单商品的总金额与调拨单商品总金额不一致！");

            }
        }
        storeWarehouseStockItemService.updateBatchById(oldItem);
        dto.getItemList().forEach(i->{
            oldItem.forEach(o->{
              if(i.getSpuNo().equals(o.getSpuNo()) && isReplace) {
                  i.setObjectJson(o.getObjectJson());
              }
            });
            i.setStockId(dto.getStockId());
            i.setWarehouseId(dto.getWarehouseId());
            i.setLastModifyTime(DateUtils.getNowDate());
            i.setStatus(Constants.NORMAL);
        });
        storeWarehouseStockItemService.saveBatch(dto.getItemList(),dto.getItemList().size());
        calcUnitConversion(dto.getDeptId(), DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
        return result;
    }

    /**
     * 删除商品入库对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreWarehouseStockByIds(String ids) {

        // 同步删除对应的调拨入库或者调拨出库单
        QueryWrapper<StoreWarehouseStock> stockQry = new QueryWrapper<>();
        stockQry.in("stock_id", Convert.toStrArray(ids));
        List<StoreWarehouseStock> storeWarehouseStockList = storeWarehouseStockMapper.selectList(stockQry);
        List<String> stockIdList = new ArrayList<>();
        storeWarehouseStockList.forEach(storeWarehouseStock -> {
            if(StockCategory.ADJUST_IN_ORDER.getCode().equals(storeWarehouseStock.getCategory())
                || StockCategory.ADJUST_OUT_ORDER.getCode().equals(storeWarehouseStock.getCategory()) ) {
                stockIdList.add(storeWarehouseStock.getObjectId());
            }

            // 删除入库单以后对应的采购和调拨单状态改为未生成
            if(StockCategory.PURCHASE_ORDER.getCode().equals(storeWarehouseStock.getCategory())) {
                QueryWrapper<StoreGoodPurchase> purchaseUpdate = new QueryWrapper<>();
                purchaseUpdate.in("purchase_id", storeWarehouseStock.getObjectId());
                StoreGoodPurchase purchase = new StoreGoodPurchase();
                purchase.setStockStatus(Constants.DELETE);
                storeGoodPurchaseMapper.update(purchase, purchaseUpdate);
            }

            if(StockCategory.ADJUST_IN_ORDER.getCode().equals(storeWarehouseStock.getCategory())
                || StockCategory.ADJUST_OUT_ORDER.getCode().equals(storeWarehouseStock.getCategory()) ) {
                QueryWrapper<StoreGoodsWarehouseAdjust> adjustUpdate = new QueryWrapper<>();
                adjustUpdate.in("adjust_id", storeWarehouseStock.getObjectId());
                StoreGoodsWarehouseAdjust adjust = new StoreGoodsWarehouseAdjust();
                adjust.setStockStatus(Constants.DELETE);
                storeGoodsWarehouseAdjustMapper.update(adjust, adjustUpdate);
            }

            String deptId = storeWarehouseStock.getDeptId();
            calcUnitConversion(deptId, DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
        });

        // 获取所有的调拨单id对应的入库单id列表
        if(CollectionUtils.isNotEmpty(stockIdList)) {
            stockQry.clear();
            stockQry.in("object_id", stockIdList);
            List<StoreWarehouseStock> allDelList = storeWarehouseStockMapper.selectList(stockQry);
            if(CollectionUtils.isNotEmpty(allDelList)) {
                List<Long> allIdList = allDelList.stream().map(StoreWarehouseStock::getStockId).collect(Collectors.toList());
                ids = StringUtils.join(allIdList, ",");
            }
        }

        QueryWrapper<StoreWarehouseStockItem> wq = new QueryWrapper<>();
        wq.in("stock_id", Convert.toStrArray(ids));
        wq.eq("status",Constants.NORMAL);
        List<StoreWarehouseStockItem> oldItem=storeWarehouseStockItemService.list(wq);
        oldItem.forEach(o->{
            o.setStatus(Constants.DELETE);
            o.setLastModifyTime(DateUtils.getNowDate());
        });

        storeWarehouseStockItemService.updateBatchById(oldItem);
        return storeWarehouseStockMapper.deleteStoreWarehouseStockByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品入库信息
     * 
     * @param stockId 商品入库ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreWarehouseStockById(Long stockId) {
        Map<String,Object> params=new HashMap<>();
        params.put("stock_id",stockId);
        params.put("status",Constants.NORMAL);
        List<StoreWarehouseStockItem> oldItem=storeWarehouseStockItemService.listByMap(params);
        oldItem.forEach(o->{
            o.setStatus(Constants.DELETE);
            o.setLastModifyTime(DateUtils.getNowDate());
        });
        storeWarehouseStockItemService.updateBatchById(oldItem);
        String deptId = storeWarehouseStockMapper.selectStoreWarehouseStockById(stockId).getDeptId();
        calcUnitConversion(deptId, DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
        return storeWarehouseStockMapper.deleteStoreWarehouseStockById(stockId);
    }

    /**
     * 计算SPU主，副单位的转换关系
     *
     * @param deptId
     * @param settingDate
     */
    private void calcUnitConversion(String deptId, String settingDate)
    {

//        List<StoreGoodsSpuUnit> spuUnits = storeGoodsSpuUnitMapper.selectStoreGoodsSpuUnitList(new StoreGoodsSpuUnit());
//        Set<String> spuUnitIds = new HashSet<>();
//        spuUnits.forEach(o->spuUnitIds.add(o.getSpuNo()));
//        // 获取最近的盘库日期
//        QueryWrapper<StoreWarehouseInventory> inventoryQueryWrapper = new QueryWrapper<>();
//        inventoryQueryWrapper.select("max(intentory_date) intentory_date");
//        inventoryQueryWrapper.eq("dept_id", deptId);
//        inventoryQueryWrapper.groupBy("dept_id");
//        StoreWarehouseInventory inventoryDate = storeWarehouseInventoryMapper.selectOne(inventoryQueryWrapper);
//
//        List<StoreWarehouseStockItem> mainUnitInventoryList = new ArrayList<>();
//        List<StoreWarehouseStockItem> subUnitInventoryList = new ArrayList<>();
//        Set<String> spuIds = new HashSet<>();
//
//        String startDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date());
//        if (inventoryDate != null)
//        {
//            startDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(inventoryDate.getIntentoryDate(), 1));
//            // 获取盘库SPU商品主，副单位数量
//            StoreWarehouseStockItem queryItem = new StoreWarehouseStockItem();
//            queryItem.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, inventoryDate.getIntentoryDate()));
//            queryItem.setDeptId(deptId);
//            List<StoreWarehouseStockItem> inventoryList = storeWarehouseStockItemMapper.selectInventoryQuantity(queryItem);
//
//            if (CollectionUtils.isNotEmpty(inventoryList))
//            {
//                // 未设置单位的入库单商品，不计算换算关系
//                for (Iterator<StoreWarehouseStockItem> it = inventoryList.iterator(); it.hasNext();){
//                    StoreWarehouseStockItem obj = it.next();
//                    if(!spuUnitIds.contains(obj.getSpuNo())) {
//                        it.remove();
//                    }
//                }
////                // 获取SPU商品的主，副单位
////                inventoryList.forEach(o -> spuIds.add(o.getSpuNo()));
////                StoreGoodsSpuUnit storeGoodsSpuUnit = new StoreGoodsSpuUnit();
////                storeGoodsSpuUnit.setSpuNoList(new ArrayList<>(spuIds));
////                List<StoreGoodsSpuUnit> unitList = storeGoodsSpuUnitMapper.selectStoreGoodsSpuUnitList(storeGoodsSpuUnit);
////
////                // 盘库SPU商品的数量按照主，副单位分组
////                unitList.forEach(unit -> inventoryList.forEach(o -> {
////                    if ((unit.getMainUnitId() + "").equals(o.getQuantityUnit()) && unit.getSpuNo().equals(o.getSpuNo()))
////                    {
////                        mainUnitInventoryList.add(o);
////                    }
////                }));
////
////                unitList.forEach(unit -> inventoryList.forEach(o -> {
////                    if ((unit.getSubUnitId() + "").equals(o.getQuantityUnit()) && unit.getSpuNo().equals(o.getSpuNo()))
////                    {
////                        o.setStocksNumber(o.getQuantity());
////                        subUnitInventoryList.add(o);
////                    }
////                }));
//                mainUnitInventoryList.addAll(inventoryList);
//                subUnitInventoryList.addAll(inventoryList);
//            }
//        }
//
//        // 获取盘库后到今天这段时间的入库单数量
//        String start = startDate.concat(" 00:00:00");
//        String end = settingDate.concat(" 23:59:59");
//        StoreWarehouseStockItem stockItem = new StoreWarehouseStockItem();
//        stockItem.setDeptId(deptId);
//        stockItem.setStartTime(start);
//        stockItem.setEndTime(end);
//        List<StoreWarehouseStockItem> warehouseStockItemList = storeWarehouseStockItemMapper.selectWareStockQuantity(stockItem);
//
//        if (CollectionUtils.isNotEmpty(warehouseStockItemList))
//        {
//            // 未设置单位的入库单商品，不计算换算关系
//            for (Iterator<StoreWarehouseStockItem> it = warehouseStockItemList.iterator(); it.hasNext();){
//                StoreWarehouseStockItem obj = it.next();
//                if(!spuUnitIds.contains(obj.getSpuNo())) {
//                    it.remove();
//                }
//            }
//            mainUnitInventoryList.addAll(warehouseStockItemList);
//            subUnitInventoryList.addAll(warehouseStockItemList);
//        }
//
//        if(CollectionUtils.isEmpty(mainUnitInventoryList)) {
//            return;
//        }
//        // 根据商品SPU计算主，副单位的数量
//        Map<String, Double> mainNumMap = mainUnitInventoryList.stream()
//            .collect(Collectors.groupingBy(StoreWarehouseStockItem::getSpuNo,
//                HashMap::new, Collectors.reducing(0d, StoreWarehouseStockItem::getQuantity,
//                    (aDouble1, aDouble2) ->
//                        new BigDecimal(aDouble1).add(new BigDecimal(aDouble2)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue())));
//
//        Map<String, Double> subNumMap = subUnitInventoryList.stream()
//            .collect(Collectors.groupingBy(StoreWarehouseStockItem::getSpuNo,
//                HashMap::new, Collectors.reducing(0d, StoreWarehouseStockItem::getStocksNumber,
//                    (aDouble1, aDouble2) ->
//                        new BigDecimal(aDouble1).add(new BigDecimal(aDouble2)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue())));
//
//
//        List<StoreGoodsSpuUnitConversion> unitConversionList = new ArrayList();
//
//        // 计算主副单位的转换关系
//        mainNumMap.forEach((k,v)->{
//            StoreGoodsSpuUnitConversion conversion = new StoreGoodsSpuUnitConversion();
//            conversion.setDeptId(deptId);
//            conversion.setSpuNo(k);
//            conversion.setSubCaseMain(1.0);
//            conversion.setMainCaseSub(1.0);
//            Double subNum = subNumMap.get(k);
//            if(subNum != null && subNum > 0 && v > 0) {
//                conversion.setMainCaseSub(new BigDecimal(subNum).divide(new BigDecimal(v),2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                conversion.setSubCaseMain(new BigDecimal(v).divide(new BigDecimal(subNum),2, BigDecimal.ROUND_HALF_UP).doubleValue());
//            }
//            unitConversionList.add(conversion);
//        });
//
//        List<String> spuNoList = new ArrayList<>();
//        unitConversionList.forEach(o->{
//            o.setDeptId(deptId);
//            spuNoList.add(o.getSpuNo());
//        });
//
//        if(CollectionUtils.isNotEmpty(spuNoList))
//        {
//            QueryWrapper<StoreGoodsSpuUnitConversion> delWrapper = new QueryWrapper<>();
//            delWrapper.in("spu_no", spuNoList);
//            delWrapper.eq("dept_id",deptId);
//            storeGoodsSpuUnitConversionMapper.delete(delWrapper);
//
//            storeGoodsSpuUnitConversionService.saveBatch(unitConversionList);
//        }
    }

    public int auditStock(StoreWarehouseStockDTO dto){

        Map<String, Object> map = new HashMap<>();
        map.put("stockIdList", Convert.toStrArray(dto.getIds()));
        map.put("auditStatus", dto.getAuditStatus());
        return storeWarehouseStockMapper.updateStockAuditStatusByIds(map);
    }
}
