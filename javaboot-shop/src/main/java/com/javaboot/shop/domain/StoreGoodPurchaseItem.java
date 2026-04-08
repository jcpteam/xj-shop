package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 商品采购明细对象 store_good_purchase_item
 * 
 * @author lqh
 * @date 2021-05-23
 */
@Data
@TableName("store_good_purchase_item")
@EqualsAndHashCode(callSuper = true)
public class StoreGoodPurchaseItem extends BaseEntity{

    private static final long serialVersionUID = -2981000726573414419L;

    /** 采购明细id */
    @TableId(value="item_id",type= IdType.AUTO)
    private Long itemId;

    /** 采购id */
    @Excel(name = "采购id")
    @TableField("purchase_id")
    private Long purchaseId;

    /** 区域id */
    @TableField(exist = false)
    private String deptId;

    /** 商品SPU */
    @Excel(name = "商品SPU")
    @TableField("spu_no")
    private String spuNo;

    /** 采购数量 */
    @Excel(name = "采购数量")
    @TableField("quantity")
    private Double quantity;


    /** 库存只数 */
    @Excel(name = "采购只数")
    private Double stocksNumber;

    /** 重量单位 */
    @Excel(name = "重量单位")
    private String weightUnit;

    /**
     * 数量单位
     */
    @Excel(name = "数量单位")
    @TableField("quantity_unit")
    private String quantityUnit;

    /**
     * 状态:0-删除，1-正常
     */
    @Excel(name = "状态:0-删除，1-正常")
    @TableField("status")
    private String status;

    /**
     * 单价
     */
    @Excel(name = "单价")
    @TableField("price")
    private Double price;


    /**
     * 税率
     */
    @Excel(name = "税率")
    @TableField("tax_rate")
    private Double taxRate;

    /**
     * 无税金额
     */
    @Excel(name = "无税金额")
    @TableField("no_tax_price")
    private Double noTaxPrice;

    /**
     * 税额
     */
    @Excel(name = "税额")
    @TableField("tax")
    private Double tax;

    /**
     * 总金额
     */
    @Excel(name = "总金额")
    @TableField("total_price")
    private Double totalPrice;


    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;

    /** 开始时间 */
    @TableField(exist = false)
    private String startTime;


    /** 结束时间 */
    @TableField(exist = false)
    private String endTime;

    /**
     * 状态: 商品备注
     */
    @Excel(name = "商品备注")
    @TableField("comment")
    private String comment;

    /**
     * 调整金额
     */
    @Excel(name = "调整金额")
    @TableField("adjust_amount")
    private Double adjustAmount;

}
