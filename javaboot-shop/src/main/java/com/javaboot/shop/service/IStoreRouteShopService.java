package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreRouteShop;
import com.javaboot.shop.dto.StoreCustomerDTO;

import java.util.List;

/**
 * 线路和商户关联Service接口
 * 
 * @author javaboot
 * @date 2021-06-05
 */
public interface IStoreRouteShopService {
    /**
     * 查询线路和商户关联
     * 
     * @param id 线路和商户关联ID
     * @return 线路和商户关联
     */
    public StoreRouteShop selectStoreRouteShopById(Long id);

    /**
     * 查询线路和商户关联列表
     * 
     * @param storeRouteShop 线路和商户关联
     * @return 线路和商户关联集合
     */
    public List<StoreRouteShop> selectStoreRouteShopList(StoreRouteShop storeRouteShop);

    /**
     * 新增线路和商户关联
     * 
     * @param storeRouteShop 线路和商户关联
     * @return 结果
     */
    public int insertStoreRouteShop(StoreRouteShop storeRouteShop);

    /**
     * 修改线路和商户关联
     * 
     * @param storeRouteShop 线路和商户关联
     * @return 结果
     */
    public int updateStoreRouteShop(StoreRouteShop storeRouteShop);

    /**
     * 批量删除线路和商户关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreRouteShopByIds(String ids);

    /**
     * 删除线路和商户关联信息
     * 
     * @param id 线路和商户关联ID
     * @return 结果
     */
    public int deleteStoreRouteShopById(Long id);
 	public List<StoreCustomerDTO> getCustomerList();}
