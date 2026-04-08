package com.javaboot.shop.domain;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 记录调整时候的退货或者补货对象 store_goods_order_log
 * 
 * @author lqh
 * @date 2021-06-03
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreGoodsOrderLog extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 日志id */
    private Long logId;

    /** 订单id */
    @Excel(name = "订单id")
    private Long orderId;

    /** 订单明细id */
    @Excel(name = "订单明细id")
    private Long itemId;

    /** 商品id */
    @Excel(name = "商品id")
    private Long goodId;

    /** 商品SPU */
    @Excel(name = "商品SPU")
    private String spuNo;

    /** 变动数量 */
    @Excel(name = "变动数量")
    private Double quantity;

    /** 变动金额 */
    @Excel(name = "变动金额")
    private Double amount;

    /** 1-创建 2-修改 3-删除 4-审核 5-分拣 6-结束 */
    @Excel(name = "1-创建 2-修改 3-删除 4-审核 5-分拣 6-结束")
    private String type;

    /** 操作人id */
    @Excel(name = "操作人id")
    private String operateUserId;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String code;
    /**
     * 操作日志
     */
    @Excel(name = "操作日志")
    private String operationLog;

}
