package com.javaboot.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.OrderPayStatus;
import com.javaboot.common.enums.ReceiptPayType;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StorePaymentFlow;
import com.javaboot.shop.domain.StoreReceipt;
import com.javaboot.shop.dto.StorePaymentAccountQueryDTO;
import com.javaboot.shop.mapper.StorePaymentAccountMapper;
import com.javaboot.shop.mapper.StorePaymentFlowMapper;
import com.javaboot.shop.mapper.StoreReceiptMapper;
import com.javaboot.shop.service.IStoreBlackWhiteService;
import com.javaboot.shop.service.IStorePaymentAccountService;
import com.javaboot.shop.service.IStorePaymentFlowService;
import com.javaboot.shop.service.IStoreReceiptService;
import com.javaboot.shop.sm2.*;
import com.javaboot.shop.vo.StorePaymentAccountVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 聚合支付流水Service业务层处理
 *
 * @author javaboot
 * @date 2021-09-13
 */
@Service
public class StorePaymentFlowServiceImpl implements IStorePaymentFlowService {

    private static final Logger logger = LoggerFactory.getLogger(StorePaymentFlowServiceImpl.class);

    @Autowired
    private StorePaymentFlowMapper storePaymentFlowMapper;

    @Autowired
    private StoreReceiptMapper storeReceiptMapper;

    @Autowired
    private IStoreReceiptService storeReceiptService;

    @Autowired
    private IStorePaymentAccountService storePaymentAccountService;

    @Autowired
    private StorePaymentAccountMapper storePaymentAccountMapper;

    @Autowired
    private IStoreBlackWhiteService storeBlackWhiteService;

    /**
     * 查询聚合支付流水
     *
     * @param id 聚合支付流水ID
     * @return 聚合支付流水
     */
    @Override
    public StorePaymentFlow selectStorePaymentFlowById(Long id) {
        return storePaymentFlowMapper.selectStorePaymentFlowById(id);
    }

    /**
     * 查询聚合支付流水列表
     *
     * @param storePaymentFlow 聚合支付流水
     * @return 聚合支付流水
     */
    @Override
    public List<StorePaymentFlow> selectStorePaymentFlowList(StorePaymentFlow storePaymentFlow) {
        return storePaymentFlowMapper.selectStorePaymentFlowList(storePaymentFlow);
    }

    /**
     * 新增聚合支付流水
     *
     * @param orderId 聚合支付流水
     * @return 结果
     */
    @Override
    public StorePaymentFlow insertStorePaymentFlow(Long orderId) {

        StorePaymentFlow storePaymentFlow = new StorePaymentFlow();
//        storePaymentFlow.setOrderId(orderId);
//        StoreGoodsOrderVO storeGoodsOrderVO = storeGoodsOrderMapper.selectStoreGoodsOrderById(orderId);
//        if(!OrderStatus.SENDED.getCode().equals(storeGoodsOrderVO.getStatus())) {
//            throw new BusinessException("订单【" + storePaymentFlow.getOrderId() + "】必须为已分拣的才允许支付！");
//        }
//
//        if(OrderPayStatus.PAYED.getCode().equals(storeGoodsOrderVO.getPayStatus())) {
//            X
//        }
//
//        //  根据订单id生成付款单,先判断是否生成过付款单
//        StoreReceiptQueryDTO storeReceiptQueryDTO = new StoreReceiptQueryDTO();
//        storeReceiptQueryDTO.setOrderIds(storePaymentFlow.getOrderId() + "");
//        storeReceiptQueryDTO.setStatus(Constants.NORMAL);
//        List<StoreReceiptVO> receiptVOS = storeReceiptMapper.selectStoreReceiptList(storeReceiptQueryDTO);
//        if(CollectionUtils.isEmpty(receiptVOS)) {
//            throw new BusinessException("订单【" + storePaymentFlow.getOrderId() + "】未生成付款单！");
//            //storeReceiptService.batchSaveStoreReceipt(storePaymentFlow.getOrderId() + "");
//        }
//
//        //判断账户余额是否足够
//        StoreReceipt nowStoreReceipt = receiptVOS.get(0);
//        StorePaymentAccountQueryDTO queryAccount = new StorePaymentAccountQueryDTO();
//        queryAccount.setMemberId(nowStoreReceipt.getMerchantId());
//        List<StorePaymentAccountVO> accList = storePaymentAccountService.selectStorePaymentAccountList(queryAccount);
//        // 直接扣除现金账户
//        if(CollectionUtils.isNotEmpty(accList) && accList.get(0).getCash() >= nowStoreReceipt.getAmount()) {
//            nowStoreReceipt.setPayStatus(OrderPayStatus.PAYED.getCode());
//            storeReceiptService.updateStoreReceipt(nowStoreReceipt);
//            storePaymentFlow.setReturnCode("SUCCESS");
//            storePaymentFlow.setTradeState("S");
//        } else {
//            // 根据生成的付款单生成支付流水
//            storePaymentFlow.setStatus(Constants.NORMAL);
//            List<StorePaymentFlow> list = storePaymentFlowMapper.selectStorePaymentFlowList(storePaymentFlow);
//            if(CollectionUtils.isEmpty(list))
//            {
//                storePaymentFlow.setCreateTime(DateUtils.getNowDate());
//                storePaymentFlow.setOrderNo(storeGoodsOrderVO.getCode());
//                storePaymentFlow.setPayOrderNo(UUID.randomUUID().toString().replaceAll("-", ""));
//                storePaymentFlowMapper.insertStorePaymentFlow(storePaymentFlow);
//
//                // 生成二维码
//                Map<String, String> rslt = genOrCode(storePaymentFlow.getPayOrderNo(),
//                        new BigDecimal(nowStoreReceipt.getAmount() * 100).intValue());
//                storePaymentFlow.setReturnCode(rslt.get("returnCode"));
//                storePaymentFlow.setRespCode(rslt.get("respCode"));
//                JSONObject jsonObject = (JSONObject)JSON.parse(rslt.get("biz_content"));
//                storePaymentFlow.setQrCode(jsonObject.getString("qrCode"));
//                storePaymentFlow.setRspTxt(JSONObject.toJSONString(rslt));
//                storePaymentFlowMapper.updateStorePaymentFlow(storePaymentFlow);
//            } else {
//                storePaymentFlow = list.get(0);
//            }
//        }
//        storePaymentFlow.setPayMoney(nowStoreReceipt.getAmount());

        return storePaymentFlow;
    }

    /**
     * 修改聚合支付流水
     *
     * @param storePaymentFlow 聚合支付流水
     * @return 结果
     */
    @Override
    public int updateStorePaymentFlow(StorePaymentFlow storePaymentFlow) {
        return storePaymentFlowMapper.updateStorePaymentFlow(storePaymentFlow);
    }

    /**
     * 删除聚合支付流水对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStorePaymentFlowByIds(String ids) {
        return storePaymentFlowMapper.deleteStorePaymentFlowByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除聚合支付流水信息
     *
     * @param id 聚合支付流水ID
     * @return 结果
     */
    @Override
    public int deleteStorePaymentFlowById(Long id) {
        return storePaymentFlowMapper.deleteStorePaymentFlowById(id);
    }

    /**
     * `trade_state` varchar(32) DEFAULT NULL COMMENT 'C - 订单已关闭 D - 交易已撤销 P - 交易在进行 F - 交易失败 S - 交易成功 R - 转入退款',
     * @param flow
     * @return
     */
    @Override
    public void payQuery(StorePaymentFlow flow) {
        String payOrderId = flow.getPayOrderNo();

        long nd =  60L * 60L * 1000L;
        // 获得两个时间的毫秒时间差异
        long diff = new Date().getTime() - flow.getCreateTime().getTime();
        // 计算差多少小时
        long hours = diff / nd;

        if(hours >= 2) {
            updateStatus(payOrderId, Constants.DELETE, "{\"returnCode\": \"FAIL\",\"errCode\":\"ORDERID_INVALID\"}");
            return;
        }

        payQuery(payOrderId);
    }

    /**
     * `trade_state` varchar(32) DEFAULT NULL COMMENT 'C - 订单已关闭 D - 交易已撤销 P - 交易在进行 F - 交易失败 S - 交易成功 R - 转入退款',
     * @param payOrderId
     * @return
     */
    @Override
    public Map<String, String> payQuery(String payOrderId) {

        Map<String, String> response = new HashMap<>();
        String signResult= signQryMethod(payOrderId);
        try {
            ObjectMapper mapper = new ObjectMapper();

            response = bankReq(signResult, Constant.queryUrl);

            // 返回结果验签
            Boolean checkResult1 = checkSign(mapper.writeValueAsString(response));
            if(!checkResult1){
                return null;
            }

            String returnCode = response.get("returnCode");
            String respCode = response.get("respCode");
            JSONObject jsonObject = (JSONObject)JSON.parse(response.get("biz_content"));
            String tradeState = jsonObject.getString("tradeState");
            String errCode = jsonObject.getString("errCode");
            if("SUCCESS".equals(returnCode) && "SUCCESS".equals(respCode) ) {

                // 更新数据库，付款单已付款， 订单已完成且已支付， 支付流水状态已完成，记录账号流水日志
                if("S".equals(tradeState)) {
                    ((IStorePaymentFlowService)AopContext.currentProxy()).updateOrderPay(payOrderId, JSON.toJSONString(response));
                } else if("C".equals(tradeState) || "D".equals(tradeState)
                        || "F".equals(tradeState) || "R".equals(tradeState)){ // 更新支付流水状态，不再进行招行查询接口调用
                    updateStatus(payOrderId, Constants.DELETE, JSON.toJSONString(response));
                }
            }

            // 更新支付流水状态，不再进行招行查询接口调用
            if("SUCCESS".equals(returnCode) && "FAIL".equals(respCode) && "ORDERID_INVALID".equals(errCode) ) {
                updateStatus(payOrderId, Constants.DELETE, JSON.toJSONString(response));
            }

            logger.info("返回结果：" + returnCode);
        } catch (Exception e) {
            logger.info("支付查询结果异常" , e);
            return response;
        }

        return  response;
    }


    private Map<String, String> genOrCode(String payOrderId, StoreReceipt nowStoreReceipt) {

        Map<String, String> response = new HashMap<>();

        String signResult = "";
        String reqUrl = Constant.url;
        if(StringUtils.isNotEmpty(nowStoreReceipt.getOpenId())) {
            signResult = signWxMethod(payOrderId, nowStoreReceipt);
            reqUrl = Constant.wxUrl;
        } else {
            signResult= signMethod(payOrderId, nowStoreReceipt);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();

            response = bankReq(signResult, reqUrl);

            logger.error("应用请求返回：{}", mapper.writeValueAsString(response));

            // 返回结果验签
            Boolean checkResult1 = checkSign(mapper.writeValueAsString(response));
            if(!checkResult1){
                return response;
            }
            String success = response.get("returnCode");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private Map<String, String> bankReq(String signResult, String url) throws Exception {
        Long  time  = System.currentTimeMillis()/1000;
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> signResultMap = null;
        signResultMap = mapper.readValue(signResult, Map.class);
        Map<String,String> apiSign = new TreeMap<>();
        apiSign.put("appid", Constant.appId);
        apiSign.put("secret", Constant.appSecret);
        apiSign.put("sign", signResultMap.get("sign"));
        apiSign.put("timestamp", "" + time);

        // MD5加密
        String MD5Content = SignatureUtil.getSignContent(apiSign);
        String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();

        // 组request头部Map
        Map<String, String> apiHeader = new HashMap<>();
        apiHeader.put("appid", Constant.appId);
        apiHeader.put("timestamp", "" + time);
        apiHeader.put("apisign", apiSignString);

        // 发送HTTP post请求
        Map<String, String> response = Utils.postForEntity(url, signResult, apiHeader);

        return response;
    }

    private Map<String, String> str2Map(String str) {
        Map<String, String> result = new HashMap<>();
        String[] results = str.split("&");
        if (results != null && results.length > 0) {
            for (int var = 0; var < results.length; ++var) {
                String pair = results[var];
                String[] kv = pair.split("=", 2);
                if (kv != null && kv.length == 2) {
                    result.put(kv[0], kv[1]);
                }
            }
        }
        return result;
    }


    private String signMethod(String orderId, StoreReceipt nowStoreReceipt){

        Integer payMoney = new BigDecimal(nowStoreReceipt.getAmount() * 100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

        String mchReserved = "付款单号:".concat(nowStoreReceipt.getCode()).concat(",区域编码:")
                .concat(nowStoreReceipt.getDeptId()).concat(",结算账号:").concat(nowStoreReceipt.getMerchantId());
        Map<String, String> requestPublicParams = new TreeMap<>();
        String requestStr = null;
        try {
            //公共请求参数
            requestPublicParams.put("version", "0.0.1");    //版本号，固定为0.0.1(必传字段)
            requestPublicParams.put("encoding", "UTF-8");   //编码方式，固定为UTF-8(必传)
            requestPublicParams.put("signMethod", "02");    //签名方法，固定为01，表示签名方式为RSA2(必传)
            //业务要素
            Map<String, String> requestTransactionParams = new HashMap<>();
            requestTransactionParams.put("body", "湖南湘佳电子商务有限公司");   //商户名称(必传)
            requestTransactionParams.put("mchReserved", mchReserved);   //商户名称(必传)
            requestTransactionParams.put("currencyCode", "156");    //交易币种，默认156，目前只支持人民币（156）
            requestTransactionParams.put("merId", Constant.MER_ID);   //商户号(必传)
            requestTransactionParams.put("notifyUrl", Constant.notifyUrl);  //交易通知地址(必传)
            requestTransactionParams.put("orderId", orderId); //商户订单号(必传)
            requestTransactionParams.put("payValidTime", "3600"); //支付有效时间
            requestTransactionParams.put("termId", "00774411");  //终端号
            requestTransactionParams.put("txnAmt", payMoney.toString());  //交易金额,单位为分(必传)
            requestTransactionParams.put("userId", "N068906208");   //收银员
            requestTransactionParams.put("tradeScene", "OFFLINE");   //交易场景：线下（必填）
            ObjectMapper mapper = new ObjectMapper();
            requestPublicParams.put("biz_content", mapper.writeValueAsString(requestTransactionParams));

            logger.info("加签前的报文内容：" + mapper.writeValueAsString(requestPublicParams));

            //私钥
            //对待加签内容进行排序拼接
            String signContent= SignatureUtil.getSignContent(requestPublicParams);
            //加签
            //            requestPublicParams.put("sign", Test.SignatureUtil.rsaSign(signContent, privateKey, "UTF-8"));

            requestPublicParams.put("sign", SM2Util.sm2Sign(signContent, Constant.PRIVATE_KEY));

            requestStr = mapper.writeValueAsString(requestPublicParams);

            logger.info("加签后的报文内容：" + requestStr);
            return requestStr;

        }catch (Exception e){
            logger.error("加签发生异常！");
            e.printStackTrace();
            return requestStr;
        }
    }


    private String signWxMethod(String orderId, StoreReceipt nowStoreReceipt){

        Integer payMoney = new BigDecimal(nowStoreReceipt.getAmount() * 100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

        String mchReserved = "付款单号:".concat(nowStoreReceipt.getCode()).concat(",区域编码:")
                .concat(nowStoreReceipt.getDeptId()).concat(",结算账号:").concat(nowStoreReceipt.getMerchantId());
        Map<String, String> requestPublicParams = new TreeMap<>();
        String requestStr = null;
        try {
            //公共请求参数
            requestPublicParams.put("version", "0.0.1");    //版本号，固定为0.0.1(必传字段)
            requestPublicParams.put("encoding", "UTF-8");   //编码方式，固定为UTF-8(必传)
            requestPublicParams.put("signMethod", "02");    //签名方法，固定为01，表示签名方式为RSA2(必传)
            //业务要素
            Map<String, String> requestTransactionParams = new HashMap<>();
            requestTransactionParams.put("body", "湖南湘佳电子商务有限公司");   //商户名称(必传)
            requestTransactionParams.put("deviceInfo", "WEB");   //设备号
            requestTransactionParams.put("subAppId", "wxcee79d26c3eda7fd");   //商户微信公众号appid
            requestTransactionParams.put("currencyCode", "156");    //交易币种，默认156，目前只支持人民币（156）
            requestTransactionParams.put("merId", Constant.MER_ID);   //商户号(必传)
            requestTransactionParams.put("notifyUrl", Constant.notifyUrl);  //交易通知地址(必传)
            requestTransactionParams.put("orderId", orderId); //商户订单号(必传)
            requestTransactionParams.put("tradeType", "JSAPI"); //商户订单号(必传)
            requestTransactionParams.put("payValidTime", "3600"); //支付有效时间
            requestTransactionParams.put("body", "湘佳电商-土黑公鸡"); //商品描述
            requestTransactionParams.put("spbillCreateIp", "47.119.206.249"); //终端IP
            requestTransactionParams.put("mchReserved", mchReserved);   //商户名称(必传)
            requestTransactionParams.put("subOpenId", nowStoreReceipt.getOpenId());
            requestTransactionParams.put("txnAmt", payMoney.toString());  //交易金额,单位为分(必传)
            requestTransactionParams.put("userId", "N069697908");   //收银员
            requestTransactionParams.put("tradeScene", "OFFLINE");   //交易场景：线下（必填）
            ObjectMapper mapper = new ObjectMapper();
            requestPublicParams.put("biz_content", mapper.writeValueAsString(requestTransactionParams));

            System.out.println("加签前的报文内容：" + mapper.writeValueAsString(requestPublicParams));

            //私钥
            //对待加签内容进行排序拼接
            String signContent= SignatureUtil.getSignContent(requestPublicParams);
            //加签
            //            requestPublicParams.put("sign", Test.SignatureUtil.rsaSign(signContent, privateKey, "UTF-8"));

            requestPublicParams.put("sign", SM2Util.sm2Sign(signContent, Constant.PRIVATE_KEY));

            requestStr = mapper.writeValueAsString(requestPublicParams);

            System.out.println("加签后的报文内容：" + requestStr);
            return requestStr;

        }catch (Exception e){
            System.out.println("加签发生异常！");
            e.printStackTrace();
            return requestStr;
        }
    }

    private Boolean checkSign(String string){
        logger.info("要验签的报文内容：" + string);
        try {
            //公钥
            //验签
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> responseBodyMap = objectMapper.readValue(string, Map.class);
            String sign = responseBodyMap.remove("sign");
            String contentStr = SignatureUtil.getSignContent(responseBodyMap);
            boolean result = SM2Util.sm2Check(contentStr, sign, Constant.PUBLIC_KEY);

            if (result) {
                logger.info("报文验签成功!");
            } else {
                logger.info("报文验签失败!");
            }
            return result;
        }catch (Exception e){
            logger.error("验签发生异常！");
            e.printStackTrace();
            return false;
        }
    }


    private  String signQryMethod(String payOrderId){
        Map<String, String> requestPublicParams = new TreeMap<>();
        String requestStr = null;
        try {
            //公共请求参数
            requestPublicParams.put("version", "0.0.1");    //版本号，固定为0.0.1(必传字段)
            requestPublicParams.put("encoding", "UTF-8");   //编码方式，固定为UTF-8(必传)
            requestPublicParams.put("signMethod", "02");    //签名方法，固定为01，表示签名方式为RSA2(必传)
            //业务要素
            Map<String, String> requestTransactionParams = new HashMap<>();
            requestTransactionParams.put("merId", Constant.MER_ID);   //商户号(必传)
            //requestTransactionParams.put("cmbOrderId", "100421091323463781244801");  //平台订单号
            requestTransactionParams.put("orderId", payOrderId); //商户订单号(必传)
            requestTransactionParams.put("userId", "N068906208");   //收银员(必传)
            ObjectMapper mapper = new ObjectMapper();
            requestPublicParams.put("biz_content", mapper.writeValueAsString(requestTransactionParams));

            logger.info("加签前的报文内容：" + mapper.writeValueAsString(requestPublicParams));

            String signContent = SignatureUtil.getSignContent(requestPublicParams);
            String sign = SM2Util.sm2Sign(signContent,Constant.PRIVATE_KEY);
            requestPublicParams.put("sign",sign);

            requestStr = mapper.writeValueAsString(requestPublicParams);
            logger.info("加签后的报文内容：" + requestStr);
            return requestStr;

        }catch (Exception e){
            logger.error("加签发生异常！", e);
            e.printStackTrace();
            return requestStr;
        }
    }

    private int updateStatus(String payOrderNo, String status, String qryRsp) {
        StorePaymentFlow storePaymentFlow = new StorePaymentFlow();
        storePaymentFlow.setPayOrderNo(payOrderNo);
        storePaymentFlow.setStatus(status);
        storePaymentFlow.setQryRspTxt(qryRsp);
        return storePaymentFlowMapper.updateStorePaymentFlowStatus(storePaymentFlow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderPay(String payOrderNo, String qryRsp) {
        StorePaymentFlow storePaymentFlow = new StorePaymentFlow();
        storePaymentFlow.setPayOrderNo(payOrderNo);
        storePaymentFlow.setStatus(Constants.NORMAL);
        List<StorePaymentFlow> list = storePaymentFlowMapper.selectStorePaymentFlowList(storePaymentFlow);
        if(CollectionUtils.isNotEmpty(list)) {

            StoreReceipt storeReceipt = storeReceiptMapper.selectStoreReceiptById(list.get(0).getReceiptId());
            storeReceipt.setPayStatus(OrderPayStatus.PAYED.getCode());
            storeReceipt.setPayType(ReceiptPayType.CMB_PAY.getCode());
            storeReceipt.setLastModifyTime(new Date());
            storeReceiptService.updateStoreReceipt(storeReceipt);

            StorePaymentAccountQueryDTO dto = new StorePaymentAccountQueryDTO();
            dto.setMemberId(storeReceipt.getMerchantId());
            List<StorePaymentAccountVO> accountVOList = storePaymentAccountMapper.selectStorePaymentAccountList(dto);
            storeBlackWhiteService.updateBlackOrWhiteStatus(accountVOList.get(0));

            updateStatus(payOrderNo, Constants.DELETE, qryRsp);
        }
    }


    /**
     * 新增聚合支付流水
     *
     * @param receiptId 聚合支付流水
     * @return 结果
     */
    @Override
    public StorePaymentFlow receiptPay(Long receiptId, String openId) {

        StorePaymentFlow storePaymentFlow = new StorePaymentFlow();
        storePaymentFlow.setReceiptId(receiptId);


        StoreReceipt nowStoreReceipt = storeReceiptMapper.selectStoreReceiptById(receiptId);
        if(OrderPayStatus.PAYED.getCode().equals(nowStoreReceipt.getPayStatus())) {
            throw new BusinessException("付款单【" + nowStoreReceipt.getReceiptId() + "】已经支付！");
        }
        StorePaymentAccountQueryDTO queryAccount = new StorePaymentAccountQueryDTO();
        queryAccount.setMemberId(nowStoreReceipt.getMerchantId());
        List<StorePaymentAccountVO> accList = storePaymentAccountService.selectStorePaymentAccountList(queryAccount);
        //判断账户余额是否足够，直接扣除现金账户
        if(CollectionUtils.isNotEmpty(accList) && accList.get(0).getCash() >= nowStoreReceipt.getAmount()) {
            nowStoreReceipt.setPayStatus(OrderPayStatus.PAYED.getCode());
            nowStoreReceipt.setPayType(ReceiptPayType.ACCOUNT_PAY.getCode());
            nowStoreReceipt.setLastModifyTime(new Date());
            storeReceiptService.updateStoreReceipt(nowStoreReceipt);
            storePaymentFlow.setReturnCode("SUCCESS");
            storePaymentFlow.setTradeState("S");
        } else {
            // 根据生成的付款单生成支付流水
//            storePaymentFlow.setStatus(Constants.NORMAL);
//            List<StorePaymentFlow> list = storePaymentFlowMapper.selectStorePaymentFlowList(storePaymentFlow);
//            if(CollectionUtils.isEmpty(list))
//            {
            storePaymentFlow.setCreateTime(DateUtils.getNowDate());
            storePaymentFlow.setOrderIds(nowStoreReceipt.getOrderIds());
            storePaymentFlow.setPayOrderNo(UUID.randomUUID().toString().replaceAll("-", ""));


            // 生成二维码
            if(StringUtils.isNotEmpty(openId)) {
                nowStoreReceipt.setOpenId(openId);
            }
            Map<String, String> rslt = genOrCode(storePaymentFlow.getPayOrderNo(),nowStoreReceipt);
            storePaymentFlow.setReturnCode(rslt.get("returnCode"));
            storePaymentFlow.setRespCode(rslt.get("respCode"));
            JSONObject jsonObject = (JSONObject)JSON.parse(rslt.get("biz_content"));
            storePaymentFlow.setQrCode(jsonObject.getString("qrCode"));
            //调试地址添加
            //storePaymentFlow.setQrCode(storePaymentFlow.getQrCode().replaceAll("https://qr.95516.com","http://payment-uat.cs.cmburl.cn"));
            storePaymentFlow.setCmbOrderNo(jsonObject.getString("cmbOrderId"));
            storePaymentFlow.setRspTxt(JSONObject.toJSONString(rslt));
            if(StringUtils.isNotEmpty(nowStoreReceipt.getOpenId())) {
                storePaymentFlow.setPayData(jsonObject.getString("payData"));
            }
            storePaymentFlowMapper.insertStorePaymentFlow(storePaymentFlow);
//            } else {
//                storePaymentFlow = list.get(0);
//                if(StringUtils.isNotEmpty(openId)) {
//                    JSONObject jsonObject = (JSONObject)JSON.parse(storePaymentFlow.getRspTxt());
//                    String bizConent = (String)jsonObject.get("biz_content");
//                    JSONObject bizObj = (JSONObject)JSON.parse(bizConent);
//                    storePaymentFlow.setPayData(bizObj.get("payData").toString());
//                }
//            }
        }
        storePaymentFlow.setPayMoney(nowStoreReceipt.getAmount());

        return storePaymentFlow;
    }
}
