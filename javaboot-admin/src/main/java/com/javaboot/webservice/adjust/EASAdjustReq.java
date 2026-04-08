package com.javaboot.webservice.adjust;

import lombok.Data;

import java.util.List;

@Data
public class EASAdjustReq
{
    private EASAdjustInfo bill;

    private List<EASAdjustItem> entry;
}
