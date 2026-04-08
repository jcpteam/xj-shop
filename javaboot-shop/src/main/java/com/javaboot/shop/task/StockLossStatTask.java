package com.javaboot.shop.task;

import com.javaboot.shop.domain.AsLossStock;
import com.javaboot.shop.service.IAsLossStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("stockLossStatTask")
public class StockLossStatTask
{

    @Autowired
    private IAsLossStockService asLossStockService;

    public void stat(String start){
        AsLossStock asLossStock = new AsLossStock();
        asLossStock.setQryDate(start);
        asLossStockService.insertAsLossStock(asLossStock);
    }
}
