package com.javaboot.shop.vo;

import com.javaboot.common.annotation.Excel;
import com.javaboot.shop.domain.StoreBlackWhite;
import lombok.Data;

/**
 * @Classname StoreBlackWhiteVO
 * @Description 黑白名单
 * @Date 2021/7/8 0008 21:18
 * @@author lqh
 */
@Data
public class StoreBlackWhiteVO extends StoreBlackWhite {
    private String typeName;

    private String merchantName;
    private String deptName;

    private String merchantType;
}
