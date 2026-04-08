package com.javaboot.shop.dto;

import lombok.Data;

@Data
public class StoreStatDTO
{

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;

    /** 部门id */
    private String deptId;

    /** 周开始时间 */
    private String weekStartTime;

    /** 周结束时间 */
    private String weekEndTime;
}
