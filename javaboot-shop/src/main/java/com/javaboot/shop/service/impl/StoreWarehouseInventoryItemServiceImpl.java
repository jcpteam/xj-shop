package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreWarehouseInventoryItemMapper;
import com.javaboot.shop.domain.StoreWarehouseInventoryItem;
import com.javaboot.shop.service.IStoreWarehouseInventoryItemService;
import com.javaboot.common.core.text.Convert;

/**
 * 商品盘库明细Service业务层处理
 * 
 * @author lqh
 * @date 2021-05-23
 */
@Service
public class StoreWarehouseInventoryItemServiceImpl implements IStoreWarehouseInventoryItemService {
    @Autowired
    private StoreWarehouseInventoryItemMapper storeWarehouseInventoryItemMapper;

    /**
     * 查询商品盘库明细
     * 
     * @param itemId 商品盘库明细ID
     * @return 商品盘库明细
     */
    @Override
    public StoreWarehouseInventoryItem selectStoreWarehouseInventoryItemById(Long itemId) {
        return storeWarehouseInventoryItemMapper.selectStoreWarehouseInventoryItemById(itemId);
    }

    /**
     * 查询商品盘库明细列表
     * 
     * @param storeWarehouseInventoryItem 商品盘库明细
     * @return 商品盘库明细
     */
    @Override
    public List<StoreWarehouseInventoryItem> selectStoreWarehouseInventoryItemList(StoreWarehouseInventoryItem storeWarehouseInventoryItem) {
        return storeWarehouseInventoryItemMapper.selectStoreWarehouseInventoryItemList(storeWarehouseInventoryItem);
    }

    /**
     * 新增商品盘库明细
     * 
     * @param storeWarehouseInventoryItem 商品盘库明细
     * @return 结果
     */
    @Override
    public int insertStoreWarehouseInventoryItem(StoreWarehouseInventoryItem storeWarehouseInventoryItem) {
        storeWarehouseInventoryItem.setCreateTime(DateUtils.getNowDate());
        return storeWarehouseInventoryItemMapper.insertStoreWarehouseInventoryItem(storeWarehouseInventoryItem);
    }

    /**
     * 修改商品盘库明细
     * 
     * @param storeWarehouseInventoryItem 商品盘库明细
     * @return 结果
     */
    @Override
    public int updateStoreWarehouseInventoryItem(StoreWarehouseInventoryItem storeWarehouseInventoryItem) {
        return storeWarehouseInventoryItemMapper.updateStoreWarehouseInventoryItem(storeWarehouseInventoryItem);
    }

    /**
     * 删除商品盘库明细对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreWarehouseInventoryItemByIds(String ids) {
        return storeWarehouseInventoryItemMapper.deleteStoreWarehouseInventoryItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品盘库明细信息
     * 
     * @param itemId 商品盘库明细ID
     * @return 结果
     */
    @Override
    public int deleteStoreWarehouseInventoryItemById(Long itemId) {
        return storeWarehouseInventoryItemMapper.deleteStoreWarehouseInventoryItemById(itemId);
    }

    @Override
    public int deleteStoreWarehouseInventoryItemByInventoryId(Long inventoryId) {
        return storeWarehouseInventoryItemMapper.deleteStoreWarehouseInventoryItemByInventoryId(inventoryId);
    }
}
