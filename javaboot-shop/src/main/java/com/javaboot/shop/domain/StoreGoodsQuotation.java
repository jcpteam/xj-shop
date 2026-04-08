package com.javaboot.shop.domain;

import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 报价信息对象 store_goods_quotation
 *
 * @author lqh
 * @date 2021-05-18
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsQuotation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 报价单id
     */
    private Long quotationId;

    /**
     * 区域id
     */
    @Excel(name = "区域id")
    private String deptId;

    /**
     * 报价单名称
     */
    @Excel(name = "报价单名称")
    private String quotationName;

    /**
     * 定价开始时间
     */
    @Excel(name = "定价开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String startTime;

    /**
     * 定价结束时间
     */
    @Excel(name = "定价结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String endTime;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String description;

    /**
     * 0-删除 1-未激活 2-已激活
     */
    @Excel(name = "0-删除 1-未激活 2-已激活")
    private String status;

    /**
     * 最后更新时间
     */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;
    /**
     * 运营开始时间
     */
    @Excel(name = "运营开始时间", width = 30)
    private String operateStartTime;

    /**
     * 运营结束时间
     */
    @Excel(name = "运营结束时间", width = 30)
    private String operateEndTime;
    /**
     * 报价单简称(对外)
     */
    @Excel(name = "报价单简称(对外)")
    private String quotationAbbreviation;

    /**
     * 商品数量
     */
    @Excel(name = "商品数量")
    private Integer goodsNum;

    /**
     * 商品数量
     */
    @Excel(name = "商户数量")
    private Integer meberNum;


}
