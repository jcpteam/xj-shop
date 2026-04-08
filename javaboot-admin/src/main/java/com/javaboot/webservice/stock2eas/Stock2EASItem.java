package com.javaboot.webservice.stock2eas;

import lombok.Data;

@Data
public class Stock2EASItem
{
    /**
     * 序号
     */
    private String seq;

    /**
     * 商品SPU
     */
    private String material;

    /**
     * 数量
     */
    private Double qty;

    /**
     * 辅助数量
     */
    private Double assistQty;

    /**
     * 税率
     */
    private Double taxRate;


    /**
     * 含税单价
     */
    private Double taxPrice;



    /**
     * 含税金额
     */
    private Double taxAmount;

    /**
     * 税额
     */
    private Double tax;

    /**
     * 无税单价
     */
    private Double price;

    /**
     * 无税金额
     */
    private Double amount;

    /**
     * 仓库编码
     */
    private String warehouse;

    /**
     * 调拨金额
     */
    private Double adjustAmount;

    /**
     * 调拨单价
     */
    private Double adjustPrice;

    /**
     * 摘要
     */
    private String remark;

}
