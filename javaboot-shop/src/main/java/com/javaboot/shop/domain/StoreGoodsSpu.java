package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 商品SPU信息对象 store_goods_spu
 *
 * @author lqh
 * @date 2021-05-23
 */
@Data
@TableName("store_goods_spu")
@EqualsAndHashCode(callSuper = true)
public class StoreGoodsSpu extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品spu
     */
    @TableId("spu_no")
    private String spuNo;

    /**
     * 编号
     */
    @Excel(name = "编号")
    @TableField("code")
    private String code;

    /**
     * 商品图片
     */
    @Excel(name = "商品图片")
    @TableField("image")
    private String image;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称")
    @TableField("name")
    private String name;



    /**
     * 基本单位
     */
    @TableField("unit")
    @Excel(name = "基本单位")
    private String unit;

    /**
     * 商品描述
     */
    @Excel(name = "商品描述")
    @TableField("description")
    private String description;

    /**
     * 状态:0-删除 1-正常
     */
    @TableField("status")
    @Excel(name = "状态:0-删除 1-正常")
    private String status;
    /**
     * 商品分类
     */
    @Excel(name = "商品分类")
    @TableField("classify_id")
    private String classifyId;
    /**
     * 最后更新时
     */
    @Excel(name = "最后更新时", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;


}
