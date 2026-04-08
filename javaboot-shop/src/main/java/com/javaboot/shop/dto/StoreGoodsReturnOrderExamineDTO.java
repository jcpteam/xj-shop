package com.javaboot.shop.dto;

import com.javaboot.common.utils.StringUtils;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @Classname StoreGoodsReturnOrderExamineDTO
 * @Description 退货单审核
 * @Date 2021/6/26 0026 23:05
 * @@author lqh
 */
@Data
public class StoreGoodsReturnOrderExamineDTO {

    private String ids;
    private List<String> idList;
    private String status;

    public void setIds(String ids) {
        this.ids = ids;
        if(StringUtils.isNotEmpty(ids)){
            this.idList= Arrays.asList(ids.split(","));
        }
    }


}
