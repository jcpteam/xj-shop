package com.javaboot.shop.domain;

import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import com.javaboot.shop.vo.StoreGoodsOrderSortingItemVO;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 收款单对象 store_receipt
 *
 * @author lqh
 * @date 2021-07-05
 */
@Data
@ToString(callSuper = true)
public class StoreReceipt extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 收款id */
    private Long receiptId;

    /** 订单id */
    private String orderIds;

    /** 客户id */
    private String merchantId;

    /** 区域id */
    private String deptId;

    /** 收款编号 */
    @Excel(name = "收款编号", width = 30, sort = 3)
    private String code;


    /** 0-未付款 1-已付款 */
    private String payStatus;

    /** 金额 */
    @Excel(name = "金额", sort = 4)
    private Double amount;

    /** 余额 */
    @Excel(name = "余额", sort = 5)
    private Double balance;

    /** 备注 */
    @Excel(name = "备注", sort = 6)
    private String comment;

    /** 凭证路径 */
    private String voucherPath;

    /** 最后更新时间 */
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd", sort = 11)
    private Date lastModifyTime;

    /** 状态：0-删除，1-正常 */
    private String status;

    /** 最后更新时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd", sort = 10)
    private Date createTime;

    /** 支付方式 1-账户支付，2-招行支付，3-线下冲账' */
    private String payType;

    @Excel(name = "支付方式", sort = 7)
    private String payTypeName;

    /** 操作员 */
    @Excel(name = "操作人", sort = 9)
    private String creatorName;

    private List<StoreGoodsOrderSortingItemVO> goosList;

    /** 微信openid */
    private String openId;
}
