package com.javaboot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;

/**
 * 用户和角色和部门关联对象 sys_user_role_dept
 * 
 * @author javaboot
 * @date 2021-05-30
 */
public class SysUserRoleDept extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 部门id */
    @Excel(name = "部门id")
    private Long roleId;

    /** 部门id */
    @Excel(name = "部门id")
    private String deptId;

    /** 是否是默认：0-否，1-是 */
    @Excel(name = "是否是默认：0-否，1-是")
    private String isDefault;

    private String loginName;
    private String userName;
    private String deptName;
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }
    public void setRoleId(Long roleId){
        this.roleId = roleId;
    }

    public Long getRoleId(){
        return roleId;
    }
    public void setDeptId(String deptId){
        this.deptId = deptId;
    }

    public String getDeptId(){
        return deptId;
    }
    public void setIsDefault(String isDefault){
        this.isDefault = isDefault;
    }

    public String getIsDefault(){
        return isDefault;
    }

    @Override
    public String toString() {
        return "SysUserRoleDept{" +
                "id=" + id +
                ", userId=" + userId +
                ", roleId=" + roleId +
                ", deptId='" + deptId + '\'' +
                ", isDefault='" + isDefault + '\'' +
                ", loginName='" + loginName + '\'' +
                ", userName='" + userName + '\'' +
                ", deptName='" + deptName + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
