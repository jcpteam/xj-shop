package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreGoodPurchaseItem;
import com.javaboot.shop.mapper.StoreGoodPurchaseItemMapper;
import com.javaboot.shop.service.IStoreGoodPurchaseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品采购明细Service业务层处理
 *
 * @author lqh
 * @date 2021-05-23
 */
@Service
public class StoreGoodPurchaseItemServiceImpl extends ServiceImpl<StoreGoodPurchaseItemMapper, StoreGoodPurchaseItem> implements IStoreGoodPurchaseItemService {
    @Autowired
    private StoreGoodPurchaseItemMapper storeGoodPurchaseItemMapper;

    /**
     * 查询商品采购明细
     *
     * @param itemId 商品采购明细ID
     * @return 商品采购明细
     */
    @Override
    public StoreGoodPurchaseItem selectStoreGoodPurchaseItemById(Long itemId) {
        return storeGoodPurchaseItemMapper.selectStoreGoodPurchaseItemById(itemId);
    }

    /**
     * 查询商品采购明细列表
     *
     * @param storeGoodPurchaseItem 商品采购明细
     * @return 商品采购明细
     */
    @Override
    public List<StoreGoodPurchaseItem> selectStoreGoodPurchaseItemList(StoreGoodPurchaseItem storeGoodPurchaseItem) {
        return storeGoodPurchaseItemMapper.selectStoreGoodPurchaseItemList(storeGoodPurchaseItem);
    }

    /**
     * 新增商品采购明细
     *
     * @param storeGoodPurchaseItem 商品采购明细
     * @return 结果
     */
    @Override
    public int insertStoreGoodPurchaseItem(StoreGoodPurchaseItem storeGoodPurchaseItem) {
        storeGoodPurchaseItem.setCreateTime(DateUtils.getNowDate());
        return storeGoodPurchaseItemMapper.insertStoreGoodPurchaseItem(storeGoodPurchaseItem);
    }

    /**
     * 修改商品采购明细
     *
     * @param storeGoodPurchaseItem 商品采购明细
     * @return 结果
     */
    @Override
    public int updateStoreGoodPurchaseItem(StoreGoodPurchaseItem storeGoodPurchaseItem) {
        return storeGoodPurchaseItemMapper.updateStoreGoodPurchaseItem(storeGoodPurchaseItem);
    }

    /**
     * 删除商品采购明细对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodPurchaseItemByIds(String ids) {
        return storeGoodPurchaseItemMapper.deleteStoreGoodPurchaseItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品采购明细信息
     *
     * @param itemId 商品采购明细ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodPurchaseItemById(Long itemId) {
        return storeGoodPurchaseItemMapper.deleteStoreGoodPurchaseItemById(itemId);
    }
}
