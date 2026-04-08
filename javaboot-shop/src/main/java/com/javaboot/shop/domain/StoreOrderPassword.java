package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 订单密码对象 store_order_password
 * 
 * @author javaboot
 * @date 2021-08-07
 */
public class StoreOrderPassword extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 明细id */
    private Long id;

    /** 区域id */
    @Excel(name = "区域id")
    private String deptId;

    /** 区域名称 */
    @Excel(name = "区域名称 ")
    private String deptName;

    /** 订单设置密码 */
    @Excel(name = "订单设置密码")
    private String password;

    /** 0-删除 1-正常 */
    @Excel(name = "0-删除 1-正常")
    private String status;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setDeptId(String deptId){
        this.deptId = deptId;
    }

    public String getDeptId(){
        return deptId;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
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

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deptId", getDeptId())
            .append("password", getPassword())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("lastModifyTime", getLastModifyTime())
            .toString();
    }
}
