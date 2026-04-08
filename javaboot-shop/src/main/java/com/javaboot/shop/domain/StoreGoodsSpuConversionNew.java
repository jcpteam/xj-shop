package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;

/**
 * spu转换比例对象 store_goods_spu_conversion_new
 * 
 * @author javaboot
 * @date 2022-03-05
 */
public class StoreGoodsSpuConversionNew extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 区域id */
    @Excel(name = "区域id")
    private String deptId;

    /** 商品spu */
    @Excel(name = "商品spu")
    private String spuNo;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String spuName;

    /** 转换比例，如1.2表示1公斤=1.2只 */
    @Excel(name = "转换比例，如1.2表示1公斤=1.2只")
    private Double conversion;

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
    public void setSpuNo(String spuNo){
        this.spuNo = spuNo;
    }

    public String getSpuNo(){
        return spuNo;
    }
    public void setSpuName(String spuName){
        this.spuName = spuName;
    }

    public String getSpuName(){
        return spuName;
    }
    public void setConversion(Double conversion){
        this.conversion = conversion;
    }

    public Double getConversion(){
        return conversion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deptId", getDeptId())
            .append("spuNo", getSpuNo())
            .append("spuName", getSpuName())
            .append("conversion", getConversion())
            .toString();
    }
}
