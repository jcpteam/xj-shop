package com.javaboot.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboot.shop.domain.StoreTempStock;
import com.javaboot.shop.domain.StoreWarehouseStockItem;
import com.javaboot.shop.dto.StoreWarehouseStockDTO;
import com.javaboot.shop.dto.StoreWarehouseStockQueryDTO;
import com.javaboot.shop.vo.StoreWarehouseStockVO;

import java.util.List;

/**
 * 商品入库Service接口
 * 
 * @author lqh
 * @date 2021-05-20
 */
public interface IStoreTempStockService extends IService<StoreTempStock>
{
}
