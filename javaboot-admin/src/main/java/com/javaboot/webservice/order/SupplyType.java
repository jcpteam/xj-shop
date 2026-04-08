package com.javaboot.webservice.order;

public enum SupplyType {
    IN(0),
    OUT(1),
    BETEEWN(2);
    private final int value;

    SupplyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}