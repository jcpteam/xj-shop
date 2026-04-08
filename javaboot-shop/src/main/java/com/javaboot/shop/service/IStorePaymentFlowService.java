package com.javaboot.shop.service;

import com.javaboot.shop.domain.StorePaymentFlow;
import java.util.List;
import java.util.Map;

/**
 * 聚合支付流水Service接口
 *
 * @author javaboot
 * @date 2021-09-13
 */
public interface IStorePaymentFlowService {
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
     * @param orderId 聚合支付流水
     * @return 结果
     */
    public StorePaymentFlow insertStorePaymentFlow(Long orderId);

    /**
     * 新增聚合支付流水
     *
     * @param receiptId 聚合支付流水
     * @return 结果
     */
    StorePaymentFlow receiptPay(Long receiptId, String openId);

    /**
     * 修改聚合支付流水
     *
     * @param storePaymentFlow 聚合支付流水
     * @return 结果
     */
    public int updateStorePaymentFlow(StorePaymentFlow storePaymentFlow);

    /**
     * 批量删除聚合支付流水
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStorePaymentFlowByIds(String ids);

    /**
     * 删除聚合支付流水信息
     *
     * @param id 聚合支付流水ID
     * @return 结果
     */
    public int deleteStorePaymentFlowById(Long id);


    void payQuery(StorePaymentFlow payOrderId);

    Map<String, String> payQuery(String payOrderId);

    void updateOrderPay(String payOrderNo, String qryRsp);
}
