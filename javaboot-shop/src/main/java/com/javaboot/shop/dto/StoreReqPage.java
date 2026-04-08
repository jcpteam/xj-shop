package com.javaboot.shop.dto;

import lombok.Data;

@Data
public class StoreReqPage
{
    private Integer pagePreNum;

    private Integer pageNo;

    public StoreReqPage(int pagePreNum, int pageNo) {
        this.pagePreNum = pagePreNum;
        this.pageNo = pageNo;
    }
}
