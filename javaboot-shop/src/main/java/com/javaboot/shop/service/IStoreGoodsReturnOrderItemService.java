package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreGoodsReturnOrderItem;
import com.javaboot.shop.vo.StoreGoodsReturnOrderItemVO;

import java.util.List;

/**
 * 退货单明细Service接口
 * 
 * @author lqh
 * @date 2021-06-26
 */
public interface IStoreGoodsReturnOrderItemService {
    /**
     * 查询退货单明细
     * 
     * @param itemId
     *            退货单明细ID
     * @return 退货单明细
     */
    public StoreGoodsReturnOrderItem selectStoreGoodsReturnOrderItemById(Long itemId);

    /**
     * 查询退货单明细列表
     * 
     * @param storeGoodsReturnOrderItem
     *            退货单明细
     * @return 退货单明细集合
     */
    public List<StoreGoodsReturnOrderItemVO>
        selectStoreGoodsReturnOrderItemList(StoreGoodsReturnOrderItem storeGoodsReturnOrderItem);

    /**
     * 新增退货单明细
     * 
     * @param storeGoodsReturnOrderItem
     *            退货单明细
     * @return 结果
     */
    public int insertStoreGoodsReturnOrderItem(StoreGoodsReturnOrderItem storeGoodsReturnOrderItem);

    /**
     * 修改退货单明细
     * 
     * @param storeGoodsReturnOrderItem
     *            退货单明细
     * @return 结果
     */
    public int updateStoreGoodsReturnOrderItem(StoreGoodsReturnOrderItem storeGoodsReturnOrderItem);

    /**
     * 批量删除退货单明细
     * 
     * @param ids
     *            需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsReturnOrderItemByIds(String ids);

    /**
     * 删除退货单明细信息
     * 
     * @param itemId
     *            退货单明细ID
     * @return 结果
     */
    public int deleteStoreGoodsReturnOrderItemById(Long itemId);

    /**
     * 删除退货单商品
     * 
     * @param returnOrderId
     * @return
     */
    int deleteStoreGoodsReturnOrderItemByReturnOrderId(Long returnOrderId);
}
