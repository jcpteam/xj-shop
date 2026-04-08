package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 商品入库对象 store_warehouse_stock
 * 
 * @author lqh
 * @date 2021-05-20
 */
@Data
@TableName("store_temp_stock")
public class StoreTempStock {

    private static final long serialVersionUID = 2034424502767370147L;

    /** 入库单编号 */
    @TableId(value = "stock_no", type = IdType.INPUT)
    private String stockNo;

}
