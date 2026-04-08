package com.javaboot.shop.dto;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreWarehouseStock;
import com.javaboot.shop.domain.StoreWarehouseStockItem;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreWarehouseStockDTO
 * @Description 入库dto
 * @Date 2021/6/4 0004 17:21
 * @@author lqh
 */
@Data
public class StoreWarehouseStockDTO extends StoreWarehouseStock {

    /**
     * 入库明细json
     */
    private String itemListJson;
    /**
     * 入库明细
     */
    private List<StoreWarehouseStockItem> itemList;

    /**
     * 入库单id
     */
    private String ids;

    /**
     * 审核状态1-仓库管理员审核 2-财务审核
     */
    private String auditStatus;

    /**
     * 采购单，调拨单是否替换原来入库单的json值
     */
    private String replaceJson;

    public void setItemListJson(String itemListJson) {
        this.itemListJson = itemListJson;
        if (StringUtils.isNotEmpty(itemListJson)) {
            this.itemList = JSONArray.parseArray(itemListJson, StoreWarehouseStockItem.class);
        }
    }
}
