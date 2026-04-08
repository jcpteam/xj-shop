package com.javaboot.webservice.stock2eas;

import com.javaboot.common.constant.Constants;
import com.javaboot.common.enums.StockCategory;
import com.javaboot.common.json.JSON;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.domain.StoreWarehouseStock;
import com.javaboot.shop.domain.StoreWarehouseStockItem;
import com.javaboot.shop.dto.StoreGoodsOrderDTO;
import com.javaboot.shop.dto.StoreReqPage;
import com.javaboot.shop.dto.StoreWarehouseStockQueryDTO;
import com.javaboot.shop.mapper.StoreWarehouseStockItemMapper;
import com.javaboot.shop.mapper.StoreWarehouseStockMapper;
import com.javaboot.shop.service.IStoreHouseService;
import com.javaboot.shop.vo.StoreWarehouseStockVO;
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

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Component(value = "stock2EASSynService")
public class Stock2EASSynService
{

    @Autowired
    private IStoreHouseService storeHouseService;

    @Autowired
    private StoreWarehouseStockMapper storeWarehouseStockMapper;

    @Autowired
    private StoreWarehouseStockItemMapper storeWarehouseStockItemMapper;

    private final Logger logger = LoggerFactory.getLogger(Stock2EASSynService.class);

    private final static int PAGE_SIZE = 200;

    public void synStock2EAS(String date) {
        if(StringUtils.isBlank(date)) {
            date = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date());
        }

        String sessionId = "";
        try {
            EASLoginProxy proxy = new EASLoginProxyServiceLocator().getEASLogin();
            WSContext context = proxy.login(EASLoginConstant.USER_NAME, EASLoginConstant.PASSWORD, EASLoginConstant.SLN_NAME, EASLoginConstant.DC_NAME, EASLoginConstant.LANGUAGE, EASLoginConstant.DB_TYPE);
            //WSContext context = proxy.login("025281", "123456", "eas", "xjmy", "L2", 0);
            sessionId = context.getSessionId();
        } catch (Exception e) {
            logger.error("eas登录出错：", e);
        }

        StoreWarehouseStockQueryDTO dto = new StoreWarehouseStockQueryDTO();
        if(!"all".equals(date)) {
            String date0 = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date());
            if(StringUtils.isNotEmpty(date)) {
                date0 = date;
            }
            dto.setBeginDate(date0.concat(" 00:00:00"));
            dto.setEndDate(date0.concat(" 23:59:59"));
        }
        dto.setStatus("1");
        dto.setSynStatus("0");
        int pageNo = 0;
        int pagePerNum = 1;
        StoreReqPage page = new StoreReqPage(pagePerNum, pageNo * pagePerNum);
        dto.setReqPage(page);

        while (true) {
            List<StoreWarehouseStockVO> list = storeWarehouseStockMapper.selectStoreWarehouseStockList(dto);
            sendData(list, sessionId);
            if(CollectionUtils.isEmpty(list) || list.size() < pagePerNum) {
                break;
            }
            List<Long> ids = list.stream().map(StoreWarehouseStockVO::getStockId).collect(Collectors.toList());
            logger.error(StringUtils.join(ids,","));
            page.setPageNo(pageNo * pagePerNum);
        }

    }

    private void sendData(List<StoreWarehouseStockVO> list, String sessionId) {
        if(CollectionUtils.isEmpty(list)) {
            return;
        }
        try {
            // 获取对应的明细列表
            Set<Long> ids = new HashSet<>();
            list.forEach(o->ids.add(o.getStockId()));
            StoreWarehouseStockItem itemDto = new StoreWarehouseStockItem();
            itemDto.setStockIds(new ArrayList<>(ids));
            itemDto.setStatus(Constants.NORMAL);
            List<StoreWarehouseStockItem> itemList = storeWarehouseStockItemMapper.selectStoreWarehouseStockItemList(itemDto);
            Map<Long, List<StoreWarehouseStockItem>> itemMap = itemList.stream().collect(Collectors.groupingBy(StoreWarehouseStockItem::getStockId));

            // 仓库列表
            List<StoreHouse> houseList = storeHouseService.selectStoreHouseListByIds(list.stream().map(StoreWarehouseStockVO::getWarehouseId).collect(Collectors.toList()));
            Map<Long, StoreHouse> houseMap = houseList.stream().collect(Collectors.toMap(key->key.getStoreId(), obj->obj));


            List<Stock2EASReq> billList = new ArrayList<>();

            String creator = "027870";

            list.forEach(o->{

                Stock2EASReq req = new Stock2EASReq();
                Stock2EASInfo bill = new Stock2EASInfo();
                bill.setBillNumber(o.getStockNo());
                bill.setSupplier(o.getSupplierId());
                bill.setAdjustAmount(o.getAdjustAmount());
                if(StockCategory.PURCHASE_ORDER.getCode().equals(o.getCategory())) {
                    bill.setBizType("purchaseIn");
                } else if(StockCategory.ADJUST_IN_ORDER.getCode().equals(o.getCategory())) {
                    bill.setBizType("moveIn");
                } else if(StockCategory.ADJUST_OUT_ORDER.getCode().equals(o.getCategory())) {
                    bill.setBizType("moveOut");
                }

                bill.setBizDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, o.getPurchaseDate()));
                bill.setInWarehsDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, o.getCreateTime()));
                bill.setStorage(o.getDeptId());
                bill.setCreator(creator);
                bill.setRemark(o.getComment());

                List<Stock2EASItem> entry = new ArrayList<>();
                itemMap.get(o.getStockId()).forEach(detail->{
                    Stock2EASItem item = new Stock2EASItem();
                    item.setMaterial(detail.getSpuNo());
                    item.setAssistQty(new BigDecimal(detail.getStocksNumber()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    item.setQty(new BigDecimal(detail.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    item.setPrice(detail.getAmount());
                    if(detail.getTaxRate() != null) {
                        item.setTaxRate(new BigDecimal(detail.getTaxRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        //无税单价
                        item.setPrice(new BigDecimal(detail.getAmount() / (1 + detail.getTaxRate() * 0.01)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                    //税额
                    item.setTax(detail.getTax());
                    //含税单价
                    item.setTaxPrice(detail.getAmount());
                    //含税金额
                    item.setTaxAmount(detail.getTotalPrice());
                    //无税金额
                    item.setAmount(detail.getNoTaxPrice());
                    if(detail.getNoTaxPrice() == null) {
                        item.setAmount(new BigDecimal(detail.getAmount() * detail.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                    item.setWarehouse(houseMap.get(o.getWarehouseId()).getStoreNo());
                    item.setSeq(detail.getItemId()+"");

                    item.setAdjustAmount(detail.getAdjustAmount());
                    if(detail.getAdjustAmount() != null) {
                        item.setAdjustPrice(new BigDecimal(detail.getAdjustAmount() / detail.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                    item.setRemark(detail.getComment());

                    entry.add(item);

                });
                req.setEntry(entry);
                req.setBill(bill);
                billList.add(req);


            });



            // 调用入库接口
            WSDSPurchaseInWSFacadeSrvProxy wsDSSaleOrderWSFacadeSrvProxy = new WSDSPurchaseInWSFacadeSrvProxyServiceLocator().getWSDSPurchaseInWSFacade();
            ((Stub) wsDSSaleOrderWSFacadeSrvProxy).setHeader("http://login.webservice.bos.kingdee.com", "SessionId", sessionId);
            logger.error("==入库单请求json:===" + com.javaboot.common.json.JSON.marshal(billList));
            String rsp = wsDSSaleOrderWSFacadeSrvProxy.crtPurchaseInBill(com.javaboot.common.json.JSON.marshal(billList));
            //返回值而
            logger.error("***订单返回值:{}", rsp);
            Stock2EASRsp rspObj = JSON.unmarshal(rsp, Stock2EASRsp.class);

            //{"result":"sucess","msg":"","data":["XJO-20211004213719-313754","XJO-20211004235914-052056"]}

            if("failed".equals(rspObj.getResult())) {
                StoreGoodsOrderDTO updateDto = new StoreGoodsOrderDTO();
                updateDto.setOrderId(list.get(0).getStockId());
                if(("采购单号:"+list.get(0).getStockNo()+"重新更新失败!").equals(rspObj.getMsg())) {
                    updateOrderSynStatus(list.get(0).getStockId(), "1");
                } else {
                    updateOrderSynStatus(list.get(0).getStockId(), "2");
                }
            }
            // 更新状态
            if("success".equals(rspObj.getResult())) {
                updateOrderSynStatus(list.get(0).getStockId(), "1");
//                list.forEach(o->{
//                    logger.error("***修改返回值:{}>{}", rspObj.getData(),o.getStockNo());
//                    if(!rspObj.getData().isEmpty()) {
//                        for(String code:rspObj.getData()){
//                            if(code.equals(o.getStockNo())) {
//                                updateOrderSynStatus(o.getStockId(), "1");
//                            }
//                        }
//                    }
//                });
            }

        } catch (Exception e) {
            updateOrderSynStatus(list.get(0).getStockId(), "2");
            logger.error("===crtPurchaseInBill接口请求报错===",e);
        }
    }

    private void updateOrderSynStatus(Long stockId, String sysStatus) {
        StoreWarehouseStock updateDto = new StoreWarehouseStock();
        updateDto.setStockId(stockId);
        updateDto.setSynStatus(sysStatus);
        int i = storeWarehouseStockMapper.updateStoreWarehouseStock(updateDto);
        logger.error("***修改返回值:{}", i);
    }

}
