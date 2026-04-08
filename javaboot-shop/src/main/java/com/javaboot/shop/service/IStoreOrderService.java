package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreOrder;

import java.util.List;

/**
 * 订单Service接口
 *
 * @author javaboot
 * @date 2019-08-29
 */
public interface IStoreOrderService {
    /**
     * 查询订单
     *
     * @param orderId 订单ID
     * @return 订单
     */
    public StoreOrder selectStoreOrderById(Integer orderId);

    /**
     * 查询订单列表
     *
     * @param storeOrder 订单
     * @return 订单集合
     */
    public List<StoreOrder> selectStoreOrderList(StoreOrder storeOrder);

    /**
     * 新增订单
     *
     * @param storeOrder 订单
     * @return 结果
     */
    public int insertStoreOrder(StoreOrder storeOrder);

    /**
     * 修改订单
     *
     * @param storeOrder 订单
     * @return 结果
     */
    public int updateStoreOrder(StoreOrder storeOrder);

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreOrderByIds(String ids);

    /**
     * 删除订单信息
     *
     * @param orderId 订单ID
     * @return 结果
     */
    public int deleteStoreOrderById(Integer orderId);
}
