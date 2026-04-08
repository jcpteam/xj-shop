package com.javaboot.common.enums;

/**
 * @Classname OrderPaysSatus
 * @Description 描述
 * @Date 2021/5/30 0030 18:55
 * @@author lqh
 */
public enum OrderPayStatus {
    NOT_PAY("0", "未付款"),
    PAYED("1", "已付款");

    private String code;
    private String desc;

    OrderPayStatus(String code, String desc) {
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
     *
     * @param code
     * @return
     */
    public static String getDescValue(String code) {

        for (OrderPayStatus orderPayStatus : values()) {
            if (orderPayStatus.getCode().equals(code)) {
                return orderPayStatus.getDesc();
            }
        }
        return null;
    }
}
