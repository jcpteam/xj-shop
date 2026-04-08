package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StoreGoodPurchase;
import com.javaboot.system.domain.SysUser;
import lombok.Data;
import org.apache.shiro.SecurityUtils;

/**
 * @Classname StoreGoodPurchaseQueryDTO
 * @Description 采购查询
 * @Date 2021/6/5 0005 13:18
 * @@author lqh
 */
@Data
public class StoreGoodPurchaseQueryDTO extends StoreGoodPurchase {

    /**
     * 操作开始时间
     */
    private String beginDate;
    /**
     * 操作结束时间
     */
    private String endDate;

    /**
     * 采购开始时间
     */
    private String purchaseBeginDate;
    /**
     * 采购结束时间
     */
    private String purchaseEndDate;

    /**
     * 登陆用户部门
     */
    private String loginUserDeptId;


    public String getLoginUserDeptId() {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        return user.isAdmin()?null:user.getDeptId();
    }
}
