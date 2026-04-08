package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StorePaymentFlow;
import java.util.List;

/**
 * 聚合支付流水Mapper接口
 *
 * @author javaboot
 * @date 2021-09-13
 */
public interface StorePaymentFlowMapper {
    /**
     * 查询聚合支付流水
     *
     * @param id 聚合支付流水ID
     * @return 聚合支付流水
     */
    public StorePaymentFlow selectStorePaymentFlowById(Long id);

    /**
     * 查询聚合支付流水列表
     *
     * @param storePaymentFlow 聚合支付流水
     * @return 聚合支付流水集合
     */
    public List<StorePaymentFlow> selectStorePaymentFlowList(StorePaymentFlow storePaymentFlow);

    /**
     * 新增聚合支付流水
     *
     * @param storePaymentFlow 聚合支付流水
     * @return 结果
     */
    public int insertStorePaymentFlow(StorePaymentFlow storePaymentFlow);

    /**
     * 修改聚合支付流水
     *
     * @param storePaymentFlow 聚合支付流水
     * @return 结果
     */
    public int updateStorePaymentFlow(StorePaymentFlow storePaymentFlow);

    int updateStorePaymentFlowStatus(StorePaymentFlow storePaymentFlow);

    /**
     * 删除聚合支付流水
     *
     * @param id 聚合支付流水ID
     * @return 结果
     */
    public int deleteStorePaymentFlowById(Long id);

    /**
     * 批量删除聚合支付流水
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStorePaymentFlowByIds(String[] ids);
}
