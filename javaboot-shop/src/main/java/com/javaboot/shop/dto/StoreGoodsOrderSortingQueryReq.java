package com.javaboot.shop.dto;

import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class StoreGoodsOrderSortingQueryReq extends BaseEntity {
    /**
     * 订单信息关键字
     */
    private Long orderId;

    private String deptId;
}
