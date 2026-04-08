package com.javaboot.common.enums;

/**
 * @Classname StockCategory
 * @Description 入库类别
 * @Date 2021/6/27 0027 3:00
 * @@author lqh
 */
public enum StockCategory {
    NOMARL("1", "正常"),
    RETURN_ORDER("2", "退货") ,
    EAS_ORDER("3", "金蝶EAS入库") ,
    PURCHASE_ORDER("4", "采购入库") ,
    ADJUST_IN_ORDER("5", "调拨入库") ,
    ADJUST_OUT_ORDER("6", "调拨出库") ;

    private String code;
    private String desc;

    StockCategory(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDescValue(String code) {
        for (StockCategory orderStatus : values()) {
            if (orderStatus.getCode().equals(code)) {
                return orderStatus.getDesc();
            }
        }
        return null;
    }
}
