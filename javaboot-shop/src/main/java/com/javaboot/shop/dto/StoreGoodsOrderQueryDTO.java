package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StoreGoodsOrder;
import com.javaboot.system.domain.SysUser;
import lombok.Data;
import lombok.ToString;
import org.apache.shiro.SecurityUtils;

import java.util.List;

/**
 * @Classname StoreGoodsOrderQueryDTO
 * @Description 订单查询
 * @Date 2021/6/2 0002 16:44
 * @@author lqh
 */
@Data
@ToString(callSuper = true)
public class StoreGoodsOrderQueryDTO extends StoreGoodsOrder {
    /**
     * 订单信息关键字
     */
    private String orderKey;
    /**
     * 收货信息关键字
     */
    private String deliveryKey;
    /**
     * 类型
     */
    private String typeKey;
    /**
     * 交货开始时间
     */
    private String deliveryBeginDate;
    /**
     * 交货结束时间
     */
    private String deliveryEndDate;
    /**
     * 创建开始时间
     */
    private String createBeginDate;
    /**
     * 创建结束时间
     */
    private String createEndDate;

    /**
     * 登陆用户部门
     */
    private String loginUserDeptId;
    /**
     * 状态集合
     */
    private List<String> statusList;

    /**
     * 商户id集合
     */
    private List<String> merchantIdList;

    /**
     * 分拣状态查询：0-全部订单，1-待分拣订单，2-已分拣订单
     */
    private String sortingStatus;

    public String getLoginUserDeptId() {
        try {
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            return user.isAdmin()?null:user.getDeptId();
        }catch (Exception e) {
        }
        return null;
    }

    /**
     * 金额范围
     */
    private Double minPrice;
    /**
     * 金额范围
     */
    private Double maxPrice;

    private List<String> orderIdList;

    private List<Long> orderIdListLong;

    /**
     * 客户结算id字符串查询条件
     */
    private String orderIds;

    //分拣搜索的线路id
    private Long routeId;
    //分拣搜索的报价单id
    private Long quotationId;

    private StoreReqPage reqPage;

    /**
     * 打印更新类型：1-配货单，2-出库单
     */
    private String printUpdateType;

}
