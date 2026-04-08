package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.shop.domain.StoreGoodsUnitPrice;
import com.javaboot.shop.mapper.StoreGoodsUnitPriceMapper;
import com.javaboot.shop.service.IStoreGoodsUnitPriceService;
import org.springframework.stereotype.Service;

/**
 * 商品单位价格Service接口
 *
 * @author lqh
 * @date 2021-06-21
 */
@Service
public class StoreGoodsUnitPriceServiceImpl extends ServiceImpl<StoreGoodsUnitPriceMapper, StoreGoodsUnitPrice> implements
    IStoreGoodsUnitPriceService
{
}
