package com.javaboot.common.enums;

/**
 * @Classname OrderSource
 * @Description 订单来源
 * @Date 2021/5/30 0030 18:43
 * @@author lqh
 */
public enum OrderSource {
    WX("1","微信"),
    REPLACE_ORDER("2","代下单"),
    SORTING_ORDER("3","分拣下单");

    private String code;
    private String desc;

    OrderSource(String code, String desc) {
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

        for (OrderSource orderSource : values()) {
            if (orderSource.getCode().equals(code)) {
                return orderSource.getDesc();
            }
        }
        return null;
    }
}
