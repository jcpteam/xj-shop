package com.javaboot.shop.domain;

import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 规格项对象 store_spec_item
 *
 * @author javaboot
 * @date 2019-08-25
 */
public class StoreSpecItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 规格项id
     */
    private Integer id;

    /**
     * 规格id
     */
    @Excel(name = "规格id")
    private Integer specId;

    /**
     * 规格项
     */
    @Excel(name = "规格项")
    private String item;

    private Boolean selected;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setSpecId(Integer specId) {
        this.specId = specId;
    }

    public Integer getSpecId() {
        return specId;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("specId", getSpecId())
                .append("item", getItem())
                .toString();
    }
}
