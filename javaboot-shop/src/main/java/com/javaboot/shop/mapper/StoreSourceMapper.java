package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreCoupon;
import com.javaboot.shop.domain.StoreSource;

import java.util.List;

/**
 * 源码管理接口
 *
 * @author javaboot
 * @date 2019-08-29
 */
public interface StoreSourceMapper {
    /**
     * 查询源码版本
     *
     * @param id 源码版本ID
     * @return 源码版本
     */
    public StoreSource selectStoreSourceById(Integer id);

    /**
     * 查询源码版本列表
     *
     * @param storeSource 源码版本
     * @return 源码版本集合
     */
    public List<StoreSource> selectStoreSourceList(StoreSource storeSource);

    /**
     * 新增源码版本
     *
     * @param storeSource 源码版本
     * @return 结果
     */
    public int insertStoreSource(StoreSource storeSource);

    /**
     * 修改源码版本
     *
     * @param storeSource 源码版本
     * @return 结果
     */
    public int updateStoreSource(StoreSource storeSource);

    /**
     * 删除源码版本
     *
     * @param id 源码版本ID
     * @return 结果
     */
    public int deleteStoreSourceById(Integer id);

    /**
     * 批量删除源码版本
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSourceByIds(String[] ids);
}
