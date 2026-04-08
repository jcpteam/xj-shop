package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreGoodsOrderItem;
import com.javaboot.shop.vo.StoreGoodsOrderItemVO;
import com.javaboot.shop.vo.StoreGoodsOrderSortingItemVO;

import java.util.List;

/**
 * 订单明细Service接口
 * 
 * @author lqh
 * @date 2021-06-01
 */
public interface IStoreGoodsOrderItemService {
    /**
     * 查询订单明细
     * 
     * @param itemId 订单明细ID
     * @return 订单明细
     */
    public StoreGoodsOrderItem selectStoreGoodsOrderItemById(Long itemId);

    /**
     * 查询订单明细列表
     * 
     * @param storeGoodsOrderItem 订单明细
     * @return 订单明细集合
     */
    public List<StoreGoodsOrderItemVO> selectStoreGoodsOrderItemList(StoreGoodsOrderItem storeGoodsOrderItem);

    /**
     * 新增订单明细
     * 
     * @param storeGoodsOrderItem 订单明细
     * @return 结果
     */
    public int insertStoreGoodsOrderItem(StoreGoodsOrderItem storeGoodsOrderItem);

    /**
     * 修改订单明细
     * 
     * @param storeGoodsOrderItem 订单明细
     * @return 结果
     */
    public int updateStoreGoodsOrderItem(StoreGoodsOrderItem storeGoodsOrderItem);

    /**
     * 批量删除订单明细
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsOrderItemByIds(String ids);

    /**
     * 删除订单明细信息
     * 
     * @param itemId 订单明细ID
     * @return 结果
     */
    public int deleteStoreGoodsOrderItemById(Long itemId);

    /**
     * 通过订单id删除明细
     *
     * @param orderId
     * @return
     */
    int deleteStoreGoodsOrderItemByOrderId(Long orderId);

    /**
     * 查询订单明细列表
     *
     * @param storeGoodsOrderItem 订单明细
     * @return 订单明细集合
     */
    public List<StoreGoodsOrderSortingItemVO> selectStoreGoodsOrderItemListForSorting(StoreGoodsOrderItem storeGoodsOrderItem);

}
