package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StoreWarehouseStock;
import com.javaboot.system.domain.SysUser;
import lombok.Data;
import org.apache.shiro.SecurityUtils;

/**
 * @Classname StoreWarehouseStockQueryDTO
 * @Description 入库查询
 * @Date 2021/6/5 0005 11:18
 * @@author lqh
 */
@Data
public class StoreWarehouseStockQueryDTO extends StoreWarehouseStock {

    /**
     * 操作开始时间
     */
    private String beginDate;
    /**
     * 操作结束时间
     */
    private String endDate;

    /**
     * 入库开始时间
     */
    private String stockBeginDate;
    /**
     * 入库结束时间
     */
    private String stockEndDate;

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

    private StoreReqPage reqPage;

    public String getLoginUserDeptId() {
        try {
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            return user.isAdmin()?null:user.getDeptId();
        }catch (Exception e) {
        }
        return null;
    }
}
