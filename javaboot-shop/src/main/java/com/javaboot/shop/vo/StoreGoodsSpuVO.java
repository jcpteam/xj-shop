package com.javaboot.shop.vo;

import com.javaboot.common.annotation.Excel;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import com.javaboot.shop.domain.StoreGoodsSpu;
import com.javaboot.shop.domain.StoreGoodsSpuUnit;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodsSpuVO
 * @Description 描述
 * @Date 2021/5/28 0028 11:24
 * @@author lqh
 */
@Data
public class StoreGoodsSpuVO extends StoreGoodsSpu {

    /**
     * 分类名称
     */
    @Excel(name = "分类名称")
    private String classifyName;

    /**
     * 主单位
     */
    private  String mainUnitId;

    /**
     * 主单位名称
     */
    private  String mainUnitName;

    /**
     * 副单位
     */
    private  String subUnitId;

    /**
     * 副单位名称
     */
    private  String subUnitName;

    /**
     * 主单位与副单位换算关系
     */
    private  Double mainCaseSub;

    /**
     * 副单位与主单位换算关系
     */
    private  Double subCaseMain;


    /**
     * 单位信息
     */
    List<StoreGoodsSalesUnit> spuUnitList;
}
