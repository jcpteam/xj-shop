package com.javaboot.common.enums;

/**
 * @Classname OrderLogType
 * @Description 订单日志类型
 * @Date 2021/6/3 0003 21:42
 * @@author lqh
 */
public enum OrderLogType {
    CREATE("1", "创建订单"),
    /**
     * 除了价格其他信息变动都算修改
     */
    UPDATE("2", "修改订单"),
    DELETE("3", "删除订单"),
    EXAMINE("4", "订单审核"),
    SORTING("5", "订单分拣"),
    FINANCE_EXAMINE("6", "订单财务审核"),
    UPDATE_PRICE("7", "订单价格修改"),
    PAY_RECEIPT("8", "订单生成付款单"),
    COMPLETE("9", "完成订单"),
    CANCEL("10", "作废"),
    CANCEL_REVERT("11", "作废还原"),
    CANCEL_CERTIFICAT("12", "删除凭证");
    private String code;
    private String desc;

    OrderLogType(String code, String desc) {
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

        for (OrderLogType orderLogType : values()) {
            if (orderLogType.getCode().equals(code)) {
                return orderLogType.getDesc();
            }
        }
        return null;
    }
}
