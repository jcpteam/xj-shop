package com.javaboot.shop.dto;

import com.javaboot.common.annotation.Excel;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
public class StoreStockExportDTO
{

    private Long stockId;

    @Excel(name = "订单编号", width = 25)
    private String stockNo;

    @Excel(name = "商品SPU")
    private String spuNo;

    @Excel(name = "商品SPU名称", width = 25)
    private String spuName;

    private String warehouseId;

    @Excel(name = "仓库名称")
    private String warehouseName;

    private String deptId;

    @Excel(name = "所属区域")
    private String deptName;

    @Excel(name = "入库主单位数量")
    private String weight;

    private String mainUnit;

    @Excel(name = "主单位")
    private String mainUnitName;

    @Excel(name = "入库副单位数量")
    private String quantity;

    private String subUnit;

    @Excel(name = "副单位")
    private String subUnitName;

    @Excel(name = "入库价格")
    private String amount;

    /**
     * 税率
     */
    @Excel(name = "税率")
    private Double taxRate;

    /**
     * 无税金额
     */
    @Excel(name = "无税金额")
    private Double noTaxPrice;

    /**
     * 税额
     */
    @Excel(name = "税额")
    private Double tax;

    /**
     * 总金额
     */
    @Excel(name = "总金额")
    private Double totalPrice;

    @Excel(name = "入库日期", width = 25, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    @Excel(name = "采购调拨日期", width = 25, dateFormat = "yyyy-MM-dd")
    private Date purchaseDate;

    /**
     * 同步状态
     */
    @Excel(name = "同步状态")
    private String synStatus;

    /**
     * 状态: 商品备注
     */
    @Excel(name = "商品备注")
    private String comment;

    /**
     * 调整金额
     */
    @Excel(name = "调整金额")
    private Double adjustAmount;

    /**
     * 入库类别：1-正常入库,2-退货入库
     */
    @Excel(name = "入库类别")
    private String category;

    /**
     * 入库类别：1-正常入库,2-退货入库
     */
    @Excel(name = "供应商")
    private String supplierId;

}
