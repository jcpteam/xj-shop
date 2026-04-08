package com.javaboot.common.enums;

/**
 * @Classname AccountFlowType
 * @Description 账号流水操作类型
 * @Date 2021/7/9 0009 20:04
 * @@author lqh
 */
public enum AccountType
{
    CASH("1","现金账户"),
    BOND("2","保证金账户"),
    REBATE("3","返点账户");

    private String code;
    private String desc;

    AccountType(String code, String desc) {
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

        for (AccountType type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return null;
    }

}
