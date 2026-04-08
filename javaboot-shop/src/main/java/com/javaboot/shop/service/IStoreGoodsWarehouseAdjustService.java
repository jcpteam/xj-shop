package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreGoodsWarehouseAdjust;
import com.javaboot.shop.dto.StoreGoodsWarehouseAdjustDTO;
import com.javaboot.shop.vo.StoreGoodsWarehouseAdjustVO;

import java.util.List;

/**
 * 商品调拨信息Service接口
 * 
 * @author lqh
 * @date 2021-06-10
 */
public interface IStoreGoodsWarehouseAdjustService {
    /**
     * 查询商品调拨信息
     * 
     * @param adjustId 商品调拨信息ID
     * @return 商品调拨信息
     */
    public StoreGoodsWarehouseAdjustVO selectStoreGoodsWarehouseAdjustById(Long adjustId);

    /**
     * 查询商品调拨信息列表
     * 
     * @param storeGoodsWarehouseAdjust 商品调拨信息
     * @return 商品调拨信息集合
     */
    public List<StoreGoodsWarehouseAdjustVO> selectStoreGoodsWarehouseAdjustList(StoreGoodsWarehouseAdjust storeGoodsWarehouseAdjust);

    /**
     * 新增商品调拨信息
     * 
     * @param storeGoodsWarehouseAdjust 商品调拨信息
     * @return 结果
     */
    public int insertStoreGoodsWarehouseAdjust(StoreGoodsWarehouseAdjustDTO storeGoodsWarehouseAdjust);

    /**
     * 修改商品调拨信息
     * 
     * @param storeGoodsWarehouseAdjust 商品调拨信息
     * @return 结果
     */
    public int updateStoreGoodsWarehouseAdjust(StoreGoodsWarehouseAdjustDTO storeGoodsWarehouseAdjust);

    /**
     * 批量删除商品调拨信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsWarehouseAdjustByIds(String ids);

    /**
     * 删除商品调拨信息信息
     * 
     * @param adjustId 商品调拨信息ID
     * @return 结果
     */
    public int deleteStoreGoodsWarehouseAdjustById(Long adjustId);

    /**
     * 调拨单生成入库单
     * @param dto
     */
    int addStockByAdjust(StoreGoodsWarehouseAdjustDTO dto);
}
