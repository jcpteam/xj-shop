package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StoreReceipt;

import lombok.Data;

import java.util.List;

/**
 * @Classname StoreReceiptQueryDTO
 * @Description 收款单查询
 * @Date 2021/7/5 0005 23:14
 * @@author lqh
 */
@Data
public class StoreReceiptQueryDTO extends StoreReceipt {

    private String merchantName;
    /**
     * 金额范围
     */
    private Double minAmount;
    /**
     * 金额范围
     */
    private Double maxAmount;

    private List<String> orderIdList;
    private String orderIds;

    private List<Long> receiptIds;

    /**
     * 支付开始时间
     */
    private String payBeginDate;
    /**
     * 支付结束时间
     */
    private String payEndDate;

    /**
     * 付款单开始时间
     */
    private String receiptBeginDate;

    /**
     * 付款单结束时间
     */
    private String receiptEndDate;
}
