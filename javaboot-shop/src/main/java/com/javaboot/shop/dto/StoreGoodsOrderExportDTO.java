package com.javaboot.shop.dto;

import com.javaboot.common.annotation.Excel;
import com.javaboot.shop.domain.StoreGoodsOrder;
import com.javaboot.system.domain.SysUser;
import lombok.Data;
import lombok.ToString;
import org.apache.shiro.SecurityUtils;

import java.util.Date;
import java.util.List;

@Data
@ToString(callSuper = true)
public class StoreGoodsOrderExportDTO {

    @Excel(name = "订单编号", width = 40)
    private String code;

    @Excel(name = "商品SPU", width = 20)
    private String spuNo;

    @Excel(name = "商品名称", width = 20)
    private String spuName;

    @Excel(name = "销售名称", width = 40)
    private String goodsName;

    @Excel(name = "销售Id", width = 40)
    private String goodsId;

    private String deptId;

    @Excel(name = "所属区域")
    private String deptName;

    private String merchantId;

    @Excel(name = "下单客户", width = 30)
    private String merchantName;

    private String settlementId;

    @Excel(name = "结算客户", width = 30)
    private String settlementName;

    @Excel(name = "重量")
    private String weight;

    @Excel(name = "数量")
    private String quantity;

    @Excel(name = "单价")
    private String price;

    @Excel(name = "支付金额")
    private String amount;

    @Excel(name = "支付状态")
    private String payStatus;

    @Excel(name = "付款单状态")
    private String receiptStatus;

    @Excel(name = "订单状态")
    private String status;

    @Excel(name = "下单日期", width = 25, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "交货日期", width = 25, dateFormat = "yyyy-MM-dd")
    private Date deliveryDate;

    @Excel(name = "订单商品备注")
    private String comment;


}
