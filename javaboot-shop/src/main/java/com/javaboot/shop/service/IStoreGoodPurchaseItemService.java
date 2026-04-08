package com.javaboot.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboot.shop.domain.StoreGoodPurchaseItem;

import java.util.List;

/**
 * 商品采购明细Service接口
 *
 * @author lqh
 * @date 2021-05-23
 */
public interface IStoreGoodPurchaseItemService extends IService<StoreGoodPurchaseItem> {
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
     * 批量删除商品采购明细
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodPurchaseItemByIds(String ids);

    /**
     * 删除商品采购明细信息
     *
     * @param itemId 商品采购明细ID
     * @return 结果
     */
    public int deleteStoreGoodPurchaseItemById(Long itemId);
}
