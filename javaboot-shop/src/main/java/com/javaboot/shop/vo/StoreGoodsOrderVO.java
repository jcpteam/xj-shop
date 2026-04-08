package com.javaboot.shop.vo;

import com.javaboot.common.annotation.Excel;
import com.javaboot.shop.domain.StoreGoodsOrder;
import com.javaboot.shop.domain.StoreGoodsOrderItem;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Classname StoreGoodsOrderVO
 * @Description 订单
 * @Date 2021/6/2 0002 16:15
 * @@author lqh
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsOrderVO extends StoreGoodsOrder {

    /**
     * 下单客户
     */
    @Excel(name = "下单客户")
    private String merchantName;
    /**
     * 结算单位
     */
    @Excel(name = "结算单位")
    private String superMerchantName;
    /**
     * 所属区域
     */
    @Excel(name = "所属区域")
    private String deptName;
    /**
     * 订单来源
     */
    @Excel(name = "订单来源")
    private String sourceName;

    /**
     * 打印状态
     */
    @Excel(name = "打印状态")
    private String printStatusName;

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
    /**
     * 交货日期
     */
    private String deliveryDateText;
    /**
     * 订单商品
     */
    private List<StoreGoodsOrderItemVO> itemList;

    /**
     * 订单商品数量
     */
    private Integer orderGoodCount;

    /**
     * 订单商品已分拣数量
     */
    private Integer orderGoodSortCount;

    /**
     * 订单对应的商品列表
     */
    private List<StoreGoodsOrderItem> goodsOrderItems;

    //订单分拣材料列表
    private List<StoreGoodsOrderSortingItemVO> sortingMaterialItemVOS;

    //商户出库单打印模板类型
    private String printType;

    private String deliveryType;

    //商户手机号
    private String contactName;
    private String contactPhone;
    private String contactAddress;

    /** 业务员名称信息 */
    private String opmanagerId;
    private String opmanagerName;
    private String opmanagerPhone;
}
