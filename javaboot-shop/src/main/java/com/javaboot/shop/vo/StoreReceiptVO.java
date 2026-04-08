package com.javaboot.shop.vo;

import com.javaboot.common.annotation.Excel;
import com.javaboot.shop.domain.StoreReceipt;
import lombok.Data;

import java.util.Date;

/**
 * @Classname StorePaymentSlipQueryVO
 * @Description 收款单
 * @Date 2021/7/5 0005 23:17
 * @@author lqh
 */
@Data
public class StoreReceiptVO extends StoreReceipt {

    @Excel(name = "结算客户", width = 30, sort = 1)
    private String merchantName;

    @Excel(name = "所属区域", sort = 2)
    private String deptName;

    @Excel(name = "支付状态", sort = 8)
    private String payStatusName;

    private String statusName;

    @Excel(name = "订单编号", width = 30,  sort = 12)
    private String orderCode;

    @Excel(name = "订单创建时间",dateFormat = "yyyy-MM-dd HH:mm:ss", width = 30, sort = 13)
    private Date orderCreateTime;

    @Excel(name = "收货日期", width = 30, dateFormat = "yyyy-MM-dd", sort = 14)
    private Date deliveryDate;
}
