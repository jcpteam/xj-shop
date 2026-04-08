package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StoreGoodsQuotationGoods;
import lombok.Data;

import java.util.List;

/**
 * @author lqh
 */
@Data
public class StoreSaleSettingDTO
{
    private Long settingId;

    List<StoreGoodsQuotationGoods> goodsList;
}
