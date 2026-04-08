package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreWarehouseStockItem;
import com.javaboot.shop.mapper.StoreWarehouseStockItemMapper;
import com.javaboot.shop.service.IStoreWarehouseStockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品入库明细Service业务层处理
 *
 * @author lqh
 * @date 2021-05-23
 */
@Service
public class StoreWarehouseStockItemServiceImpl extends ServiceImpl<StoreWarehouseStockItemMapper, StoreWarehouseStockItem> implements IStoreWarehouseStockItemService {
    @Autowired
    private StoreWarehouseStockItemMapper storeWarehouseStockItemMapper;

    /**
     * 查询商品入库明细
     *
     * @param itemId 商品入库明细ID
     * @return 商品入库明细
     */
    @Override
    public StoreWarehouseStockItem selectStoreWarehouseStockItemById(Long itemId) {
        return storeWarehouseStockItemMapper.selectStoreWarehouseStockItemById(itemId);
    }

    /**
     * 查询商品入库明细列表
     *
     * @param storeWarehouseStockItem 商品入库明细
     * @return 商品入库明细
     */
    @Override
    public List<StoreWarehouseStockItem> selectStoreWarehouseStockItemList(StoreWarehouseStockItem storeWarehouseStockItem) {
        return storeWarehouseStockItemMapper.selectStoreWarehouseStockItemList(storeWarehouseStockItem);
    }

    /**
     * 新增商品入库明细
     *
     * @param storeWarehouseStockItem 商品入库明细
     * @return 结果
     */
    @Override
    public int insertStoreWarehouseStockItem(StoreWarehouseStockItem storeWarehouseStockItem) {
        storeWarehouseStockItem.setCreateTime(DateUtils.getNowDate());
        return storeWarehouseStockItemMapper.insertStoreWarehouseStockItem(storeWarehouseStockItem);
    }


    /**
     * 修改商品入库明细
     *
     * @param storeWarehouseStockItem 商品入库明细
     * @return 结果
     */
    @Override
    public int updateStoreWarehouseStockItem(StoreWarehouseStockItem storeWarehouseStockItem) {
        return storeWarehouseStockItemMapper.updateStoreWarehouseStockItem(storeWarehouseStockItem);
    }

    /**
     * 删除商品入库明细对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreWarehouseStockItemByIds(String ids) {
        return storeWarehouseStockItemMapper.deleteStoreWarehouseStockItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品入库明细信息
     *
     * @param itemId 商品入库明细ID
     * @return 结果
     */
    @Override
    public int deleteStoreWarehouseStockItemById(Long itemId) {
        return storeWarehouseStockItemMapper.deleteStoreWarehouseStockItemById(itemId);
    }

    @Override
    public List<StoreWarehouseStockItem> selectStockItemListForSorting(StoreWarehouseStockItem storeWarehouseStockItem) {
        return storeWarehouseStockItemMapper.selectStockItemListForSorting(storeWarehouseStockItem);
    }
}
