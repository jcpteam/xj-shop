package com.javaboot.webservice.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EASOrderInfoItem
{
    private String material;

    /**
     * 辅助单位
     */
    private BigDecimal assistQty;

    private BigDecimal qty;

    private BigDecimal price;

    private BigDecimal amount;

    private Integer supplyMode;

    private String sendDate;

    private String deliveryDate;

    private String warehouse;

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
