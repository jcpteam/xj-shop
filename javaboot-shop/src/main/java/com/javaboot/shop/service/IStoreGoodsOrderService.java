package com.javaboot.shop.service;

import java.util.List;

import com.javaboot.common.exception.BusinessException;
import com.javaboot.shop.domain.StoreDeliverRoute;
import com.javaboot.shop.domain.StoreGoodsQuotation;
import com.javaboot.shop.dto.StoreGoodsCheckTodayOrder;
import com.javaboot.shop.dto.StoreGoodsOrderDTO;
import com.javaboot.shop.dto.StoreGoodsOrderQueryDTO;
import com.javaboot.shop.dto.StoreGoodsOrderSortingQueryReq;
import com.javaboot.shop.vo.SortingQueryMerchantInfo;
//import com.javaboot.shop.vo.StoreGoodsOrderSortingCountVO.java;
import com.javaboot.shop.vo.StoreGoodsOrderSortingCountVO;
import com.javaboot.shop.vo.StoreGoodsOrderSortingItemVO;
import com.javaboot.shop.vo.StoreGoodsOrderVO;
import com.javaboot.system.domain.SysUser;

/**
 * 订单信息主Service接口
 *
 * @author lqh
 * @date 2021-05-30
 */
public interface IStoreGoodsOrderService {
    /**
     * 查询订单信息主
     *
     * @param orderId
     *            订单信息主ID
     * @return 订单信息主
     */
    public StoreGoodsOrderVO selectStoreGoodsOrderById(Long orderId);

    /**
     * 查询订单信息主列表
     *
     * @param storeGoodsOrder
     *            订单信息主
     * @return 订单信息主集合
     */
    public List<StoreGoodsOrderVO> selectStoreGoodsOrderList(StoreGoodsOrderQueryDTO storeGoodsOrder);

    /**
     * 查询订单信息主列表
     *
     * @param storeGoodsOrder
     *            订单信息主
     * @return 订单信息主集合
     */
    public List<StoreGoodsOrderVO> selectStoreGoodsOrderListForApp(StoreGoodsOrderQueryDTO storeGoodsOrder);

    /**
     * 新增订单信息主
     *
     * @param storeGoodsOrder
     *            订单信息主
     * @return 结果
     */
    public int insertStoreGoodsOrder(StoreGoodsOrderDTO storeGoodsOrder,boolean isApp) throws BusinessException;

    /**
     * 修改订单信息主
     *
     * @param storeGoodsOrder
     *            订单信息主
     * @return 结果
     */
    public int updateStoreGoodsOrder(StoreGoodsOrderDTO storeGoodsOrder,boolean isApp);

    /**
     * 批量删除订单信息主
     *
     * @param ids
     *            需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsOrderByIds(String ids);

    public int deleteStoreGoodsOrderByIdsCheckIfNeedRestore(String ids,boolean restoreCart);

    /**
     * 作废订单还原
     * @param ids
     * @return
     */
    int revertStoreGoodsOrderByIds(String ids);

    /**
     * 审核订单
     *
     * @param ids
     * @return
     */
    int examineStoreGoodsOrderByIds(String ids);

    /**
     * 财务审核订单
     *
     * @param ids
     * @return
     */
    int financialExamineStoreGoodsOrderByIds(String ids);

    /**
     * 财务取消订单审核
     *
     * @param ids
     * @return
     */
    int financialCancelStoreGoodsOrderByIds(String ids);

    /**
     * 删除订单信息主信息
     *
     * @param orderId
     *            订单信息主ID
     * @return 结果
     */
    public int deleteStoreGoodsOrderById(Long orderId);

    /**
     * 查询订单信息主列表
     *
     * @param storeGoodsOrder
     *            订单信息主
     * @return 订单信息主集合
     */
    public List<StoreGoodsOrderVO> selectStoreGoodsOrderListForSorting(StoreGoodsOrderQueryDTO storeGoodsOrder);

    /**
     * 获取订单已分拣的数量
     * 
     * @param ids
     * @return
     */
    public List<StoreGoodsOrderSortingCountVO> getStoreGoodsOrderSortingCountByIds(String ids);

    public List<SortingQueryMerchantInfo> getSortingMerchantInfo();

    public List<StoreDeliverRoute> getSortingRouteInfo();

    public List<StoreGoodsQuotation> getSortingQuotationInfo();

    /**
     * 查询订单信息主
     *
     * @param req
     *            订单信息主ID
     * @return 订单信息主
     */
    public List<StoreGoodsOrderSortingItemVO> selectStoreGoodsOrderByIdForSorting(StoreGoodsOrderSortingQueryReq req);

    /**
     * 添加日志:与商品无关
     *
     * @param order
     * @param type
     * @param user
     */
    void addLogNoGoods(StoreGoodsOrderVO order, String type, SysUser user, String operationLog);

    /**
     * 修改订单状态为原状态
     * 
     * @param orderIdList
     * @return
     */
    int updateStatusOfOld(List<String> orderIdList);
    /**
     * 修改原状态
     *
     * @param orderIdList
     * @return
     */
    int updateOldOfStatus(List<String> orderIdList);

    /**
     * 修改订单状态为原状态
     *
     * @param orderIdList
     * @return
     */
    int updateStoreGoodsOrderPrintByIds(List<String> orderIdList);

    /**
     * @param orderIdList
     * @return
     */
    int updateStoreGoodsOrderOdoPrintByIds(List<String> orderIdList);

    /**
     * 检测客户今日订单
     * @param checkTodayOrder
     * @return
     */
    List<Long> checkTodayOrder(StoreGoodsCheckTodayOrder checkTodayOrder);

    /**
     * 查询未支付的订单列表
     * @return
     */
    List<StoreGoodsOrderVO> queryUnpayOrderList();
}
