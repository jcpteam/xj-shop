package com.javaboot.shop.vo;

import com.javaboot.shop.domain.StoreGoodsOrderItem;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodsOrderItemVO
 * @Description 订单明细
 * @Date 2021/6/10 0010 21:30
 * @@author lqh
 */
@Data
public class StoreGoodsOrderItemVO extends StoreGoodsOrderItem {
    /**
     * 销售单位
     */
    private List<StoreGoodsSalesUnit> storeGoodsSalesUnitList;
}
