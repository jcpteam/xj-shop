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
 * 商品采购对象 store_good_purchase
 *
 * @author lqh
 * @date 2021-05-23
 */
@Data
@TableName("store_good_purchase")
@EqualsAndHashCode(callSuper = true)
public class StoreGoodPurchase extends BaseEntity {

    private static final long serialVersionUID = 2371456479855383284L;

    /** 采购id */
    @TableId(value="purchase_id",type= IdType.AUTO)
    private Long purchaseId;

    /** 采购单编号 */
    @Excel(name = "采购单编号")
    @TableField("purchase_no")
    private String purchaseNo;

    /** 区域id */
    @Excel(name = "区域id")
    @TableField("dept_id")
    private String deptId;

    /** 区域id */
    @Excel(name = "采购日期")
    @TableField("purchase_date")
    private Date purchaseDate;

    /** 区域id */
    @Excel(name = "仓库id")
    @TableField("warehouse_id")
    private Long warehouseId;

    /** 区域id */
    @Excel(name = "供应商id")
    @TableField("supplier_id")
    private String supplierId;

    /**
     * 状态:0-删除，1-正常
     */
    @Excel(name = "状态:0-删除，1-正常")
    @TableField("status")
    private String status;

    /**
     * 状态:0-删除，1-正常
     */
    @Excel(name = "入库单状态:1-未生成，1-已生成")
    @TableField("stock_status")
    private String stockStatus;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;

    /**
     * 商品备注
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
