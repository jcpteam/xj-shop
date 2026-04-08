package com.javaboot.system.vo;

import com.javaboot.common.annotation.ExcelImport;
import lombok.Data;

/**
 * @Classname SysDeptVO
 * @Description 描述
 * @Date 2021/5/29 0029 21:16
 * @@author lqh
 */
@Data
public class SysDeptVO {

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 父部门ID
     */
    private String parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名称
     */
    private String deptName;

}
