package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 商品入库明细对象 store_warehouse_stock_item
 *
 * @author lqh
 * @date 2021-05-23
 */
@Data
@TableName("store_temp_stock_item")
public class  StoreTempStockItem{

    /**
     * 入库明细id
     */
    @TableId(value = "item_id", type = IdType.INPUT)
    private String itemId;

    /**
     * 入库id
     */
    @TableField(value = "stock_no")
    private String stockNo;

    /**
     * 入库单json
     */
    @TableField("item_json")
    private String itemJson;

}
