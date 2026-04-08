package com.javaboot.shop.mapper;

import com.javaboot.common.core.dao.BaseMapper;
import com.javaboot.shop.domain.StoreGoodPurchase;
import com.javaboot.shop.dto.StoreGoodPurchaseQueryDTO;
import com.javaboot.shop.vo.StoreGoodPurchaseVO;

import java.util.List;

/**
 * 商品采购Mapper接口
 * 
 * @author lqh
 * @date 2021-05-23
 */
public interface StoreGoodPurchaseMapper extends BaseMapper<StoreGoodPurchase> {
    /**
     * 查询商品采购
     * 
     * @param purchaseId 商品采购ID
     * @return 商品采购
     */
    public StoreGoodPurchaseVO selectStoreGoodPurchaseById(Long purchaseId);

    /**
     * 查询商品采购列表
     * 
     * @param storeGoodPurchase 商品采购
     * @return 商品采购集合
     */
    public List<StoreGoodPurchaseVO> selectStoreGoodPurchaseList(StoreGoodPurchaseQueryDTO storeGoodPurchase);

    /**
     * 新增商品采购
     * 
     * @param storeGoodPurchase 商品采购
     * @return 结果
     */
    public int insertStoreGoodPurchase(StoreGoodPurchase storeGoodPurchase);

    /**
     * 修改商品采购
     * 
     * @param storeGoodPurchase 商品采购
     * @return 结果
     */
    public int updateStoreGoodPurchase(StoreGoodPurchase storeGoodPurchase);

    /**
     * 删除商品采购
     * 
     * @param purchaseId 商品采购ID
     * @return 结果
     */
    public int deleteStoreGoodPurchaseById(Long purchaseId);

    /**
     * 批量删除商品采购
     * 
     * @param purchaseIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodPurchaseByIds(String[] purchaseIds);
}
