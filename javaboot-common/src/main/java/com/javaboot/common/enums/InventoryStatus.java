package com.javaboot.common.enums;

/**
 * @Classname InventoryStatus
 * @Description 盘库盘点状态
 * @Date 2021/6/14 0014 16:01
 * @@author lqh
 */
public enum InventoryStatus {
    DEFICIT("0","盘亏"),
    PROFIT("1","盘赢");

    private String code;
    private String desc;

    InventoryStatus(String code, String desc) {
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

        for (InventoryStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}
