package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreGoodPurchase;
import com.javaboot.shop.dto.StoreGoodPurchaseDTO;
import com.javaboot.shop.dto.StoreGoodPurchaseQueryDTO;
import com.javaboot.shop.vo.StoreGoodPurchaseVO;

import java.util.List;

/**
 * 商品采购Service接口
 * 
 * @author lqh
 * @date 2021-05-23
 */
public interface IStoreGoodPurchaseService {
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
    public int insertStoreGoodPurchase(StoreGoodPurchaseDTO storeGoodPurchase);

    /**
     * 修改商品采购
     * 
     * @param storeGoodPurchase 商品采购
     * @return 结果
     */
    public int updateStoreGoodPurchase(StoreGoodPurchaseDTO storeGoodPurchase);

    /**
     * 批量删除商品采购
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodPurchaseByIds(String ids);

    /**
     * 删除商品采购信息
     * 
     * @param purchaseId 商品采购ID
     * @return 结果
     */
    public int deleteStoreGoodPurchaseById(Long purchaseId);

    /**
     * 采购单生成入库单
     * @param dto
     */
    int addStockByPurchase(StoreGoodPurchaseDTO dto);
}
