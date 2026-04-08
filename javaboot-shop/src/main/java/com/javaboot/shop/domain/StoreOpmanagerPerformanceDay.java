package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 业务员每日绩效对象 store_opmanager_performance_day
 * 
 * @author javaboot
 * @date 2021-12-21
 */
public class StoreOpmanagerPerformanceDay extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 业务员id */
    @Excel(name = "业务员id")
    private String opmanagerId;

    /** 统计日期，如：2020-07-20 */
    @Excel(name = "统计日期，如：2020-07-20")
    private String performanceDay;

    /** 绩效总金额 */
    @Excel(name = "绩效总金额")
    private Double totalPrice;

    /** 成本金额 */
    @Excel(name = "成本金额")
    private Double costPrice;

    /** 毛利金额 */
    @Excel(name = "毛利金额")
    private Double profitPrice;

    /** 备注 */
    @Excel(name = "备注")
    private String comment;

    /** 状态：0-删除，1-正常 */
    @Excel(name = "状态：0-删除，1-正常")
    private String status;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    private String opmanagerName;

    public String getOpmanagerName() {
        return opmanagerName;
    }

    public void setOpmanagerName(String opmanagerName) {
        this.opmanagerName = opmanagerName;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setOpmanagerId(String opmanagerId){
        this.opmanagerId = opmanagerId;
    }

    public String getOpmanagerId(){
        return opmanagerId;
    }
    public void setPerformanceDay(String performanceDay){
        this.performanceDay = performanceDay;
    }

    public String getPerformanceDay(){
        return performanceDay;
    }
    public void setTotalPrice(Double totalPrice){
        this.totalPrice = totalPrice;
    }

    public Double getTotalPrice(){
        return totalPrice;
    }
    public void setCostPrice(Double costPrice){
        this.costPrice = costPrice;
    }

    public Double getCostPrice(){
        return costPrice;
    }
    public void setProfitPrice(Double profitPrice){
        this.profitPrice = profitPrice;
    }

    public Double getProfitPrice(){
        return profitPrice;
    }
    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return comment;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
    public void setLastModifyTime(Date lastModifyTime){
        this.lastModifyTime = lastModifyTime;
    }

    public Date getLastModifyTime(){
        return lastModifyTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("opmanagerId", getOpmanagerId())
            .append("performanceDay", getPerformanceDay())
            .append("totalPrice", getTotalPrice())
            .append("costPrice", getCostPrice())
            .append("profitPrice", getProfitPrice())
            .append("comment", getComment())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("lastModifyTime", getLastModifyTime())
            .toString();
    }
}
