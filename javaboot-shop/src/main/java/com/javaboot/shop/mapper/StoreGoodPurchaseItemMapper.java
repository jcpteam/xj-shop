package com.javaboot.shop.mapper;

import com.javaboot.common.core.dao.BaseMapper;
import com.javaboot.shop.domain.StoreGoodPurchaseItem;
import com.javaboot.shop.dto.StoreGoodPurchaseQueryDTO;
import com.javaboot.shop.dto.StoreGoodsPurchaseExportDTO;

import java.util.List;

/**
 * 商品采购明细Mapper接口
 * 
 * @author lqh
 * @date 2021-05-23
 */
public interface StoreGoodPurchaseItemMapper extends BaseMapper<StoreGoodPurchaseItem> {
    /**
     * 查询商品采购明细
     * 
     * @param itemId 商品采购明细ID
     * @return 商品采购明细
     */
    public StoreGoodPurchaseItem selectStoreGoodPurchaseItemById(Long itemId);

    /**
     * 查询商品采购明细列表
     * 
     * @param storeGoodPurchaseItem 商品采购明细
     * @return 商品采购明细集合
     */
    public List<StoreGoodPurchaseItem> selectStoreGoodPurchaseItemList(StoreGoodPurchaseItem storeGoodPurchaseItem);

    /**
     * 新增商品采购明细
     * 
     * @param storeGoodPurchaseItem 商品采购明细
     * @return 结果
     */
    public int insertStoreGoodPurchaseItem(StoreGoodPurchaseItem storeGoodPurchaseItem);

    /**
     * 修改商品采购明细
     * 
     * @param storeGoodPurchaseItem 商品采购明细
     * @return 结果
     */
    public int updateStoreGoodPurchaseItem(StoreGoodPurchaseItem storeGoodPurchaseItem);

    /**
     * 删除商品采购明细
     * 
     * @param itemId 商品采购明细ID
     * @return 结果
     */
    public int deleteStoreGoodPurchaseItemById(Long itemId);

    /**
     * 批量删除商品采购明细
     * 
     * @param itemIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodPurchaseItemByIds(String[] itemIds);


    public List<StoreGoodPurchaseItem> selectPurchaseItemNum(StoreGoodPurchaseItem storeGoodPurchaseItem);

    List<StoreGoodsPurchaseExportDTO> selectExportGoodsPurchaseItemList(StoreGoodPurchaseQueryDTO dto);
}
