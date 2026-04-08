package com.javaboot.webservice.stock;

import lombok.Data;

@Data
public class EASStockInfo
{

//    "supplierName": "湖南润乐食品有限公司(控股)",
//        "adminName": "长沙大客户部",
//        "bizDate": "2021-07-31",
//        "supplierNumber": "00000008",
//        "adminNumber": "041201",
//        "rn": "45",
//        "billNumber": "CR041201210731005",
//        "paymentType": "赊购"

    /**
     * 入库单id
     */
    private String billNumber;

    /**
     * 入库单明细id
     */
    private String serialNumber;

    /**
     * 供应商id
     */
    private String supplierNumber;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     *  采购入库单业务日期
     */
    private String bizDate;

    /**
     *  采购订单日期
     */
    private String purOrderDate;

    /**
     * 入库日期
     */
    private String inWarehsDate;

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




}
