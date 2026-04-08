package com.javaboot.shop.vo;

import java.util.Map;

/**
 * @ClassName SpecVO
 * @author javaboot
 * @Date 2019/8/27
 **/
public class SpecAttrVO {
    private String goodsId;

    private Map<String, String> item;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Map<String, String> getItem() {
        return item;
    }

    public void setItem(Map<String, String> item) {
        this.item = item;
    }
}
