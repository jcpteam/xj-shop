package com.javaboot.common.enums;

/**
 * 订单状态
 *
 * @author lqh
 */
public enum OrderStatus {
    DELETE("0", "删除"),
    WAITING_REVIEW("1", "待审核"),
    WAITING_SORTING("2", "待分拣"),
    SENDED("3", "已分拣"),
    FINISH("4", "已完成"),
    CANCEL("5", "已作废");

    private String code;
    private String desc;

    OrderStatus(String code, String desc) {
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
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.getCode().equals(code)) {
                return orderStatus.getDesc();
            }
        }
        return null;
    }


}
