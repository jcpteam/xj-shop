package com.javaboot.shop.vo;

import com.javaboot.shop.domain.StoreAccountFlow;
import lombok.Data;

/**
 * @Classname StoreAccountFlowVO
 * @Description 账号流水
 * @Date 2021/7/9 0009 20:11
 * @@author lqh
 */
@Data
public class StoreAccountFlowVO extends StoreAccountFlow {
    private String typeName;
}
