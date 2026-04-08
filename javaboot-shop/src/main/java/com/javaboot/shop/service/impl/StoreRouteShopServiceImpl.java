package com.javaboot.shop.service.impl;

import java.util.List;

import com.javaboot.shop.dto.StoreCustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreRouteShopMapper;
import com.javaboot.shop.domain.StoreRouteShop;
import com.javaboot.shop.service.IStoreRouteShopService;
import com.javaboot.common.core.text.Convert;

/**
 * 线路和商户关联Service业务层处理
 * 
 * @author javaboot
 * @date 2021-06-05
 */
@Service
public class StoreRouteShopServiceImpl implements IStoreRouteShopService {
    @Autowired
    private StoreRouteShopMapper storeRouteShopMapper;

    /**
     * 查询线路和商户关联
     * 
     * @param id 线路和商户关联ID
     * @return 线路和商户关联
     */
    @Override
    public StoreRouteShop selectStoreRouteShopById(Long id) {
        return storeRouteShopMapper.selectStoreRouteShopById(id);
    }

    /**
     * 查询线路和商户关联列表
     * 
     * @param storeRouteShop 线路和商户关联
     * @return 线路和商户关联
     */
    @Override
    public List<StoreRouteShop> selectStoreRouteShopList(StoreRouteShop storeRouteShop) {
        return storeRouteShopMapper.selectStoreRouteShopList(storeRouteShop);
    }

    /**
     * 新增线路和商户关联
     * 
     * @param storeRouteShop 线路和商户关联
     * @return 结果
     */
    @Override
    public int insertStoreRouteShop(StoreRouteShop storeRouteShop) {
        return storeRouteShopMapper.insertStoreRouteShop(storeRouteShop);
    }

    /**
     * 修改线路和商户关联
     * 
     * @param storeRouteShop 线路和商户关联
     * @return 结果
     */
    @Override
    public int updateStoreRouteShop(StoreRouteShop storeRouteShop) {
        return storeRouteShopMapper.updateStoreRouteShop(storeRouteShop);
    }

    /**
     * 删除线路和商户关联对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreRouteShopByIds(String ids) {
        return storeRouteShopMapper.deleteStoreRouteShopByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除线路和商户关联信息
     * 
     * @param id 线路和商户关联ID
     * @return 结果
     */
    @Override
    public int deleteStoreRouteShopById(Long id) {
        return storeRouteShopMapper.deleteStoreRouteShopById(id);
    }
    @Override
    public List<StoreCustomerDTO> getCustomerList() {
        return storeRouteShopMapper.getCustomerList();
    }}
