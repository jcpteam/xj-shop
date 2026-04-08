package com.javaboot.shop.mapper;

import com.javaboot.common.core.dao.BaseMapper;
import com.javaboot.shop.domain.StoreWarehouseStockItem;
import com.javaboot.shop.dto.StoreStockExportDTO;
import com.javaboot.shop.dto.StoreWarehouseStockQueryDTO;

import java.util.List;

/**
 * 商品入库明细Mapper接口
 *
 * @author lqh
 * @date 2021-05-23
 */
public interface StoreWarehouseStockItemMapper  extends BaseMapper<StoreWarehouseStockItem> {
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
     * 删除商品入库明细
     *
     * @param itemId 商品入库明细ID
     * @return 结果
     */
    public int deleteStoreWarehouseStockItemById(Long itemId);

    /**
     * 批量删除商品入库明细
     *
     * @param itemIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreWarehouseStockItemByIds(String[] itemIds);

    public List<StoreWarehouseStockItem> selectWarehouseStockItemNum(StoreWarehouseStockItem storeWarehouseStockItem);

    /**
     * 获取当天入库单的SPU商品数量
     * @param storeWarehouseStockItem
     * @return
     */
    public List<StoreWarehouseStockItem> selectWareStockQuantity(StoreWarehouseStockItem storeWarehouseStockItem);

    /**
     * 获取最近一次盘库的SPU商品主，副单位数量
     * @param storeWarehouseStockItem
     * @return
     */
    public List<StoreWarehouseStockItem> selectInventoryQuantity(StoreWarehouseStockItem storeWarehouseStockItem);

    List<StoreStockExportDTO> selectExportStockItemList(StoreWarehouseStockQueryDTO dto);

    public List<StoreWarehouseStockItem> selectStockItemListForSorting(StoreWarehouseStockItem storeWarehouseStockItem);
}
