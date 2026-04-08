package com.javaboot.shop.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodsClassifyTreeVO
 * @Description 分类树结构
 * @Date 2021/5/25 0025 10:33
 * @@author lqh
 */
@Data
@Builder
public class StoreGoodsClassifyTreeVO {
    /**
     * 值
     */
    private String value;
    /**
     * 父Id
     */
    private String parentId;
    /**
     * 显示内容
     */
    private String label;

    /**
     * 子节点
     */
    private List<StoreGoodsClassifyTreeVO> children;
}
