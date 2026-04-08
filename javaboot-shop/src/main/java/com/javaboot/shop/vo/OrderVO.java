package com.javaboot.shop.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * OrderVO
 *
 * @author javaboot
 * @version 1.0.0
 * @date 2020/3/10 8:00
 */
public class OrderVO {
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 订单状态 0-未确认 1-已确认(已收货） 2-已评价
     */
    private Integer orderStatus;
    /**
     * 发货状态 0-未发货 1-已发货 2-退货中 3-退货完成
     */
    private Integer shippingStatus;
    /**
     * 支付状态 0-未支付 1-已支付
     */
    private Integer payStatus;
    /**
     * 支付方式:1-微信 2-支付宝 3-余额 4-转账
     */
    private Integer payMethod;
    /**
     * 支付方式名称
     */
    private String payName;
    /**
     * 应付款金额
     */
    private Double orderAmount;
    /**
     * 下单时间
     */
    private Date addTime;
    /**
     * 支付时间
     */
    private Integer payTime;
    /**
     * 删除|取消时间
     */
    private Long deletedTime;
    /**
     * 用户假删除标识,1:删除,0未删除
     */
    private Integer deleted;
    /**
     * 商品id
     */
    private Integer goodsId;
    /**
     * 商品名称
     */
    private String goodsName;

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

    public Integer getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(Integer shippingStatus) {
        this.shippingStatus = shippingStatus;
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

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getPayTime() {
        return payTime;
    }

    public void setPayTime(Integer payTime) {
        this.payTime = payTime;
    }

    public Long getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(Long deletedTime) {
        this.deletedTime = deletedTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("orderId", orderId)
                .append("orderSn", orderSn)
                .append("userId", userId)
                .append("orderStatus", orderStatus)
                .append("shippingStatus", shippingStatus)
                .append("payStatus", payStatus)
                .append("payMethod", payMethod)
                .append("payName", payName)
                .append("orderAmount", orderAmount)
                .append("addTime", addTime)
                .append("payTime", payTime)
                .append("deletedTime", deletedTime)
                .append("deleted", deleted)
                .append("goodsId", goodsId)
                .append("goodsName", goodsName)
                .toString();
    }
}
