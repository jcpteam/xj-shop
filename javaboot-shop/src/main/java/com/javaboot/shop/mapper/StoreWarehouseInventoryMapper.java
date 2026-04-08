package com.javaboot.shop.mapper;

import com.javaboot.common.core.dao.BaseMapper;
import com.javaboot.shop.domain.StoreWarehouseInventory;
import com.javaboot.shop.dto.StoreWarehouseInventoryDTO;
import com.javaboot.shop.vo.StoreWarehouseInventoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 商品盘库Mapper接口
 * 
 * @author lqh
 * @date 2021-05-23
 */
public interface StoreWarehouseInventoryMapper extends BaseMapper<StoreWarehouseInventory> {
    /**
     * 查询商品盘库
     * 
     * @param inventoryId 商品盘库ID
     * @return 商品盘库
     */
    public StoreWarehouseInventoryVO selectStoreWarehouseInventoryById(Long inventoryId);

    /**
     * 查询商品盘库列表
     * 
     * @param storeWarehouseInventory 商品盘库
     * @return 商品盘库集合
     */
    public List<StoreWarehouseInventoryVO> selectStoreWarehouseInventoryList(StoreWarehouseInventory storeWarehouseInventory);

    /**
     * 查询仓库最新的盘库记录
     * @param warehouseId
     * @return
     */
    StoreWarehouseInventoryVO selectLastStoreWarehouseInventoryByWarehouseId(@Param("warehouseId") Long warehouseId, @Param("intentoryDate") Date intentoryDate);

    /**
     * 新增商品盘库
     * 
     * @param storeWarehouseInventory 商品盘库
     * @return 结果
     */
    public int insertStoreWarehouseInventory(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     * 修改商品盘库
     * 
     * @param storeWarehouseInventory 商品盘库
     * @return 结果
     */
    public int updateStoreWarehouseInventory(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     * 删除商品盘库
     * 
     * @param inventoryId 商品盘库ID
     * @return 结果
     */
    public int deleteStoreWarehouseInventoryById(Long inventoryId);

    /**
     * 批量删除商品盘库
     * 
     * @param inventoryIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreWarehouseInventoryByIds(String[] inventoryIds);
}
