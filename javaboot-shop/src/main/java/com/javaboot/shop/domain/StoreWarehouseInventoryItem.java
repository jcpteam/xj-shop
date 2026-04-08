package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品盘库明细对象 store_warehouse_inventory_item
 *
 * @author lqh
 * @date 2021-05-23
 */
@Data
@TableName("store_warehouse_inventory_item")
@EqualsAndHashCode(callSuper = true)
public class StoreWarehouseInventoryItem extends BaseEntity{

    /** 明细id */
    @TableId(value="item_id",type= IdType.AUTO)
    private Long itemId;

    /** 盘库id */
    @Excel(name = "盘库id")
    @TableField("inventory_id")
    private Long inventoryId;

    /** 商品SPU */
    @Excel(name = "商品SPU")
    @TableField("spu_no")
    private String spuNo;

    /** 商品SPU名称  */
    @Excel(name = "商品SPU名称")
    @TableField(exist = false)
    private String spuName;

    /** 商品税率  */
    @Excel(name = "商品税率")
    @TableField(exist = false)
    private Double taxRate;

    /** 变动类型:0-报损,1-报溢 */
    @Excel(name = "变动类型:0-报损,1-报溢")
    @TableField("change_type")
    private String changeType;

    /** 单位 */
    @Excel(name = "单位")
    @TableField("unit")
    private String unit;


    /** 单位名称  */
    @Excel(name = "单位")
    @TableField(exist = false)
    private String unitName;

    /** 副单位 */
    @Excel(name = "副单位")
    @TableField(exist = false)
    private String subUnit;


    /** 副单位名称  */
    @Excel(name = "副单位名称")
    @TableField(exist = false)
    private String subUnitName;

    /** 单位类型  */
    @Excel(name = "单位类型1-主单位 2-副单位")
    @TableField(exist = false)
    private String unitType;

    /** 库存只数 */
    @Excel(name = "库存只数")
    @TableField("stock_lonely_quantity")
    private Double stockLonelyQuantity;

    /** 库存数量 */
    @Excel(name = "库存数量")
    @TableField("stock_quantity")
    private Double stockQuantity;

    /** 盘点数量 */
    @Excel(name = "盘点数量")
    @TableField("inventory_quantity")
    private Double inventoryQuantity;
    /** 盘点只数 */
    @Excel(name = "盘点只数")
    @TableField("inventory_lonely_quantity")
    private Double inventoryLonelyQuantity;
    /** 初始库存成本 */
    @Excel(name = "初始库存成本")
    @TableField("initial_inventory_cost")
    private Double initialInventoryCost;

    /** 入库金额 */
    @Excel(name = "入库金额")
    @TableField("storage_amount")
    private Double storageAmount;

    /** 盘点状态:0-盘亏,1-盘赢 */
    @Excel(name = "盘点状态:0-盘亏,1-盘赢")
    @TableField("inventory_status")
    private String inventoryStatus;

    /** 盘点结果 */
    @Excel(name = "盘点结果")
    @TableField("inventory_result")
    private Double inventoryResult;

    /** 销售数量 */
    @Excel(name = "销售数量")
    @TableField("sales_quantity")
    private Double salesQuantity;

    /** 销售只数 */
    @Excel(name = "销售只数")
    @TableField("sales_lonely_quantity")
    private Double salesLonelyQuantity;

    /** 销售金额 */
    @Excel(name = "销售金额")
    @TableField("sales_amount")
    private Double salesAmount;

    /** 剩余数量 */
    @Excel(name = "剩余数量")
    @TableField("remaining_quantity")
    private Double remainingQuantity;

    /** 状态:0-删除 1-正常 */
    @Excel(name = "状态:0-删除 1-正常")
    @TableField("status")
    private String status;

    /** 盘点后单价 */
    @Excel(name = "盘点后单价")
    @TableField("price")
    private Double price;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;

    /** 备注 */
    @Excel(name = "备注")
    @TableField("comment")
    private String comment;



}
