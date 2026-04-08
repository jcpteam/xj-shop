package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品调拨信息对象 store_goods_warehouse_adjust
 * 
 * @author lqh
 * @date 2021-06-10
 */
@Data
@TableName("store_goods_warehouse_adjust")
@EqualsAndHashCode(callSuper = true)
public class StoreGoodsWarehouseAdjust extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 调拨编号 */
    @Excel(name = "调拨编号")
    @TableField("adjust_no")
    private String adjustNo;

    /** 调拨id */
    @TableId(value="adjust_id",type= IdType.AUTO)
    private Long adjustId;

    /** 区域id */
    @TableField("dept_id")
    private String deptId;

    /** 目标仓库id */
    @TableField("target_warehouse_id")
    private Long targetWarehouseId;

    /** 源仓库id */
    @TableField("source_warehouse_id")
    private Long sourceWarehouseId;

    /** 操作者id */
    @TableField("creator_id")
    private String creatorId;

    /** 调拨日期 */
    @TableField("adjust_time")
    private Date adjustTime;

    /** 最后更新时间 */
    @TableField("last_modify_time")
    private Date lastModifyTime;

    /**
     * 状态:0-删除，1-正常
     */
    @TableField("status")
    private String status;

    /**
     * 状态:0-删除，1-正常
     */
    @Excel(name = "入库单状态:1-未生成，1-已生成")
    @TableField("stock_status")
    private String stockStatus;

    /**
     * 状态:0-待同步，1-已同步
     */
    @TableField("syn_status")
    private String synStatus;

    /**
     * 操作开始时间
     */
    @TableField(exist = false)
    private String beginDate;

    /**
     * 操作结束时间
     */
    @TableField(exist = false)
    private String endDate;

    /**
     * 商品备注
     */
    @Excel(name = "商品备注")
    @TableField("comment")
    private String comment;

}
