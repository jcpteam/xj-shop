package com.javaboot.shop.vo;

import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class SortingQueryMerchantInfo extends BaseEntity {
    /**
     * 客户id
     */
    private String merchantId;
    /**
     * 客户名称
     */
    private String merchantName;
}
