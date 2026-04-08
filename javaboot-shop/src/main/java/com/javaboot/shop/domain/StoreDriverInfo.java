package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 司机信息对象 store_driver_info
 * 
 * @author javaboot
 * @date 2021-05-25
 */
public class StoreDriverInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 司机id */
    private Long driverId;

    /** 司机名称 */
    @Excel(name = "司机名称")
    private String driverName;

    /** 驾驶证编号 */
    @Excel(name = "驾驶证编号")
    private String driverLicense;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String driverPhone;

    /** 所属区域 */
    @Excel(name = "所属区域")
    private String deptId;

    /** 所属区域 */
    @Excel(name = "所属区域名称")
    private String deptName;

    /** 所属线路 */
    @Excel(name = "所属线路")
    private Long routeId;

    /** 所属线路 */
    @Excel(name = "所属线路名称")
    private String routeName;

    /** 状态：0-删除，1-正常，2-停用 */
    @Excel(name = "状态：0-删除，1-正常，2-停用")
    private String status;

    /** 最后更新时间 */
    private Date lastModifyTime;

    @Override
    public String toString() {
        return "StoreDriverInfo{" +
                "driverId=" + driverId +
                ", driverName='" + driverName + '\'' +
                ", driverLicense='" + driverLicense + '\'' +
                ", driverPhone='" + driverPhone + '\'' +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", routeId=" + routeId +
                ", routeName='" + routeName + '\'' +
                ", status='" + status + '\'' +
                ", lastModifyTime=" + lastModifyTime +
                '}';
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setDriverId(Long driverId){
        this.driverId = driverId;
    }

    public Long getDriverId(){
        return driverId;
    }
    public void setDriverName(String driverName){
        this.driverName = driverName;
    }

    public String getDriverName(){
        return driverName;
    }
    public void setDriverLicense(String driverLicense){
        this.driverLicense = driverLicense;
    }

    public String getDriverLicense(){
        return driverLicense;
    }
    public void setDriverPhone(String driverPhone){
        this.driverPhone = driverPhone;
    }

    public String getDriverPhone(){
        return driverPhone;
    }
    public void setDeptId(String deptId){
        this.deptId = deptId;
    }

    public String getDeptId(){
        return deptId;
    }
    public void setRouteId(Long routeId){
        this.routeId = routeId;
    }

    public Long getRouteId(){
        return routeId;
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
