package com.javaboot.shop.dto;

import com.javaboot.common.annotation.Excel;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
public class StoreGoodsAdjustExportDTO
{

    @Excel(name = "调拨单编号", width = 40)
    private String code;

    @Excel(name = "商品SPU", width = 20)
    private String spuNo;

    @Excel(name = "商品名称", width = 20)
    private String spuName;

    private String deptId;

    @Excel(name = "所属区域")
    private String deptName;

    @Excel(name = "重量")
    private String unitNumber;

    @Excel(name = "数量")
    private String adjustNumber;

    @Excel(name = "金额")
    private String amount;

    private String sourceWarehouseId;

    @Excel(name = "源仓库")
    private String sourceWarehouseName;

    private String targetWarehouseId;

    @Excel(name = "目标仓库")
    private String targetWarehouseName;

    @Excel(name = "入库单状态")
    private String stockStatus;

    @Excel(name = "调拨日期", width = 25, dateFormat = "yyyy-MM-dd")
    private Date adjustTime;

    @Excel(name = "创建日期", width = 25, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
