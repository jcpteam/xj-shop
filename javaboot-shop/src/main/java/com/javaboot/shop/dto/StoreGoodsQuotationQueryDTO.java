package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StoreGoodsQuotation;
import com.javaboot.system.domain.SysUser;
import lombok.Data;
import lombok.ToString;
import org.apache.shiro.SecurityUtils;

import java.util.List;

/**
 * @Classname StoreGoodsQuotationQueryDTO
 * @Description 报价单查询
 * @Date 2021/6/2 0002 17:48
 * @@author lqh
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsQuotationQueryDTO extends StoreGoodsQuotation {
    /**
     * 登陆用户部门
     */
    private String loginUserDeptId;


    /**
     * 登陆用户部门
     */
    private List<Long> quotationIdList;

    /**
     * 请求参数-区域id
     */
    private String deptId;

    /**
     * 是否查询全部
     */
    private boolean queryAll;

    public String getLoginUserDeptId() {
        try {
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            return user.isAdmin()?null:user.getDeptId();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
