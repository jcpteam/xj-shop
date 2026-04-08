package com.javaboot.common.enums;

/**
 * @Classname ReturnOrderStatus
 * @Description 退货单状态
 * @Date 2021/6/26 0026 21:41
 * @@author lqh
 */
public enum ReturnOrderStatus {
    DELETE("0", "删除"),
    WAITING_REVIEW("1", "待审核"),
    PASS("2", "通过"),
    NO_PASS("3", "未通过");

    private String code;
    private String desc;

    ReturnOrderStatus(String code, String desc) {
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
        for (ReturnOrderStatus orderStatus : values()) {
            if (orderStatus.getCode().equals(code)) {
                return orderStatus.getDesc();
            }
        }
        return null;
    }
}
