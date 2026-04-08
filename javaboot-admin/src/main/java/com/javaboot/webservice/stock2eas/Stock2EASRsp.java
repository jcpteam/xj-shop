package com.javaboot.webservice.stock2eas;

import lombok.Data;

import java.util.List;

@Data
public class Stock2EASRsp
{
    private String result;

    private String msg;

    private List<String> data;
}
