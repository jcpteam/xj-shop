package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 控价对象 store_price_setting
 * 
 * @author javaboot
 * @date 2021-06-24
 */
public class StorePriceSetting extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 控价id */
    private Long settingId;

    /** 所属中心id */
    @Excel(name = "所属中心id")
    private String deptId;

    /** 所属中心id */
    @Excel(name = "所属中心")
    private String deptName;

    /** 控价日期 */
    @Excel(name = "控价日期", width = 30, dateFormat = "yyyy-MM-dd")
    private String optDate;

    /** 0：未审核    1：已审核 */
    @Excel(name = "0：未审核    1：已审核")
    private String status;

    /** 商品数量 */
    @Excel(name = "商品数量")
    private Long goodsCount;

    /** 报价单数量 */
    @Excel(name = "报价单数量")
    private Long adjustCount;

    /** 最后修改时间 */
    @Excel(name = "最后修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    private String submitStatus;

    public String getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus = submitStatus;
    }

    public void setSettingId(Long settingId){
        this.settingId = settingId;
    }

    public Long getSettingId(){
        return settingId;
    }
    public void setDeptId(String deptId){
        this.deptId = deptId;
    }

    public String getDeptId(){
        return deptId;
    }
    public void setOptDate(String optDate){
        this.optDate = optDate;
    }

    public String getOptDate(){
        return optDate;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
    public void setGoodsCount(Long goodsCount){
        this.goodsCount = goodsCount;
    }

    public Long getGoodsCount(){
        return goodsCount;
    }
    public void setAdjustCount(Long adjustCount){
        this.adjustCount = adjustCount;
    }

    public Long getAdjustCount(){
        return adjustCount;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("settingId", getSettingId())
            .append("deptId", getDeptId())
            .append("optDate", getOptDate())
            .append("status", getStatus())
            .append("goodsCount", getGoodsCount())
            .append("adjustCount", getAdjustCount())
            .append("lastModifyTime", getLastModifyTime())
            .toString();
    }
}
