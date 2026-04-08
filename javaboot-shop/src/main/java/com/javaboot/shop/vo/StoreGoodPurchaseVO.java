package com.javaboot.shop.vo;

import com.javaboot.shop.domain.StoreGoodPurchase;
import com.javaboot.shop.domain.StoreGoodPurchaseItem;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodPurchaseQueryVO
 * @Description 采购
 * @Date 2021/6/5 0005 13:20
 * @@author lqh
 */
@Data
public class StoreGoodPurchaseVO extends StoreGoodPurchase {
    /**
     * 所属区域
     */
    private String deptName;

    /**
     * 所属仓库
     */
    private String warehouseName;

    /**
     * 所属仓库
     */
    private String stockTime;

    /**
     * 采购明细
     */
    private List<StoreGoodPurchaseItem> itemList;
}
