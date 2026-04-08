package com.javaboot.common.enums;

/**
 * @Classname InventoryChangeType
 * @Description 盘库变动类型
 * @Date 2021/6/14 0014 16:00
 * @@author lqh
 */
public enum InventoryChangeType {
    LOSS("0", "报损"),
    OVERFLOW("1", "报溢");

    private String code;
    private String desc;

    InventoryChangeType(String code, String desc) {
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

        for (InventoryChangeType changeType : values()) {
            if (changeType.getCode().equals(code)) {
                return changeType.getDesc();
            }
        }
        return null;
    }
}
