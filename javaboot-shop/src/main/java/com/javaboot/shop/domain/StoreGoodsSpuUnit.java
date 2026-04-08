package com.javaboot.shop.domain;

import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * spu单位对象 store_goods_spu_unit
 *
 * @author lqh
 * @date 2021-06-20
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsSpuUnit extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 商品spu */
    private String spuNo;

    /** 主单位 */
    @Excel(name = "主单位")
    private Long mainUnitId;

    /** 主单位 */
    @Excel(name = "主单位名称")
    private String mainUnitName;

    /** 副单位 */
    @Excel(name = "副单位")
    private Long subUnitId;

    /** 主单位 */
    @Excel(name = "副单位名称")
    private String subUnitName;

    /** 最后更新时 */
    @Excel(name = "最后更新时", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    private List<String> spuNoList;

    private  String  spuNos;
    /**
     * 主单位
     */
    private  StoreGoodsSalesUnit mainUnit;
    /**
     * 副单位
     */
    private  StoreGoodsSalesUnit subUnit;

    /**
     * 单位信息
     */
    List<StoreGoodsSalesUnit> unitList;

    /** 转主单位 */
    @Excel(name = "转主单位换算关系")
    private Double subCaseMain;

    /** 转副单位 */
    @Excel(name = "转副单位换算关系")
    private Double mainCaseSub;

}
