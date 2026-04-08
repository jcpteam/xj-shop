package com.javaboot.shop.vo;

import com.javaboot.common.core.domain.BaseEntity;
import com.javaboot.shop.domain.StoreDeliverRoute;
import com.javaboot.shop.domain.StoreGoodsQuotation;
import lombok.Data;

import java.util.List;

@Data
public class StoreSortingQueryParamsVO extends BaseEntity {
    private List<SortingQueryMerchantInfo> merchantInfoList;

    private List<StoreDeliverRoute> routeList;

    private List<StoreGoodsQuotation> quotationList;
}
