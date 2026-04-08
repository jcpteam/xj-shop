package com.javaboot.shop.dto;

import com.javaboot.common.annotation.Excel;
import com.javaboot.shop.domain.AsOrderStock;
import lombok.Data;

@Data
public class AsOrderStockDTO  extends AsOrderStock
{
    /**
     * 查询条件-日期
     */
    private String qryDate;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结算时间
     */
    private String endDate;

    /**
     * 部门名称
     */
    @Excel(name = "所属区域", sort = 1)
    private String deptName;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称", sort = 2)
    private String spuName;

    /**
     * 商户id
     */
    private String merchantId;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 在岗人数
     */
    private Double sortUserNum;

    /**
     * 人均分拣数
     */
    private Double sortAvgNum;

    /**
     * 月订单主单位总数
     */
    private Double monthOrderMainTotalQuantiy;

    /**
     * 月订单副单位总数
     */
    private Double monthOrderSubTotalQuantiy;

    /**
     * 月订单总金额
     */
    private Double monthOrderTotalPrice;


    /**
     * 月订单总金额
     */
    private Double allOrderTotalPrice;

    /**
     * 月库存主单位总数
     */
    private Double monthStockMainTotalQuanity;

    /**
     * 月库存副单位总数
     */
    private Double monthStockSubTotalQuanity;

    /**
     * 月库存总金额
     */
    private Double monthStockTotalPrice;

    /**
     * 盘存总数
     */
    private Double remainingQuantity;

    /**
     * 月盘存总数
     */
    private Double monthRemainingQuantity;

    /**
     * 月毛利
     */
    private Double monthGrossProfit;

}
