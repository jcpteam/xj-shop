package com.javaboot.system.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SysDeptTreeVO
 * @Description 部门树结构（参照fromSelect4.js数据结构）
 * @Date 2021/5/20 0020 11:30
 * @@author lqh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysDeptSelectVO {
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;

}
