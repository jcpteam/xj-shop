package com.javaboot.shop.domain;

import lombok.Data;

@Data
public class AsGoodsQuotation
{
    /**
     * 报价单数量
     */
    private Integer qNum;

    /**
     * 在售数量
     */
    private Integer goodsNum;

    /**
     * 正常数量
     */
    private Integer normalNum;

    /**
     * 区域id
     */
    private String deptId;

    /**
     * 区域名称
     */
    private String deptName;

    /**
     * 商品spu
     */
    private String spuNo;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 正常数量
     */
    private Integer finishNum;

    /**
     * 未支付金额
     */
    private Double noPayAmount;

    /**
     * 已支付金额
     */
    private Double payAmount;

}
