package com.javaboot.common.enums;

/**
 * @Classname OrderPrintStatus
 * @Description 订单打印状态
 * @Date 2021/5/30 0030 18:52
 * @@author lqh
 */
public enum OrderPrintStatus {
    NOT_PRINT("0","未打印"),
    PRINTED("1","已打印");

    private String code;
    private String desc;

    OrderPrintStatus(String code, String desc) {
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

    /**
     * 自己定义一个静态方法,通过code返回枚举常量对象
     * @param code
     * @return
     */
    public static String getDescValue(String code) {
        for (OrderPrintStatus orderPrintStatus : values()) {
            if (orderPrintStatus.getCode().equals(code)) {
                return orderPrintStatus.getDesc();
            }
        }
        return null;
    }
}
