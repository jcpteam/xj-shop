package com.javaboot.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboot.shop.domain.StoreWarehouseStockItem;

import java.util.List;

/**
 * 商品入库明细Service接口
 *
 * @author lqh
 * @date 2021-05-23
 */
public interface IStoreWarehouseStockItemService extends IService<StoreWarehouseStockItem> {
    /**
     * 查询商品入库明细
     *
     * @param itemId 商品入库明细ID
     * @return 商品入库明细
     */
    public StoreWarehouseStockItem selectStoreWarehouseStockItemById(Long itemId);

    /**
     * 查询商品入库明细列表
     *
     * @param storeWarehouseStockItem 商品入库明细
     * @return 商品入库明细集合
     */
    public List<StoreWarehouseStockItem> selectStoreWarehouseStockItemList(StoreWarehouseStockItem storeWarehouseStockItem);

    /**
     * 新增商品入库明细
     *
     * @param storeWarehouseStockItem 商品入库明细
     * @return 结果
     */
    public int insertStoreWarehouseStockItem(StoreWarehouseStockItem storeWarehouseStockItem);



    /**
     * 修改商品入库明细
     *
     * @param storeWarehouseStockItem 商品入库明细
     * @return 结果
     */
    public int updateStoreWarehouseStockItem(StoreWarehouseStockItem storeWarehouseStockItem);

    /**
     * 批量删除商品入库明细
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreWarehouseStockItemByIds(String ids);

    /**
     * 删除商品入库明细信息
     *
     * @param itemId 商品入库明细ID
     * @return 结果
     */
    public int deleteStoreWarehouseStockItemById(Long itemId);

    public List<StoreWarehouseStockItem> selectStockItemListForSorting(StoreWarehouseStockItem storeWarehouseStockItem);
}
