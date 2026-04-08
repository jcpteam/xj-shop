package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 预警设置对象 store_early_warning
 * 
 * @author javaboot
 * @date 2021-11-15
 */
public class StoreEarlyWarning extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 预警key */
    @Excel(name = "预警key")
    private String warningKey;

    /** 区域id */
    @Excel(name = "区域id")
    private String deptId;

    /** 状态：0-关，1-开 */
    @Excel(name = "状态：0-关，1-开")
    private String status;

    /** 预警备注 */
    @Excel(name = "预警备注")
    private String comment;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setWarningKey(String warningKey){
        this.warningKey = warningKey;
    }

    public String getWarningKey(){
        return warningKey;
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
    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return comment;
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
            .append("warningKey", getWarningKey())
            .append("deptId", getDeptId())
            .append("status", getStatus())
            .append("comment", getComment())
            .append("createTime", getCreateTime())
            .append("lastModifyTime", getLastModifyTime())
            .toString();
    }
}
