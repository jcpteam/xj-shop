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
 * 销售单位对象 store_goods_sales_unit
 * 
 * @author lqh
 * @date 2021-05-29
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsSalesUnit extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 单位id */
    private Long unitId;

    /** 规格名称 */
    @Excel(name = "销售单位")
    private String name;

    /** 结算单位 */
    @Excel(name = "结算单位")
    private String settlementUnit;

    /** 换算关系 */
    @Excel(name = "换算关系")
    private Double conversionRelationship;

    /** 排序 */
    @Excel(name = "排序")
    private Integer orderNum;

    /** 状态：0-删除,1-启用,2-禁用 */
    @Excel(name = "状态：0-删除,1-启用,2-禁用")
    private String status;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;
    /**
     * 单位ids
     */
    private List unitIds;
}
