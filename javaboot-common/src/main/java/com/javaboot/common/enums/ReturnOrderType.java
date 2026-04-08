package com.javaboot.common.enums;

/**
 * @Classname ReturnOrderType
 * @Description 退货单类型
 * @Date 2021/6/26 0026 21:42
 * @@author lqh
 */
public enum ReturnOrderType {

    NO_DELIVERY("1", "退货,无提货"),
    LOSS("2", "退货,运损"),
    WAREHOUSING("3", "退货入库");

    private String code;
    private String desc;

    ReturnOrderType(String code, String desc) {
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
        for (ReturnOrderType orderStatus : values()) {
            if (orderStatus.getCode().equals(code)) {
                return orderStatus.getDesc();
            }
        }
        return null;
    }
}
