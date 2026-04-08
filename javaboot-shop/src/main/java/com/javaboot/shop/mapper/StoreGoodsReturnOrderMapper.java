package com.javaboot.shop.mapper;

import java.util.List;

import com.javaboot.shop.dto.StoreGoodsReturnOrderDTO;
import com.javaboot.shop.dto.StoreGoodsReturnOrderExamineDTO;
import com.javaboot.shop.dto.StoreGoodsReturnOrderQueryDTO;
import com.javaboot.shop.vo.StoreGoodsReturnOrderVO;

/**
 * 退货单信息主Mapper接口
 * 
 * @author lqh
 * @date 2021-06-26
 */
public interface StoreGoodsReturnOrderMapper {
    /**
     * 查询退货单信息主
     * 
     * @param ReturnOrderId 退货单信息主ID
     * @return 退货单信息主
     */
    public StoreGoodsReturnOrderVO selectStoreGoodsReturnOrderById(Long ReturnOrderId);

    /**
     * 查询退货单信息主列表
     * 
     * @param storeGoodsReturnOrder 退货单信息主
     * @return 退货单信息主集合
     */
    public List<StoreGoodsReturnOrderVO> selectStoreGoodsReturnOrderList(StoreGoodsReturnOrderQueryDTO storeGoodsReturnOrder);

    /**
     * 新增退货单信息主
     * 
     * @param storeGoodsReturnOrder 退货单信息主
     * @return 结果
     */
    public int insertStoreGoodsReturnOrder(StoreGoodsReturnOrderDTO storeGoodsReturnOrder);

    /**
     * 修改退货单信息主
     * 
     * @param storeGoodsReturnOrder 退货单信息主
     * @return 结果
     */
    public int updateStoreGoodsReturnOrder(StoreGoodsReturnOrderDTO storeGoodsReturnOrder);

    /**
     * 删除退货单信息主
     * 
     * @param returnOrderId 退货单信息主ID
     * @return 结果
     */
    public int deleteStoreGoodsReturnOrderById(Long returnOrderId);

    /**
     * 批量删除退货单信息主
     * 
     * @param returnOrderIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsReturnOrderByIds(String[] returnOrderIds);

    /**
     * 审核
     * @param examine
     * @return
     */
    int examine(StoreGoodsReturnOrderExamineDTO examine);
}
