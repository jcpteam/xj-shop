package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreWarehouseInventory;
import com.javaboot.shop.domain.StoreWarehouseInventoryItem;
import com.javaboot.shop.dto.StoreWarehouseInventoryDTO;
import com.javaboot.shop.vo.StoreWarehouseInventoryVO;

import java.util.List;

/**
 * 商品盘库Service接口
 * 
 * @author lqh
 * @date 2021-05-23
 */
public interface IStoreWarehouseInventoryService {
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
     * 批量删除商品盘库
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreWarehouseInventoryByIds(String ids);

    /**
     * 删除商品盘库信息
     * 
     * @param inventoryId 商品盘库ID
     * @return 结果
     */
    public int deleteStoreWarehouseInventoryById(Long inventoryId);

    /**
     * 计算库存和总金额
     * @param storeWarehouseInventory
     * @return
     */
    List<StoreWarehouseInventoryItem> calculationInventoryGoods(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     * 根据单位初始化盘库的商品列表
     * @param storeWarehouseInventory
     * @return
     */
    List<StoreWarehouseInventoryItem> initInventorySpus(StoreWarehouseInventoryDTO storeWarehouseInventory);

    /**
     * 仓库最近一次盘库时间
     * @param houseId
     * @return
     */
    String lastInventoryDate(Long houseId);

    /**
     * 自动盘库
     * @param storeWarehouseInventory
     * @return
     */
    int autoInventory(StoreWarehouseInventoryDTO storeWarehouseInventory);
}
