package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StoreAccountFlow;
import lombok.Data;

/**
 * @Classname StoreAccountFlowQueryDTO
 * @Description 账号流水查询
 * @Date 2021/7/9 0009 20:08
 * @@author lqh
 */
@Data
public class StoreAccountFlowQueryDTO extends StoreAccountFlow {
    /**
     * 关键字
     */
    private String searchKey;

    /**
     * 部门id
     */
    private String deptId;
}
