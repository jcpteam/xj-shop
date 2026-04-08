package com.javaboot.shop.vo;

import com.javaboot.shop.domain.StoreGoodsReturnOrderItem;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodsReturnOrderItemVO
 * @Description 退货单明细
 * @Date 2021/6/26 0026 22:01
 * @@author lqh
 */
@Data
public class StoreGoodsReturnOrderItemVO extends StoreGoodsReturnOrderItem {
    /**
     * 销售单位
     */
    private List<StoreGoodsSalesUnit> storeGoodsSalesUnitList;
}
