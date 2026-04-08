package com.javaboot.common.enums;

/**
 * @Classname GoodsQuotationStatus
 * @Description 报价状态枚举
 * @Date 2021/5/19 0019 19:42
 * @@author lqh
 */
public enum GoodsQuotationStatus {

    ON("1","on"),
    OFF("2","off");
    private String value;
    private String code;

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    GoodsQuotationStatus(String value, String code) {
        this.value = value;
        this.code = code;
    }
}
