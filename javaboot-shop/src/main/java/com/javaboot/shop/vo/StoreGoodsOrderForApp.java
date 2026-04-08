package com.javaboot.shop.vo;

import com.javaboot.shop.domain.StoreMember;
import lombok.Data;

import java.util.List;

/**
 * @author javaboot
 * 获取钉钉列表返回值
 */
@Data
public class StoreGoodsOrderForApp
{
    private StoreGoodsOrderVO storeGoodsOrder;

    private StoreMember storeMember;

    private List<StoreGoodsQuotationGoodsVO> goodsList;
}
