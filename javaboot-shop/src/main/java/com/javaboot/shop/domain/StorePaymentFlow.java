package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 聚合支付流水对象 store_payment_flow
 * 
 * @author javaboot
 * @date 2021-09-13
 */
public class StorePaymentFlow extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 流水id */
    private Long id;

    /** 传给招行的订单号，订单号+随机数 */
    @Excel(name = "传给招行的订单号，订单号+随机数")
    private String payOrderNo;

    /** 订单id" */
    @Excel(name = "订单id")
    private Long receiptId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderIds;

    /** 招行生成的订单号 */
    @Excel(name = "招行生成的订单号")
    private String cmbOrderNo;

    /** 二维码地址 */
    @Excel(name = "二维码地址")
    private String qrCode;

    /** 0-删除 1-正常 */
    @Excel(name = "0-删除 1-正常")
    private String status;

    /** SUCCESS/FAIL,此字段是通信标识，非交易标识，交易是否成功需要查看respCode来判断。SUCCESS表示商户上送的报文符合规范，FAIL表示报文内的字段不符合规范，包括长度超长、非法字符、签名错误等 */
    @Excel(name = "SUCCESS/FAIL,此字段是通信标识，非交易标识，交易是否成功需要查看respCode来判断。SUCCESS表示商户上送的报文符合规范，FAIL表示报文内的字段不符合规范，包括长度超长、非法字符、签名错误等")
    private String returnCode;

    /** 招行返回的业务错误码，成功为SUCCESS，失败为FAIL，若此时errCode为SYSTERM_ERROR则表示本次请求处理异常，并非交易结果是失败状态，需要商户再次发起查询确认 */
    @Excel(name = "招行返回的业务错误码，成功为SUCCESS，失败为FAIL，若此时errCode为SYSTERM_ERROR则表示本次请求处理异常，并非交易结果是失败状态，需要商户再次发起查询确认")
    private String respCode;

    /** 查询结果返回的报文体 */
    @Excel(name = "查询结果返回的报文体")
    private String rspTxt;

    private String qryRspTxt;

    /** C - 订单已关闭 D - 交易已撤销 P - 交易在进行 F - 交易失败 S - 交易成功 R - 转入退款 */
    @Excel(name = "C - 订单已关闭 D - 交易已撤销 P - 交易在进行 F - 交易失败 S - 交易成功 R - 转入退款")
    private String tradeState;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    //需要支付的金额
    private Double payMoney;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String merchantName;

    /**
     * 微信调起支付数据
     */
    private String payData;

    public Double getPayMoney()
    {
        return payMoney;
    }

    public void setPayMoney(Double payMoney)
    {
        this.payMoney = payMoney;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setPayOrderNo(String payOrderNo){
        this.payOrderNo = payOrderNo;
    }

    public String getPayOrderNo(){
        return payOrderNo;
    }
    public void setCmbOrderNo(String cmbOrderNo){
        this.cmbOrderNo = cmbOrderNo;
    }

    public String getCmbOrderNo(){
        return cmbOrderNo;
    }
    public void setQrCode(String qrCode){
        this.qrCode = qrCode;
    }

    public String getQrCode(){
        return qrCode;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
    public void setReturnCode(String returnCode){
        this.returnCode = returnCode;
    }

    public String getReturnCode(){
        return returnCode;
    }
    public void setRespCode(String respCode){
        this.respCode = respCode;
    }

    public String getRespCode(){
        return respCode;
    }
    public void setRspTxt(String rspTxt){
        this.rspTxt = rspTxt;
    }

    public String getRspTxt(){
        return rspTxt;
    }
    public void setTradeState(String tradeState){
        this.tradeState = tradeState;
    }

    public String getTradeState(){
        return tradeState;
    }
    public void setLastModifyTime(Date lastModifyTime){
        this.lastModifyTime = lastModifyTime;
    }

    public Date getLastModifyTime(){
        return lastModifyTime;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public String getQryRspTxt() {
        return qryRspTxt;
    }

    public void setQryRspTxt(String qryRspTxt) {
        this.qryRspTxt = qryRspTxt;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public String getMerchantName()
    {
        return merchantName;
    }

    public void setMerchantName(String merchantName)
    {
        this.merchantName = merchantName;
    }

    public String getPayData()
    {
        return payData;
    }

    public void setPayData(String payData)
    {
        this.payData = payData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("payOrderNo", getPayOrderNo())
            .append("orderIds", getOrderIds())
            .append("cmbOrderNo", getCmbOrderNo())
            .append("qrCode", getQrCode())
            .append("status", getStatus())
            .append("returnCode", getReturnCode())
            .append("respCode", getRespCode())
            .append("rspTxt", getRspTxt())
            .append("tradeState", getTradeState())
            .append("payMoney", getPayMoney())
            .append("createTime", getCreateTime())
            .append("lastModifyTime", getLastModifyTime())
            .toString();
    }
}
