package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;

/**
 * 分拣每日考勤员工关联对象 store_sorting_daily_user
 * 
 * @author javaboot
 * @date 2021-06-12
 */
public class StoreSortingDailyUser extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** null */
    private Long id;

    /** 每日考勤id */
    @Excel(name = "每日考勤id")
    private Long workId;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    private String userName;
    private String loginName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setWorkId(Long workId){
        this.workId = workId;
    }

    public Long getWorkId(){
        return workId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    @Override
    public String toString() {
        return "StoreSortingDailyUser{" +
                "id=" + id +
                ", workId=" + workId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", loginName='" + loginName + '\'' +
                '}';
    }
}
