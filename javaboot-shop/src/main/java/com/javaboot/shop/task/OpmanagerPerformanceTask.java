package com.javaboot.shop.task;

import com.javaboot.common.enums.OrderStatus;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.AsOrderStock;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.domain.StoreOpmanagerPerformanceDay;
import com.javaboot.shop.dto.AsOrderStockDTO;
import com.javaboot.shop.dto.StoreGoodsOrderQueryDTO;
import com.javaboot.shop.dto.StoreGoodsOrderSortingQueryReq;
import com.javaboot.shop.service.*;
import com.javaboot.shop.vo.StoreGoodsOrderSortingItemVO;
import com.javaboot.shop.vo.StoreGoodsOrderVO;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务员绩效定时任务
 */
@Component("opmanagerPerformanceTask")
public class OpmanagerPerformanceTask
{
    private static final Logger logger = LoggerFactory.getLogger(OpmanagerPerformanceTask.class);

    @Autowired
    private IStoreMemberService storeMemberService;

    @Autowired
    private IStoreGoodsOrderService storeGoodsOrderService;

    @Autowired
    private IStoreOpmanagerPerformanceDayService opmanagerPerformanceDayService;

    @Autowired
    private IAsOrderStockService asOrderStockService;

    public void exeTask() {
        exeTask(getYestoday());
    }

    private String getYestoday(){
        //2、遍历业务员，以及下面的客户列表，查询客户前一天的订单列表
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -1); //前一天
        String yestoday = sdf.format(ca.getTime());
        return yestoday;
    }

    /**
     * 每天定时任务8点开始跑，查询所有业务员信息，再查询业务员下面所有商户前一天的订单。统计订单下单总额、成本、毛利
     */
    public void exeTask(String excDate) {
        logger.info("opmanagerPerformancTask exeTask start....");
        //1、查询所有业务员信息，以及业务员对应的客户信息列表
        List<StoreMember> storeMemberList = storeMemberService.queryOpmanagerList();
        if(CollectionUtils.isNotEmpty(storeMemberList)){
            //将客户按照业务员分组
            Map<String,List<String>> opmanagerId2MemberList = new HashMap<>();
            for (StoreMember member : storeMemberList){
                List<String> memberIdList = opmanagerId2MemberList.get(member.getOpmanagerId());
                if(memberIdList == null){
                    memberIdList = new ArrayList<>();
                    opmanagerId2MemberList.put(member.getOpmanagerId(),memberIdList);
                }
                memberIdList.add(member.getId() + "");
            }

            //2、遍历业务员，以及下面的客户列表，查询客户前一天的订单列表
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
//            ca.setTime(new Date()); //设置时间为当前时间
//            ca.add(Calendar.DATE, -1); //前一天
            String yestoday = excDate;
            if(StringUtils.isEmpty(yestoday)){
                yestoday = getYestoday();
            }
            logger.info("opmanagerPerformancTask exeTask yestoday...." + yestoday);
            for (String opmanagerId:opmanagerId2MemberList.keySet()){
                List<String> memberIds = opmanagerId2MemberList.get(opmanagerId);
                List<StoreGoodsOrderVO> storeGoodsOrderVOList = null;
                if(CollectionUtils.isNotEmpty(memberIds)){
                    //查询前一天所有客户的订单列表数据
                    StoreGoodsOrderQueryDTO goodsOrderQueryDTO = new StoreGoodsOrderQueryDTO();
                    goodsOrderQueryDTO.setMerchantIdList(memberIds);
                    List<String> statusList = new ArrayList<>();
                    statusList.add(OrderStatus.SENDED.getCode());
                    statusList.add(OrderStatus.FINISH.getCode());
                    goodsOrderQueryDTO.setStatusList(statusList);
                    goodsOrderQueryDTO.setCreateBeginDate(yestoday + " 00:00:00");
                    goodsOrderQueryDTO.setCreateEndDate(yestoday + " 23:59:59");
                    storeGoodsOrderVOList = storeGoodsOrderService.selectStoreGoodsOrderList(goodsOrderQueryDTO);
                }

                StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay = new StoreOpmanagerPerformanceDay();
                storeOpmanagerPerformanceDay.setOpmanagerId(opmanagerId);
                storeOpmanagerPerformanceDay.setPerformanceDay(yestoday);

                //先判断是否已有数据，有则先删除
                List<StoreOpmanagerPerformanceDay> existList = opmanagerPerformanceDayService.selectStoreOpmanagerPerformanceDayList(storeOpmanagerPerformanceDay);
                if(CollectionUtils.isNotEmpty(existList)){
                    for (StoreOpmanagerPerformanceDay del : existList){
                        StoreOpmanagerPerformanceDay update = new StoreOpmanagerPerformanceDay();
                        update.setId(del.getId());
                        update.setStatus("0");
                        opmanagerPerformanceDayService.updateStoreOpmanagerPerformanceDay(update);
                    }
                }

                if(CollectionUtils.isNotEmpty(memberIds)){
                    storeOpmanagerPerformanceDay.setComment(memberIds.toString());
                }
                //遍历商户订单列表，统计订单总金额
                //3、统计业务员对应客户列表的数据
                Double total = 0d;
                Double costPrice = 0d;
                if(CollectionUtils.isNotEmpty(storeGoodsOrderVOList)){
                    Map<String, AsOrderStockDTO> spuNoDeptId2Cost = new HashMap<>();
                    for (StoreGoodsOrderVO order:storeGoodsOrderVOList){
                        if(order.getSortingPrice() != null){
                            total = add(total,order.getSortingPrice());
                        }

                        //查询订单里面是的商品列表,获取到spu列表再查询对应的成本值
                        StoreGoodsOrderSortingQueryReq goodsReq = new StoreGoodsOrderSortingQueryReq();
                        goodsReq.setOrderId(order.getOrderId());
                        goodsReq.setDeptId(order.getDeptId());
                        List<StoreGoodsOrderSortingItemVO> goodsList = storeGoodsOrderService.selectStoreGoodsOrderByIdForSorting(goodsReq);
                        if(CollectionUtils.isNotEmpty(goodsList)){
                            //筛选spuNo
                            List<String> spuNos = new ArrayList<>();
                            for (StoreGoodsOrderSortingItemVO vo : goodsList) {
                                String key = vo.getSpuNo() + "_" + order.getDeptId();
                                if(spuNoDeptId2Cost.get(key) == null){
                                    if(!spuNos.contains(vo.getSpuNo())){
                                        spuNos.add(vo.getSpuNo());
                                    }
                                }
                            }
                            //查询spu对应的成本单价
                            if(CollectionUtils.isNotEmpty(spuNos)){
                                AsOrderStock asOrderStock = new AsOrderStock();
                                asOrderStock.setDeptId(order.getDeptId());
                                asOrderStock.setSpuNos(spuNos);
                                asOrderStock.setStatDate(DateUtils.parseDate(yestoday));
                                List<AsOrderStockDTO> asOrderStocks = asOrderStockService.selectAsOrderStockList(asOrderStock);
                                if(CollectionUtils.isNotEmpty(asOrderStocks)){
                                    //缓存商品成本信息
                                    for (AsOrderStockDTO dto : asOrderStocks){
                                        String key = dto.getSpuNo() + "_" + order.getDeptId();
                                        spuNoDeptId2Cost.put(key,dto);
                                    }
                                }
                            }
                            //计算订单的所有商品成本价格
                            for (StoreGoodsOrderSortingItemVO vo : goodsList) {
                                String key = vo.getSpuNo() + "_" + order.getDeptId();
                                if(spuNoDeptId2Cost.get(key) != null){
                                    AsOrderStockDTO dto = spuNoDeptId2Cost.get(key);
                                    if(dto.getCostPrice() != null && vo.getSortingWeight() != null){
                                        costPrice = add(costPrice,mul(dto.getCostPrice(),vo.getSortingWeight()));
                                    }
                                }
                            }
                        }
                    }
                }

                storeOpmanagerPerformanceDay.setTotalPrice(new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                storeOpmanagerPerformanceDay.setCostPrice(new BigDecimal(costPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                storeOpmanagerPerformanceDay.setProfitPrice(new BigDecimal(sub(total,costPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                opmanagerPerformanceDayService.insertStoreOpmanagerPerformanceDay(storeOpmanagerPerformanceDay);
            }
        }
        logger.info("opmanagerPerformancTask exeTask end....");
    }


    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static Double add(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.add(b2).doubleValue();
    }

    public static double sub(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.subtract(b2).doubleValue();
    }

    public static Double mul(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }

}
