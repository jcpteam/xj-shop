package com.javaboot.common.enums;

/**
 * @Classname ReceiptStatus
 * @Description 描述
 * @Date 2021/8/1 0001 11:12
 * @@author lqh
 */
public enum ReceiptStatus {
    DELETE("0", "删除"),
    NORMAL("1", "正常"),
    CANCEL("2", "已作废");

    private String code;
    private String desc;

    ReceiptStatus(String code, String desc) {
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
        for (ReceiptStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}
