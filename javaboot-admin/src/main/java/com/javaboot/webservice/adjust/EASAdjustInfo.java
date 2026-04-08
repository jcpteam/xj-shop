package com.javaboot.webservice.adjust;

import lombok.Data;

@Data
public class EASAdjustInfo
{
    private String bizType;

    private String creator;

    private String outStorageOrg;

    private String inStorageOrg;

    private String remark;
}
