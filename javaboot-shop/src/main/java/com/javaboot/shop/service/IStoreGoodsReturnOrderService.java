package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreGoodsReturnOrder;
import com.javaboot.shop.dto.StoreGoodsReturnOrderDTO;
import com.javaboot.shop.dto.StoreGoodsReturnOrderExamineDTO;
import com.javaboot.shop.dto.StoreGoodsReturnOrderQueryDTO;
import com.javaboot.shop.vo.StoreGoodsReturnOrderVO;

import java.util.List;

/**
 * 退货单信息主Service接口
 * 
 * @author lqh
 * @date 2021-06-26
 */
public interface IStoreGoodsReturnOrderService {
    /**
     * 查询退货单信息主
     * 
     * @param returnOrderId 退货单信息主ID
     * @return 退货单信息主
     */
    public StoreGoodsReturnOrderVO selectStoreGoodsReturnOrderById(Long returnOrderId);

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
     * 批量删除退货单信息主
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsReturnOrderByIds(String ids);

    /**
     * 删除退货单信息主信息
     * 
     * @param returnOrderId 退货单信息主ID
     * @return 结果
     */
    public int deleteStoreGoodsReturnOrderById(Long returnOrderId);

    /**
     * 审核
     * @param examine
     * @return
     */
    int examine(StoreGoodsReturnOrderExamineDTO examine);
}
