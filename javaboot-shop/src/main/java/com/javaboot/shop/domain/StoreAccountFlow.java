package com.javaboot.shop.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 账户流水对象 store_account_flow
 * 
 * @author lqh
 * @date 2021-07-09
 */
@Data
@ToString(callSuper = true)
public class StoreAccountFlow extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 流水ID */
    private Long flowId;
    /** 账户ID */
    @Excel(name = "账户ID")
    private String accountId;
    /** 账户号 */
    @Excel(name = "账户号")
    private String accountNumber;

    /** 操作类型：0-预付，1-充值 */
    @Excel(name = "操作类型：0-预付，1-充值")
    private String type;

    /** 原有金额 */
    @Excel(name = "原有金额")
    private Double oldAmount;

    /** 变动金额 */
    @Excel(name = "变动金额")
    private Double changeAmount;

    /** 现有金额 */
    @Excel(name = "现有金额")
    private Double nowAmount;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    @Excel(name = "下单客户名称")
    private String nickName;

    @Excel(name = "结算客户名称")
    private String settlementName;

    private String remark;

}
