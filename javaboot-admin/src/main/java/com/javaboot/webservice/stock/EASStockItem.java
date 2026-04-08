package com.javaboot.webservice.stock;

import lombok.Data;

@Data
public class EASStockItem
{
    /***
     *      "supplierName": "湘佳屠宰部（新）",
     * 		"serialNumber": "4",
     * 		"bizDate": "2021-07-31",
     * 		"supplierNumber": "01000019",
     * 		"baseQty": "71.2",
     * 		"baseUnitName": "公斤",
     * 		"warehouseName": "上海大仓",
     * 		"paymentType": "赊购",
     * 		"adminName": "上海业务区",
     * 		"materialName": "土黑公鸡（已开膛）",
     * 		"warehouseNumber": "0409",
     * 		"materialNumber": "050401010008",
     * 		"assistQty": "40",
     * 		"adminNumber": "040803",
     * 		"taxPrice": "14.5",
     * 		"rn": "201",
     * 		"billNumber": "CR040803210731001",
     * 		"taxAmount": "1032.4",
     * 		"assistUnitName": "只"
     *
     */
//
//    /**
//     * 入库单id
//     */
//    private String billNumber;
//
//    /**
//     * 入库单明细id
//     */
//    private String serialNumber;
//
//    /**
//     * 供应商id
//     */
//    private String supplierNumber;
//
//    /**
//     * 供应商名称
//     */
//    private String supplierName;
//
//    /**
//     * 入库日期
//     */
//    private String bizDate;

    /**
     * 仓库编号
     */
    private String warehouseNumber;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 付款方式编码：001：现销 002：赊销
     */
    private String paymentType;

    /**
     * 区域id
     */
    private String adminNumber;

    /**
     * 区域名称
     */
    private String adminName;

    /**
     * 商品SPU
     */
    private String materialNumber;

    /**
     * 商品名
     */
    private String materialName;

    /**
     * 主单位数量
     */
    private Double baseQty;

    /**
     * 主单位名称
     */
    private String baseUnitName;

    /**
     * 副单位数量
     */
    private Double assistQty;

    /**
     * 副单位名称
     */
    private String assistUnitName;

    /**
     * 单价
     */
    private Double taxPrice;

    /**
     * 总价
     */
    private Double taxAmount;

}
