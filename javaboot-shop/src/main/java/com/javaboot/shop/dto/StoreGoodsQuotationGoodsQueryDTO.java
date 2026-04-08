package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StoreGoodsQuotationGoods;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodsQuotationGoodsVO
 * @Description 报价单商品查询
 * @Date 2021/5/26 0026 17:19
 * @@author lqh
 */
@Data
public class StoreGoodsQuotationGoodsQueryDTO extends StoreGoodsQuotationGoods {
    /**
     * 查询关键字
     */
    private String searchKey;

    /**
     * 区域id
     */
    private String deptId;
    /**
     * 是否查询全部
     */
    private boolean queryAll;


    /**
     * 商品分类
     */
    private String classifyId;

    /**
     * 登陆用户部门
     */
    private String loginUserDeptId;

    /**
     * 商品id列表
     */
    private List<Long> goodsIdList;

    /**
     * 商品id列表
     */
    private String goodsIds;


}
