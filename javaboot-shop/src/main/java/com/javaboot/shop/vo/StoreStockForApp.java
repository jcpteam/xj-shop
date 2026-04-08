package com.javaboot.shop.vo;

import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.domain.StoreMember;
import lombok.Data;

import java.util.List;

/**
 * @author javaboot
 * 获取钉钉列表返回值
 */
@Data
public class StoreStockForApp
{
    private StoreWarehouseStockVO storeWarehouseStock;

    private List<StoreHouse> houseList;
}
