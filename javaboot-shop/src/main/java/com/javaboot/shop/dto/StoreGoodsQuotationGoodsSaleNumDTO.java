package com.javaboot.shop.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.javaboot.common.annotation.Excel;
import lombok.Builder;
import lombok.Data;

/**
 * @Classname StoreGoodsQuotationGoodsSaleNumDTO
 * @Description 报价单商品库存操作
 * @Date 2021/6/3 0003 11:36
 * @@author lqh
 */
@Data
@Builder
public class StoreGoodsQuotationGoodsSaleNumDTO {

    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 购买数量
     */
    private Double buyNum;

    /**
     * 是否增加库存
     */
    private Boolean isAdd;

    private String spuNo;

    private String startTime;

    private String endTime;

    private String deptId;
}
