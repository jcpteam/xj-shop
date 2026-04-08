package com.javaboot.webservice.order;

import lombok.Data;

import java.util.List;

@Data
public class EASOrderReq
{
    private EASOrderInfo bill;

    private List<EASOrderInfoItem> entry;
}
