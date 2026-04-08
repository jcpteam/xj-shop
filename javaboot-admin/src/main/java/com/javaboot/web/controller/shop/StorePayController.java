package com.javaboot.web.controller.shop;

import com.javaboot.shop.domain.StorePaymentFlow;
import com.javaboot.shop.service.IStorePaymentFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * FOUNDER SECURITIES
 * DATA-Group
 * wangxiaotao@foundersc.com
 * 2021/7/25 9:35
 **/


@Controller
@RequestMapping("/shop/xjpay")
public class StorePayController {
    public static final Logger log = LoggerFactory.getLogger(StorePayController.class);

    @Autowired
    IStorePaymentFlowService storePaymentFlowService;

    @RequestMapping(value = "/qrcode/{receiptId}", method = RequestMethod.GET)
    @ResponseBody
    public StorePaymentFlow genQrcode(@PathVariable("receiptId") String receiptId, @RequestParam(value="openId", required = false) String openId ) {
        return storePaymentFlowService.receiptPay(Long.parseLong(receiptId), openId);
    }

    @RequestMapping(value = "/query/{payOrderId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> payQuery(@PathVariable("payOrderId") String payOrderId) {
        return storePaymentFlowService.payQuery(payOrderId);
    }


//    @RequestMapping(value = "/qrcodepay", method = RequestMethod.POST)
//    public Map<String, String> qrcodepay(@RequestBody String orderId,Integer payMoney) {
//        String signResult= signMethod(orderId,payMoney);
//        Long  time  = System.currentTimeMillis()/1000;
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String,String> signResultMap = null;
//            signResultMap = mapper.readValue(signResult, Map.class);
//            Map<String,String> apiSign = new TreeMap<>();
//            apiSign.put("appid", Constant.appId);
//            apiSign.put("secret", Constant.appSecret);
//            apiSign.put("sign", signResultMap.get("sign"));
//            apiSign.put("timestamp", "" + time);
//
//            // MD5加密
//            String MD5Content = SignatureUtil.getSignContent(apiSign);
//            String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();
//
//            // 组request头部Map
//            Map<String, String> apiHeader = new HashMap<>();
//            apiHeader.put("appid", Constant.appId);
//            apiHeader.put("timestamp", "" + time);
//            apiHeader.put("apisign", apiSignString);
//
//            // 发送HTTP post请求
//            Map<String,String> response = Utils.postForEntity(Constant.url, signResult, apiHeader);
//
//            System.out.println(mapper.writeValueAsString(response));
//            // 返回结果验签
//            Boolean checkResult1 = checkSign(mapper.writeValueAsString(response));
//            if(!checkResult1){
//                return null;
//            }
//            String success = response.get("returnCode");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return  null;
//    }
//
//
//    @RequestMapping(value = "/orderquery", method = RequestMethod.POST)
//    public Map<String, String> orderquery(@RequestBody String orderId) {
//
//        String signResult= signMethod(orderId);
//        Long  time  = System.currentTimeMillis()/1000;
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String,String> signResultMap = mapper.readValue(signResult, Map.class);
//
//            // 组apiSign加密Map
//            Map<String,String> apiSign = new TreeMap<>();
//            apiSign.put("appid", Constant.appId);
//            apiSign.put("secret", Constant.appSecret);
//            apiSign.put("sign", signResultMap.get("sign"));
//            apiSign.put("timestamp", "" + time);
//
//            // MD5加密
//            String MD5Content = SignatureUtil.getSignContent(apiSign);
//            String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();
//
//            // 组request头部Map
//            Map<String, String> apiHeader = new HashMap<>();
//            apiHeader.put("appid", Constant.appId);
//            apiHeader.put("timestamp", "" + time);
//            apiHeader.put("apisign", apiSignString);
//
//            // 发送HTTP post请求
//            Map<String,String> response = Utils.postForEntity(Constant.queryUrl, signResult, apiHeader);
//
//            // 返回结果验签
//            Boolean checkResult1 = checkSign(mapper.writeValueAsString(response));
//            if(!checkResult1){
//                return null;
//            }
//
//            String success = response.get("returnCode");
//            System.out.println("返回结果：" + success);
//        } catch (Exception e) {
//            return null;
//        }
//
//        return  null;
//
//    }
//
//    @RequestMapping(value = "/payNotify", method = RequestMethod.POST)
//    public Map<String, String> payNotify(@RequestBody String requestBodyString) {
//        System.out.println(requestBodyString);
//        log.info("====pay return:"+requestBodyString);
//        Map<String, String> respData = new HashMap<>();
//        //设置响应数据
//        respData.put("version", "0.0.1");//版本号，固定为0.0.1(必传)
//        respData.put("encoding", "UTF-8");//编码方式，固定为UTF-8(必传)
//        respData.put("signMethod", "02");//签名方法，固定为01,表示签名方法为RSA2(必传)
//        try {
//            respData.put("returnCode", "SUCCESS"); //SUCCESS表示商户接收通知成功并校验成功
//            //非空校验
//            if (requestBodyString == null || "".equals(requestBodyString.trim())) {
//                respData.put("returnCode", "FAIL");
//                return respData;
//            }
//            Map<String, String> requestBodyMap = str2Map(requestBodyString);
//            Map<String, String> resultMap = requestBodyMap.entrySet().stream().collect(Collectors.toMap(e -> SignatureUtil.decode(e.getKey()), e -> SignatureUtil.decode(e.getValue())));
//            if (resultMap == null) {
//                respData.put("returnCode", "FAIL");
//                return respData;
//            }
//            String sign = resultMap.remove("sign");
//            //对待加签内容进行排序拼接
//            String contentStr = SignatureUtil.getSignContent(resultMap);
//            //验证签名-使用招行公钥进行验签
//            boolean flag = SM2Util.sm2Check(contentStr,sign,Constant.PUBLIC_KEY);
//            if (!flag) {
//                //验签失败
//                System.out.println("验签失败");
//                respData.put("returnCode", "FAIL");
//                return respData;
//            }
//            log.info("====pay return:"+respData.toString());
//            System.out.println("验签成功");
//            //......（处理自身业务逻辑）
//            //......
//            //......
//            respData.put("respCode", "SUCCESS");//业务错误码，成功为SUCCESS，失败为FAIL
//            /*如果处理自身业务逻辑发生错误，返回
//            respData.put("respCode","FAIL");
//            respData.put("respMsg","error_msg");
//             */
//            //对待加签内容进行排序拼接
//            //加签-使用商户私钥加签
//            return respData;
//        } catch (Exception e) {
//            e.printStackTrace();
//            respData.put("returnCode", "FAIL");
//            return respData;
//        }
//
//    }
//        private Map<String, String> str2Map(String str) {
//            Map<String, String> result = new HashMap<>();
//            String[] results = str.split("&");
//            if (results != null && results.length > 0) {
//                for (int var = 0; var < results.length; ++var) {
//                    String pair = results[var];
//                    String[] kv = pair.split("=", 2);
//                    if (kv != null && kv.length == 2) {
//                        result.put(kv[0], kv[1]);
//                    }
//                }
//            }
//            return result;
//        }
//
//
//    private String signMethod(String orderID, Integer payMoney ){
//        Map<String, String> requestPublicParams = new TreeMap<>();
//        String requestStr = null;
//        try {
//            //公共请求参数
//            requestPublicParams.put("version", "0.0.1");    //版本号，固定为0.0.1(必传字段)
//            requestPublicParams.put("encoding", "UTF-8");   //编码方式，固定为UTF-8(必传)
//            requestPublicParams.put("signMethod", "02");    //签名方法，固定为01，表示签名方式为RSA2(必传)
//            //业务要素
//            Map<String, String> requestTransactionParams = new HashMap<>();
//            requestTransactionParams.put("body", "湖南湘佳电子商务有限公司");   //商户名称(必传)
//            requestTransactionParams.put("currencyCode", "156");    //交易币种，默认156，目前只支持人民币（156）
//            requestTransactionParams.put("merId", "3089991074200HI");   //商户号(必传)
//            requestTransactionParams.put("notifyUrl", Constant.notifyUrl);  //交易通知地址(必传)
//            requestTransactionParams.put("orderId", orderID); //商户订单号(必传)
//            requestTransactionParams.put("payValidTime", "3600"); //支付有效时间
//            requestTransactionParams.put("termId", "00774411");  //终端号
//            requestTransactionParams.put("txnAmt", payMoney.toString());  //交易金额,单位为分(必传)
//            requestTransactionParams.put("userId", "N003327837");   //收银员
//            requestTransactionParams.put("tradeScene", "OFFLINE");   //交易场景：线下（必填）
//            ObjectMapper mapper = new ObjectMapper();
//            requestPublicParams.put("biz_content", mapper.writeValueAsString(requestTransactionParams));
//
//            System.out.println("加签前的报文内容：" + mapper.writeValueAsString(requestPublicParams));
//
//            //私钥
//            //对待加签内容进行排序拼接
//            String signContent= SignatureUtil.getSignContent(requestPublicParams);
//            //加签
////            requestPublicParams.put("sign", Test.SignatureUtil.rsaSign(signContent, privateKey, "UTF-8"));
//
//            requestPublicParams.put("sign", SM2Util.sm2Sign(signContent, Constant.PRIVATE_KEY));
//
//            requestStr = mapper.writeValueAsString(requestPublicParams);
//
//            System.out.println("加签后的报文内容：" + requestStr);
//            return requestStr;
//
//        }catch (Exception e){
//            System.out.println("加签发生异常！");
//            e.printStackTrace();
//            return requestStr;
//        }
//    }
//
//
//   private Boolean checkSign(String string){
//        System.out.println("要验签的报文内容：" + string);
//        try {
//            //公钥
//            //验签
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, String> responseBodyMap = objectMapper.readValue(string, Map.class);
//            String sign = responseBodyMap.remove("sign");
//            String contentStr = SignatureUtil.getSignContent(responseBodyMap);
//            boolean result = SM2Util.sm2Check(contentStr, sign, Constant.PUBLIC_KEY);
//
//            if (result) {
//                System.out.println("报文验签成功!");
//            } else {
//                System.out.println("报文验签失败!");
//            }
//            return result;
//        }catch (Exception e){
//            System.out.println("验签发生异常！");
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//    private static String signMethod(String orderId){
//        Map<String, String> requestPublicParams = new TreeMap<>();
//        String requestStr = null;
//        try {
//            //公共请求参数
//            requestPublicParams.put("version", "0.0.1");    //版本号，固定为0.0.1(必传字段)
//            requestPublicParams.put("encoding", "UTF-8");   //编码方式，固定为UTF-8(必传)
//            requestPublicParams.put("signMethod", "02");    //签名方法，固定为01，表示签名方式为RSA2(必传)
//            //业务要素
//            Map<String, String> requestTransactionParams = new HashMap<>();
//            requestTransactionParams.put("merId", "3089991074200HI");   //商户号(必传)
//            requestTransactionParams.put("cmbOrderId", "100421091010140350007730");  //平台订单号
//            requestTransactionParams.put("orderId", orderId); //商户订单号(必传)
//            requestTransactionParams.put("userId", "N003327837");   //收银员(必传)
//            ObjectMapper mapper = new ObjectMapper();
//            requestPublicParams.put("biz_content", mapper.writeValueAsString(requestTransactionParams));
//
//            System.out.println("加签前的报文内容：" + mapper.writeValueAsString(requestPublicParams));
//
//            String signContent = SignatureUtil.getSignContent(requestPublicParams);
//            String sign = SM2Util.sm2Sign(signContent,Constant.PRIVATE_KEY);
//            requestPublicParams.put("sign",sign);
//
//            requestStr = mapper.writeValueAsString(requestPublicParams);
//            System.out.println("加签后的报文内容：" + requestStr);
//            return requestStr;
//
//        }catch (Exception e){
//            System.out.println("加签发生异常！");
//            e.printStackTrace();
//            return requestStr;
//        }
//    }
}
