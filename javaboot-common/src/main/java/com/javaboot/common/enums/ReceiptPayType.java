package com.javaboot.common.enums;

public enum ReceiptPayType
{
    ACCOUNT_PAY("1", "账户支付"),
    CMB_PAY("2", "招行支付"),
    OFFLINE_PAY("3", "线下冲账");

    private String code;
    private String desc;

    ReceiptPayType(String code, String desc) {
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
        for (ReceiptPayType receiptPayType : values()) {
            if (receiptPayType.getCode().equals(code)) {
                return receiptPayType.getDesc();
            }
        }
        return null;
    }

}
