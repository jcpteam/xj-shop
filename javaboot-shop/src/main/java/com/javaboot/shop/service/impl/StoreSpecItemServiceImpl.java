package com.javaboot.shop.service.impl;

import com.javaboot.common.core.text.Convert;
import com.javaboot.shop.domain.StoreSpecItem;
import com.javaboot.shop.mapper.StoreSpecItemMapper;
import com.javaboot.shop.service.IStoreSpecItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 规格项Service业务层处理
 *
 * @author javaboot
 * @date 2019-08-25
 */
@Service
public class StoreSpecItemServiceImpl implements IStoreSpecItemService {
    @Autowired
    private StoreSpecItemMapper storeSpecItemMapper;

    /**
     * 查询规格项
     *
     * @param id 规格项ID
     * @return 规格项
     */
    @Override
    public StoreSpecItem selectStoreSpecItemById(Integer id) {
        return storeSpecItemMapper.selectStoreSpecItemById(id);
    }

    /**
     * 查询规格项列表
     *
     * @param storeSpecItem 规格项
     * @return 规格项
     */
    @Override
    public List<StoreSpecItem> selectStoreSpecItemList(StoreSpecItem storeSpecItem) {
        return storeSpecItemMapper.selectStoreSpecItemList(storeSpecItem);
    }

    /**
     * 新增规格项
     *
     * @param storeSpecItem 规格项
     * @return 结果
     */
    @Override
    public int insertStoreSpecItem(StoreSpecItem storeSpecItem) {

        return storeSpecItemMapper.insertStoreSpecItem(storeSpecItem);
    }

    /**
     * 修改规格项
     *
     * @param storeSpecItem 规格项
     * @return 结果
     */
    @Override
    public int updateStoreSpecItem(StoreSpecItem storeSpecItem) {
        return storeSpecItemMapper.updateStoreSpecItem(storeSpecItem);
    }

    /**
     * 删除规格项对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreSpecItemByIds(String ids) {
        return storeSpecItemMapper.deleteStoreSpecItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除规格项信息
     *
     * @param id 规格项ID
     * @return 结果
     */
    @Override
    public int deleteStoreSpecItemById(Integer id) {
        return storeSpecItemMapper.deleteStoreSpecItemById(id);
    }
}
