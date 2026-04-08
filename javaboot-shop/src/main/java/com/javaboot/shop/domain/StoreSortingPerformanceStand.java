package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 分拣绩效标准信息对象 store_sorting_performance_stand
 * 
 * @author javaboot
 * @date 2021-07-01
 */
public class StoreSortingPerformanceStand extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 客户类型：大客户   批发客户  零散客户 */
    @Excel(name = "客户类型：大客户   批发客户  零散客户")
    private String customerType;

    /** 商品spu */
    @Excel(name = "商品spu")
    private String spuNo;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String spuName;

    /** 绩效单位 */
    @Excel(name = "绩效单位")
    private Long performUnitId;

    /** 绩效单位名称 */
    @Excel(name = "绩效单位名称")
    private String performUnitName;

    /** 商品单位 */
    @Excel(name = "商品单位")
    private Long goodUnitId;

    /** 商品单位名称 */
    @Excel(name = "商品单位名称")
    private String goodUnitName;

    /** 单位比例 */
    @Excel(name = "单位比例")
    private Double unitRatio;

    /** 分拣权重 */
    @Excel(name = "分拣权重")
    private Double sortingWeightRatio;

    /** 最后更新时间 */
    private Date lastModifyTime;

    //订单商品id
    private Long orderItemId;

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getPerformUnitName() {
        return performUnitName;
    }

    public void setPerformUnitName(String performUnitName) {
        this.performUnitName = performUnitName;
    }

    public String getGoodUnitName() {
        return goodUnitName;
    }

    public void setGoodUnitName(String goodUnitName) {
        this.goodUnitName = goodUnitName;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setCustomerType(String customerType){
        this.customerType = customerType;
    }

    public String getCustomerType(){
        return customerType;
    }
    public void setSpuNo(String spuNo){
        this.spuNo = spuNo;
    }

    public String getSpuNo(){
        return spuNo;
    }
    public void setPerformUnitId(Long performUnitId){
        this.performUnitId = performUnitId;
    }

    public Long getPerformUnitId(){
        return performUnitId;
    }
    public void setGoodUnitId(Long goodUnitId){
        this.goodUnitId = goodUnitId;
    }

    public Long getGoodUnitId(){
        return goodUnitId;
    }
    public void setUnitRatio(Double unitRatio){
        this.unitRatio = unitRatio;
    }

    public Double getUnitRatio(){
        return unitRatio;
    }
    public void setSortingWeightRatio(Double sortingWeightRatio){
        this.sortingWeightRatio = sortingWeightRatio;
    }

    public Double getSortingWeightRatio(){
        return sortingWeightRatio;
    }
    public void setLastModifyTime(Date lastModifyTime){
        this.lastModifyTime = lastModifyTime;
    }

    public Date getLastModifyTime(){
        return lastModifyTime;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    @Override
    public String toString() {
        return "StoreSortingPerformanceStand{" +
                "id=" + id +
                ", customerType='" + customerType + '\'' +
                ", spuNo='" + spuNo + '\'' +
                ", spuName='" + spuName + '\'' +
                ", performUnitId=" + performUnitId +
                ", performUnitName=" + performUnitName +
                ", goodUnitId=" + goodUnitId +
                ", goodUnitName=" + goodUnitName +
                ", unitRatio=" + unitRatio +
                ", sortingWeightRatio=" + sortingWeightRatio +
                ", lastModifyTime=" + lastModifyTime +
                ", orderItemId=" + orderItemId +
                '}';
    }
}
