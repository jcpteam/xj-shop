package com.javaboot.webservice.order;

import com.javaboot.common.json.JSON;
import com.javaboot.common.json.JSONObject;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreGoodsOrderItem;
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.dto.StoreGoodsOrderDTO;
import com.javaboot.shop.dto.StoreGoodsOrderQueryDTO;
import com.javaboot.shop.dto.StoreReqPage;
import com.javaboot.shop.mapper.*;
import com.javaboot.shop.vo.StoreGoodsOrderItemVO;
import com.javaboot.shop.vo.StoreGoodsOrderVO;
import com.javaboot.system.domain.SysDept;
import com.javaboot.system.mapper.SysDeptMapper;
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

@Component(value = "easOrderSyncService")
public class EASOrderSyncService
{

    private final Logger logger = LoggerFactory.getLogger(EASOrderSyncService.class);
    @Autowired
    private StoreGoodsOrderMapper storeGoodsOrderMapper;

    @Autowired
    private StoreGoodsOrderItemMapper storeGoodsOrderItemMapper;

    @Autowired
    private StoreMemberMapper storeMemberMapper;

    @Autowired
    private StoreGoodsSpuUnitConversionMapper storeGoodsSpuUnitConversionMapper;

    @Autowired
    private StoreHouseMapper storeHouseMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    public void syncOrder(String date) {

        String sessionId = "";
        try {
            EASLoginProxy proxy = new EASLoginProxyServiceLocator().getEASLogin();
            WSContext context = proxy.login(EASLoginConstant.USER_NAME, EASLoginConstant.PASSWORD, EASLoginConstant.SLN_NAME, EASLoginConstant.DC_NAME, EASLoginConstant.LANGUAGE, EASLoginConstant.DB_TYPE);
            sessionId = context.getSessionId();
        } catch (Exception e) {
        }

        StoreGoodsOrderQueryDTO dto = new StoreGoodsOrderQueryDTO();
        if(!"all".equals(date)) {
            String date0 = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date());
            String date1 = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(new Date(), 1));
            if(StringUtils.isNotEmpty(date)) {
                date0 = date;
                date1 = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(DateUtils.parseDate(date), 1));
            }
            dto.setCreateBeginDate(date0.concat(" 09:00:00"));
            dto.setCreateEndDate(date1.concat(" 09:00:00"));
        }
        dto.setStatusList(Arrays.asList(new String[]{"3","4"}));
        dto.setFinancialAuditStatus("1");
        dto.setSynStatus("0");
        int pageNo = 0;
        int pagePerNum = 1;
        StoreReqPage page = new StoreReqPage(pagePerNum, pageNo * pagePerNum);
        dto.setReqPage(page);


        while (true) {
            List<StoreGoodsOrderVO> list = storeGoodsOrderMapper.selectStoreGoodsOrderList(dto);
            sendData(list, sessionId);
            if(CollectionUtils.isEmpty(list) || list.size() < pagePerNum) {
                break;
            }
            List<Long> ids = list.stream().map(StoreGoodsOrderVO::getOrderId).collect(Collectors.toList());
            logger.error(StringUtils.join(ids,","));
            page.setPageNo(pageNo * pagePerNum);
        }




    }

    private void sendData(List<StoreGoodsOrderVO> list, String sessionId) {
        if(CollectionUtils.isEmpty(list)) {
            return;
        }
        try {
            // 获取对应的明细列表
            Set<Long> ids = new HashSet<>();
            list.forEach(o->ids.add(o.getOrderId()));
            StoreGoodsOrderItem itemDto = new StoreGoodsOrderItem();
            itemDto.setOrderIds(new ArrayList<>(ids));
            List<StoreGoodsOrderItemVO> itemList = storeGoodsOrderItemMapper.selectStoreGoodsOrderItemList(itemDto);
            Map<Long, List<StoreGoodsOrderItem>> itemMap = itemList.stream().collect(Collectors.groupingBy(StoreGoodsOrderItem::getOrderId));


            Map<String, Object> conversionMap = new HashMap<>();
            List<EASOrderReq> billList = new ArrayList<>();

            list.forEach(o->{
                EASOrderReq req = new EASOrderReq();
                EASOrderInfo bill = new EASOrderInfo();
                bill.setOrderNumber(o.getCode());
                bill.setBizType("210");
                // 客户id转换成客户编号
                StoreMember storeMember = storeMemberMapper.selectStoreMemberById(Long.parseLong(o.getMerchantId()));
                bill.setCustomer(storeMember.getCustomerNo());
                bill.setSaleOrg(o.getDeptId());
                // TODO 填充订单的创建人和订单对应的业务员
                SysDept sysDept = sysDeptMapper.selectDeptById(o.getDeptId());
                String creator = "024926";
                if(sysDept != null && StringUtils.isNotBlank(sysDept.getLeader())) {
                    creator = sysDept.getLeader();
                }
                bill.setSalePerson(creator);
                // 创建人暂时没有
                bill.setCreator(creator);
                bill.setPaymentType("002");

                String updateTime = "";
                String beginTime1 = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, o.getCreateTime())+" 09:00:00";
                String endTime1 = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, o.getCreateTime())+" 23:59:59";
                String beginTime2 = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, o.getCreateTime())+" 00:00:00";
                String endTime2 = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, o.getCreateTime())+" 08:59:59";
                if(o.getCreateTime().before(DateUtils.parseDate(endTime1))
                        &&o.getCreateTime().after(DateUtils.parseDate(beginTime1))){
                    updateTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(DateUtils.parseDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,o.getCreateTime())), 1));
                }else if(o.getCreateTime().before(DateUtils.parseDate(endTime2))
                        &&o.getCreateTime().after(DateUtils.parseDate(beginTime2))){
                    updateTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,o.getCreateTime());
                }

                System.out.println(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, o.getCreateTime()));
                System.out.println(updateTime);

    //            bill.setBizDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, o.getCreateTime()));
                bill.setBizDate(updateTime);
                bill.setRemark(StringUtils.isEmpty(o.getRemark()) ? "备注" : o.getRemark());

                List<EASOrderInfoItem> entry = new ArrayList<>();
                itemMap.get(o.getOrderId()).forEach(detail->{
                    EASOrderInfoItem item = new EASOrderInfoItem();
                    item.setMaterial(detail.getSpuNo());

                    //副单位换算成主单位的数量
                    //                conversionMap.clear();
                    //                conversionMap.put("deptId", o.getDeptId());
                    //                conversionMap.put("spuNo", detail.getSpuNo());
                    //                List<StoreGoodsSpuUnitConversion> conversionList = storeGoodsSpuUnitConversionMapper.selectSpuConversion(conversionMap);
                    //                if(CollectionUtils.isNotEmpty(conversionList) && detail.getUnitId().equals(conversionList.get(0).getSubUnitId())) {
                    //                    item.setQty(new BigDecimal(detail.getSortingWeight()).multiply(new BigDecimal(conversionList.get(0).getSubCaseMain())).setScale(2,BigDecimal.ROUND_HALF_UP));
                    //                } else {
                    //                    item.setQty(new BigDecimal(detail.getSortingWeight()));
                    //                }
                    if(detail.getSortingQuantity()==null){
                        logger.error(JSONObject.valueAsStr(detail));
                    }
                    item.setAssistQty(new BigDecimal(detail.getSortingQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    item.setQty(new BigDecimal(detail.getSortingWeight()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    item.setPrice(new BigDecimal(detail.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    item.setAmount(new BigDecimal(detail.getSortingPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    item.setSupplyMode(SupplyType.IN.getValue());
    //                item.setSendDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,o.getDeliveryDate()));
    //                item.setDeliveryDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, o.getDeliveryDate()));
                    item.setSendDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,o.getDeliveryDate()));
                    item.setDeliveryDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, o.getDeliveryDate()));
                    // item.setWarehouse(o.getWarehouseId() + "");

                    // 填充仓库
                    StoreHouse storeHouse = new StoreHouse();
                    storeHouse.setDeptId(o.getDeptId());
                    List<StoreHouse> storeHouseList = storeHouseMapper.selectStoreHouseList(storeHouse);
                    if(CollectionUtils.isNotEmpty(storeHouseList)) {
                        item.setWarehouse(storeHouseList.get(0).getStoreNo());
                    } else {
                        item.setWarehouse("0412");
                    }
                    entry.add(item);

                });
                req.setEntry(entry);
                req.setBill(bill);
                billList.add(req);


            });



            //登录
//            EASLoginProxy proxy = new EASLoginProxyServiceLocator().getEASLogin();
//            WSContext context = proxy.login(EASLoginConstant.USER_NAME, EASLoginConstant.PASSWORD, EASLoginConstant.SLN_NAME, EASLoginConstant.DC_NAME, EASLoginConstant.LANGUAGE, EASLoginConstant.DB_TYPE);
//            logger.error("====请求参数：{}====", JSON.marshal(billList));
            // 调用订单接口
            WSDSSaleOrderWSFacadeSrvProxy wsDSSaleOrderWSFacadeSrvProxy = new WSDSSaleOrderWSFacadeSrvProxyServiceLocator().getWSDSSaleOrderWSFacade();
            ((Stub) wsDSSaleOrderWSFacadeSrvProxy).setHeader("http://login.webservice.bos.kingdee.com", "SessionId", sessionId);
            logger.error("==订单请求json:===" + JSON.marshal(billList));
            String rsp = wsDSSaleOrderWSFacadeSrvProxy.crtSaleOrderBill(JSON.marshal(billList));
            //返回值而
            logger.error("***订单返回值:{}", rsp);
            EASOrderRsp rspObj = JSON.unmarshal(rsp, EASOrderRsp.class);

            //{"result":"sucess","msg":"","data":["XJO-20211004213719-313754","XJO-20211004235914-052056"]}

            if("failed".equals(rspObj.getResult())) {
                StoreGoodsOrderDTO updateDto = new StoreGoodsOrderDTO();
                updateDto.setOrderId(list.get(0).getOrderId());
                if(("订单号:"+list.get(0).getCode()+"重新更新失败!").equals(rspObj.getMsg())) {
                    updateOrderSynStatus(list.get(0).getOrderId(), "1");
                } else {
                    updateOrderSynStatus(list.get(0).getOrderId(), "2");
                }
            }
            // 更新状态
            if("success".equals(rspObj.getResult())) {
                list.forEach(o->{
                    logger.error("***修改返回值:{}>{}", rspObj.getData(),o.getCode());
                    if(!rspObj.getData().isEmpty()) {
                        for(String code:rspObj.getData()){
                            if(code.equals(o.getCode())) {
                                updateOrderSynStatus(o.getOrderId(), "1");
                            }
                        }
                    }
                });
            }

        } catch (Exception e) {
            updateOrderSynStatus(list.get(0).getOrderId(), "2");
            logger.error("===crtSaleOrderBill接口请求报错===",e);
        }
    }

    private void updateOrderSynStatus(Long orderId, String sysStatus) {
        StoreGoodsOrderDTO updateDto = new StoreGoodsOrderDTO();
        updateDto.setOrderId(orderId);
        updateDto.setSynStatus(sysStatus);
        int i = storeGoodsOrderMapper.updateStoreGoodsOrder(updateDto);
        logger.error("***修改返回值:{}", i);
    }
}
