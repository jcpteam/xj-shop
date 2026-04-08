package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 分拣框信息对象 store_sorting_box
 * 
 * @author javaboot
 * @date 2021-06-11
 */
public class StoreSortingBox extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 框子名称 */
    @Excel(name = "框子名称")
    private String boxName;

    /** box_weight */
    @Excel(name = "box_weight")
    private Double boxWeight;

    /** 状态：0-删除，1-正常 */
    private String status;

    /** 最后修改时间 */
    private Date lastModifyTime;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setBoxName(String boxName){
        this.boxName = boxName;
    }

    public String getBoxName(){
        return boxName;
    }
    public void setBoxWeight(Double boxWeight){
        this.boxWeight = boxWeight;
    }

    public Double getBoxWeight(){
        return boxWeight;
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
            .append("boxName", getBoxName())
            .append("boxWeight", getBoxWeight())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("lastModifyTime", getLastModifyTime())
            .toString();
    }
}
