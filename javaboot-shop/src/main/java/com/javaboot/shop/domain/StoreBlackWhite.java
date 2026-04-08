package com.javaboot.shop.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;
import java.util.List;

/**
 * 黑白名单对象 store_black_white
 * 
 * @author lqh
 * @date 2021-07-08
 */
@Data
@ToString(callSuper = true)
public class StoreBlackWhite extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long blackWhiteId;

    /** 账户ID */
    private String accountId;
    /** 客户id */
    @Excel(name = "客户id")
    private String merchantId;

    /** 区域id */
    @Excel(name = "区域id")
    private String deptId;

    /** 0-白名单，1-黑名单 */
    @Excel(name = "0-白名单，1-黑名单")
    private String type;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    /** 状态:0-删除 1-正常 */
    @Excel(name = "状态:0-删除 1-正常")
    private String status;
    private String searchKey;

    private String ids;
    private List<String> idList;

}
