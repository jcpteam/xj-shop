package com.javaboot.shop.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;
import java.util.List;

/**
 * 控价详情对象 store_price_setting_detail
 * 
 * @author javaboot
 * @date 2021-07-04
 */
@Data
@ToString(callSuper = true)
public class StorePriceSettingDetail extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
//    @Excel(name = "控价编号")
    private Integer settingId;

    /** 报价单id */
    private Long priceId;

    /** 报价单id */
//    @Excel(name = "报价单编码")
    private Long quotationId;

    /** 商品SPUid */
//    @Excel(name = "商品SPU编码")
    private String spuNo;
    private String spuId;

    /** 商品goods_id */
    @Excel(name = "商品编码")
    private String goodsId;

    /** 区域中心id */
//    @Excel(name = "区域中心编码")
    private String deptId;

    @Excel(name = "SPU名称")
    private String spuName;
    @Excel(name = "销售区域")
    private String deptName;
    @Excel(name = "报价单名称")
    private String quotationName;
    @Excel(name = "商品销售名称")
    private String propertyName;
    @Excel(name = "结算单位")
    private String unitName;
    private String unitSellName;
    private Double sellPrice;
    private Double subCaseMain;

    /** 单位id */
//    @Excel(name = "单位id")
    private String unitId;

    /** 单位对应的价格 */
    @Excel(name = "结算价格")
    private Double price;

    /** 控价日期 */
    @Excel(name = "控价日期", width = 30)
    private String settingDate;

    /** 状态 0  未审核    1  已审核 */
//    @Excel(name = "状态 0  未审核    1  已审核")
    private String status;

    /** $column.columnComment */
//    @Excel(name = "状态 0  未审核    1  已审核", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;


    /**
     * 商品id集合用于列表查询(勿删)
     */
    private List<Long> goodsIdList;

    private String paramsType;
    private String quotationIds;
    private String spuNos;
    private List<String> quotationIdList;
    private List<String> spuNoList;

    public List<String> getQuotationIdList()
    {
        return quotationIdList;
    }

    public void setQuotationIdList(List<String> quotationIdList)
    {
        this.quotationIdList = quotationIdList;
    }

    public List<String> getSpuNoList()
    {
        return spuNoList;
    }

    public void setSpuNoList(List<String> spuNoList)
    {
        this.spuNoList = spuNoList;
    }

    public String getQuotationIds()
    {
        return quotationIds;
    }

    public void setQuotationIds(String quotationIds)
    {
        this.quotationIds = quotationIds;
    }

    public String getSpuNos()
    {
        return spuNos;
    }

    public void setSpuNos(String spuNos)
    {
        this.spuNos = spuNos;
    }

    public String getParamsType() {
        return paramsType;
    }

    public void setParamsType(String paramsType) {
        this.paramsType = paramsType;
    }

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public String getSpuNo() {
        return spuNo;
    }

    public void setSpuNo(String spuNo) {
        this.spuNo = spuNo;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getQuotationName() {
        return quotationName;
    }

    public void setQuotationName(String quotationName) {
        this.quotationName = quotationName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public List<Long> getGoodsIdList() {
        return goodsIdList;
    }

    public void setGoodsIdList(List<Long> goodsIdList) {
        this.goodsIdList = goodsIdList;
    }

    public String getUnitSellName() {
        return unitSellName;
    }

    public void setUnitSellName(String unitSellName) {
        this.unitSellName = unitSellName;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Double getSubCaseMain() {
        return subCaseMain;
    }

    public void setSubCaseMain(Double subCaseMain) {
        this.subCaseMain = subCaseMain;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }
}
