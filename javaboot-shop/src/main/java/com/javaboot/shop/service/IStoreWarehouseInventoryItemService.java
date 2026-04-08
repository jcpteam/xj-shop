package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreWarehouseInventoryItem;
import java.util.List;

/**
 * 商品盘库明细Service接口
 * 
 * @author lqh
 * @date 2021-05-23
 */
public interface IStoreWarehouseInventoryItemService {
    /**
     * 查询商品盘库明细
     * 
     * @param itemId 商品盘库明细ID
     * @return 商品盘库明细
     */
    public StoreWarehouseInventoryItem selectStoreWarehouseInventoryItemById(Long itemId);

    /**
     * 查询商品盘库明细列表
     * 
     * @param storeWarehouseInventoryItem 商品盘库明细
     * @return 商品盘库明细集合
     */
    public List<StoreWarehouseInventoryItem> selectStoreWarehouseInventoryItemList(StoreWarehouseInventoryItem storeWarehouseInventoryItem);

    /**
     * 新增商品盘库明细
     * 
     * @param storeWarehouseInventoryItem 商品盘库明细
     * @return 结果
     */
    public int insertStoreWarehouseInventoryItem(StoreWarehouseInventoryItem storeWarehouseInventoryItem);

    /**
     * 修改商品盘库明细
     * 
     * @param storeWarehouseInventoryItem 商品盘库明细
     * @return 结果
     */
    public int updateStoreWarehouseInventoryItem(StoreWarehouseInventoryItem storeWarehouseInventoryItem);

    /**
     * 批量删除商品盘库明细
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreWarehouseInventoryItemByIds(String ids);

    /**
     * 删除商品盘库明细信息
     * 
     * @param itemId 商品盘库明细ID
     * @return 结果
     */
    public int deleteStoreWarehouseInventoryItemById(Long itemId);
    /**
     * 删除商品盘库明细信息
     *
     * @param inventoryId 商品盘库ID
     * @return 结果
     */
    int deleteStoreWarehouseInventoryItemByInventoryId(Long inventoryId);
}
