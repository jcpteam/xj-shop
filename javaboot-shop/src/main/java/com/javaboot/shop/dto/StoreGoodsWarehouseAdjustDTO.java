package com.javaboot.shop.dto;

import com.alibaba.fastjson.JSONArray;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreGoodsWarehouseAdjust;
import com.javaboot.shop.domain.StoreGoodsWarehouseAdjustItem;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodsWarehouseAdjustDTO
 * @Description 调拨
 * @Date 2021/6/12 0012 15:12
 * @@author lqh
 */
@Data
public class StoreGoodsWarehouseAdjustDTO extends StoreGoodsWarehouseAdjust {

    private String ids;

    private String stockDate;

    /**
     * 调拨明细json
     */
    private String itemListJson;
    /**
     * 调拨明细
     */
    private List<StoreGoodsWarehouseAdjustItem> itemList;

    public void setItemListJson(String itemListJson) {
        this.itemListJson = itemListJson;
        if (StringUtils.isNotEmpty(itemListJson)) {
            this.itemList = JSONArray.parseArray(itemListJson, StoreGoodsWarehouseAdjustItem.class);
        }
    }
}
