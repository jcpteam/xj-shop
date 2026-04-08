package com.javaboot.shop.domain;

import com.javaboot.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 商品源码版本 store_source
 *
 * @author javaboot
 * @date 2019-08-25
 */
public class StoreSource extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;
    /**
     * 版本
     */
    private String version;
    /**
     * 下载地址
     */
    private String downUrl;
    /**
     * 商品
     */
    private Integer goodsId;
    private String goodsName;
    /**
     * 规格
     */
    private Integer specId;
    private String specName;
    /**
     * 规格项
     */
    private Integer specItemId;
    private String specItemName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSpecId() {
        return specId;
    }

    public void setSpecId(Integer specId) {
        this.specId = specId;
    }

    public Integer getSpecItemId() {
        return specItemId;
    }

    public void setSpecItemId(Integer specItemId) {
        this.specItemId = specItemId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecItemName() {
        return specItemName;
    }

    public void setSpecItemName(String specItemName) {
        this.specItemName = specItemName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("version", version)
                .append("downUrl", downUrl)
                .append("goodsId", goodsId)
                .append("specId", specId)
                .append("specItemId", specItemId)
                .toString();
    }
}
