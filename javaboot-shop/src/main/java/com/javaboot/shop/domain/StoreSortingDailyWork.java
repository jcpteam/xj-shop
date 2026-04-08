package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;
import java.util.List;

/**
 * 分拣每日考勤对象 store_sorting_daily_work
 * 
 * @author javaboot
 * @date 2021-06-11
 */
public class StoreSortingDailyWork extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 日期 */
    @Excel(name = "日期")
    private String workDay;

    private List<String> workDayList;

    private String workDayStartDate;
    private String workDayEndDate;

    /** 所属区域 */
    @Excel(name = "所属区域")
    private String deptId;

    private String deptName;

    private Integer sortingUserCount;

    /** 状态：0-删除，1-正常 */
    private String status;

    /** 最后修改时间 */
    private Date lastModifyTime;

    private String userIds;

    public String getSortingUserId() {
        return sortingUserId;
    }

    public void setSortingUserId(String sortingUserId) {
        this.sortingUserId = sortingUserId;
    }

    //分拣用户id
    private String sortingUserId;

    private String loginUserDeptId;

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getLoginUserDeptId() {
        return loginUserDeptId;
    }

    public void setLoginUserDeptId(String loginUserDeptId) {
        this.loginUserDeptId = loginUserDeptId;
    }

    @Override
    public String toString() {
        return "StoreSortingDailyWork{" +
                "id=" + id +
                ", workDay='" + workDay + '\'' +
                ", workDayList=" + workDayList +
                ", workDayStartDate='" + workDayStartDate + '\'' +
                ", workDayEndDate='" + workDayEndDate + '\'' +
                ", deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", sortingUserCount=" + sortingUserCount +
                ", status='" + status + '\'' +
                ", lastModifyTime=" + lastModifyTime +
                ", userIds='" + userIds + '\'' +
                ", sortingUserId='" + sortingUserId + '\'' +
                ", loginUserDeptId='" + loginUserDeptId + '\'' +
                '}';
    }

    public List<String> getWorkDayList() {
        return workDayList;
    }

    public void setWorkDayList(List<String> workDayList) {
        this.workDayList = workDayList;
    }

    public String getWorkDayStartDate() {
        return workDayStartDate;
    }

    public void setWorkDayStartDate(String workDayStartDate) {
        this.workDayStartDate = workDayStartDate;
    }

    public String getWorkDayEndDate() {
        return workDayEndDate;
    }

    public void setWorkDayEndDate(String workDayEndDate) {
        this.workDayEndDate = workDayEndDate;
    }

    public Integer getSortingUserCount() {
        return sortingUserCount;
    }

    public void setSortingUserCount(Integer sortingUserCount) {
        this.sortingUserCount = sortingUserCount;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setWorkDay(String workDay){
        this.workDay = workDay;
    }

    public String getWorkDay(){
        return workDay;
    }
    public void setDeptId(String deptId){
        this.deptId = deptId;
    }

    public String getDeptId(){
        return deptId;
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
