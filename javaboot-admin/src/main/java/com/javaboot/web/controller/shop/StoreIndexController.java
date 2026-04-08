package com.javaboot.web.controller.shop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StorePaymentFlow;
import com.javaboot.shop.service.IStorePaymentFlowService;
import com.javaboot.shop.sm2.MD5Utils;
import com.javaboot.shop.sm2.SM2Util;
import com.javaboot.shop.sm2.SignatureUtil;
import com.javaboot.shop.sm2.Utils;
import com.javaboot.web.controller.tool.HttpGetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;


/**
 * 微信认证流程
 * 
 * @author lqh
 * @date 2021-07-09
 */
@Controller
public class StoreIndexController {


    @Autowired
    private IStorePaymentFlowService storePaymentFlowService;

//    private static final String url = "https://api.cmbchina.com/polypay/v1.0/mchorders/onlinepay";  //pRD
//    private static String privateKey = "9EF470ABEBA22EAA4592034964FFAA14D3C302A3F9082A053F244DE342FA2162";
//    private static String publicKey =   "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEI2JuBTsKz9bAJudAxaGEAshoYFTOAGs2XQzGg93NlW67em9KSacSd0z0KVHsV87JtO38ZrEExHw7+qSCB3Qmsw==";
//    private static final String appId = "27fe0105-97db-44d5-b840-4b694c9d9af9";
//    private static final String appSecret = "35f0993c-748d-4e81-927c-2661852b1c59";
//
//
//    private static final String notifyUrl = "http://csds.xiangjiamuye.com/shop/xjpay/notify";


    /**
     * 1.验证微信域名
     * @return
     */
    @ResponseBody
    @RequestMapping(value=("/MP_verify_08OeIEILAoEvlPgs.txt"),method=RequestMethod.GET)
    public String flow() {
        return "08OeIEILAoEvlPgs";
    }

    /**
     * 1.验证微信域名
     * @return
     */
    @ResponseBody
    @RequestMapping(value=("/WXPAY_verify_270835973.txt"),method=RequestMethod.GET)
    public String wxpay() {
        return "b95ac6c742fbf69e875587a66642ecfe";
    }

    /**
     * 获取用户的openid准备跳转接口
     * @param request
     * @param response
     */
    @RequestMapping(value=("/redirectUrl"),method=RequestMethod.GET)
    public void redirectUrl(HttpServletRequest request, HttpServletResponse response) {
        StringBuffer sb = new StringBuffer();
        StringBuffer encodeUrl = new StringBuffer(300);
        //公众号中配置的回调域名（网页授权回调域名）
        String doname = "http://csds.xiangjiamuye.com";
        String appId = "wxcee79d26c3eda7fd";

        sb.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=");
        sb.append(appId);
        String url = "";

        String receiptId = request.getParameter("receiptId");//获取receiptId
        try {
            //对重定向url进行编码，官方文档要求
            encodeUrl.append("http://csds.xiangjiamuye.com/xjshop/#/pages/order/orderpaymid");
            url = URLEncoder.encode(encodeUrl.toString(), "utf-8");
            sb.append("&redirect_uri=").append(url);
            //网页授权的静默授权snsapi_base
            sb.append("&response_type=code&scope=snsapi_base&state=" + receiptId +"#wechat_redirect");
            System.out.println("重定向url编码失败：>>" + sb.toString());
            response.sendRedirect(sb.toString());
        } catch (UnsupportedEncodingException e) {
           System.out.println("重定向url编码失败：>>" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("response重定向失败：>>" + e.getMessage());
            e.printStackTrace();
        }
    }


//    @ResponseBody
//    @RequestMapping(value=("/getOpenId"),method=RequestMethod.GET)
//    public String getOpenId() {
//        return "MvRzRPigcsRKcgDd";
//    }

//    @ResponseBody

    /**
     * 获取到openid，调用招行的接口，完成下单，根据返回的参数在前段js代码调起支付
     * @param request
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/toPlayerInfo")
    @ResponseBody
    public StorePaymentFlow toPlayerInfo(HttpServletRequest request, @RequestParam(value="receiptId", required = false) String receiptId ) {
        // 获取openid
        String code = request.getParameter("code");//获取code
        System.out.println("重定向url编码code：>>" + code);
        Map params = new HashMap();
//        params.put("appid", "wxe3a73857c1df6195");
//        params.put("secret", "aca13e16ae0d17c8da2966c1ed4ea680");
        params.put("appid", "wxcee79d26c3eda7fd");
        params.put("secret", "0343d797c758d7bef464a50424e878dd");
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        String result = HttpGetUtil.httpRequestToString(
                "https://api.weixin.qq.com/sns/oauth2/access_token", params);
        JSONObject jsonObject = JSON.parseObject(result);
        String openId = jsonObject.get("openid").toString();

        if(StringUtils.isNotEmpty(openId) && StringUtils.isNotEmpty(receiptId)) {
            return storePaymentFlowService.receiptPay(Long.parseLong(receiptId), openId);
        }
        return null;
//
//        String signResult= signMethod(openid);
//        Long  time  = System.currentTimeMillis()/1000;
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String,String> signResultMap = mapper.readValue(signResult, Map.class);
//
//            // 组apiSign加密Map
//            Map<String,String> apiSign = new TreeMap<>();
//            apiSign.put("appid", appId);
//            apiSign.put("secret", appSecret);
//            apiSign.put("sign", signResultMap.get("sign"));
//            apiSign.put("timestamp", "" + time);
//
//            // MD5加密
//            String MD5Content = SignatureUtil.getSignContent(apiSign);
//            String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();
//
//            // 组request头部Map
//            Map<String, String> apiHeader = new HashMap<>();
//            apiHeader.put("appid", appId);
//            apiHeader.put("timestamp", "" + time);
//            apiHeader.put("apisign", apiSignString);
//
//            // 发送HTTP post请求
//            Map<String,String> response1 = Utils.postForEntity(url, signResult, apiHeader);
//
//            System.out.println(mapper.writeValueAsString(response1));
//            // 返回结果验签
//            Boolean checkResult1 = checkSign(mapper.writeValueAsString(response1));
//            if(!checkResult1){
//                return "wxpay";
//            }
//
//            String success = response1.get("returnCode");
//            System.out.println("返回结果：" + success);
//            mmap.put("resp",mapper.writeValueAsString(response1));
//            return "wxpay";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "wxpay";
//        }

    }



//    private static String signMethod(String  openid){
//        Map<String, String> requestPublicParams = new TreeMap<>();
//        String requestStr = null;
//        try {
//            //公共请求参数
//            requestPublicParams.put("version", "0.0.1");    //版本号，固定为0.0.1(必传字段)
//            requestPublicParams.put("encoding", "UTF-8");   //编码方式，固定为UTF-8(必传)
//            requestPublicParams.put("signMethod", "02");    //签名方法，固定为01，表示签名方式为RSA2(必传)
//            //业务要素
//            Map<String, String> requestTransactionParams = new HashMap<>();
////            requestTransactionParams.put("body", "湖南湘佳电子商务有限公司");   //商户名称(必传)
//            requestTransactionParams.put("deviceInfo", "WEB");   //设备号
//            requestTransactionParams.put("subAppId", "wxcee79d26c3eda7fd");   //商户微信公众号appid
//            requestTransactionParams.put("currencyCode", "156");    //交易币种，默认156，目前只支持人民币（156）
//            requestTransactionParams.put("merId", "308999154220005");   //商户号(必传)
//            requestTransactionParams.put("notifyUrl", notifyUrl);  //交易通知地址(必传)
//            requestTransactionParams.put("orderId", "XJ00001118015"+new Random().nextInt(999999)); //商户订单号(必传)
//            requestTransactionParams.put("tradeType", "JSAPI"); //商户订单号(必传)
//            requestTransactionParams.put("payValidTime", "100"); //支付有效时间
//            requestTransactionParams.put("body", "湘佳电商-土黑公鸡"); //商品描述
//            requestTransactionParams.put("spbillCreateIp", "47.119.206.249"); //终端IP
////            requestTransactionParams.put("mchReserved", "XJ00001119"); //支付有效时间
//            requestTransactionParams.put("subOpenId", "om1Y_6lXNsxPVg6zi0F-kjZiBhDk");
//            requestTransactionParams.put("txnAmt", "1");  //交易金额,单位为分(必传)
//            requestTransactionParams.put("userId", "N069697908");   //收银员
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
//            requestPublicParams.put("sign", SM2Util.sm2Sign(signContent, privateKey));
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
//    private static Boolean checkSign(String string){
//        System.out.println("要验签的报文内容：" + string);
//        try {
//            //公钥
//            //验签
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, String> responseBodyMap = objectMapper.readValue(string, Map.class);
//            String sign = responseBodyMap.remove("sign");
//            String contentStr = SignatureUtil.getSignContent(responseBodyMap);
//            boolean result = SM2Util.sm2Check(contentStr, sign, publicKey);
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

}
