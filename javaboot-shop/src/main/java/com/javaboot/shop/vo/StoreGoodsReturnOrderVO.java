package com.javaboot.shop.vo;

import com.javaboot.common.annotation.Excel;
import com.javaboot.shop.domain.StoreGoodsReturnOrder;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodsReturnOrderVO
 * @Description 退货单
 * @Date 2021/6/26 0026 21:59
 * @@author lqh
 */
@Data
public class StoreGoodsReturnOrderVO extends StoreGoodsReturnOrder {
    /**
     * 下单客户
     */
    @Excel(name = "下单客户")
    private String merchantName;
    /**
     * 所属区域
     */
    @Excel(name = "所属区域")
    private String deptName;

    /**
     * 所属仓库
     */
    @Excel(name = "所属仓库")
    private String warehouseName;
    /**
     * 类型
     */
    @Excel(name = "类型")
    private String typeName;

    /**
     * 付款状态
     */
    @Excel(name = "付款状态")
    private String payStatusName;

    /**
     * 订单状态
     */
    @Excel(name = "订单状态")
    private String statusName;
    /** 仓库类型 */
    @Excel(name = "仓库类型")
    private String storeType;
    /**
     * 退货单商品
     */
    private List<StoreGoodsReturnOrderItemVO> itemList;

}
