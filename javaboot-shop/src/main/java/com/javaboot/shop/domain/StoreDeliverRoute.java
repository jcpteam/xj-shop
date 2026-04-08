package com.javaboot.shop.domain;

import com.javaboot.common.annotation.ExcelImport;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 线路信息对象 store_deliver_route
 * 
 * @author javaboot
 * @date 2021-05-25
 */
public class StoreDeliverRoute extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 线路id */
    private Long routeId;

    /** 线路名称 */
    @Excel(name = "线路名称")
    private String routeName;

    /** 所属区域 */
    @Excel(name = "区域编号")
    private String deptId;

    @Excel(name = "区域名称")
    private String deptName;

    /** 配送时间 */
    private String deliverTime;

    /** 状态 */
    private String status;

    /** 最后更新时间 */
    private Date lastModifyTime;

    private Integer customerCount;

    private String searchKey;

    @Excel(name = "客户编号")
    private String customerId;

    @Excel(name = "客户名称")
    private String customerName;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    public void setRouteId(Long routeId){
        this.routeId = routeId;
    }

    public Long getRouteId(){
        return routeId;
    }
    public void setRouteName(String routeName){
        this.routeName = routeName;
    }

    public String getRouteName(){
        return routeName;
    }
    public void setDeliverTime(String deliverTime){
        this.deliverTime = deliverTime;
    }

    public String getDeliverTime(){
        return deliverTime;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptId()
    {
        return deptId;
    }

    public void setDeptId(String deptId)
    {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return "StoreDeliverRoute{" +
                "routeId=" + routeId +
                ", routeName='" + routeName + '\'' +
                ", deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", deliverTime='" + deliverTime + '\'' +
                ", status='" + status + '\'' +
                ", lastModifyTime=" + lastModifyTime +
                ", customerCount=" + customerCount +
                '}';
    }
}
