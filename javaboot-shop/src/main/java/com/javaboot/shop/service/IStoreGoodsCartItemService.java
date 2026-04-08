package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreGoodsCartItem;
import java.util.List;

/**
 * 购物车明细Service接口
 * 
 * @author javaboot
 * @date 2021-08-04
 */
public interface IStoreGoodsCartItemService {
    /**
     * 查询购物车明细
     * 
     * @param itemId 购物车明细ID
     * @return 购物车明细
     */
    public StoreGoodsCartItem selectStoreGoodsCartItemById(Long itemId);

    /**
     * 查询购物车明细列表
     * 
     * @param storeGoodsCartItem 购物车明细
     * @return 购物车明细集合
     */
    public List<StoreGoodsCartItem> selectStoreGoodsCartItemList(StoreGoodsCartItem storeGoodsCartItem);

    /**
     * 新增购物车明细
     * 
     * @param storeGoodsCartItem 购物车明细
     * @return 结果
     */
    public int insertStoreGoodsCartItem(StoreGoodsCartItem storeGoodsCartItem);

    /**
     * 修改购物车明细
     * 
     * @param storeGoodsCartItem 购物车明细
     * @return 结果
     */
    public int updateStoreGoodsCartItem(StoreGoodsCartItem storeGoodsCartItem);

    /**
     * 批量删除购物车明细
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsCartItemByIds(String ids);

    /**
     * 删除购物车明细信息
     * 
     * @param itemId 购物车明细ID
     * @return 结果
     */
    public int deleteStoreGoodsCartItemById(Long itemId);

    public int delCartGoodsWithUserId(StoreGoodsCartItem storeGoodsCartItem);
}
