package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.javaboot.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 商品单位价格对象 store_goods_quotation_unit_price
 *
 * @author lqh
 * @date 2021-06-21
 */
@Data
@TableName("store_goods_quotation_unit_price")
public class StoreGoodsUnitPrice
{
    /**
     * 报价单商品id
     */
    @TableField(value="goods_id")
    private Long goodsId;

    /**
     * 单位id
     */
    @TableField(value="unit_id")
    private Long unitId;

    /**
     * 单位名称
     */
    @TableField(exist = false)
    private String unitName;

    /**
     * 价格
     */
    @Excel(name = "价格")
    @TableField("price")
    private Double price;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("create_time")
    private Date createTime;



    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;
}
