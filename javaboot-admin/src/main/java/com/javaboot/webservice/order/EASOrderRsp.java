package com.javaboot.webservice.order;

import lombok.Data;

import java.util.List;

@Data
public class EASOrderRsp
{
    private String result;

    private String msg;

    private List<String> data;
}
