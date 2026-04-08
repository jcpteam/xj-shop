package com.javaboot.shop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@TableName("store_member_comment")
@EqualsAndHashCode(callSuper = true)
public class StoreMemberComment extends BaseEntity
{

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户id
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 报价单商品id
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * 商品spu
     */
    @TableField("spu_no")
    private String spuNo;

    /**
     * 备注
     */
    @TableField("comment")
    private String comment;

    /**
     * 最后更新时间
     */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("last_modify_time")
    private Date lastModifyTime;

}
