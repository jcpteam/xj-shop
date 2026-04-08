package com.javaboot.shop.vo;

import com.javaboot.shop.domain.StoreWarehouseInventory;
import com.javaboot.shop.domain.StoreWarehouseInventoryItem;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreWarehouseInventoryVO
 * @Description 盘点
 * @Date 2021/6/14 0014 15:15
 * @@author lqh
 */
@Data
public class StoreWarehouseInventoryVO extends StoreWarehouseInventory {
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 区域名称
     */
    private String deptName;
    /**
     * 盘点人名称
     */
    private String creatorIdName;
    /**
     * 盘点日期
     */
    private String intentoryDateText;
    /**
     * 盘点明细
     */
    private List<StoreWarehouseInventoryItem> itemList;
}
