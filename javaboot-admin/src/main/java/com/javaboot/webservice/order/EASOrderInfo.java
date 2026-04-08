package com.javaboot.webservice.order;

import lombok.Data;

@Data
public class EASOrderInfo
{
    private String orderNumber;

    private String bizType;

    private String customer;

    private String saleOrg;

    private String salePerson;

    private String creator;

    private String paymentType;

    private String remark;

    private String bizDate;
}
