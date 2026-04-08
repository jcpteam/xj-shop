package com.javaboot.shop.dto;

import com.javaboot.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class StoreSaleSettingExportDTO
{
    /**
     * 区域id
     */
    @Excel(name = "区域编号")
    private String deptId;

    /**
     * 区域id
     */
    @Excel(name = "区域名称")
    private String deptName;

    /**
     * 商品SPU
     */
    @Excel(name = "SPU")
    private String spuNo;

    /**
     * 商品SPU
     */
    @Excel(name = "商品名称", width = 30)
    private String spuName;

    /**
     * 商品SPU
     */
    @Excel(name = "上数调整量")
    private String quanintiy;

    /**
     * 上数日期
     */
    @Excel(name = "上数日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

}
