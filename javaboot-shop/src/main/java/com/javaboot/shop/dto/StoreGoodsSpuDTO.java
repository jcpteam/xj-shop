package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StoreGoodsSpu;
import lombok.Data;

/**
 * @Classname StoreGoodsSpuDTO
 * @Description 描述
 * @Date 2021/5/28 0028 16:03
 * @@author lqh
 */
@Data
public class StoreGoodsSpuDTO extends StoreGoodsSpu {
    /**
     * 查询关键字
     */
    private String searchKey;
    private Boolean isQueryAll;

    public Boolean getIsQueryAll() {
        if (isQueryAll == null) {
            isQueryAll = false;
        }
        return isQueryAll;
    }
}
