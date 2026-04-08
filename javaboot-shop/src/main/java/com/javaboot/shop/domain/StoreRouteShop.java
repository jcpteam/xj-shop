package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;

/**
 * 线路和商户关联对象 store_route_shop
 * 
 * @author javaboot
 * @date 2021-06-05
 */
public class StoreRouteShop extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 线路id */
    @Excel(name = "线路id")
    private Long routeId;

    private String routeName;    /** 商户id */
    @Excel(name = "商户id")
    private Long customerId;

    private String customerName;

    private String adress;

    private String phone;    /** 配送顺序 */
    @Excel(name = "配送顺序")
    private Integer sequenceNo;

    private String searchKey;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setRouteId(Long routeId){
        this.routeId = routeId;
    }

    public Long getRouteId(){
        return routeId;
    }
    public void setCustomerId(Long customerId){
        this.customerId = customerId;
    }

    public Long getCustomerId(){
        return customerId;
    }
    public void setSequenceNo(Integer sequenceNo){
        this.sequenceNo = sequenceNo;
    }

    public Integer getSequenceNo(){
        return sequenceNo;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "StoreRouteShop{" +
                "id=" + id +
                ", routeId=" + routeId +
                ", routeName='" + routeName + '\'' +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", adress='" + adress + '\'' +
                ", phone='" + phone + '\'' +
                ", sequenceNo=" + sequenceNo +
                '}';
    }
}
