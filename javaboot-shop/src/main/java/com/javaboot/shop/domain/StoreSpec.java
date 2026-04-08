package com.javaboot.shop.domain;

import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 商品规格(独立)对象 store_spec
 *
 * @author javaboot
 * @date 2019-08-25
 */
public class StoreSpec extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 规格表
     */
    private Integer id;

    /**
     * 规格类型
     */
    private Integer typeId;

    /**
     * 规格类型名称
     */
    @Excel(name = "规格类型")
    private String typeName;

    /**
     * 规格名称
     */
    @Excel(name = "规格名称")
    private String name;

    /**
     * 规格项
     */
    private String specItem;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否需要检索：1是，0否
     */
    private Integer searchIndex;

    private List<StoreSpecItem> storeSpecItems;

    public List<StoreSpecItem> getStoreSpecItems() {
        return storeSpecItems;
    }

    public void setStoreSpecItems(List<StoreSpecItem> storeSpecItems) {
        this.storeSpecItems = storeSpecItems;
    }

    public String getSpecItem() {
        return specItem;
    }

    public void setSpecItem(String specItem) {
        this.specItem = specItem;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSearchIndex(Integer searchIndex) {
        this.searchIndex = searchIndex;
    }

    public Integer getSearchIndex() {
        return searchIndex;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("typeId", getTypeId())
                .append("name", getName())
                .append("specItem", getSpecItem())
                .append("sort", getSort())
                .append("searchIndex", getSearchIndex())
                .toString();
    }
}
