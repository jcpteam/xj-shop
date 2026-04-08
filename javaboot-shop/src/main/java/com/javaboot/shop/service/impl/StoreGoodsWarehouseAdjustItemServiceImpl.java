package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreGoodsWarehouseAdjustItemMapper;
import com.javaboot.shop.domain.StoreGoodsWarehouseAdjustItem;
import com.javaboot.shop.service.IStoreGoodsWarehouseAdjustItemService;
import com.javaboot.common.core.text.Convert;

/**
 * 商品调拨明细Service业务层处理
 * 
 * @author lqh
 * @date 2021-06-10
 */
@Service
public class StoreGoodsWarehouseAdjustItemServiceImpl implements IStoreGoodsWarehouseAdjustItemService {
    @Autowired
    private StoreGoodsWarehouseAdjustItemMapper storeGoodsWarehouseAdjustItemMapper;

    /**
     * 查询商品调拨明细
     * 
     * @param itemId 商品调拨明细ID
     * @return 商品调拨明细
     */
    @Override
    public StoreGoodsWarehouseAdjustItem selectStoreGoodsWarehouseAdjustItemById(Long itemId) {
        return storeGoodsWarehouseAdjustItemMapper.selectStoreGoodsWarehouseAdjustItemById(itemId);
    }

    /**
     * 查询商品调拨明细列表
     * 
     * @param storeGoodsWarehouseAdjustItem 商品调拨明细
     * @return 商品调拨明细
     */
    @Override
    public List<StoreGoodsWarehouseAdjustItem> selectStoreGoodsWarehouseAdjustItemList(StoreGoodsWarehouseAdjustItem storeGoodsWarehouseAdjustItem) {
        return storeGoodsWarehouseAdjustItemMapper.selectStoreGoodsWarehouseAdjustItemList(storeGoodsWarehouseAdjustItem);
    }

    /**
     * 新增商品调拨明细
     * 
     * @param storeGoodsWarehouseAdjustItem 商品调拨明细
     * @return 结果
     */
    @Override
    public int insertStoreGoodsWarehouseAdjustItem(StoreGoodsWarehouseAdjustItem storeGoodsWarehouseAdjustItem) {
        storeGoodsWarehouseAdjustItem.setCreateTime(DateUtils.getNowDate());
        return storeGoodsWarehouseAdjustItemMapper.insertStoreGoodsWarehouseAdjustItem(storeGoodsWarehouseAdjustItem);
    }

    /**
     * 修改商品调拨明细
     * 
     * @param storeGoodsWarehouseAdjustItem 商品调拨明细
     * @return 结果
     */
    @Override
    public int updateStoreGoodsWarehouseAdjustItem(StoreGoodsWarehouseAdjustItem storeGoodsWarehouseAdjustItem) {
        return storeGoodsWarehouseAdjustItemMapper.updateStoreGoodsWarehouseAdjustItem(storeGoodsWarehouseAdjustItem);
    }

    /**
     * 删除商品调拨明细对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsWarehouseAdjustItemByIds(String ids) {
        return storeGoodsWarehouseAdjustItemMapper.deleteStoreGoodsWarehouseAdjustItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品调拨明细信息
     * 
     * @param itemId 商品调拨明细ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsWarehouseAdjustItemById(Long itemId) {
        return storeGoodsWarehouseAdjustItemMapper.deleteStoreGoodsWarehouseAdjustItemById(itemId);
    }
    /**
     * 根据调度id删除明细
     *
     * @param adjustId
     * @return
     */
    @Override
    public int deleteStoreGoodsWarehouseAdjustItemByAdjustId(Long adjustId) {
        return storeGoodsWarehouseAdjustItemMapper.deleteStoreGoodsWarehouseAdjustItemByAdjustId(adjustId);
    }
}
