package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 仓库信息对象 store_house
 * 
 * @author javaboot
 * @date 2021-05-25
 */
public class StoreHouse extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 仓库id */
    private Long storeId;

    /** 仓库编号 */
    @Excel(name = "仓库编号")
    private String storeNo;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String storeName;

    /** 所属区域 */
    @Excel(name = "所属区域")
    private String deptId;

    @Excel(name = "区域名称")
    private String deptName;

    /** 仓库类型 */
    @Excel(name = "仓库类型")
    private String storeType;

    /** 状态：0-删除，1-正常 */
    private String status;

    /** 最后更新时间 */
    private Date lastModifyTime;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setStoreId(Long storeId){
        this.storeId = storeId;
    }

    public Long getStoreId(){
        return storeId;
    }
    public void setStoreNo(String storeNo){
        this.storeNo = storeNo;
    }

    public String getStoreNo(){
        return storeNo;
    }
    public void setStoreName(String storeName){
        this.storeName = storeName;
    }

    public String getStoreName(){
        return storeName;
    }
    public void setDeptId(String deptId){
        this.deptId = deptId;
    }

    public String getDeptId(){
        return deptId;
    }
    public void setStoreType(String storeType){
        this.storeType = storeType;
    }

    public String getStoreType(){
        return storeType;
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
        return "StoreHouse{" +
                "storeId=" + storeId +
                ", storeNo='" + storeNo + '\'' +
                ", storeName='" + storeName + '\'' +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", storeType='" + storeType + '\'' +
                ", status='" + status + '\'' +
                ", lastModifyTime=" + lastModifyTime +
                '}';
    }
}
