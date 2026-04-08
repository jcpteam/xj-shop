package com.javaboot.shop.mapper;

import com.javaboot.common.core.dao.BaseMapper;
import com.javaboot.shop.domain.StoreWarehouseInventoryItem;
import com.javaboot.shop.dto.StoreWarehouseInventoryDTO;
import com.javaboot.shop.dto.StoreWarehouseInventoryExportDTO;
import com.javaboot.shop.vo.StoreGoodsSpuVO;

import java.util.List;

/**
 * 商品盘库明细Mapper接口
 * 
 * @author lqh
 * @date 2021-05-23
 */
public interface StoreWarehouseInventoryItemMapper extends BaseMapper<StoreWarehouseInventoryItem> {
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
     * 删除商品盘库明细
     * 
     * @param itemId 商品盘库明细ID
     * @return 结果
     */
    public int deleteStoreWarehouseInventoryItemById(Long itemId);

    /**
     * 批量删除商品盘库明细
     * 
     * @param itemIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreWarehouseInventoryItemByIds(String[] itemIds);

    /**
     * 删除商品盘库明细信息
     *
     * @param inventoryId 商品盘库ID
     * @return 结果
     */
    int deleteStoreWarehouseInventoryItemByInventoryId(Long inventoryId);

    /**
     * 获取计算盘库的报价单中商品SPU列表
     * @param storeWarehouseInventory
     * @return
     */
    public List<StoreWarehouseInventoryItem> selectInventorySpus(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     * 订单商品主单位和副单位的数量，金额统计
     * @param storeWarehouseInventory
     * @return
     */
    List<StoreWarehouseInventoryItem>  selectSpuOrderInventory(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     * 商品主单位和辅单位换算关系
     * @param storeWarehouseInventory
     * @return
     */
    List<StoreGoodsSpuVO>  selectSpuUnitConversion(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     * 入库单商品主单位和副单位的数量，金额统计
     * @param storeWarehouseInventory
     * @return
     */
    List<StoreWarehouseInventoryItem>  selectWarehouseStockInventory(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     * 调拨出库单商品主单位和副单位的数量，金额统计
     * @param storeWarehouseInventory
     * @return
     */
    List<StoreWarehouseInventoryItem>  selectWarehouseStockAdjust(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     * 盘库商品主单位和副单位的数量
     * @param storeWarehouseInventory
     * @return
     */
    List<StoreWarehouseInventoryItem> selectWarehouseInventory(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     * 获取需要盘库的商品SPU列表
     * @param storeWarehouseInventory
     * @return
     */
    List<StoreWarehouseInventoryItem>  selectAllInventorySpus(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     * 订单损耗计算
     * @param storeWarehouseInventory
     * @return
     */
    List<StoreWarehouseInventoryItem> selectSpuOrderLoss(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     *
     * 入库损耗计算
     * @param storeWarehouseInventory
     * @return
     */
    List<StoreWarehouseInventoryItem> selectWarehouseStockLoss(StoreWarehouseInventoryDTO storeWarehouseInventory);

    List<StoreWarehouseInventoryExportDTO>  selectExportInventoryItemList(StoreWarehouseInventoryDTO StoreWarehouseInventory);

}
