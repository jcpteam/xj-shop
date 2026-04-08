package com.javaboot.shop.dto;

import com.alibaba.fastjson.JSONArray;
import com.javaboot.shop.domain.StoreGoodsOrder;
import com.javaboot.shop.domain.StoreGoodsOrderItem;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname StoreGoodsOrderDTO
 * @Description 描述
 * @Date 2021/6/2 0002 10:10
 * @@author lqh
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsOrderDTO extends StoreGoodsOrder {
    /**
     * 订单商品
     */
    private List<StoreGoodsOrderItem> itemList;
    private String itemData;
    /**
     * 菜单类型
     */
    private String menuType;

    /**
     * 订单密码
     */
    private String orderPassword;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 是否是从分拣直接下的订单  1：是
     */
    private String fromSorting;

    /**
     * 是否是钉钉端下单  1：是
     */
    private String fromDingding;

    public void setItemData(String itemData) {
        this.itemData = itemData;
        if (StringUtils.isNotEmpty(itemData)) {
            this.itemList = JSONArray.parseArray(itemData, StoreGoodsOrderItem.class);
            BigDecimal totalPrice = new BigDecimal(0);
            for (StoreGoodsOrderItem item : this.itemList) {
                BigDecimal amount = calculateAmount(item.getPrice(), item.getSortingWeight(), item.getDiscount());
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
    private static BigDecimal calculateAmount(double price, double quantity, double discount) {
        return BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(quantity)).multiply(BigDecimal.valueOf(discount)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
