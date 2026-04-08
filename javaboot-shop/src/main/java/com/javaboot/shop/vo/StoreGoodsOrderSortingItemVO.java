package com.javaboot.shop.vo;

import com.javaboot.shop.domain.StoreSortingBox;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodsOrderItemVO
 * @Description 订单明细
 * @Date 2021/6/10 0010 21:30
 * @@author lqh
 */
@Data
public class StoreGoodsOrderSortingItemVO extends StoreGoodsOrderItemVO {
    /**
     * 分拣框子列表
     */
    private List<StoreSortingBox> sortingBoxList;

    private Long mainUnitId; //主单位
    private String mainUnitName; //主单位名称
    private Long subUnitId;  //副单位
    private String subUnitName;  //副单位名称
    private Double subCaseMain;  //副单位转主单位比例值
    private Double mainCaseSub;  //主单位转副单位比例值


    private Long unitId1;   //经过比较后前端显示除销售单位外的单位id
    private String unitName1;  //经过比较后前端显示除销售单位外的单位名称
    private Double quantity1;  //经过比较后前端显示除销售单位外的订单数量值
}
