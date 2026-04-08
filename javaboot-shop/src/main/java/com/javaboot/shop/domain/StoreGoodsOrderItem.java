package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 订单明细对象 store_goods_order_item
 *
 * @author lqh
 * @date 2021-06-01
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsOrderItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 明细id
     */
    private Long itemId;

    /**
     * 订单id
     */
    @Excel(name = "订单id")
    private Long orderId;

    /**
     * 商品id
     */
    @Excel(name = "商品id")
    private Long goodsId;
    /**
     * 商品spu
     */
    @Excel(name = "商品spu")
    private String spuNo;
    /**
     * 商品数量
     */
    @Excel(name = "商品数量")
    private Double quantity;

    /**
     * 商品金额
     */
    @Excel(name = "商品金额")
    private Double amount;

    /**
     * 商品金额
     */
    @Excel(name = "分拣后的商品实际金额")
    private Double sortingPrice;

    /**
     * 商品价格
     */
    @Excel(name = "商品价格")
    private Double price;

    /**
     * 商品折扣
     */
    @Excel(name = "商品折扣")
    private Double discount;

    /**
     * 商品备注
     */
    @Excel(name = "商品备注")
    private String comment;

    /**
     * 最后更新时间
     */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    /**
     * 销售单位id
     */
    @Excel(name = "销售单位id")
    private Long unitId;

    /**
     * 销售单位id
     */
    @Excel(name = "销售单位名称")
    private String unitName;

    /**
     * 之前商品数量
     */
    private Double oldQuantity;
    /**
     * 之前商品价格
     */
    private Double oldPirce;

    private List<Long> orderIds;

    /**
     * 订单id
     */
    @Excel(name = "部门id")
    private String deptId;

    /** 开始时间 */
    @TableField(exist = false)
    private String startTime;

    /** 结束时间 */
    @TableField(exist = false)
    private String endTime;

    private String printStatus;
    private String sortingStatus;

    /**
     * 之前分拣数量
     */
    private Double oldSortingWeight;

    /**
     * 订单明细表冗余分拣重量
     */
    private Double srcSortingWeight;

    /**
     * 订单明细表冗余分拣数量
     */
    private Double srcSortingQuantity;

    /** 分拣重量 */
    @Excel(name = "分拣重量")
    private Double sortingWeight;

    /** 分拣数量 */
    @Excel(name = "分拣数量")
    private Double sortingQuantity;

    /** 分拣数量单位 */
    @Excel(name = "分拣数量单位")
    private Long sortingQuantityUnit;

    /** 分拣日期 */
    @Excel(name = "分拣日期")
    private String sortingDay;

    /** 分拣员 */
    @Excel(name = "分拣员")
    private String sortingUserId;

    private String classifyName;
    private String spuName;
    private String goodName;

    private String goodsImg;
    private String merchantId;
    private String  keywords;

}
