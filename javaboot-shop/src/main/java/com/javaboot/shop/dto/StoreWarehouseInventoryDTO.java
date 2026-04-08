package com.javaboot.shop.dto;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreGoodPurchaseItem;
import com.javaboot.shop.domain.StoreWarehouseInventory;
import com.javaboot.shop.domain.StoreWarehouseInventoryItem;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreWarehouseInventoryDTO
 * @Description 盘点
 * @Date 2021/6/14 0014 15:13
 * @@author lqh
 */
@Data
public class StoreWarehouseInventoryDTO extends StoreWarehouseInventory {

    private Boolean isQueryAll;

    public Boolean getIsQueryAll() {
        if (isQueryAll == null) {
            isQueryAll = false;
        }
        return isQueryAll;
    }

    /**
     * 盘点明细json
     */
    private String itemListJson;


    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;

    /**
     * 商品SPU
     */
    private String spuNo;

    /**
     * 商品SPU
     */
    private String category;

    /**
     * 盘点明细
     */
    private List<StoreWarehouseInventoryItem> itemList;

    public void setItemListJson(String itemListJson) {
        this.itemListJson = itemListJson;
        if (StringUtils.isNotEmpty(itemListJson)) {
            this.itemList = JSONArray.parseArray(itemListJson, StoreWarehouseInventoryItem.class);
        }
    }
}
