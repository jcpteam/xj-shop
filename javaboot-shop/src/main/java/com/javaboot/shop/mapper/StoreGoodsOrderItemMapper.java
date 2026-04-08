package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreGoodsOrderItem;
import com.javaboot.shop.dto.StoreGoodsOrderExportDTO;
import com.javaboot.shop.dto.StoreGoodsOrderQueryDTO;
import com.javaboot.shop.vo.StoreGoodsOrderItemVO;
import com.javaboot.shop.vo.StoreGoodsOrderSortingItemVO;

import java.util.List;

/**
 * 订单明细Mapper接口
 *
 * @author lqh
 * @date 2021-06-01
 */
public interface StoreGoodsOrderItemMapper {
    /**
     * 查询订单明细
     *
     * @param itemId 订单明细ID
     * @return 订单明细
     */
    public StoreGoodsOrderItem selectStoreGoodsOrderItemById(Long itemId);

    /**
     * 查询订单明细列表
     *
     * @param storeGoodsOrderItem 订单明细
     * @return 订单明细集合
     */
    public List<StoreGoodsOrderItemVO> selectStoreGoodsOrderItemList(StoreGoodsOrderItem storeGoodsOrderItem);

    /**
     * 新增订单明细
     *
     * @param storeGoodsOrderItem 订单明细
     * @return 结果
     */
    public int insertStoreGoodsOrderItem(StoreGoodsOrderItem storeGoodsOrderItem);

    /**
     * 修改订单明细
     *
     * @param storeGoodsOrderItem 订单明细
     * @return 结果
     */
    public int updateStoreGoodsOrderItem(StoreGoodsOrderItem storeGoodsOrderItem);

    /**
     * 删除订单明细
     *
     * @param itemId 订单明细ID
     * @return 结果
     */
    public int deleteStoreGoodsOrderItemById(Long itemId);

    /**
     * 批量删除订单明细
     *
     * @param itemIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsOrderItemByIds(String[] itemIds);

    /**
     * 通过订单id删除明细
     *
     * @param orderId
     * @return
     */
    int deleteStoreGoodsOrderItemByOrderId(Long orderId);

    /**
     * 当天订单SPU商品数量
     * @param storeGoodsOrderItem
     * @return
     */
    List<StoreGoodsOrderItem> selectOrderItemNum(StoreGoodsOrderItem storeGoodsOrderItem);

    /**
     * 当天订单报价单商品数量
     * @param storeGoodsOrderItem
     * @return
     */
    List<StoreGoodsOrderItem> selectOrderGoodsNum(StoreGoodsOrderItem storeGoodsOrderItem);

    /**
     * 当天订单SPU商品主副单位换算后数量
     * @param storeGoodsOrderItem
     * @return
     */
    List<StoreGoodsOrderItem> selectOrderSpuConversionNum(StoreGoodsOrderItem storeGoodsOrderItem);

    /**
     * 当天订单报价单商品主副单位换算后数量
     * @param storeGoodsOrderItem
     * @return
     */
    List<StoreGoodsOrderItem> selectOrderGoodsConversionNum(StoreGoodsOrderItem storeGoodsOrderItem);


    /**
     * 查询订单明细列表
     *
     * @param storeGoodsOrderItem 订单明细
     * @return 订单明细集合
     */
    public List<StoreGoodsOrderSortingItemVO> selectStoreGoodsOrderItemListForSorting(StoreGoodsOrderItem storeGoodsOrderItem);

    List<StoreGoodsOrderItem> selectOrderItemComments(StoreGoodsOrderItem storeGoodsOrderItem);

    List<StoreGoodsOrderExportDTO> selectExportGoodsOrderItemList(StoreGoodsOrderQueryDTO storeGoodsOrder);
}
