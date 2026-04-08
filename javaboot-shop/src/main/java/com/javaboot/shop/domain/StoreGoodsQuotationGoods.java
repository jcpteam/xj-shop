package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

/**
 * 商品列表对象 store_goods_quotation_goods
 *
 * @author lqh
 * @date 2021-05-23
 */
@Data
@TableName("store_goods_quotation_goods")
@EqualsAndHashCode(callSuper = true)
public class StoreGoodsQuotationGoods extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "goods_id", type = IdType.AUTO)
    private Long goodsId;

    /**
     * 报价单id
     */
    @TableField("quotation_id")
    private Long quotationId;

    /**
     * 商品spu
     */
    @Excel(name = "商品spu")
    @TableField("spu_no")
    private String spuNo;


    /**
     * 单价
     */
    @Excel(name = "单价")
    @TableField("price")
    private Double price;

    /**
     * 属性名称
     */
    @Excel(name = "属性名称")
    @TableField("property_name")
    private String propertyName;

    /**
     * 商品副标题
     */
    @Excel(name = "商品副标题")
    @TableField("goods_subtitle")
    private String goodsSubtitle;

    /**
     * 商品介绍
     */
    @Excel(name = "商品介绍")
    @TableField("goods_introduce")
    private String goodsIntroduce;

    /**
     * 所属商户等级
     */
    @Excel(name = "所属商户等级")
    @TableField("level")
    private String level;

    /**
     * 所属商户id
     */
    @Excel(name = "所属商户id")
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 状态:0-删除，1上架，2-下架
     */
    @Excel(name = "状态:0-删除，1上架，2-下架")
    @TableField("status")
    private String status;

    /**
     * 最后更新时间
     */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;


    /**
     * 销售单位id
     */
    @Excel(name = "销售单位id")
    @TableField("unit_ids")
    private String unitIds;


    /**
     * 上数设置百分比
     */
    @Excel(name = "上数设置库存数")
    @TableField("sale_num")
    private Double saleNum;

    /**
     * 上数设置百分比
     */
    @Excel(name = "上数设置百分比")
    @TableField("sale_percent")
    private Double salePercent;

    @TableField(exist = false)
    private String unitJson;
    /**
     * 图片
     */
    @TableField("url")
    private String url;

    /**
     * 图片
     */
    @TableField(exist = false)
    private String deptId;

}
