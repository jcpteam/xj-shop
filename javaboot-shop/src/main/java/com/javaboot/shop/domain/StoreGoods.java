package com.javaboot.shop.domain;

import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 商品主对象 store_goods
 *
 * @author javaboot
 * @date 2019-08-25
 */
public class StoreGoods extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品编号
     */
    @Excel(name = "商品编号")
    private String goodsSn;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称")
    private String goodsName;

    /**
     * 市场价
     */
    @Excel(name = "市场价(零售价)")
    private Double marketPrice;

    /**
     * 本店价
     */
    @Excel(name = "本店价(供货价)")
    private Double shopPrice;

    /**
     * 商品简单描述
     */
    private String goodsRemark;

    /**
     * 商品详细描述
     */
    private String goodsContent;

    /**
     * 商品上传原始图(主图)
     */
    private String goodsLogo;

    /**
     * 是否上架 0-未上架 1-已上架
     */
    @Excel(name = "是否上架 0-未上架 1-已上架")
    private Integer isOnSale;

    /**
     * 商品排序
     */
    @Excel(name = "商品排序")
    private Integer sort;

    /**
     * 是否推荐0-未推荐 1-已推荐
     */
    @Excel(name = "是否推荐0-未推荐 1-已推荐")
    private Integer isRecommend;

    /**
     * 是否新品0-不是 1-是
     */
    @Excel(name = "是否新品0-不是 1-是")
    private Integer isNew;

    /**
     * 商品所属类型id，取值表goods_type的id
     */
    private Integer goodsType;

    /**
     * 商品规格类型，取值表goods_type的id
     */
    private Integer specType;

    /**
     * 演示地址
     */
    private String demoUrl;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Double getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(Double shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getGoodsRemark() {
        return goodsRemark;
    }

    public void setGoodsRemark(String goodsRemark) {
        this.goodsRemark = goodsRemark;
    }

    public String getGoodsContent() {
        return goodsContent;
    }

    public void setGoodsContent(String goodsContent) {
        this.goodsContent = goodsContent;
    }

    public String getGoodsLogo() {
        return goodsLogo;
    }

    public void setGoodsLogo(String goodsLogo) {
        this.goodsLogo = goodsLogo;
    }

    public Integer getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(Integer isOnSale) {
        this.isOnSale = isOnSale;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getSpecType() {
        return specType;
    }

    public void setSpecType(Integer specType) {
        this.specType = specType;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public void setDemoUrl(String demoUrl) {
        this.demoUrl = demoUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("goodsId", goodsId)
                .append("goodsSn", goodsSn)
                .append("goodsName", goodsName)
                .append("marketPrice", marketPrice)
                .append("shopPrice", shopPrice)
                .append("goodsRemark", goodsRemark)
                .append("goodsContent", goodsContent)
                .append("goodsLogo", goodsLogo)
                .append("isOnSale", isOnSale)
                .append("sort", sort)
                .append("isRecommend", isRecommend)
                .append("isNew", isNew)
                .append("goodsType", goodsType)
                .append("specType", specType)
                .append("demoUrl", demoUrl)
                .toString();
    }
}
