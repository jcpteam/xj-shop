package com.javaboot.webservice.adjust;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EASAdjustItem
{
    private String material;

    private BigDecimal qty;

    private BigDecimal price;

    private BigDecimal amount;

    private Date inPlanDate;

    private Date outPlanDate;

    private String inWarehouse;

    private String outWarehouse;

    public BigDecimal getQty()
    {
        return qty.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPrice()
    {
        return price.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getAmount()
    {
        return amount.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
}
