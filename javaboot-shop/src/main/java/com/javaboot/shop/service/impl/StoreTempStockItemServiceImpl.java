package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.shop.domain.StoreTempStockItem;
import com.javaboot.shop.mapper.StoreTempStockItemMapper;
import com.javaboot.shop.service.IStoreTempStockItemService;
import org.springframework.stereotype.Service;

/**
 * 商品入库Service业务层处理
 * 
 * @author lqh
 * @date 2021-05-20
 */
@Service
public class StoreTempStockItemServiceImpl extends ServiceImpl<StoreTempStockItemMapper, StoreTempStockItem>
    implements IStoreTempStockItemService
{

}
