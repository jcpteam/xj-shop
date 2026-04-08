package com.javaboot.shop.vo;

import com.javaboot.common.annotation.Excel;
import com.javaboot.shop.domain.StoreGoodsQuotation;
import lombok.Data;

/**
 * @Classname StoreGoodsQuotationVO
 * @Description 描述
 * @Date 2021/5/20 0020 21:53
 * @@author lqh
 */
@Data
public class StoreGoodsQuotationVO extends StoreGoodsQuotation {

    /**
     * 定价开始时间
     */
    @Excel(name = "定价开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String startTimeText;

    /**
     * 定价结束时间
     */
    @Excel(name = "定价结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String endTimeText;

    /**
     * 运营开始时间
     */
    @Excel(name = "运营开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String operateStartTimeText;

    /**
     * 运营结束时间
     */
    @Excel(name = "运营结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String operateEndTimeText;

}
