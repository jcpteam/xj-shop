package com.javaboot.shop.vo;

import com.javaboot.shop.domain.StoreGoodsOrderLog;
import lombok.Data;
import lombok.ToString;

/**
 * @Classname StoreGoodsOrderLogVO
 * @Description 描述
 * @Date 2021/6/3 0003 21:46
 * @@author lqh
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsOrderLogVO extends StoreGoodsOrderLog {

    /**
     * 1-创建 2-修改 3-删除 4-审核 5-分拣 6-结束
     */
    private String typeName;

    /**
     * 操作人id
     */
    private String operateUserName;
}
