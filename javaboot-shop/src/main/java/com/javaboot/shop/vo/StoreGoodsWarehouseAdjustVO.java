package com.javaboot.shop.vo;

import com.javaboot.common.annotation.Excel;
import com.javaboot.shop.domain.StoreGoodsWarehouseAdjust;
import com.javaboot.shop.domain.StoreGoodsWarehouseAdjustItem;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodsWarehouseAdjustVO
 * @Description 调拨
 * @Date 2021/6/12 0012 15:10
 * @@author lqh
 */
@Data
public class StoreGoodsWarehouseAdjustVO extends StoreGoodsWarehouseAdjust {

    /**
     * 所属区域
     */
    @Excel(name = "目标仓库")
    private String deptName;
    /**
     * 源仓库
     */
    @Excel(name = "源仓库")
    private String sourceWarehouseName;
    /**
     * 目标仓库
     */
    @Excel(name = "目标仓库")
    private String targetWarehouseName;
    /**
     * 调拨人名称
     */
    @Excel(name = "调拨人名称")
    private String creatorName;
    /**
     * 调拨时间
     */
    @Excel(name = "调拨时间")
    private String adjustTimeText;

    /**
     * 所属仓库
     */
    private String stockTime;

    /**
     * 调拨明细
     */
    private List<StoreGoodsWarehouseAdjustItem> itemList;
}
