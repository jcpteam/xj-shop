package com.javaboot.shop.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.javaboot.common.annotation.Excel;
import com.javaboot.shop.domain.StoreGoodsQuotationGoods;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import com.javaboot.shop.domain.StoreGoodsSpuUnitConversion;
import com.javaboot.shop.domain.StoreGoodsUnitPrice;
import lombok.Data;

import java.util.List;

/**
 * @Classname StoreGoodsQuotationGoodsVO
 * @Description 报价单商品
 * @Date 2021/5/26 0026 22:55
 * @@author lqh
 */
@Data
public class  StoreGoodsQuotationGoodsVO extends StoreGoodsQuotationGoods {
    /**
     * 区域id
     */
    @Excel(name = "区域id")
    private String deptId;
    /**
     * 区域名称
     */
    @Excel(name = "区域id")
    private String deptName;
    /**
     * spu名称
     */
    @Excel(name = "spu名称")
    private String spuName;
    /**
     * 商品图片
     */
    @Excel(name = "商品图片")
    private String goodsImg;

    /**
     * 编号
     */
    @Excel(name = "编号")
    private String goodsCode;

    /**
     * 基本单位
     */
    @Excel(name = "基本单位")
    private String baseUnit;

    /**
     * 商品分类
     */
    @Excel(name = "商品分类")
    private String classifyId;

    /**
     * 分类名称
     */
    @Excel(name = "分类名称")
    private String classifyName;
    /**
     * 销售单位
     */
    @Excel(name = "规格（销售单位）")
    private List<StoreGoodsSalesUnit> storeGoodsSalesUnitList;


    /**
     * 结算单位名称
     */
    private String mainUnitName;

    /**
     * 分类名称
     */
    @Excel(name = "报价单名称")
    private String quotationName;

    /**
     * 销售单位单价
     */
    @Excel(name = "销售单位单价")
    private List<StoreGoodsUnitPrice> unitPriceList;


    private Double inQuanintiy;

    /**
     * 主单位id
     */
    private Long mainUnitId;


    /**
     * 父单位id
     */
    private Long subUnitId;

    /**
     * 结算单位名称
     */
    private String subUnitName;

    /**
     * 转主单位
     */
    private Double subCaseMain;

    /**
     * 转副单位
     */
    private Double mainCaseSub;
    /**
     * 控价
     */
    private Double settingPrice;

    /**
     * spu商品富文本详情
     */
    private String description;

    private Double settingQuanintiy;

    /**
     * 商户商品备注信息
     */
    private String memberComment;

}
