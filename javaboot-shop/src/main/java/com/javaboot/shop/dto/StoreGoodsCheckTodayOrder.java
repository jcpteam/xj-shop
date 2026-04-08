package com.javaboot.shop.dto;

import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * @author vvv
 * @description: 一句话功能简述
 * @date 11/10/21 2:00 PM
 **/
@Data
public class StoreGoodsCheckTodayOrder extends BaseEntity {
    /**
     * 创建开始时间
     */
    private String createBeginDate;
    /**
     * 创建结束时间
     */
    private String createEndDate;

    /**
     * 商户id
     */
    private String merchantId;
}
