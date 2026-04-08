package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreDeliverRoute;
import com.javaboot.shop.domain.StoreGoodsQuotation;
import com.javaboot.shop.dto.StoreGoodsCheckTodayOrder;
import com.javaboot.shop.dto.StoreGoodsOrderDTO;
import com.javaboot.shop.dto.StoreGoodsOrderQueryDTO;
import com.javaboot.shop.vo.SortingQueryMerchantInfo;
import com.javaboot.shop.vo.StoreGoodsOrderSortingCountVO;
import com.javaboot.shop.vo.StoreGoodsOrderVO;
import com.javaboot.shop.vo.StoreSortingQueryParamsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单信息主Mapper接口
 *
 * @author lqh
 * @date 2021-05-30
 */
public interface StoreGoodsOrderMapper {
    /**
     * 查询订单信息主
     *
     * @param orderId 订单信息主ID
     * @return 订单信息主
     */
    public StoreGoodsOrderVO selectStoreGoodsOrderById(Long orderId);

    /**
     * 查询订单信息主列表
     *
     * @param storeGoodsOrder 订单信息主
     * @return 订单信息主集合
     */
    public List<StoreGoodsOrderVO> selectStoreGoodsOrderList(StoreGoodsOrderQueryDTO storeGoodsOrder);

    /**
     * 查询订单信息主列表
     *
     * @param storeGoodsOrder 订单信息主
     * @return 订单信息主集合
     */
    public List<StoreGoodsOrderVO> selectStoreGoodsOrderListForApp(StoreGoodsOrderQueryDTO storeGoodsOrder);

    /**
     * 新增订单信息主
     *
     * @param storeGoodsOrder 订单信息主
     * @return 结果
     */
    public int insertStoreGoodsOrder(StoreGoodsOrderDTO storeGoodsOrder);

    /**
     * 修改订单信息主
     *
     * @param storeGoodsOrder 订单信息主
     * @return 结果
     */
    public int updateStoreGoodsOrder(StoreGoodsOrderDTO storeGoodsOrder);

    /**
     * 删除订单信息主
     *
     * @param orderId 订单信息主ID
     * @return 结果
     */
    public int deleteStoreGoodsOrderById(Long orderId);

    /**
     * 批量删除订单信息主
     *
     * @param orderIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsOrderByIds(String[] orderIds);

    /**
     * 审核订单
     *
     * @param orderIds
     * @return
     */
    int examineStoreGoodsOrderByIds(String[] orderIds);

    /**
     * 财务审核订单
     * @param orderIds
     * @return
     */
    int financialExamineStoreGoodsOrderByIds(String[] orderIds);

    /**
     * 财务审核订单
     * @param orderIds
     * @return
     */
    int financialCancelStoreGoodsOrderByIds(String[] orderIds);

    /**
     * 查询订单信息主列表
     *
     * @param storeGoodsOrder 订单信息主
     * @return 订单信息主集合
     */
    public List<StoreGoodsOrderVO> selectStoreGoodsOrderListForSorting(StoreGoodsOrderQueryDTO storeGoodsOrder);

    /**
     * 获取订单分拣的商品总数
     * @param orderIds
     * @return
     */
    List<StoreGoodsOrderSortingCountVO> getStoreGoodsOrderSortingCountByIds(String[] orderIds);

    List<SortingQueryMerchantInfo> getSortingMerchantInfo(String loginUserDeptId);
    List<StoreDeliverRoute> getSortingRouteInfo(String loginUserDeptId);
    List<StoreGoodsQuotation> getSortingQuotationInfo(String loginUserDeptId);

    /**
     * 修改订单状态为原状态
     * @param orderIdList
     * @return
     */
    int updateStatusOfOld(@Param("orderIdList") List<String> orderIdList);    /**
     * 修改原订单状态
     * @param orderIdList
     * @return
     */
    int updateOldOfStatus(@Param("orderIdList") List<String> orderIdList);

    /**
     * 删除报价单商品时，获取订单商品数量
     * @param goodsIdList
     * @return
     */
    int getOrderGoodsCount(@Param("goodsIdList") List<String> goodsIdList);

    /**
     *
     * 生成付款单状态
     * @param map
     * @return
     */
    int updateReceiptStatus(Map<String, Object> map);

    public int updateStoreGoodsOrderPrintByIds(@Param("orderIdList") List<String> orderIdList);
    public int updateStoreGoodsOrderOdoPrintByIds(@Param("orderIdList") List<String> orderIdList);

    /**
     * 检测客户进入下单订单
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
