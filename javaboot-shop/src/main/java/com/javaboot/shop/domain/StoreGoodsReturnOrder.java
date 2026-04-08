package com.javaboot.shop.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 退货单信息主对象 store_goods_retun_order
 * 
 * @author lqh
 * @date 2021-06-26
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsReturnOrder extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 退货单id */
    private Long returnOrderId;

    /** 客户id */
    @Excel(name = "客户id")
    private String merchantId;

    /** 区域id */
    @Excel(name = "区域id")
    private String deptId;

    /** 编号 */
    @Excel(name = "编号")
    private String code;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private Double price;

    /** 0-未付款 1-已付款 */
    @Excel(name = "0-未付款 1-已付款")
    private String payStatus;

    /** 状态:0-删除 1-待审核 2-通过 3-不通过 */
    @Excel(name = "状态:0-删除 1-待审核 2-通过 3-不通过")
    private String status;

    /** 订单备注 */
    @Excel(name = "订单备注")
    private String comment;

    /** 仓库id */
    @Excel(name = "仓库id")
    private Long warehouseId;

    /** 类型:1-退货,无提货 2-退货,运损 3-退货入库 */
    @Excel(name = "类型:1-退货,无提货 2-退货,运损 3-退货入库")
    private String returnType;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;


}
