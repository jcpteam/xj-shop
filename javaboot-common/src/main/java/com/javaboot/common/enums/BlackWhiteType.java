package com.javaboot.common.enums;

/**
 * @Classname BlackWhiteType
 * @Description 描述
 * @Date 2021/7/8 0008 21:16
 * @@author lqh
 */
public enum BlackWhiteType {
    White("0", "白名单"),
    Black("1", "黑名单"),
    WhiteForever("2", "永久白名单");

    private String code;
    private String desc;

    BlackWhiteType(String code, String desc) {
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
        for (BlackWhiteType type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return null;
    }
}
