package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;
import java.util.List;

/**
 * spu商品单位换算关系对象 store_goods_spu_unit_conversion
 * 
 * @author lqh
 * @date 2021-06-20
 */
@Data
@ToString(callSuper = true)
@TableName("store_goods_spu_unit_conversion")
public class StoreGoodsSpuUnitConversion extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 区域id */
    @Excel(name = "区域id")
    @TableField("dept_id")
    private String deptId;

    /** 商品spu */
    @Excel(name = "商品spu")
    @TableField("spu_no")
    private String spuNo;

    /** 转主单位 */
    @Excel(name = "转主单位")
    @TableField("sub_case_main")
    private Double subCaseMain;

    /** 转副单位 */
    @Excel(name = "转副单位")
    @TableField("main_case_sub")
    private Double mainCaseSub;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("create_time")
    private Date lastModifyTime;

    @TableField(exist = false)
    private List<String> spuNoList;

    /** 主单位id */
    @TableField(exist = false)
    private Long mainUnitId;

    /** 副单位id */
    @TableField(exist = false)
    private Long subUnitId;

}
