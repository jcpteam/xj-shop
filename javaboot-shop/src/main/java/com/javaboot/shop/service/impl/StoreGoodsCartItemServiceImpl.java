package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreGoodsCartItemMapper;
import com.javaboot.shop.domain.StoreGoodsCartItem;
import com.javaboot.shop.service.IStoreGoodsCartItemService;
import com.javaboot.common.core.text.Convert;

/**
 * 购物车明细Service业务层处理
 * 
 * @author javaboot
 * @date 2021-08-04
 */
@Service
public class StoreGoodsCartItemServiceImpl implements IStoreGoodsCartItemService {
    @Autowired
    private StoreGoodsCartItemMapper storeGoodsCartItemMapper;

    /**
     * 查询购物车明细
     * 
     * @param itemId 购物车明细ID
     * @return 购物车明细
     */
    @Override
    public StoreGoodsCartItem selectStoreGoodsCartItemById(Long itemId) {
        return storeGoodsCartItemMapper.selectStoreGoodsCartItemById(itemId);
    }

    /**
     * 查询购物车明细列表
     * 
     * @param storeGoodsCartItem 购物车明细
     * @return 购物车明细
     */
    @Override
    public List<StoreGoodsCartItem> selectStoreGoodsCartItemList(StoreGoodsCartItem storeGoodsCartItem) {
        return storeGoodsCartItemMapper.selectStoreGoodsCartItemList(storeGoodsCartItem);
    }

    /**
     * 新增购物车明细
     * 
     * @param storeGoodsCartItem 购物车明细
     * @return 结果
     */
    @Override
    public int insertStoreGoodsCartItem(StoreGoodsCartItem storeGoodsCartItem) {
        storeGoodsCartItem.setCreateTime(DateUtils.getNowDate());
        return storeGoodsCartItemMapper.insertStoreGoodsCartItem(storeGoodsCartItem);
    }

    /**
     * 修改购物车明细
     * 
     * @param storeGoodsCartItem 购物车明细
     * @return 结果
     */
    @Override
    public int updateStoreGoodsCartItem(StoreGoodsCartItem storeGoodsCartItem) {
        return storeGoodsCartItemMapper.updateStoreGoodsCartItem(storeGoodsCartItem);
    }

    /**
     * 删除购物车明细对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsCartItemByIds(String ids) {
        return storeGoodsCartItemMapper.deleteStoreGoodsCartItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除购物车明细信息
     * 
     * @param itemId 购物车明细ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsCartItemById(Long itemId) {
        return storeGoodsCartItemMapper.deleteStoreGoodsCartItemById(itemId);
    }
    /**
     * 删除购物车明细信息
     *
     * @param storeGoodsCartItem 购物车明细ID
     * @return 结果
     */
    @Override
    public int delCartGoodsWithUserId(StoreGoodsCartItem storeGoodsCartItem) {
        return storeGoodsCartItemMapper.delCartGoodsWithUserId(storeGoodsCartItem);
    }
}
