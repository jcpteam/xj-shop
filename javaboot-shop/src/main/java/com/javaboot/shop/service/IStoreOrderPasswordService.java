package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreOrderPassword;
import java.util.List;

/**
 * 订单密码Service接口
 * 
 * @author javaboot
 * @date 2021-08-07
 */
public interface IStoreOrderPasswordService {
    /**
     * 查询订单密码
     * 
     * @param id 订单密码ID
     * @return 订单密码
     */
    public StoreOrderPassword selectStoreOrderPasswordById(Long id);

    /**
     * 查询订单密码列表
     * 
     * @param storeOrderPassword 订单密码
     * @return 订单密码集合
     */
    public List<StoreOrderPassword> selectStoreOrderPasswordList(StoreOrderPassword storeOrderPassword);

    /**
     * 新增订单密码
     * 
     * @param storeOrderPassword 订单密码
     * @return 结果
     */
    public int insertStoreOrderPassword(StoreOrderPassword storeOrderPassword);

    /**
     * 修改订单密码
     * 
     * @param storeOrderPassword 订单密码
     * @return 结果
     */
    public int updateStoreOrderPassword(StoreOrderPassword storeOrderPassword);

    /**
     * 批量删除订单密码
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreOrderPasswordByIds(String ids);

    /**
     * 删除订单密码信息
     * 
     * @param id 订单密码ID
     * @return 结果
     */
    public int deleteStoreOrderPasswordById(Long id);
}
