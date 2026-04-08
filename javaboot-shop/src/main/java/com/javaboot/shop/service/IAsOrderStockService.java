package com.javaboot.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboot.shop.domain.AsGoodsQuotation;
import com.javaboot.shop.domain.AsOrderStock;
import com.javaboot.shop.dto.AsOrderStockDTO;

import java.util.List;
import java.util.Map;

/**
 * 订单统计Service接口
 * 
 * @author javaboot
 * @date 2021-11-20
 */
public interface IAsOrderStockService extends IService<AsOrderStock>
{

    List<AsOrderStockDTO> selectAsOrderStockList(AsOrderStock asOrderStock);

    /**
     * 定时任务统计订单及库存
     * 
     * @param asOrderStock 订单统计
     * @return 结果
     */
    int insertAsOrderStock(AsOrderStockDTO asOrderStock);

    /**
     * 报表1-经营指标统计表
     * @param asOrderStock
     * @return
     */
    List<AsOrderStockDTO> deptDaySaleTotal(AsOrderStockDTO asOrderStock);

    /**
     * 报表2-销售计划
     * @param asOrderStock
     * @return
     */
    List<AsOrderStockDTO> deptMonthSaleTotal(AsOrderStockDTO asOrderStock);

    /**
     * 报表3-物料销售情况统计
     * @param asOrderStock
     * @return
     */
    List<Map<String, String>> spuDaySaleTotal(AsOrderStockDTO asOrderStock);

    /**
     * 报表7-物料统计
     * @param asOrderStock
     * @return
     */
    List<AsGoodsQuotation> calcAsGoodsQuotation(AsOrderStockDTO asOrderStock);

    /**
     * 报表8-订单金额统计
     * @param asOrderStock
     * @return
     */
    AsGoodsQuotation orderPayStat(AsOrderStockDTO asOrderStock);

    /**
     * 报表8-订单状态统计
     * @param asOrderStock
     * @return
     */
    AsGoodsQuotation orderStatusStat(AsOrderStockDTO asOrderStock);

    /**
     * 区域销售汇总
     * @param asOrderStock
     * @return
     */
    List<AsOrderStockDTO> deptSaleCalc(AsOrderStockDTO asOrderStock);

    /**
     * 客户销售统计
     * @param asOrderStock
     * @return
     */
    List<AsOrderStockDTO> mechantSaleCalc(AsOrderStockDTO asOrderStock);

}
