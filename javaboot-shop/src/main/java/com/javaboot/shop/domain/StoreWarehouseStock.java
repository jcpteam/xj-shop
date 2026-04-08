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
 * 商品入库对象 store_warehouse_stock
 *
 * @author lqh
 * @date 2021-05-20
 */
@Data
@TableName("store_warehouse_stock")
@EqualsAndHashCode(callSuper = true)
public class StoreWarehouseStock extends BaseEntity {

    private static final long serialVersionUID = 2034429302767370147L;

    /** 入库id */
    @TableId(value="stock_id",type= IdType.AUTO)
    private Long stockId;

    /** 入库单编号 */
    @Excel(name = "入库单编号")
    @TableField("stock_no")
    private String stockNo;

    /** 仓库id */
    @Excel(name = "仓库id")
    @TableField("warehouse_id")
    private Long warehouseId;

    /** 供应商id */
    @Excel(name = "供应商id")
    @TableField("supplier_id")
    private String supplierId;

    /** 区域id */
    @Excel(name = "区域id")
    @TableField("dept_id")
    private String deptId;

    /** 仓库类别1-商品 2-物料 */
    @Excel(name = "仓库类别1-商品 2-物料")
    @TableField("stock_type")
    private String stockType;
    /**
     * 状态:0-删除，1-正常
     */
    @Excel(name = "状态:0-删除，1-正常")
    @TableField("status")
    private String status;

    /**
     * 状态:0-待同步，1-已同步 2-同步失败
     */
    @Excel(name = "状态:0-删除，1-正常")
    @TableField("syn_status")
    private String synStatus;

    /**
     * 入库类别：1-正常入库,2-退货入库
     */
    @Excel(name = "入库类别：1-正常入库,2-退货入库,3-EAS入库,4-采购单入库,5-调拨单入库")
    @TableField("category")
    private String category;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;

    /** 数据库入库时间 */
    @Excel(name = "数据库入库时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("insert_time")
    private Date insertTime;


    /** 数据库入库时间 */
    @Excel(name = "采购时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("purchase_date")
    private Date purchaseDate;


    /**
     * 入库类别：1-正常入库,2-退货入库
     */
    @Excel(name = "0-待仓库管理员确认;1-仓库管理员审核通过;2-财务审核通过")
    @TableField("audit_status")
    private String auditStatus;

    /**
     * 采购单id
     */
    @Excel(name = "采购单id")
    @TableField("object_id")
    private String objectId;

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
