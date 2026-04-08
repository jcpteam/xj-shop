package com.javaboot.shop.mapper;

import com.javaboot.common.core.dao.BaseMapper;
import com.javaboot.shop.domain.AsGoodsQuotation;
import com.javaboot.shop.domain.AsOrderStock;
import com.javaboot.shop.dto.AsOrderStockDTO;

import java.util.List;

/**
 * 订单统计Mapper接口
 * 
 * @author javaboot
 * @date 2021-11-20
 */
public interface AsOrderStockMapper extends BaseMapper<AsOrderStock>
{

    List<AsOrderStockDTO> selectAsOrderStockList(AsOrderStock asOrderStock);

    List<AsOrderStockDTO> selectDeptDaySale(AsOrderStockDTO dto);

    List<AsOrderStockDTO> selectDeptMonthSale(AsOrderStockDTO dto);

    List<AsOrderStockDTO> selectDeptMonthGoodsSale(AsOrderStockDTO dto);

    List<AsOrderStockDTO> selectTableHeader(AsOrderStockDTO dto);

    List<AsOrderStockDTO> selectSpuDaySale(AsOrderStockDTO dto);

    List<AsGoodsQuotation> selectAsGoodsQuotation(AsOrderStockDTO dto);

    AsGoodsQuotation selectOrderPayStat(AsOrderStockDTO dto);

    AsGoodsQuotation selectOrderStatusStat(AsOrderStockDTO dto);

    List<AsOrderStockDTO> selectMechantOrderStat(AsOrderStockDTO dto);


}
