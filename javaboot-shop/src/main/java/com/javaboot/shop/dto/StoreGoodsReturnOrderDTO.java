package com.javaboot.shop.dto;

import com.alibaba.fastjson.JSONArray;
import com.javaboot.shop.domain.StoreGoodsOrderItem;
import com.javaboot.shop.domain.StoreGoodsReturnOrder;
import com.javaboot.shop.domain.StoreGoodsReturnOrderItem;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname StoreGoodsRetunOrderDTO
 * @Description 退货单
 * @Date 2021/6/26 0026 21:51
 * @@author lqh
 */
@Data
public class StoreGoodsReturnOrderDTO extends StoreGoodsReturnOrder {

    /**
     * 订单商品
     */
    private List<StoreGoodsReturnOrderItem> itemList;
    private String itemData;

    public void setItemData(String itemData) {
        this.itemData = itemData;
        if (StringUtils.isNotEmpty(itemData)) {
            this.itemList = JSONArray.parseArray(itemData, StoreGoodsReturnOrderItem.class);
            BigDecimal totalPrice = new BigDecimal(0);
            for (StoreGoodsReturnOrderItem item : this.itemList) {
                BigDecimal amount = calculateAmount(item.getPrice(), item.getQuantity());
                item.setAmount(amount.doubleValue());
                totalPrice = totalPrice.add(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            setPrice(totalPrice.doubleValue());
        }
    }

    /**
     * 计算金额
     * @param price
     * @param quantity
     * @return
     */
    private static BigDecimal calculateAmount(double price, double quantity) {
        return BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(quantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
