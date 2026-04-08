package com.javaboot.shop.domain;

import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 分拣记录详情对象 store_sorting_detail
 * 
 * @author javaboot
 * @date 2021-07-05
 */
@Data
@ToString(callSuper = true)
public class StoreSortingDetail extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 订单id */
    @Excel(name = "订单id")
    private Long orderId;

    /** 订单明细id */
    @Excel(name = "订单明细id")
    private Long orderItemId;

    /** 分拣重量 */
    @Excel(name = "分拣重量")
    private Double sortingWeight;

    /** 分拣数量 */
    @Excel(name = "分拣数量")
    private Double sortingQuantity;

    /** 分拣数量单位 */
    @Excel(name = "分拣数量单位")
    private Long sortingQuantityUnit;

    /** 分拣日期 */
    @Excel(name = "分拣日期")
    private String sortingDay;

    /** 分拣员 */
    @Excel(name = "分拣员")
    private String sortingUserId;

    /** 状态：0-删除，1-正常 */
    @Excel(name = "状态：0-删除，1-正常")
    private String status;

    /** 最后修改时间 */
    @Excel(name = "最后修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    /** 缺货状态：0-缺货 1-有货 */
    @Excel(name = "缺货状态：0-缺货 1-有货")
    private String outStockStatus;

    //分拣绩效结果
    private Double performanceResult;

    //分拣人员列表id
    private List<String> sortingUserIds;

    //分拣日期：开始日期和结束日期
    private String workDayStartDate;
    private String workDayEndDate;

    private String deptId;
}
