package com.javaboot.webservice.stock2eas;

import lombok.Data;

import java.util.List;

@Data
public class Stock2EASReq
{
    private Stock2EASInfo bill;

    private List<Stock2EASItem> entry;
}
