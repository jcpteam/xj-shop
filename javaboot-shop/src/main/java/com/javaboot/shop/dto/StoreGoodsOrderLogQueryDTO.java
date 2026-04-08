package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StoreGoodsOrderLog;
import lombok.Data;
import lombok.ToString;

/**
 * @Classname StoreGoodsOrderLogQueryDTO
 * @Description 描述
 * @Date 2021/6/3 0003 23:00
 * @@author lqh
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsOrderLogQueryDTO extends StoreGoodsOrderLog {
    /**
     * 创建开始时间
     */
    private String beginDate;
    /**
     * 创建结束时间
     */
    private String endDate;
}
