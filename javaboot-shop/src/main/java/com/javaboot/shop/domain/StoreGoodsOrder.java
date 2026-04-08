package com.javaboot.shop.domain;

import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 订单信息主对象 store_goods_order
 *
 * @author shop
 * @date 2021-05-30
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 仓库Id
     */
    private Long warehouseId;

    /**
     * 客户id
     */
    @Excel(name = "客户id")
    private String merchantId;

    /**
     * 区域id
     */
    @Excel(name = "区域id")
    private String deptId;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String code;

    /**
     * 订单金额
     */
    @Excel(name = "订单金额")
    private Double price;

    /**
     * 订单金额
     */
    @Excel(name = "分拣后的订单实际金额")
    private Double sortingPrice;

    /**
     * 订单来源1-微信 2-代客下单
     */
    @Excel(name = "订单来源1-微信 2-代客下单")
    private String source;

    /**
     * 0-未打印 1-已打印
     */
    @Excel(name = "0-未打印 1-已打印")
    private String printStatus;

    /**
     * 0-未付款 1-已付款
     */
    @Excel(name = "0-未付款 1-已付款")
    private String payStatus;

    /**
     * 0-删除 1-待审核 2-待分拣 3-已发货 4-已完成
     */
    @Excel(name = "0-删除 1-待审核 2-待分拣 3-已发货 4-已完成")
    private String status;

    /**
     * 状态:0-待同步，1-已同步 2-同步失败
     */
    private String synStatus;

    /**
     * 状态:0-未生成，1-已生成
     */
    private String receiptStatus;

    /**
     * 原状态
     */
    private String oldStatus;

    /**
     * 订单备注
     */
    @Excel(name = "订单备注")
    private String comment;

    /**
     * 交货日期
     */
    @Excel(name = "交货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryDate;

    /**
     * 收货日期
     */
    @Excel(name = "收货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveDate;

    /**
     * 财务审核状态:0-待审核,1-已通过
     */
    private String financialAuditStatus;
    /**
     * 最后更新时间
     */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 0-未打印 1-已打印
     */
    @Excel(name = "出库单打印状态：0-未打印 1-已打印")
    private String odoPrintStatus;

    /**
     * 创建人id
     */
    private String creatorId;
}
