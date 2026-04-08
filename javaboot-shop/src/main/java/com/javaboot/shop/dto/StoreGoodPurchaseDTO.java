package com.javaboot.shop.dto;

import com.alibaba.fastjson.JSONArray;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreGoodPurchase;
import com.javaboot.shop.domain.StoreGoodPurchaseItem;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodPurchaseDTO
 * @Description 采购
 * @Date 2021/6/5 0005 13:18
 * @@author lqh
 */
@Data
public class StoreGoodPurchaseDTO extends StoreGoodPurchase {


    private String ids;

    private String stockDate;

    /**
     * 采购明细json
     */
    private String itemListJson;
    /**
     * 采购明细
     */
    private List<StoreGoodPurchaseItem> itemList;

    public void setItemListJson(String itemListJson) {
        this.itemListJson = itemListJson;
        if (StringUtils.isNotEmpty(itemListJson)) {
            this.itemList = JSONArray.parseArray(itemListJson, StoreGoodPurchaseItem.class);
        }
    }
}
