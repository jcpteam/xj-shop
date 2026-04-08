package com.javaboot.shop.domain;

import com.javaboot.shop.vo.StoreGoodsQuotationGoodsVO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 购物车明细对象 store_goods_cart_item
 * 
 * @author javaboot
 * @date 2021-08-04
 */
public class StoreGoodsCartItem extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 明细id */
    private Long itemId;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 商品id */
    @Excel(name = "商品id")
    private String goodsId;

    /** 商品spu */
    @Excel(name = "商品spu")
    private String spuNo;

    /** 商品数量 */
    @Excel(name = "商品数量")
    private Double quantity;

    /** 商品金额（加入购物车金额） */
    @Excel(name = "商品金额", readConverterExp = "加=入购物车金额")
    private Double amount;
    /**
     * 现在的总金额
     */
    private Double nowAmount;

    /** 商品价格（加入购物车时价格） */
    @Excel(name = "商品价格", readConverterExp = "加=入购物车时价格")
    private Double price;
    /**
     * 现在的价格
     */
    private Double nowPrice;

    private Double unitId;

    /** 商品备注 */
    @Excel(name = "商品备注")
    private String comment;

    private String goodsImg;

    /** 0-删除 1-正常 2-已下单 */
    @Excel(name = "0-删除 1-正常 2-已下单")
    private String status;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    private StoreGoodsQuotationGoodsVO storeGoodsQuotationGoods;

    public StoreGoodsQuotationGoodsVO getStoreGoodsQuotationGoods() {
        return storeGoodsQuotationGoods;
    }

    public void setStoreGoodsQuotationGoods(StoreGoodsQuotationGoodsVO storeGoodsQuotationGoods) {
        this.storeGoodsQuotationGoods = storeGoodsQuotationGoods;
    }

    @Override
    public String toString() {
        return "StoreGoodsCartItem{" +
                "itemId=" + itemId +
                ", userId='" + userId + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", spuNo='" + spuNo + '\'' +
                ", quantity=" + quantity +
                ", amount=" + amount +
                ", nowAmount=" + nowAmount +
                ", price=" + price +
                ", nowPrice=" + nowPrice +
                ", unitId=" + unitId +
                ", comment='" + comment + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", status='" + status + '\'' +
                ", lastModifyTime=" + lastModifyTime +
                '}';
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public void setItemId(Long itemId){
        this.itemId = itemId;
    }

    public Long getItemId(){
        return itemId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }
    public void setGoodsId(String goodsId){
        this.goodsId = goodsId;
    }

    public String getGoodsId(){
        return goodsId;
    }
    public void setSpuNo(String spuNo){
        this.spuNo = spuNo;
    }

    public String getSpuNo(){
        return spuNo;
    }
    public void setQuantity(Double quantity){
        this.quantity = quantity;
    }

    public Double getQuantity(){
        return quantity;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public Double getAmount(){
        return amount;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public Double getPrice(){
        return price;
    }
    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return comment;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
    public void setLastModifyTime(Date lastModifyTime){
        this.lastModifyTime = lastModifyTime;
    }

    public Date getLastModifyTime(){
        return lastModifyTime;
    }

    public Double getUnitId() {
        return unitId;
    }

    public void setUnitId(Double unitId) {
        this.unitId = unitId;
    }

    public Double getNowAmount() {
        return nowAmount;
    }

    public void setNowAmount(Double nowAmount) {
        this.nowAmount = nowAmount;
    }

    public Double getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(Double nowPrice) {
        this.nowPrice = nowPrice;
    }

}
