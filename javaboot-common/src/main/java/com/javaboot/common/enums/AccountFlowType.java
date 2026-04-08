package com.javaboot.common.enums;

/**
 * @Classname AccountFlowType
 * @Description 账号流水操作类型
 * @Date 2021/7/9 0009 20:04
 * @@author lqh
 */
public enum AccountFlowType {
    //ADVANCE("0","预付"),
    RECHARGE("1","充值"),
    PAY("2","支付"),
    WITHDRAW("3","提现"),
    CANCEL_CERTIFICAT("4","冲账还原");

    private String code;
    private String desc;

    AccountFlowType(String code, String desc) {
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

        for (AccountFlowType type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return null;
    }

}
