package com.javaboot.shop.domain;

import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 商品规格关联价格对象 store_spec_goods_price
 *
 * @author javaboot
 * @date 2019-08-26
 */
public class StoreSpecGoodsPrice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 规格商品id
     */
    private Integer itemId;

    /**
     * 商品id
     */
    @Excel(name = "商品id")
    private Integer goodsId;

    /**
     * 规格键名
     */
    @Excel(name = "规格键名")
    private String key;

    /**
     * 规格键名中文
     */
    @Excel(name = "规格键名中文")
    private String keyName;

    /**
     * 本店价
     */
    @Excel(name = "本店价")
    private Double price;

    /**
     * 市场价
     */
    @Excel(name = "市场价")
    private Double marketPrice;

    /**
     * 库存数量
     */
    @Excel(name = "库存数量")
    private Long storeCount;

    /**
     * 商品条形码
     */
    @Excel(name = "商品条形码")
    private String barCode;

    /**
     * SKU
     */
    @Excel(name = "SKU")
    private String sku;

    /**
     * 规格商品主图
     */
    @Excel(name = "规格商品主图")
    private String specImg;

    /**
     * 活动id
     */
    @Excel(name = "活动id")
    private Integer promId;

    /**
     * 参加活动类型
     */
    @Excel(name = "参加活动类型")
    private Integer promType;


    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setStoreCount(Long storeCount) {
        this.storeCount = storeCount;
    }

    public Long getStoreCount() {
        return storeCount;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }

    public void setSpecImg(String specImg) {
        this.specImg = specImg;
    }

    public String getSpecImg() {
        return specImg;
    }

    public void setPromId(Integer promId) {
        this.promId = promId;
    }

    public Integer getPromId() {
        return promId;
    }

    public void setPromType(Integer promType) {
        this.promType = promType;
    }

    public Integer getPromType() {
        return promType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("itemId", getItemId())
                .append("goodsId", getGoodsId())
                .append("key", getKey())
                .append("keyName", getKeyName())
                .append("price", getPrice())
                .append("marketPrice", getMarketPrice())
                .append("storeCount", getStoreCount())
                .append("barCode", getBarCode())
                .append("sku", getSku())
                .append("specImg", getSpecImg())
                .append("promId", getPromId())
                .append("promType", getPromType())
                .toString();
    }
}
