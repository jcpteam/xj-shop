package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;

/**
 * 留言反馈对象 store_feedback
 *
 * @author javaboot
 * @date 2020-03-21
 */
public class StoreFeedback extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 联系人
     */
    @Excel(name = "联系人")
    private String name;

    /**
     * 联系方式
     */
    @Excel(name = "联系方式")
    private String contact;

    /**
     * 留言内容
     */
    @Excel(name = "留言内容")
    private String content;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("contact", getContact())
                .append("content", getContent())
                .append("createTime", getCreateTime())
                .toString();
    }
}
