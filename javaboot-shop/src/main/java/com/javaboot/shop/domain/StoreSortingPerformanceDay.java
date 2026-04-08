package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 分拣绩效每日对象 store_sorting_performance_day
 * 
 * @author javaboot
 * @date 2021-07-20
 */
public class StoreSortingPerformanceDay extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 绩效关联月份id */
    @Excel(name = "绩效关联月份id")
    private Long monthId;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    private String userName;

    /** 区域id */
    @Excel(name = "区域id")
    private String deptId;
    private String deptName;

    /** 分拣日期，如：2020-07-20 */
    @Excel(name = "分拣日期，如：2020-07-20")
    private String sortingDay;

    /** 绩效结果 */
    @Excel(name = "绩效结果")
    private Double result;
    /** 绩效结果 */
    @Excel(name = "采购结果")
    private Double stockResult;
    /** 绩效结果 */
    @Excel(name = "调拨结果")
    private Double adjustResult;

    /** 核算人员id */
    @Excel(name = "核算人员id")
    private String operatorId;
    private String operatorName;

    /** 状态：0-删除，1-正常 */
    @Excel(name = "状态：0-删除，1-正常")
    private String status;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    @Override
    public String toString() {
        return "StoreSortingPerformanceDay{" +
                "id=" + id +
                ", monthId=" + monthId +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", sortingDay='" + sortingDay + '\'' +
                ", result=" + result +
                ", stockResult=" + stockResult +
                ", adjustResult=" + adjustResult +
                ", operatorId='" + operatorId + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", status='" + status + '\'' +
                ", lastModifyTime=" + lastModifyTime +
                '}';
    }

    public Double getStockResult() {
        return stockResult;
    }

    public void setStockResult(Double stockResult) {
        this.stockResult = stockResult;
    }

    public Double getAdjustResult() {
        return adjustResult;
    }

    public void setAdjustResult(Double adjustResult) {
        this.adjustResult = adjustResult;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setMonthId(Long monthId){
        this.monthId = monthId;
    }

    public Long getMonthId(){
        return monthId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }
    public void setDeptId(String deptId){
        this.deptId = deptId;
    }

    public String getDeptId(){
        return deptId;
    }
    public void setSortingDay(String sortingDay){
        this.sortingDay = sortingDay;
    }

    public String getSortingDay(){
        return sortingDay;
    }
    public void setResult(Double result){
        this.result = result;
    }

    public Double getResult(){
        return result;
    }
    public void setOperatorId(String operatorId){
        this.operatorId = operatorId;
    }

    public String getOperatorId(){
        return operatorId;
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

}
