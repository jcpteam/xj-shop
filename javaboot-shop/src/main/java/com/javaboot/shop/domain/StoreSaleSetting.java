package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品上数设置对象 store_sale_setting
 *
 * @author lqh
 * @date 2021-05-19
 */
@Data
@TableName("store_sale_setting")
@EqualsAndHashCode(callSuper = true)
public class StoreSaleSetting extends BaseEntity
{

    private static final long serialVersionUID = -1488831743550461506L;

    /**
     * 上数id
     */
    @TableId(value="setting_id",type= IdType.AUTO)
    private Long settingId;

    /**
     * 区域id
     */
    @Excel(name = "区域id")
    @TableField("dept_id")
    private String deptId;

    /**
     * 区域id
     */
    @Excel(name = "区域名称")
    @TableField(exist = false)
    private String deptName;

    /**
     * 商品SPU
     */
    @Excel(name = "商品SPU")
    @TableField("spu_no")
    private String spuNo;

    /**
     * 商品SPU
     */
    @Excel(name = "商品名称")
    @TableField(exist = false)
    private String spuName;

    /**
     * 昨日库存数量
     */
    @Excel(name = "昨日库存数量")
    @TableField("quanintiy")
    private Double quanintiy;

    /**
     * 在途数量
     */
    @Excel(name = "在途数量")
    @TableField("in_quanintiy")
    private Double inQuanintiy;

    /**
     * 到货数量
     */
    @Excel(name = "到货数量")
    @TableField("ready_quanintiy")
    private Double readyQuanintiy;

    /**
     * 上数商品规格
     */
    @Excel(name = "上数商品规格")
    @TableField("specifications")
    private String specifications;

    /**
     * 上数数量
     */
    @Excel(name = "上数数量")
    @TableField("setting_quanintiy")
    private Double settingQuanintiy;

    /**
     * 上数数量
     */
    @Excel(name = "上数数量")
    @TableField("self_quanintiy")
    private Double selfQuanintiy;

    /**
     * 订单数量
     */
    @Excel(name = "订单数量")
    @TableField(exist = false)
    private Double orderQuanintiy;

    /**
     * 上数数量
     */
    @Excel(name = "状态 0-删除 1-正常")
    @TableField("status")
    private String status;


    /**
     * 最后更新时间
     */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("create_time")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;

    /**
     * 部门id列表
     */
    @TableField(exist = false)
    private List<String> deptIdList;

    /**
     * 主单位id
     */
    @TableField(exist = false)
    private Long mainUnitId;


    /**
     * 父单位id
     */
    @TableField(exist = false)
    private Long subUnitId;

    @TableField(exist = false)
    private List<String> spuNoList;

    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;

    @TableField(exist = false)
    private String createTimeStart;

    @TableField(exist = false)
    private String createTimeEnd;

}
