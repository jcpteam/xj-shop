package com.javaboot.shop.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 支付账户对象 store_payment_account
 * 
 * @author lqh
 * @date 2021-07-09
 */
@Data
@ToString(callSuper = true)
public class StorePaymentAccount extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 账户ID */
    private String accountId;

    /** 商户id */
    @Excel(name = "商户id")
    private String memberId;
    /** 商户id */
    @Excel(name = "区域id")
    private String deptId;
    /** 账户号 */
    @Excel(name = "账户号")
    private String accountNumber;
    /** 账单周期 */
    @Excel(name = "账单周期")
    private String billDays;

    /** 所属门店 */
    @Excel(name = "所属门店")
    private String storeId;

    /** 现金账户 */
    @Excel(name = "现金账户")
    private Double cash;
    /** 返点账户 */
    @Excel(name = "返点账户")
    private Double rebateAccount;
    /** 保证金账户 */
    @Excel(name = "保证金账户")
    private Double bondAccount;

    /** 0-删除 1-启用 2-禁用 */
    @Excel(name = "0-删除 1-启用 2-禁用")
    private String status;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    /** 1-现金账户 2-保证金账户 3-返点账户 */
    @Excel(name = "账户类型")
    private String accountType;


    /** 1-充值 3-提现 */
    @Excel(name = "操作类型")
    private String operateType;

    /** 金额 */
    @Excel(name = "金额")
    private Double amount;


    private String remark;

}
