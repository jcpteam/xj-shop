package com.javaboot.webservice.stock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.domain.StoreWarehouseStockItem;
import com.javaboot.shop.dto.StoreWarehouseStockDTO;
import com.javaboot.shop.mapper.StoreGoodsSalesUnitMapper;
import com.javaboot.shop.mapper.StoreHouseMapper;
import com.javaboot.shop.service.IStoreWarehouseStockService;
import com.javaboot.webservice.EASLoginConstant;
import com.javaboot.webservice.login.EASLoginProxy;
import com.javaboot.webservice.login.EASLoginProxyServiceLocator;
import com.javaboot.webservice.login.WSContext;
import org.apache.axis.client.Stub;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Component(value = "easStockSynService")
public class EASStockSynService {

    @Autowired
    private StoreHouseMapper storeHouseMapper;

    @Autowired
    private StoreGoodsSalesUnitMapper storeGoodsSalesUnitMapper;

    @Autowired
    private IStoreWarehouseStockService storeWarehouseStockService;

    private final Logger logger = LoggerFactory.getLogger(EASStockSynService.class);

    private final static int PAGE_SIZE = 200;

    public void synStock(String date) {
        if(StringUtils.isBlank(date)) {
            date = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date());
        }

        String sessionId = "";
        try {
            EASLoginProxy proxy = new EASLoginProxyServiceLocator().getEASLogin();
            WSContext context = proxy.login(EASLoginConstant.USER_NAME, EASLoginConstant.PASSWORD, EASLoginConstant.SLN_NAME, EASLoginConstant.DC_NAME, EASLoginConstant.LANGUAGE, EASLoginConstant.DB_TYPE);
            sessionId = context.getSessionId();
        } catch (Exception e) {
        }

        List<StoreGoodsSalesUnit> units = storeGoodsSalesUnitMapper.selectStoreGoodsSalesUnitList(new StoreGoodsSalesUnit());
        Map<String, StoreGoodsSalesUnit> spuUnitMap = units.stream().collect(Collectors.toMap(key->key.getName(), obj->obj));
        int i = 1;
        while(true) {

            try
            {
                List<EASStockReq>  items = getStockPage(i, PAGE_SIZE, date, spuUnitMap, sessionId);

                i++;

                if(CollectionUtils.isEmpty(items) || items.size() < PAGE_SIZE ) {
                    // 根据订单编号组装订单主-订单明细结构的数据
                    // addStockData();
                    break;
                }

            } catch (Exception e) {
                logger.error("获取入库单异常", e);
                break;
            }
        }

    }

    /**
     * 调用接口获取数据
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    private List<EASStockReq> getStockPage(int pageNo, int pageSize, String date,  Map<String, StoreGoodsSalesUnit> spuUnitMap, String sessionId) throws Exception {
        //登录
        // EASLoginProxy proxy = new EASLoginProxyServiceLocator().getEASLogin();
        // WSContext context = proxy.login(EASLoginConstant.USER_NAME, EASLoginConstant.PASSWORD, EASLoginConstant.SLN_NAME, EASLoginConstant.DC_NAME, EASLoginConstant.LANGUAGE, EASLoginConstant.DB_TYPE);

        WSDSPurInWarehsWSFacadeSrvProxy wsDSPurInWarehsWSFacadeSrvProxy = new WSDSPurInWarehsWSFacadeSrvProxyServiceLocator().getWSDSPurInWarehsWSFacade();
        ((Stub) wsDSPurInWarehsWSFacadeSrvProxy).setHeader("http://login.webservice.bos.kingdee.com", "SessionId", sessionId);
        String stockRsp = wsDSPurInWarehsWSFacadeSrvProxy.getPurInWarehsInfo(pageNo, pageSize, date);
        List<EASStockReq> stockItems = JSONArray.parseArray(stockRsp, EASStockReq.class);

//        if(CollectionUtils.isNotEmpty(stockItems)) {
//            // 入临时表
//            item2TempTable(stockItems);
//        }
        if(CollectionUtils.isNotEmpty(stockItems)) {
            // 入库单数据插入
            addStockReqData(stockItems, spuUnitMap);
        }

        return stockItems;
    }

//    /**
//     * @param items
//     */
//    @Transactional
//    private void item2TempTable(List<EASStockItem> items) {
//        Set<String> stocks = new HashSet<>();
//        items.forEach(o->stocks.add(o.getBillNumber()));
//        List<StoreTempStock> list = new ArrayList<>();
//        stocks.forEach(o->{
//            StoreTempStock tempStock = new StoreTempStock();
//            tempStock.setStockNo(o);
//            list.add(tempStock);
//        });
//        storeTempStockService.removeByIds(new ArrayList<>(stocks));
//        storeTempStockService.saveBatch(list);
//
//        List<StoreTempStockItem> itemList = new ArrayList<>();
//        Set<String> stockItemIds = new HashSet<>();
//        items.forEach(o->{
//            StoreTempStockItem item = new StoreTempStockItem();
//            item.setStockNo(o.getBillNumber());
//            item.setItemId(o.getBillNumber().concat("_").concat(o.getSerialNumber()));
//            stockItemIds.add(item.getItemId());
//            item.setItemJson(JSON.toJSONString(o));
//            itemList.add(item);
//        });
//        storeTempStockItemService.removeByIds(new ArrayList<>(stockItemIds));
//        storeTempStockItemService.saveBatch(itemList);
//    }

//    /**
//     * 根据订单编号组装订单主-订单明细结构的数据
//     */
//    @Transactional
//    private void addStockData() {
//
//        // 从临时表获取所有入库单id
//        List<StoreTempStock> storeTempStocks = storeTempStockService.list();
//        for (StoreTempStock o : storeTempStocks)
//        {
//            QueryWrapper<StoreTempStockItem> queryWrapper = new QueryWrapper();
//            queryWrapper.eq("stock_no", o.getStockNo());
//            List<StoreTempStockItem> dbList = storeTempStockItemService.list(queryWrapper);
//            EASStockItem main = JSON.parseObject(dbList.get(0).getItemJson(), EASStockItem.class);
//            if(StringUtils.isBlank(main.getAdminNumber()))  {
//                logger.error("部门为空，错误数据：{}", dbList.get(0).getItemJson() );
//            }
//            StoreWarehouseStockDTO warehouseStock = new StoreWarehouseStockDTO();
//            warehouseStock.setDeptId(main.getAdminNumber());
//            warehouseStock.setStockNo(main.getBillNumber());
//            warehouseStock.setAuditStatus("1");
//            warehouseStock.setStatus(Constants.NORMAL);
//            warehouseStock.setSupplierId(main.getSupplierNumber());
//            warehouseStock.setCreateTime(DateUtils.parseDate(main.getBizDate()));
//            warehouseStock.setCategory("3");
//            warehouseStock.setItemList(new ArrayList<>());
//
//            //根据仓库编号获取仓库id
//            StoreHouse storeHouse = new StoreHouse();
//            storeHouse.setStoreNo(main.getWarehouseNumber());
//            List<StoreHouse> storeHouseList = storeHouseMapper.selectStoreHouseList(storeHouse);
//            if (CollectionUtils.isEmpty(storeHouseList))
//            {
//                continue;
//            }
//            warehouseStock.setWarehouseId(storeHouseList.get(0).getStoreId());
//            warehouseStock.setStockType(storeHouseList.get(0).getStoreType());
//
//            dbList.forEach(tempStockItem -> {
//                EASStockItem obj = JSON.parseObject(tempStockItem.getItemJson(), EASStockItem.class);
//                StoreWarehouseStockItem stockItem = new StoreWarehouseStockItem();
//                stockItem.setDeptId(obj.getAdminNumber());
//                stockItem.setQuantity(obj.getBaseQty());
//                stockItem.setAmount(obj.getTaxPrice());
//                stockItem.setStocksNumber(obj.getAssistQty());
//                stockItem.setSpuNo(obj.getMaterialNumber());
//                stockItem.setQuantityUnit(obj.getBaseUnitName());
//                stockItem.setWarehouseId(warehouseStock.getWarehouseId());
//                stockItem.setStatus(Constants.NORMAL);
//                if(StringUtils.isBlank(obj.getAssistUnitName())){
//                    stockItem.setWeightUnit(obj.getBaseUnitName());
//                } else {
//                    stockItem.setWeightUnit(obj.getAssistUnitName());
//                }
//                stockItem.setStockId(warehouseStock.getStockId());
//                warehouseStock.getItemList().add(stockItem);
//            });
//            int rslt = storeWarehouseStockService.insertEASStock(warehouseStock);
//            if (rslt == 0)
//            {
//                logger.error("订单{}已经存在了", warehouseStock.getStockNo());
//            }
//
//            List<String> itemIds = dbList.stream().map(StoreTempStockItem::getItemId).collect(Collectors.toList());
//            storeTempStockService.removeById(o.getStockNo());
//            storeTempStockService.removeByIds(itemIds);
//        }
//    }

    private void addStockReqData(List<EASStockReq> reqList,  Map<String, StoreGoodsSalesUnit> spuUnitMap) {
        for(EASStockReq req : reqList) {

            if(StringUtils.isBlank(req.getBill().getAdminNumber()))  {
                logger.error("部门为空，错误数据：{}", JSON.toJSONString(req) );
                continue;
            }
            if(CollectionUtils.isEmpty(req.getEntry()))  {
                logger.error("商品明细数据为空：{}", JSON.toJSONString(req) );
                continue;
            }
            StoreWarehouseStockDTO warehouseStock = new StoreWarehouseStockDTO();
            warehouseStock.setDeptId(req.getBill().getAdminNumber());
            warehouseStock.setStockNo(req.getBill().getBillNumber());
            warehouseStock.setAuditStatus("1");
            warehouseStock.setStatus(Constants.NORMAL);
            warehouseStock.setSupplierId(StringUtils.isNotBlank(req.getBill().getSupplierNumber()) ? req.getBill().getSupplierNumber() : "1");
            warehouseStock.setCreateTime(DateUtils.parseDate(req.getBill().getInWarehsDate()));
            warehouseStock.setCategory("3");
            warehouseStock.setItemList(new ArrayList<>());

            //根据仓库编号获取仓库id
            StoreHouse storeHouse = new StoreHouse();
            storeHouse.setStoreNo(req.getEntry().get(0).getWarehouseNumber());
            List<StoreHouse> storeHouseList = storeHouseMapper.selectStoreHouseList(storeHouse);
            if (CollectionUtils.isEmpty(storeHouseList))
            {
                continue;
            }
            warehouseStock.setWarehouseId(storeHouseList.get(0).getStoreId());
            warehouseStock.setStockType(storeHouseList.get(0).getStoreType());

            req.getEntry().forEach(tempStockItem -> {
                StoreWarehouseStockItem stockItem = new StoreWarehouseStockItem();
                stockItem.setDeptId(tempStockItem.getAdminNumber());
                stockItem.setQuantity(tempStockItem.getBaseQty());
                stockItem.setAmount(tempStockItem.getTaxPrice());
                stockItem.setStocksNumber(tempStockItem.getAssistQty());
                stockItem.setSpuNo(tempStockItem.getMaterialNumber());
                stockItem.setQuantityUnit(tempStockItem.getBaseUnitName());
                StoreGoodsSalesUnit mainUnit = spuUnitMap.get(tempStockItem.getBaseUnitName());
                if(mainUnit != null) {
                    stockItem.setQuantityUnit(mainUnit.getUnitId() + "");
                }
                stockItem.setWarehouseId(warehouseStock.getWarehouseId());
                stockItem.setStatus(Constants.NORMAL);
                if(StringUtils.isBlank(tempStockItem.getAssistUnitName())){
                    stockItem.setWeightUnit(tempStockItem.getBaseUnitName());
                } else {
                    stockItem.setWeightUnit(tempStockItem.getAssistUnitName());
                }
                StoreGoodsSalesUnit subUnit = spuUnitMap.get(stockItem.getWeightUnit());
                if(subUnit != null) {
                    stockItem.setWeightUnit(subUnit.getUnitId() + "");
                }
                stockItem.setStockId(warehouseStock.getStockId());
                warehouseStock.getItemList().add(stockItem);
            });
            int rslt = storeWarehouseStockService.insertEASStock(warehouseStock);
            if (rslt == 0)
            {
                logger.error("入库单{}已经存在了", warehouseStock.getStockNo());
            }
        }
    }
}
