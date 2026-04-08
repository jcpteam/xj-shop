package com.javaboot.shop.service.impl;

import com.javaboot.common.core.text.Convert;
import com.javaboot.shop.domain.StoreOrder;
import com.javaboot.shop.mapper.StoreOrderMapper;
import com.javaboot.shop.service.IStoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单业务层处理
 *
 * @author javaboot
 * @date 2019-08-29
 */
@Service
public class StoreOrderServiceImpl implements IStoreOrderService {
    @Autowired
    private StoreOrderMapper storeOrderMapper;

    /**
     * 查询订单
     *
     * @param orderId 订单ID
     * @return 订单
     */
    @Override
    public StoreOrder selectStoreOrderById(Integer orderId) {
        return storeOrderMapper.selectStoreOrderById(orderId);
    }

    /**
     * 查询订单列表
     *
     * @param storeOrder 订单
     * @return 订单
     */
    @Override
    public List<StoreOrder> selectStoreOrderList(StoreOrder storeOrder) {
        List<StoreOrder> list = storeOrderMapper.selectStoreOrderList(storeOrder);
        return list;
    }

    /**
     * 新增订单主
     *
     * @param storeOrder 订单主
     * @return 结果
     */
    @Override
    public int insertStoreOrder(StoreOrder storeOrder) {
        return storeOrderMapper.insertStoreOrder(storeOrder);
    }

    /**
     * 修改订单主
     *
     * @param storeOrder 订单主
     * @return 结果
     */
    @Override
    public int updateStoreOrder(StoreOrder storeOrder) {
        return storeOrderMapper.updateStoreOrder(storeOrder);
    }

    /**
     * 删除订单主对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreOrderByIds(String ids) {
        return storeOrderMapper.deleteStoreOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除订单主信息
     *
     * @param orderId 订单主ID
     * @return 结果
     */
    @Override
    public int deleteStoreOrderById(Integer orderId) {
        return storeOrderMapper.deleteStoreOrderById(orderId);
    }
}
