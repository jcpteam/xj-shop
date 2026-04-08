package com.javaboot.system.vo;

import com.javaboot.common.annotation.Excel;
import com.javaboot.system.domain.SysUser;

public class SortingDailyUserReq extends SysUser {
    @Excel(name = "每日考勤id")
    private Long workId;

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }
}
