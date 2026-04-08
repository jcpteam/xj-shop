package com.javaboot.webservice.stock;

import lombok.Data;

import java.util.List;

@Data
public class EASStockReq
{
    private EASStockInfo bill;

    private List<EASStockItem> entry;
}
