package com.javaboot.shop.service.impl;

import com.javaboot.common.core.text.Convert;
import com.javaboot.shop.domain.StoreSource;
import com.javaboot.shop.mapper.StoreSourceMapper;
import com.javaboot.shop.service.IStoreSourceService;
import com.javaboot.shop.service.IStoreSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 源码管理业务层处理
 *
 * @author javaboot
 * @date 2019-08-29
 */
@Service
public class StoreSourceServiceImpl implements IStoreSourceService {
    @Autowired
    private StoreSourceMapper storeSourceMapper;

    /**
     * 查询源码管理
     *
     * @param id 源码管理ID
     * @return 源码管理
     */
    @Override
    public StoreSource selectStoreSourceById(Integer id) {
        return storeSourceMapper.selectStoreSourceById(id);
    }

    /**
     * 查询源码管理列表
     *
     * @param storeCoupon 源码管理
     * @return 源码管理
     */
    @Override
    public List<StoreSource> selectStoreSourceList(StoreSource storeCoupon) {
        return storeSourceMapper.selectStoreSourceList(storeCoupon);
    }

    /**
     * 新增源码管理
     *
     * @param storeCoupon 源码管理
     * @return 结果
     */
    @Override
    public int insertStoreSource(StoreSource storeCoupon) {
        return storeSourceMapper.insertStoreSource(storeCoupon);
    }

    /**
     * 修改源码管理
     *
     * @param storeCoupon 源码管理
     * @return 结果
     */
    @Override
    public int updateStoreSource(StoreSource storeCoupon) {
        return storeSourceMapper.updateStoreSource(storeCoupon);
    }

    /**
     * 删除源码管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreSourceByIds(String ids) {
        return storeSourceMapper.deleteStoreSourceByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除源码管理信息
     *
     * @param id 源码管理ID
     * @return 结果
     */
    @Override
    public int deleteStoreSourceById(Integer id) {
        return storeSourceMapper.deleteStoreSourceById(id);
    }
}
