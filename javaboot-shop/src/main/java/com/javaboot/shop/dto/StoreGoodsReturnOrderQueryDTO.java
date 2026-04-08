package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StoreGoodsReturnOrder;
import com.javaboot.system.domain.SysUser;
import lombok.Data;
import org.apache.shiro.SecurityUtils;

import java.util.List;

/**
 * @Classname StoreGoodsReturnOrderQuery
 * @Description 退货单
 * @Date 2021/6/26 0026 21:58
 * @@author lqh
 */
@Data
public class StoreGoodsReturnOrderQueryDTO extends StoreGoodsReturnOrder {

    /**
     * 订单信息关键字
     */
    private String orderKey;
    /**
     * 收货信息关键字
     */
    private String deliveryKey;
    /**
     * 创建开始时间
     */
    private String createBeginDate;
    /**
     * 创建结束时间
     */
    private String createEndDate;

    /** 退货单id */
    private List<String> returnOrderIds;
    public String getLoginUserDeptId() {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        return user.isAdmin()?null:user.getDeptId();
    }
}
