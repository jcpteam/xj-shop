package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreGoodsWarehouseAdjustItem;
import java.util.List;

/**
 * 商品调拨明细Mapper接口
 * 
 * @author lqh
 * @date 2021-06-10
 */
public interface StoreGoodsWarehouseAdjustItemMapper {
    /**
     * 查询商品调拨明细
     * 
     * @param itemId 商品调拨明细ID
     * @return 商品调拨明细
     */
    public StoreGoodsWarehouseAdjustItem selectStoreGoodsWarehouseAdjustItemById(Long itemId);

    /**
     * 查询商品调拨明细列表
     * 
     * @param storeGoodsWarehouseAdjustItem 商品调拨明细
     * @return 商品调拨明细集合
     */
    public List<StoreGoodsWarehouseAdjustItem> selectStoreGoodsWarehouseAdjustItemList(StoreGoodsWarehouseAdjustItem storeGoodsWarehouseAdjustItem);

    /**
     * 新增商品调拨明细
     * 
     * @param storeGoodsWarehouseAdjustItem 商品调拨明细
     * @return 结果
     */
    public int insertStoreGoodsWarehouseAdjustItem(StoreGoodsWarehouseAdjustItem storeGoodsWarehouseAdjustItem);

    /**
     * 修改商品调拨明细
     * 
     * @param storeGoodsWarehouseAdjustItem 商品调拨明细
     * @return 结果
     */
    public int updateStoreGoodsWarehouseAdjustItem(StoreGoodsWarehouseAdjustItem storeGoodsWarehouseAdjustItem);

    /**
     * 删除商品调拨明细
     * 
     * @param itemId 商品调拨明细ID
     * @return 结果
     */
    public int deleteStoreGoodsWarehouseAdjustItemById(Long itemId);

    /**
     * 批量删除商品调拨明细
     * 
     * @param itemIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsWarehouseAdjustItemByIds(String[] itemIds);

    /**
     * 根据调度id删除明细
     *
     * @param adjustId
     * @return
     */
    int deleteStoreGoodsWarehouseAdjustItemByAdjustId(Long adjustId);
}
