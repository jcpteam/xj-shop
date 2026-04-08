package com.javaboot.shop.dto;

import com.javaboot.common.annotation.Excel;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
public class StoreWarehouseInventoryExportDTO  {

    private Long stockId;

    @Excel(name = "盘点编号", width = 25)
    private String inventoryNo;

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

    @Excel(name = "盘库日期", width = 25, dateFormat = "yyyy-MM-dd")
    private Date intentoryDate;

    @Excel(name = "库存主单位数量")
    private String stockQuantity;

    @Excel(name = "库存副单位数量")
    private String stockLonelyQuantity;

    @Excel(name = "盘库主单位数量")
    private String inventoryQuantity;

    @Excel(name = "盘库副单位数量")
    private String inventoryLonelyQuantity;

    @Excel(name = "损益主单位数量")
    private String inventoryResult;

    @Excel(name = "盘库总数量")
    private String totalQuantity;

    @Excel(name = "备注")
    private String comment;

}
