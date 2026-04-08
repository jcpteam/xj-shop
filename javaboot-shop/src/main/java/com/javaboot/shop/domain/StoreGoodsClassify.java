package com.javaboot.shop.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 商品分类对象 store_good_classify
 *
 * @author lqh
 * @date 2021-05-23
 */
@Data
@ToString
public class StoreGoodsClassify extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 分类编号
     */
    private String classifyId;
    @Excel(name = "上级Id")
    private String parentId;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String name;

    /**
     * 别称
     */
    @Excel(name = "别称")
    private String anotherName;

    /**
     * 级别
     */
    @Excel(name = "级别")
    private String level;

    /**
     * 数量单位
     */
    @Excel(name = "数量单位")
    private String unit;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Integer sort;

    /**
     * 状态:0删除,1正常
     */
    @Excel(name = "状态:0删除,1正常")
    private String status;

    /**
     * 最后更新时间
     */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    /**
     * 分类Ids
     */
    List<String> classifyIds;
}
