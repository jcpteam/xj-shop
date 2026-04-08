package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 商品上数日志对象 store_sale_setting_log
 * 
 * @author lqh
 * @date 2021-06-01
 */
@Data
@TableName("store_sale_setting_log")
@EqualsAndHashCode(callSuper = true)
public class StoreSaleSettingLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 日志id */
    @TableId(value="log_id",type= IdType.AUTO)
    private Long logId;

    /** 上数id */
    @Excel(name = "上数id")
    @TableField("setting_id")
    private Long settingId;

    /** 报价单商品id */
    @Excel(name = "报价单商品id")
    @TableField("good_id")
    private Long goodId;

    /**
     * 报价单商品名称
     */
    @TableField(exist = false)
    private String goodName;

    /** 商品SPU */
    @Excel(name = "商品SPU")
    @TableField("spu_no")
    private String spuNo;

    /**
     * 商品名称
     */
    @TableField(exist = false)
    private String spuName;

    /** 原内容json */
    @Excel(name = "原内容json")
    @TableField("old_content")
    private String oldContent;

    /** 修改后内容json */
    @Excel(name = "修改后内容json")
    @TableField("new_content")
    private String newContent;

    /** 创建人 */
    @Excel(name = "创建人")
    @TableField("creator_id")
    private String creatorId;

    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String creatorName;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;

    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String quotatioName;


}
