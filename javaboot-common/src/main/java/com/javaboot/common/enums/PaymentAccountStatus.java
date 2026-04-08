package com.javaboot.common.enums;

/**
 * @Classname PaymentAccountStatus
 * @Description 描述
 * @Date 2021/7/9 0009 20:01
 * @@author lqh
 */
public enum PaymentAccountStatus {
    DELETE("0","删除"),
    ENABLE("1","启用"),
    DISABLE("2","禁用");

    private String code;
    private String desc;

    PaymentAccountStatus(String code, String desc) {
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

        for (PaymentAccountStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}
