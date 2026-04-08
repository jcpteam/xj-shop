package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * loss对象 as_loss_stock
 * 
 * @author javaboot
 * @date 2022-01-20
 */
@Data
@TableName("as_loss_stock")
@EqualsAndHashCode(callSuper = true)
public class AsLossStock extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String deptName;

    /**
     * 商品名称
     */
    @TableField(exist = false)
    private String spuName;

    /** 商品spu */
    @TableField("spu_no")
    private String spuNo;

    /** 区域id */
    @TableField("dept_id")
    private String deptId;

    /** 统计日期 */
    @TableField("stat_date")
    private Date statDate;

    /** 总损耗 */
    @Excel(name = "总损耗")
    @TableField("total_loss_quanity")
    private Double totalLossQuanity;

    /** 订单损耗 */
    @Excel(name = "订单损耗")
    @TableField("order_loss_quanity")
    private Double orderLossQuanity;

    /** 入库损耗 */
    @Excel(name = "入库损耗")
    @TableField("stock_loss_quanity")
    private Double stockLossQuanity;

    /** 在途损耗 */
    @Excel(name = "在途损耗")
    @TableField("intransit_loss_quanity")
    private Double intransitLossQuanity;

    /**
     * 查询条件-日期
     */
    @TableField(exist = false)
    private String qryDate;

}
