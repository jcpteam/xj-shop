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
 * 商品盘库对象 store_warehouse_inventory
 *
 * @author lqh
 * @date 2021-06-14
 */
@Data
@TableName("store_warehouse_inventory")
@EqualsAndHashCode(callSuper = true)
public class  StoreWarehouseInventory extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 盘库id */
    @TableId(value = "inventory_id",type= IdType.AUTO)
    private Long inventoryId;

    /** 盘点编号 */
    @Excel(name = "盘点编号")
    @TableField("inventory_no")
    private String inventoryNo;

    /** 仓库id */
    @Excel(name = "仓库id")
    @TableField("warehouse_id")
    private Long warehouseId;

    /** 区域id */
    @Excel(name = "区域id")
    @TableField("dept_id")
    private String deptId;

    /** 操作者 */
    @Excel(name = "操作者")
    @TableField("creator_id")
    private String creatorId;

    /** 盘点总数量 */
    @Excel(name = "盘点总数量")
    @TableField("quantity")
    private Double quantity;

    /** 盘点只数 */
    @Excel(name = "盘点只数")
    @TableField("lonely_number")
    private Double lonelyNumber;

    /** 盘点总金额 */
    @Excel(name = "盘点总金额")
    @TableField("amount")
    private Double amount;

    /** 盘点日期 */
    @Excel(name = "盘点日期", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("intentory_date")
    private Date intentoryDate;

    /** 盘点开始日期 */
    @TableField("intentory_src_id")
    private Long intentorySrcId;

    /** 0-删除 1-正常 */
    @Excel(name = "0-删除 1-正常")
    @TableField("status")
    private String status;

    /** 0-不覆盖 1-覆盖 */
    @Excel(name = "0-不覆盖 1-覆盖")
    @TableField("replace_flag")
    private String replaceFlag;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("create_time")
    private Date createTime;
    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;

    /** 开始时间 */
    @TableField(exist = false)
    private String startTime;

    /** 结束时间 */
    @TableField(exist = false)
    private String endTime;

}
