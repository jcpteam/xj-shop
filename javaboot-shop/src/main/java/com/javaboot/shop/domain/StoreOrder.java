package com.javaboot.shop.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import com.javaboot.common.enums.OrderStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 订单对象 store_order
 *
 * @author javaboot
 * @date 2019-08-29
 */
public class StoreOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String orderSn;

    /**
     * 用户id
     */
    @Excel(name = "用户id")
    private String userId;

    /**
     * 订单状态 0-未确认 1-已确认(已收货） 2-已评价
     */
    @Excel(name = "订单状态 0-未确认 1-已确认(已收货） 2-已评价")
    private Integer orderStatus;

    /**
     * 支付状态 0-未支付 1-已支付
     */
    @Excel(name = "支付状态 0-未支付 1-已支付")
    private Integer payStatus;

    /**
     * 支付方式:1-微信 2-支付宝 3-余额 4-转账
     */
    @Excel(name = "支付方式:1-微信 2-支付宝 3-余额 4-转账")
    private Integer payMethod;

    /**
     * 支付方式名称
     */
    private String payName;

    /**
     * 优惠券抵扣
     */
    @Excel(name = "优惠券抵扣")
    private Double couponPrice;

    /**
     * 应付款金额
     */
    @Excel(name = "应付款金额")
    private Double orderAmount;

    /**
     * 订单总价
     */
    @Excel(name = "订单总价")
    private Double totalAmount;

    /**
     * 下单时间
     */
    @Excel(name = "下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date addTime;

    /**
     * 支付时间
     */
    @Excel(name = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date payTime;

    /**
     * 用户假删除标识,1:删除,0未删除
     */
    private Integer deleted;

    /**
     * 商品id
     */
    @Excel(name = "商品id")
    private Integer goodsId;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称")
    private String goodsName;

    /**
     * 商品规格key
     */
    @Excel(name = "商品规格key")
    private String specKey;

    /**
     * 规格对应的中文名字
     */
    @Excel(name = "规格对应的中文名字")
    private String specKeyName;

    /** 商品规格 **/
    @Excel(name = "商品规格")
    private String specName;
    /** 授权时长 **/
    @Excel(name = "授权时长")
    private String authName;
    /** 到期时间 */
    @Excel(name = "到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTime;

    /**
     * 产品源码
     */
    private StoreSource storeSource;

    /**
     * 用户信息
     */
    private StoreMember storeMember;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(Double couponPrice) {
        this.couponPrice = couponPrice;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getSpecKey() {
        return specKey;
    }

    public void setSpecKey(String specKey) {
        this.specKey = specKey;
    }

    public String getSpecKeyName() {
        return specKeyName;
    }

    public void setSpecKeyName(String specKeyName) {
        this.specKeyName = specKeyName;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public StoreMember getStoreMember() {
        return storeMember;
    }

    public void setStoreMember(StoreMember storeMember) {
        this.storeMember = storeMember;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public StoreSource getStoreSource() {
        return storeSource;
    }

    public void setStoreSource(StoreSource storeSource) {
        this.storeSource = storeSource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("orderId", orderId)
                .append("orderSn", orderSn)
                .append("userId", userId)
                .append("orderStatus", orderStatus)
                .append("payStatus", payStatus)
                .append("payMethod", payMethod)
                .append("payName", payName)
                .append("couponPrice", couponPrice)
                .append("orderAmount", orderAmount)
                .append("totalAmount", totalAmount)
                .append("addTime", addTime)
                .append("payTime", payTime)
                .append("deleted", deleted)
                .append("goodsId", goodsId)
                .append("goodsName", goodsName)
                .append("specKey", specKey)
                .append("specKeyName", specKeyName)
                .append("specName", specName)
                .append("authName", authName)
                .append("expirationTime", expirationTime)
                .append("storeSource", storeSource)
                .append("storeMember", storeMember)
                .toString();
    }
}
