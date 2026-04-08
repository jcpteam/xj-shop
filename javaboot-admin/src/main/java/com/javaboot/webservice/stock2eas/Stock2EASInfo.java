package com.javaboot.webservice.stock2eas;

import lombok.Data;

@Data
public class Stock2EASInfo
{

    /**
     * 入库单id
     */
    private String billNumber;

    /**
     * 仓库id
     */
    private String storage;

    /**
     * 供应商id
     */
    private String supplier;


    /**
     *  业务类型 purchaseIn,moveIn,moveOut
     */
    private String bizType;

    /**
     *  业务日期
     */
    private String bizDate;

    /**
     *  入库日期
     */
    private String inWarehsDate;


    /**
     * 摘要
     */
    private String remark;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 调拨金额
     */
    private Double adjustAmount;
}
