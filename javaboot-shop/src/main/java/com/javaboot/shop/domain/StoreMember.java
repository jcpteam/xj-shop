package com.javaboot.shop.domain;

import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 商城会员信息对象 store_member
 *
 * @author javaboot
 * @date 2019-08-30
 */
@Data
@ToString(callSuper = true)
public class StoreMember extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** null */
    private Long id;

    /** vip编号 */
    @Excel(name = "vip编号")
    private String customerNo;

    /** 会员QQ OPENID */
    @Excel(name = "会员QQ OPENID")
    private String openidQq;

    /** 会员微信OPENID */
    @Excel(name = "会员微信OPENID")
    private String openidWx;

    /** 会员手机号 */
    @Excel(name = "会员手机号")
    private String phone;

    /** 登录密码 */
    @Excel(name = "登录密码")
    private String password;

    /** 会员昵称 */
    @Excel(name = "会员昵称")
    private String nickname;

    /** 会员头像 */
    @Excel(name = "会员头像")
    private String headimg;

    /** 性别 */
    @Excel(name = "性别")
    private String sex;

    /** 会员级别：普通、中级、高级 */
    @Excel(name = "会员级别：普通、中级、高级")
    private String level;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 所在城市/地址 */
    @Excel(name = "所在城市/地址")
    private String address;

    /** 个性签名 */
    @Excel(name = "个性签名")
    private String signature;

    /** 消费累计额度 用于用户等级 */
    @Excel(name = "消费累计额度 用于用户等级")
    private Double totalAmount;

    /** 用户金额 */
    @Excel(name = "用户金额")
    private Double userMoney;

    /** 累积分佣金额 */
    @Excel(name = "累积分佣金额")
    private Double distributMoney;

    /** 冻结金额 */
    @Excel(name = "冻结金额")
    private Double frozenMoney;

    /** 用户下线总数 */
    @Excel(name = "用户下线总数")
    private Integer underlingNumber;

    /** 消费积分 */
    @Excel(name = "消费积分")
    private Integer payPoints;

    /** 默认收货地址 */
    @Excel(name = "默认收货地址")
    private Integer addressId;

    /** 会员状态0待审核 1-已审核 2-已拒绝 */
    @Excel(name = "会员状态0待审核 1-已审核 2-已拒绝")
    private Integer status;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createAt;

    /** 审核时间 */
    @Excel(name = "审核时间")
    private Long statusTime;

    /** 客户区域 */
    @Excel(name = "客户区域")
    private String customerArea;

    /** 客户区域 */
    @Excel(name = "客户区域")
    private String deptName;

    /** 信用证代码 */
    @Excel(name = "信用证代码")
    private String creditcode;

    /**
     * 区域销售人员上传金蝶用
     */
    private String leader;

    /** 客户分类 */
    @Excel(name = "客户分类")
    private String customerClass;

    /** 客户类别：社区门店、电商平台、大客户、批发 */
    @Excel(name = "客户类别：社区门店、电商平台、大客户、批发")
    private String customerType;

    /** 客户所属(上级) */
    @Excel(name = "客户所属(上级)")
    private String superCustomer;

    /** 客户所属(上级)名称 */
    @Excel(name = "客户所属(上级)名称")
    private String superCustomerName;

    /** 结算客户 */
    @Excel(name = "结算客户")
    private String billingCusotmer;

    /** 身份证号码 */
    @Excel(name = "身份证号码")
    private String cardNumber;

    /** 收货地址 */
    @Excel(name = "收货地址")
    private String receiveAddress;

    /** 客户类型 */
    @Excel(name = "客户类型")
    private String customerKinds;


    /** 报价单id */
    @Excel(name = "报价单id")
    private Long quotationId;


    /** 报价单id */
    @Excel(name = "报价单名称")
    private String quotationName;

    private List<String> ids;

    private List<String> customerNOs;

    private List<Long> quotationIds;

    private String memberIds;

    /** 仓库id */
    @Excel(name = "仓库id")
    private Long houseId;

    /** 职位 */
    @Excel(name = "职位")
    private String position;


    /** 联系人姓名 */
    @Excel(name = "联系人姓名")
    private String contactName;

    /** 电话 */
    @Excel(name = "电话")
    private String telephone;

    /** 业务员 */
    @Excel(name = "业务员")
    private String opmanagerId;

    /** 结算类型 */
    @Excel(name = "结算类型 1-单独结算 2-统一结算")
    private String settlementType;

    /** 结算类型 */
    @Excel(name = "结算类型 1-配送 2-自提")
    private String deliveryType;

    /** 结算类型 */
    @Excel(name = "结算账号id")
    private String settlementId;

    /** 结算类型 */
    @Excel(name = "结算名称")
    private String settlementName;

    //打印类型
    private String printType;

    /** 业务员名称信息 */
    private String opmanagerName;
    private String opmanagerPhone;
}
