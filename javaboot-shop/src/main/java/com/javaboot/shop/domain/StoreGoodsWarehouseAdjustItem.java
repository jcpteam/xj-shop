package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;
import java.util.List;

/**
 * 商品调拨明细对象 store_goods_warehouse_adjust_item
 * 
 * @author lqh
 * @date 2021-06-10
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsWarehouseAdjustItem extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 明细id */
    private Long itemId;

    /** 调拨id */
    @Excel(name = "调拨id")
    private Long adjustId;

    /** 商品SPU */
    @Excel(name = "商品SPU")
    private String spuNo;

    /** 调拨数量 */
    @Excel(name = "调拨数量")
    private Double adjustNumber;

    /** 单位数量 */
    @Excel(name = "单位数量")
    private Double unitNumber;

    /** 金额 */
    @Excel(name = "金额")
    private Double amount;

    /** 数量单位 */
    @Excel(name = "数量单位")
    private String quantityUnit;

    /** 重量单位 */
    @Excel(name = "重量单位")
    private String weightUnit;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    /**
     * 状态:0-删除，1-正常
     */
    @Excel(name = "状态:0-删除，1-正常")
    @TableField("status")
    private String status;

    /**
     * 状态: 商品备注
     */
    @Excel(name = "商品备注")
    @TableField("comment")
    private String comment;

    @TableField(exist = false)
    private List<Long> adjustIds;

}
