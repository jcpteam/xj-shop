package com.javaboot.shop.dto;

import com.javaboot.common.annotation.Excel;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
public class StoreGoodsPurchaseExportDTO
{

    @Excel(name = "采购单编号", width = 40)
    private String code;

    @Excel(name = "商品SPU", width = 20)
    private String spuNo;

    @Excel(name = "商品名称", width = 20)
    private String spuName;

    private String deptId;

    @Excel(name = "所属区域")
    private String deptName;

    @Excel(name = "重量")
    private String weight;

    @Excel(name = "数量")
    private String quantity;

    @Excel(name = "含税单价")
    private String price;

    @Excel(name = "税率")
    private String taxRate;

    @Excel(name = "无税金额")
    private String noTaxPrice;

    @Excel(name = "税额")
    private String tax;

    @Excel(name = "税价合计")
    private String totalPrice;

    @Excel(name = "入库单状态")
    private String stockStatus;

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
    
    @Excel(name = "创建日期", width = 25, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
