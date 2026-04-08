package com.javaboot.shop.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 退货单明细对象 store_goods_return_order_item
 * 
 * @author lqh
 * @date 2021-06-26
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsReturnOrderItem extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 明细id */
    private Long itemId;

    /** 退货单id */
    @Excel(name = "退货单id")
    private Long returnOrderId;

    /** 商品id */
    @Excel(name = "商品id")
    private String goodsId;

    /** 商品spu */
    @Excel(name = "商品spu")
    private String spuNo;

    /** 商品数量 */
    @Excel(name = "商品数量")
    private Double quantity;

    /** 商品金额 */
    @Excel(name = "商品金额")
    private Double amount;

    /** 商品价格 */
    @Excel(name = "商品价格")
    private Double price;

    /** 商品备注 */
    @Excel(name = "商品备注")
    private String comment;
    /**
     * 销售单位id
     */
    @Excel(name = "销售单位id")
    private Long unitId;
    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;


}
