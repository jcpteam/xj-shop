package com.javaboot.webservice;

import com.alibaba.fastjson.JSONArray;
import com.javaboot.common.json.JSON;
import com.javaboot.common.json.JSONObject;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.webservice.login.EASLoginProxy;
import com.javaboot.webservice.login.EASLoginProxyServiceLocator;
import com.javaboot.webservice.login.WSContext;
import com.javaboot.webservice.order.*;
import com.javaboot.webservice.adjust.*;
import com.javaboot.webservice.stock.EASStockItem;
import com.javaboot.webservice.stock.WSDSPurInWarehsWSFacadeSrvProxy;
import com.javaboot.webservice.stock.WSDSPurInWarehsWSFacadeSrvProxyServiceLocator;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebServiceTest
{
    public static void main(String[] args)
    {
        test();
    }

    public static String test(){
        String wsdlUrl ="http://erp.xiangjiamuye.com:6888/ormrpc/services/WSDSSaleIssueWsFacade?wsdl";
        //看具体响应的WSDL中的namespace
        String nameSpaceUri ="urn:client.dssaleissuewsfacade";
        Service service = new Service();
        Call call;
        String returnvalue = "";
        try {



            EASLoginProxy proxy = null;
            WSContext context = null;
            //登录
            proxy = new EASLoginProxyServiceLocator().getEASLogin();
            context = proxy.login("025281", "123456", "eas", "xjmy", "L2", 1);
            //登录成功返回 SessionId
            System.out.println("***getSessionId:"+context.getSessionId());
            //            if(StringUtils.isBlank(context.getSessionId())){
            //                throw new Exception("login fail");
            //            }


//            List<EASOrderReq> billList = new ArrayList<>();
//
//            EASOrderReq req = new EASOrderReq();
//
//            EASOrderInfo bill = new EASOrderInfo();
//            bill.setOrderNumber("XJOR_202107181111");
//            bill.setBizType("210");
//            bill.setCustomer("43090047");
//            bill.setSaleOrg("040803");
//            bill.setSalePerson("025281");
//            bill.setCreator("025281");
//            bill.setPaymentType("002");
//            bill.setRemark("这是一个备注");
//
//            List<EASOrderInfoItem> entry = new ArrayList<>();
//            EASOrderInfoItem item = new EASOrderInfoItem();
//            item.setMaterial("050401010001");
//            item.setQty(new BigDecimal(534.33));
//            item.setPrice(new BigDecimal(10.98));
//            item.setAmount(new BigDecimal(34.45));
//            item.setSupplyMode(SupplyType.IN.getValue());
//            item.setSendDate(new Date());
//            item.setDeliveryDate(new Date());
//            item.setWarehouse("0414");
//            entry.add(item);
//
//            req.setBill(bill);
//            req.setEntry(entry);
//            billList.add(req);
//
//            System.out.println("====req====="+JSON.marshal(billList));
//
//            WSDSSaleOrderWSFacadeSrvProxy wsDSSaleOrderWSFacadeSrvProxy = null;
//            String rsp = null;
//
//            // 调用订单接口
//            wsDSSaleOrderWSFacadeSrvProxy = new WSDSSaleOrderWSFacadeSrvProxyServiceLocator().getWSDSSaleOrderWSFacade();
//            ((Stub) wsDSSaleOrderWSFacadeSrvProxy).setHeader("http://login.webservice.bos.kingdee.com", "SessionId", context.getSessionId());
//            rsp = wsDSSaleOrderWSFacadeSrvProxy.crtSaleOrderBill(JSON.marshal(billList));
//            //返回值
//            System.out.println("***订单返回值:"+rsp);
//
//
//
//
//            List<EASAdjustReq> adjustReqList = new ArrayList<>();
//
//            EASAdjustReq adjustReq = new EASAdjustReq();
//
//            EASAdjustInfo adjustInfo = new EASAdjustInfo ();
//            adjustInfo.setBizType("320");
//            adjustInfo.setCreator("025281");
//            adjustInfo.setInStorageOrg("040802");
//            adjustInfo.setOutStorageOrg("040803");
//            adjustInfo.setRemark("");
//            adjustReq.setBill(adjustInfo);
//
//            List<EASAdjustItem> adjustItems = new ArrayList<>();
//            EASAdjustItem adjustItem = new EASAdjustItem();
//            adjustItem.setMaterial("050401010001");
//            adjustItem.setQty(new BigDecimal(534.33));
//            adjustItem.setPrice(new BigDecimal(10.98));
//            adjustItem.setAmount(new BigDecimal(34.45));
//            adjustItem.setInPlanDate(new Date());
//            adjustItem.setOutPlanDate(new Date());
//            adjustItem.setInWarehouse("0409");
//            adjustItem.setOutWarehouse("0408");
//            adjustItems.add(adjustItem);
//            adjustReq.setEntry(adjustItems);
//
//            adjustReqList.add(adjustReq);
//
//            // 调用调库接口
//            WSDSStockMoveWSFacadeSrvProxy wsDSStockMoveWSFacadeSrvProxy = null;
//            String rsp2 = null;
//            wsDSStockMoveWSFacadeSrvProxy = new WSDSStockMoveWSFacadeSrvProxyServiceLocator().getWSDSStockMoveWSFacade();
//            ((Stub) wsDSStockMoveWSFacadeSrvProxy).setHeader("http://login.webservice.bos.kingdee.com", "SessionId", context.getSessionId());
//            rsp2 = wsDSStockMoveWSFacadeSrvProxy.crtStockTransferBill(JSON.marshal(adjustReqList));
//            //返回值
//            System.out.println("===调拨返回值===:"+rsp2);
//
//
            // 调用订单接口
            WSDSPurInWarehsWSFacadeSrvProxy wsDSPurInWarehsWSFacadeSrvProxy = new WSDSPurInWarehsWSFacadeSrvProxyServiceLocator().getWSDSPurInWarehsWSFacade();
            ((Stub) wsDSPurInWarehsWSFacadeSrvProxy).setHeader("http://login.webservice.bos.kingdee.com", "SessionId", context.getSessionId());
            String stockRsp = wsDSPurInWarehsWSFacadeSrvProxy.getPurInWarehsInfo(1,200,
                "2021-09-16");
            List<EASStockItem> stockItems = JSONArray.parseArray(stockRsp, EASStockItem.class);
            EASStockItem stockItem = stockItems.get(0);
            //返回值
            System.out.println("***入库单返回值:"+stockRsp);



            //
            //            call.setTargetEndpointAddress("http://erp.xiangjiamuye.com:6888/ormrpc/services/EASLogin?wsdl");
            //            call.setOperationName(new QName("urn:client","login"));
            //            call.addParameter("userName", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
            //            call.addParameter("password", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
            //            call.addParameter("slnName", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
            //            call.addParameter("dcName", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
            //            call.addParameter("language", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
            //            call.addParameter("dbType", Constants.XSD_INT, ParameterMode.IN);
            //
            //            call.setReturnType(new QName("urn:client", "WSContext"));
            //            call.setReturnClass(WSContext.class);
            ////            call.setReturnQName(new QName("","loginReturn"));
            //            //超时
            //            call.setTimeout(Integer.valueOf(1000*600000*60));
            //            call.setMaintainSession(true);
            //            //登陆接口参数 userName password slnName dcName language dbType
            //            WSContext rs = (WSContext)call.invoke(new Object[]{"am","","eas","bos80demo","l2",Integer.valueOf(0)});
            //            if(StringUtils.isBlank(rs.getSessionId())){
            //                throw new Exception("login fail");
            //            }
            //            String sessionId = rs.getSessionId();
            //            System.out.println(sessionId);
            //
            //
            //            //设置operation 名称
            //            call.setTargetEndpointAddress(wsdlUrl);
            //            call.setOperationName(new QName(nameSpaceUri, "crtSaleIssueBill"));
            //            call.addParameter("data", org.apache.axis.Constants.XSD_STRING, ParameterMode.IN);
            //
            //            SOAPHeaderElement header = new SOAPHeaderElement("http://erp.xiangjiamuye.com:6888","SessionId", sessionId);
            //            call.addHeader(header);
            //
            //
            //            //设置返回类型为对象数组
            //            call.setReturnClass(String.class);
            //
            //            returnvalue = (String) call.invoke(new Object[]{"M107530000"});
            //
            //
            //            System.out.println(returnvalue);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return returnvalue;

    }
}
