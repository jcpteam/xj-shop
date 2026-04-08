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
import java.util.List;

/**
 * 商品入库明细对象 store_warehouse_stock_item
 *
 * @author lqh
 * @date 2021-05-23
 */
@Data
@TableName("store_warehouse_stock_item")
@EqualsAndHashCode(callSuper = true)
public class StoreWarehouseStockItem extends BaseEntity {

    /**
     * 入库明细id
     */
    @TableId(value = "item_id", type = IdType.AUTO)
    private Long itemId;

    /**
     * 入库id
     */
    @TableField(value = "stock_id")
    private Long stockId;

    /**
     * 商品SPU
     */
    @Excel(name = "商品SPU")
    @TableField("spu_no")
    private String spuNo;

    /**
     * 仓库id
     */
    @Excel(name = "仓库id")
    @TableField("warehouse_id")
    private Long warehouseId;


    /**
     * 入库金额
     */
    @Excel(name = "入库金额")
    @TableField("amount")
    private Double amount;


    /**
     * 最后更新时间
     */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;


    /**
     * 库存数量
     */
    @Excel(name = "库存数量")
    @TableField("quantity")
    private Double quantity;

    /**
     * 库存只数
     */
    @Excel(name = "库存只数")
    @TableField("stocks_number")
    private Double stocksNumber;

    /**
     * 库存数量
     */
    @Excel(name = "实际库存数量")
    @TableField("real_quantity")
    private Double realQuantity;

    /**
     * 库存只数
     */
    @Excel(name = "实际库存只数")
    @TableField("real_stocks_number")
    private Double realStocksNumber;

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
     * 重量单位
     */
    @Excel(name = "重量单位")
    @TableField("weight_unit")
    private String weightUnit;

    /** 区域id */
    @TableField(exist = false)
    private String deptId;


    /** 开始时间 */
    @TableField(exist = false)
    private String startTime;

    /** 结束时间 */
    @TableField(exist = false)
    private String endTime;

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

    @TableField("object_json")
    private String objectJson;

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

    @TableField(exist = false)
    private List<Long> stockIds;

    @TableField(exist = false)
    private String startDate;
    @TableField(exist = false)
    private String endDate;
    @TableField(exist = false)
    private String createDay;
    @TableField(exist = false)
    private String category;
}
