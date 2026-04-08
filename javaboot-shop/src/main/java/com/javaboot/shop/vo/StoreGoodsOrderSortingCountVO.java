package com.javaboot.shop.vo;

import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 订单分拣数量对象
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsOrderSortingCountVO extends BaseEntity {
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单商品数量
     */
    private Integer orderGoodCount;

    /**
     * 订单商品已分拣数量
     */
    private Integer orderGoodSortCount;
}
