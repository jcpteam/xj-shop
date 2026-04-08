package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;
import java.util.List;

/**
 * 订单统计对象 as_order_stock
 * 
 * @author javaboot
 * @date 2021-11-20
 */
@Data
@TableName("as_order_stock")
@EqualsAndHashCode(callSuper = true)
public class AsOrderStock extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 商品spu */
    @TableField("spu_no")
    private String spuNo;

    /** 区域id */
    @TableField("dept_id")
    private String deptId;

    /** 订单主单位数量 */
    @TableField("order_main_total_quantiy")
    @Excel(name = "订单主单位数量", sort = 4, cellType = Excel.ColumnType.NUMERIC)
    private Double orderMainTotalQuantiy;

    /** 订单副单位数量 */
    @TableField("order_sub_total_quantiy")
    @Excel(name = "订单副单位数量", sort = 5, cellType = Excel.ColumnType.NUMERIC)
    private Double orderSubTotalQuantiy;

    /** 订单总金额 */
    @TableField("order_total_price")
    @Excel(name = "订单总金额", sort = 6, cellType = Excel.ColumnType.NUMERIC)
    private Double orderTotalPrice;

    /** 订单平均单价 */
    @TableField("order_avg_price")
    @Excel(name = "订单单价", sort = 7, cellType = Excel.ColumnType.NUMERIC)
    private Double orderAvgPrice;

    /** 入库单主单位数量 */
    @TableField("stock_main_total_quanity")
    @Excel(name = "入库单主单位数量", sort = 8, cellType = Excel.ColumnType.NUMERIC)
    private Double stockMainTotalQuanity;

    /** 入库单副单位数量 */
    @TableField("stock_sub_total_quanity")
    @Excel(name = "入库单副单位数量", sort = 9, cellType = Excel.ColumnType.NUMERIC)
    private Double stockSubTotalQuanity;

    /** 入库单总金额 */
    @TableField("stock_total_price")
    @Excel(name = "库存总额", sort = 10, cellType = Excel.ColumnType.NUMERIC)
    private Double stockTotalPrice;

    /** 入库单平均金额 */
    @TableField("stock_avg_price")
    @Excel(name = "库存单价", sort = 11, cellType = Excel.ColumnType.NUMERIC)
    private Double stockAvgPrice;


    /** 盘库单主单位数量（结存） */
    @TableField("inventory_main_total_quanity")
    @Excel(name = "结存主单位数量", sort = 12, cellType = Excel.ColumnType.NUMERIC)
    private Double inventoryMainTotalQuanity;

    /** 盘库单副单位数量（结存） */
    @TableField("inventory_sub_total_quanity")
    @Excel(name = "结存副单位数量", sort = 13, cellType = Excel.ColumnType.NUMERIC)
    private Double inventorySubTotalQuanity;

    /** 盘库单主单位数量 */
    @TableField("inventory_today_main_total_quanity")
    @Excel(name = "盘库单主单位数量", sort = 14, cellType = Excel.ColumnType.NUMERIC)
    private Double inventoryTodayMainTotalQuanity;

    /** 盘库单副单位数量 */
    @TableField("inventory_today_sub_total_quanity")
    @Excel(name = "盘库单副单位数量", sort = 15, cellType = Excel.ColumnType.NUMERIC)
    private Double inventoryTodaySubTotalQuanity;

    /** 盘存金额 = 盘存数量 * 每日成本价格*/
    @TableField("inventory_today_price")
    @Excel(name = "盘存金额", sort = 16, cellType = Excel.ColumnType.NUMERIC)
    private Double inventoryTodayPrice;

    /** 损益数量（主） */
    @Excel(name = "损益数量（主）", sort = 19, cellType = Excel.ColumnType.NUMERIC)
    @TableField("inventory_result")
    private Double inventoryResult;

    /** 损益金额*/
    @Excel(name = "损益金额", sort = 20, cellType = Excel.ColumnType.NUMERIC)
    @TableField("inventory_result_price")
    private Double inventoryResultPrice;

    /** 统计日期 */
    // @Excel(name = "统计日期", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("stat_date")
    private Date statDate;

    /** 财务日期 */
    @Excel(name = "收货日期", width = 30, dateFormat = "yyyy-MM-dd", sort = 3)
    @TableField("delivery_date")
    private Date deliveryDate;

    /**
     * 日毛利
     */
    @TableField(exist = false)
    @Excel(name = "毛利", sort = 17, cellType = Excel.ColumnType.NUMERIC)
    private Double grossProfit;

    /** 每日平均成本 */
    @TableField("cost_price")
    @Excel(name = "每日成本价", sort = 18, cellType = Excel.ColumnType.NUMERIC)
    private Double costPrice;

    @TableField(exist = false)
    private List<String> spuNos;
}
