package com.javaboot.shop.vo;

import java.util.List;

import com.javaboot.shop.domain.StoreWarehouseStock;
import com.javaboot.shop.domain.StoreWarehouseStockItem;

import lombok.Data;

/**
 * @Classname StoreWarehouseStockVO
 * @Description 描述
 * @Date 2021/6/5 0005 10:22
 * @@author lqh
 */
@Data
public class StoreWarehouseStockVO extends StoreWarehouseStock {
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 仓库所属区域
     */
    private String deptName;
    /**
     * 仓库类型名称
     */
    private String stockTypeName;
    /**
     * 入库类别：1-正常入库,2-退货入库
     */
    private String categoryName;
    /**
     * 入库明细
     */
    private List<StoreWarehouseStockItem> itemList;
}
