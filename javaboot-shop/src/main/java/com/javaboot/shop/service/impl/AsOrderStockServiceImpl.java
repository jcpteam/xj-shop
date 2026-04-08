package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.common.enums.StockCategory;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.AsGoodsQuotation;
import com.javaboot.shop.domain.AsOrderStock;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.domain.StoreWarehouseInventoryItem;
import com.javaboot.shop.dto.AsOrderStockDTO;
import com.javaboot.shop.dto.StoreStatDTO;
import com.javaboot.shop.dto.StoreWarehouseInventoryDTO;
import com.javaboot.shop.mapper.AsOrderStockMapper;
import com.javaboot.shop.mapper.StoreMemberMapper;
import com.javaboot.shop.mapper.StoreStatDetailMapper;
import com.javaboot.shop.mapper.StoreWarehouseInventoryItemMapper;
import com.javaboot.shop.service.IAsOrderStockService;
import com.javaboot.shop.task.StockAndOrderStatTask;
import com.javaboot.system.service.ISysDeptService;
import com.javaboot.system.vo.SysDeptSelectVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单统计Service业务层处理
 * 
 * @author javaboot
 * @date 2021-11-20
 */
@Service
public class AsOrderStockServiceImpl extends ServiceImpl<AsOrderStockMapper, AsOrderStock> implements IAsOrderStockService {
    private static final Logger logger = LoggerFactory.getLogger(StoreSaleSettingServiceImpl.class);

    @Autowired
    private AsOrderStockMapper asOrderStockMapper;

    @Autowired
    private StoreMemberMapper storeMemberMapper;

    @Autowired
    private StoreWarehouseInventoryItemMapper storeWarehouseInventoryItemMapper;

    @Autowired
    private StoreStatDetailMapper storeStatDetailMapper;

    @Autowired
    private ISysDeptService deptService;

    @Override
    public List<AsOrderStockDTO> selectAsOrderStockList(AsOrderStock asOrderStock)
    {
        List<AsOrderStockDTO> list = asOrderStockMapper.selectAsOrderStockList(asOrderStock);

        // 填充区域id
        if(CollectionUtils.isNotEmpty(list)) {

            // 获取所有的部门
            List<SysDeptSelectVO> deptList = deptService.selectDeptListById(null);
            Map<String, SysDeptSelectVO> deptMap = deptList.stream().collect(Collectors.toMap(key->key.getValue(), obj->obj));

            // 获取所有商品
            List<StoreWarehouseInventoryItem>  spuList = storeWarehouseInventoryItemMapper.selectAllInventorySpus(null);
            Map<String, StoreWarehouseInventoryItem> spuMap = spuList.stream().collect(Collectors.toMap(key->key.getSpuNo(), obj->obj));

            list.forEach(obj->{
                obj.setDeptName(deptMap.get(obj.getDeptId()).getName());
                obj.setSpuName(spuMap.get(obj.getSpuNo()).getSpuName());
                if(obj.getOrderTotalPrice() == null) {
                    obj.setOrderTotalPrice(0.0);
                }
                if(obj.getOrderMainTotalQuantiy() == null) {
                    obj.setOrderMainTotalQuantiy(0.0);
                }

                if(obj.getCostPrice() == null) {
                    obj.setCostPrice(0.0);
                }
                // 当天商品毛利 = 销售总价格 - 每日成本 * 销售数量
                obj.setGrossProfit(new BigDecimal(obj.getOrderTotalPrice() - (obj.getCostPrice() * obj.getOrderMainTotalQuantiy()) ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            });
        }
        return list;
    }

    /**
     * 新增订单统计
     *
     * @param asOrderStock 订单统计
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAsOrderStock(AsOrderStockDTO asOrderStock) {
        // 获取所有商品
        List<StoreWarehouseInventoryItem>  spuList = storeWarehouseInventoryItemMapper.selectAllInventorySpus(null);
        Map<String, StoreWarehouseInventoryItem> spuMap = spuList.stream().collect(Collectors.toMap(key->key.getSpuNo(), obj->obj));

        Date start = new Date();
        if(StringUtils.isNotBlank(asOrderStock.getQryDate())) {
            start = DateUtils.parseDate(asOrderStock.getQryDate());
        }
        // 计算当期时间前一天的月开始时间与当期时间的相差天数
        //Date yesterday = DateUtils.addDays(start, -1);
        //String monthFirst = DateUtils.getMonthFirstDay(yesterday);
        //Long interval = DateUtils.getDatePoorLong(DateUtils.getNowDate(), DateUtils.parseDate(monthFirst));
        Date monthFirst = DateUtils.addDays(start, -33);
        Long interval = 33L;

        // 获取所有的部门
        List<SysDeptSelectVO> deptList = deptService.selectDeptListById(null);
        for(int i = 0; i < interval; i++) {

            String startDay = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(monthFirst, i));

            for (SysDeptSelectVO o : deptList)
            {
//                if(!"040601".equals(o.getValue()) ) {
//                    System.out.println(o.getValue());
//                    continue;
//                }
                List<AsOrderStock> asOrderStockList = new ArrayList<>();

                StoreWarehouseInventoryDTO storeWarehouseInventory = new StoreWarehouseInventoryDTO();
                storeWarehouseInventory.setDeptId(o.getValue());

                // 盘库结存开始时间和结束时间
                String inventoryDay = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(DateUtils.parseDate(startDay), -1));
                storeWarehouseInventory.setStartTime(inventoryDay.concat(" 00:00:00"));
                storeWarehouseInventory.setEndTime(inventoryDay.concat(" 23:59:59"));
                // 获取上一天选中仓库的盘库单主单位和副单位的数量
                List<StoreWarehouseInventoryItem> warehouseInventoryList = storeWarehouseInventoryItemMapper.selectWarehouseInventory(
                        storeWarehouseInventory);

                // 盘库结存开始时间和结束时间
                String inventoryToday = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.parseDate(startDay));
                storeWarehouseInventory.setStartTime(inventoryToday.concat(" 00:00:00"));
                storeWarehouseInventory.setEndTime(inventoryToday.concat(" 23:59:59"));
                // 获取统计当天选中仓库的盘库单主单位和副单位的数量，用于计算损益数量和损益金额
                List<StoreWarehouseInventoryItem> warehouseInventoryTodayList = storeWarehouseInventoryItemMapper.selectWarehouseInventory(
                    storeWarehouseInventory);

                // 库存开始时间和结束时间
                storeWarehouseInventory.setStartTime(startDay.concat(" 00:00:00"));
                storeWarehouseInventory.setEndTime(startDay.concat(" 23:59:59"));
                // 排除调拨出库的入库单
                storeWarehouseInventory.setCategory(StockCategory.ADJUST_OUT_ORDER.getCode());
                // 获取当期月1号开始时间和当天日期，选中仓库的入库单主单位和副单位的数量
                List<StoreWarehouseInventoryItem> warehouseStockInventoryList = storeWarehouseInventoryItemMapper.selectWarehouseStockInventory(
                    storeWarehouseInventory);

                // 获取统计当天选中仓库的盘库单主单位和副单位的数量，用于计算损益数量和损益金额
                List<StoreWarehouseInventoryItem> warehouseAdjustTodayList = storeWarehouseInventoryItemMapper.selectWarehouseStockAdjust(
                    storeWarehouseInventory);

                // 订单开始和结束时间
                storeWarehouseInventory.setStartTime(startDay.concat(" 09:00:00"));
                storeWarehouseInventory.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(monthFirst, (i+1))).concat(" 09:00:00"));
                List<StoreWarehouseInventoryItem> orderInventoryList = storeWarehouseInventoryItemMapper.selectSpuOrderInventory(
                    storeWarehouseInventory);

                if (CollectionUtils.isEmpty(warehouseStockInventoryList)
                    && CollectionUtils.isEmpty(orderInventoryList)
                    && CollectionUtils.isEmpty(warehouseInventoryList))
                {
                    continue;
                }

                Set<String> spuNoList = new HashSet<>();
                spuNoList.addAll(warehouseStockInventoryList.stream().map(StoreWarehouseInventoryItem::getSpuNo).collect(Collectors.toList()));
                spuNoList.addAll(orderInventoryList.stream().map(StoreWarehouseInventoryItem::getSpuNo).collect(Collectors.toList()));
                spuNoList.addAll(warehouseInventoryList.stream().map(StoreWarehouseInventoryItem::getSpuNo).collect(Collectors.toList()));

                spuNoList.forEach(spuNo -> {
                    AsOrderStock item = new AsOrderStock();
                    item.setSpuNo(spuNo);
                    item.setDeptId(o.getValue());
                    // 计算当前部门的订单销售总公斤数和总只数
                    orderInventoryList.stream().filter(obj -> obj.getSpuNo().equals(spuNo)).forEach(order -> {
                        item.setOrderMainTotalQuantiy(order.getSalesQuantity());
                        item.setOrderSubTotalQuantiy(order.getSalesLonelyQuantity());
                        item.setOrderTotalPrice(order.getPrice());
                        // 无税订单金额 = 订单金额/（1+SPU税率）
                        double taxRate = spuMap.get(spuNo).getTaxRate() != null ? spuMap.get(spuNo).getTaxRate() : 0.0;
                        item.setOrderTotalPrice(new BigDecimal(item.getOrderTotalPrice() / (1 + taxRate  * 0.01)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        if(order.getSalesQuantity() > 0) {
                            item.setOrderAvgPrice(new BigDecimal(order.getPrice() / item.getOrderMainTotalQuantiy()).setScale(2,
                                BigDecimal.ROUND_HALF_UP).doubleValue());
                        } else {
                            item.setOrderAvgPrice(0.0);
                        }

                    });

                    // 计算当前部门的入库单销售总公斤数和总只数
                    warehouseStockInventoryList.stream().filter(obj -> obj.getSpuNo().equals(spuNo)).forEach(stock -> {
                        item.setStockMainTotalQuanity(stock.getStockQuantity());
                        item.setStockSubTotalQuanity(stock.getStockLonelyQuantity());
                        item.setStockTotalPrice(stock.getPrice());
                        if(stock.getStockQuantity() > 0) {
                            item.setStockAvgPrice(new BigDecimal(stock.getPrice() / stock.getStockQuantity()).setScale(2,
                                BigDecimal.ROUND_HALF_UP).doubleValue());
                        } else {
                            item.setStockAvgPrice(0.0);
                        }

                    });

                    // 计算当前部门的盘库商品总公斤数和总只数(结存数量)
                    warehouseInventoryList.stream().filter(obj -> obj.getSpuNo().equals(spuNo)).forEach(stock -> {
                        item.setInventoryMainTotalQuanity(stock.getInventoryQuantity());
                        item.setInventorySubTotalQuanity(stock.getInventoryLonelyQuantity());
                    });

                    // 损益数量,当天的盘库商品总公斤数和总只数
                    if(CollectionUtils.isNotEmpty(warehouseInventoryTodayList)) {
                        warehouseInventoryTodayList.stream().filter(obj -> obj.getSpuNo().equals(spuNo)).forEach(stock -> {
                            item.setInventoryTodayMainTotalQuanity(stock.getInventoryQuantity());
                            item.setInventoryTodaySubTotalQuanity(stock.getInventoryLonelyQuantity());
                            // 损益数量
                            item.setInventoryResult(stock.getInventoryResult());
                        });
                    }

                    // 计算商品的每日成本均价
                    // 获取上一天的结存价格，如果不存在就以当天的入库单价作为当日成本价格
                    double costPrice = item.getStockAvgPrice() != null  ? item.getStockAvgPrice() : 0.0;

                    // 计算结转金额
                    double yesterdayInventoryTodayPrice = 0.0;

                    // 没有值就用最近的数据做每日成本价,默认循环30天数据，取最近的一条不为0的成本价格数据
                    for(int t = 1; t <= interval; t++) {
                        String yesterday = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(DateUtils.parseDate(startDay), -t));
                        // 库存开始时间和结束时间
                        AsOrderStock qry = new AsOrderStock();
                        qry.setStatDate(DateUtils.parseDate(yesterday));
                        qry.setDeptId(o.getValue());
                        qry.setSpuNo(spuNo);
                        List<AsOrderStockDTO> yesterDayAsStock = asOrderStockMapper.selectAsOrderStockList(qry);
                        if(t == 1 && CollectionUtils.isNotEmpty(yesterDayAsStock) && yesterDayAsStock.get(0).getInventoryTodayPrice() != null) {
                            yesterdayInventoryTodayPrice = yesterDayAsStock.get(0).getInventoryTodayPrice();
                        }
                        if(CollectionUtils.isNotEmpty(yesterDayAsStock) && yesterDayAsStock.get(0).getCostPrice() != null && yesterDayAsStock.get(0).getCostPrice() > 0) {
                            costPrice = yesterDayAsStock.get(0).getCostPrice();
                            break;
                        }
                    }



                    // 今日剩余数量 = 昨天结存数量 + 今日入库数量 - 今日销售数量
                    double inventoryNum = ((item.getInventoryMainTotalQuanity() != null && item.getInventoryMainTotalQuanity() > 0) ? item.getInventoryMainTotalQuanity() : 0.0);
                    double stockNum = (item.getStockMainTotalQuanity() != null ? item.getStockMainTotalQuanity() : 0.0);
                    double leftQuantity = inventoryNum + stockNum ;

                    // 今日库存金额 = 结存数量总金额 + 今天入库金额
                    // double leftPrice = costPrice * inventoryNum + (item.getStockTotalPrice() != null ? item.getStockTotalPrice() : 0.0 );
                    // 结存金额修改为取上一次的结存金额
                    // 03-23修改：结存数量为负数时候，计算成本用的结存金额改成0
                    double yesterdayInventoryTodayPriceTemp = 0;
                    if(item.getInventoryMainTotalQuanity() != null && item.getInventoryMainTotalQuanity() >= 0) {
                        yesterdayInventoryTodayPriceTemp = yesterdayInventoryTodayPrice;
                    }
                    double leftPrice = yesterdayInventoryTodayPriceTemp + (item.getStockTotalPrice() != null ? item.getStockTotalPrice() : 0.0 );
                    item.setCostPrice(costPrice);
                    if(leftQuantity > 0) {
                        item.setCostPrice(new BigDecimal(leftPrice / leftQuantity).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }

                    // 计算损益金额
                    if(item.getInventoryResult() != null)
                        item.setInventoryResultPrice(new BigDecimal(item.getCostPrice() * item.getInventoryResult()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    item.setStatDate(DateUtils.parseDate(startDay));
                    item.setDeliveryDate(DateUtils.addDays(item.getStatDate(), 1));

                    // 计算盘存金额
                    //盘存金额 = 结转金额 + 入库金额 -（每日订单数量*每日成本价）- (损益数量*每日成本价) - 调拨出库总金额
                    // 这里损益数量数据库存的负数，应该变成加上(损益数量*每日成本价)
                    AsOrderStock temp = new AsOrderStock();
                    temp.setInventoryTodayPrice(0.0);
                    warehouseAdjustTodayList.stream().filter(obj -> obj.getSpuNo().equals(spuNo)).forEach(stock -> {
                        temp.setInventoryTodayPrice(stock.getPrice());
                    });
                    double inventoryTodayPrice = yesterdayInventoryTodayPrice
                        + (item.getStockTotalPrice() == null ? 0.0 : item.getStockTotalPrice())
                        - (item.getOrderMainTotalQuantiy() == null ? 0.0 : item.getOrderMainTotalQuantiy()) * item.getCostPrice()
                        + (item.getInventoryResultPrice() == null ? 0.0 : item.getInventoryResultPrice())
                        - temp.getInventoryTodayPrice() ;
                    item.setInventoryTodayPrice(inventoryTodayPrice);

                    asOrderStockList.add(item);
                });

                if (CollectionUtils.isNotEmpty(asOrderStockList))
                {
                    // 清空昨天数据的数据
                    QueryWrapper<AsOrderStock> delWrapper = new QueryWrapper<>();
                    delWrapper.eq("dept_id", o.getValue());
                    delWrapper.eq("stat_date", startDay);
                    int count = this.asOrderStockMapper.delete(delWrapper);
                    logger.debug("删除的统计数据{}", count);

                    this.saveBatch(asOrderStockList);
                }
            }
        }

        return 1;
    }

    /**
     * 经营统计报表
     * @param asOrderStock
     * @return
     */
    @Override
    public List<AsOrderStockDTO> deptDaySaleTotal(AsOrderStockDTO asOrderStock)
    {
        List<AsOrderStockDTO> list = asOrderStockMapper.selectDeptDaySale(asOrderStock);

        // 获取所有的部门
        List<SysDeptSelectVO> deptList = deptService.selectDeptListById(null);
        Map<String, SysDeptSelectVO> deptMap = deptList.stream().collect(Collectors.toMap(key->key.getValue(), obj->obj));

        // 计算分拣人数和人均分拣数
        StoreStatDTO storeStatDTO = new StoreStatDTO();
        storeStatDTO.setDeptId(asOrderStock.getDeptId());
        List<StoreMember> memberList = storeStatDetailMapper.selectSortMebersList(storeStatDTO);

        Map<String, StoreMember> sortNumMap = memberList.stream().collect(Collectors.toMap(key->key.getCustomerArea(), obj->obj));

        list.forEach(obj -> {
            obj.setDeptName(deptMap.get(obj.getDeptId()).getName());
            obj.setSortUserNum(0.0);
            obj.setSortAvgNum(0.0);
            obj.setGrossProfit(obj.getOrderTotalPrice() - obj.getStockTotalPrice());
            StoreMember sortMember = sortNumMap.get(obj.getDeptId());
            if(sortMember != null) {
                obj.setSortUserNum(sortMember.getTotalAmount());
                if(obj.getOrderMainTotalQuantiy() > 0) {
                    obj.setSortAvgNum(new BigDecimal(obj.getOrderMainTotalQuantiy() / obj.getSortUserNum()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            }
        });

        return list;
    }

    /**
     * 报表2-销售计划
     * @param asOrderStock
     * @return
     */
    @Override
    public List<AsOrderStockDTO> deptMonthSaleTotal(AsOrderStockDTO asOrderStock)
    {
        String endTime = asOrderStock.getQryDate();
        // 计算请求时间对应的月的第一天
        if(StringUtils.isEmpty(asOrderStock.getQryDate())) {
            endTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(new Date(), -1));
        }
        String monthFirst = DateUtils.getMonthFirstDay(DateUtils.parseDate(endTime));
        if(StringUtils.isBlank(asOrderStock.getStartDate())) {
            asOrderStock.setStartDate(monthFirst);
        }
        if(StringUtils.isBlank(asOrderStock.getEndDate())) {
            asOrderStock.setEndDate(endTime);
        }

        List<AsOrderStockDTO> monthList = asOrderStockMapper.selectDeptMonthSale(asOrderStock);
        List<AsOrderStockDTO> todayList = asOrderStockMapper.selectDeptDaySale(asOrderStock);

        // 获取所有的部门
        List<SysDeptSelectVO> deptList = deptService.selectDeptListById(null);
        Map<String, SysDeptSelectVO> deptMap = deptList.stream().collect(Collectors.toMap(key->key.getValue(), obj->obj));

        monthList.forEach(monthObj->{
            monthObj.setDeptName(deptMap.get(monthObj.getDeptId()).getName());

            monthObj.setOrderMainTotalQuantiy(0.0);
            monthObj.setOrderSubTotalQuantiy(0.0);
            monthObj.setOrderTotalPrice(0.0);
            monthObj.setStockMainTotalQuanity(0.0);
            monthObj.setStockSubTotalQuanity(0.0);
            monthObj.setStockTotalPrice(0.0);
            monthObj.setGrossProfit(0.0);
            monthObj.setRemainingQuantity(0.0);
            monthObj.setMonthGrossProfit(new BigDecimal(monthObj.getMonthOrderTotalPrice() - monthObj.getMonthStockTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            monthObj.setMonthRemainingQuantity(new BigDecimal(monthObj.getMonthStockMainTotalQuanity() - monthObj.getMonthOrderMainTotalQuantiy()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            // 填充今日数量
            todayList.forEach(todayObj->{
                if(monthObj.getDeptId().equals(todayObj.getDeptId())) {
                    monthObj.setOrderMainTotalQuantiy(todayObj.getOrderMainTotalQuantiy());
                    monthObj.setOrderSubTotalQuantiy(todayObj.getOrderSubTotalQuantiy());
                    monthObj.setOrderTotalPrice(todayObj.getOrderTotalPrice());
                    monthObj.setStockMainTotalQuanity(todayObj.getStockMainTotalQuanity());
                    monthObj.setStockSubTotalQuanity(todayObj.getStockSubTotalQuanity());
                    monthObj.setStockTotalPrice(todayObj.getStockTotalPrice());
                    monthObj.setGrossProfit(new BigDecimal(todayObj.getOrderTotalPrice() - todayObj.getStockTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    monthObj.setRemainingQuantity(new BigDecimal(monthObj.getStockMainTotalQuanity() - monthObj.getOrderMainTotalQuantiy()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            });
        });
        return monthList;
    }

    /**
     * 每日销售计划
     * @param asOrderStock
     * @return
     */
    @Override
    public List<Map<String, String>> spuDaySaleTotal(AsOrderStockDTO asOrderStock)
    {
        List<StoreWarehouseInventoryItem>  spuList = storeWarehouseInventoryItemMapper.selectAllInventorySpus(null);
        Map<String, StoreWarehouseInventoryItem> spuMap = spuList.stream().collect(Collectors.toMap(key->key.getSpuNo(), obj->obj));
        List<Map<String, String>> mapList = new ArrayList<>();
        List<AsOrderStockDTO> list = asOrderStockMapper.selectSpuDaySale(asOrderStock);
        List<String> spuNoList = list.stream().map(AsOrderStockDTO::getSpuNo).distinct().collect(Collectors.toList());
        spuNoList.forEach(spu->{
            Map<String, String> map = new HashMap<>();
            map.put("spuNo", spuMap.get(spu).getSpuName());
            list.forEach(obj->{
                if(obj.getSpuNo().equals(spu)) {
                    map.put(obj.getDeptId(), obj.getOrderMainTotalQuantiy()+"");
                }

            });
            mapList.add(map);
        });
        return mapList;
    }

    /**
     * 物料销售情况
     * @param asOrderStock
     * @return
     */
    @Override
    public List<AsGoodsQuotation> calcAsGoodsQuotation(AsOrderStockDTO asOrderStock)
    {
        List<AsGoodsQuotation>  goodsQuotationList = asOrderStockMapper.selectAsGoodsQuotation(asOrderStock);

        // 获取所有商品
        List<StoreWarehouseInventoryItem>  spuList = storeWarehouseInventoryItemMapper.selectAllInventorySpus(null);
        Map<String, StoreWarehouseInventoryItem> spuMap = spuList.stream().collect(Collectors.toMap(key->key.getSpuNo(), obj->obj));

        // 获取所有的部门
        List<SysDeptSelectVO> deptList = deptService.selectDeptListById(null);
        Map<String, SysDeptSelectVO> deptMap = deptList.stream().collect(Collectors.toMap(key->key.getValue(), obj->obj));

        goodsQuotationList.forEach(goodsQuotation->{
            goodsQuotation.setSpuName(spuMap.get(goodsQuotation.getSpuNo()).getSpuName());
            goodsQuotation.setDeptName(deptMap.get(goodsQuotation.getDeptId()).getName());
        });
        return goodsQuotationList;
    }

    /**
     * 区域销售汇总
     * @param asOrderStock
     * @return
     */
    public List<AsOrderStockDTO> deptSaleCalc(AsOrderStockDTO asOrderStock) {
        initMonthStartAndEnd(asOrderStock);
        List<AsOrderStockDTO> monthList = asOrderStockMapper.selectDeptMonthGoodsSale(asOrderStock);

        // 部门列表
        List<SysDeptSelectVO> deptList = deptService.selectDeptListById(null);
        Map<String, SysDeptSelectVO> deptMap = deptList.stream().collect(Collectors.toMap(key->key.getValue(), obj->obj));

        // 获取所有商品
        List<StoreWarehouseInventoryItem>  spuList = storeWarehouseInventoryItemMapper.selectAllInventorySpus(null);
        Map<String, StoreWarehouseInventoryItem> spuMap = spuList.stream().collect(Collectors.toMap(key->key.getSpuNo(), obj->obj));


        monthList.forEach(monthObj->{
            monthObj.setSpuName(spuMap.get(monthObj.getSpuNo()).getSpuName());
            monthObj.setDeptName(deptMap.get(monthObj.getDeptId()).getName());

            monthObj.setOrderAvgPrice(0.0);
            if(monthObj.getMonthOrderMainTotalQuantiy() > 0) {
                monthObj.setOrderAvgPrice(new BigDecimal(monthObj.getMonthOrderTotalPrice() / monthObj.getMonthOrderMainTotalQuantiy()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }

            monthObj.setMonthGrossProfit(new BigDecimal(monthObj.getMonthOrderTotalPrice() - monthObj.getMonthStockTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            monthObj.setMonthRemainingQuantity(new BigDecimal(monthObj.getMonthStockMainTotalQuanity() - monthObj.getMonthOrderMainTotalQuantiy()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        });
        return  monthList;
    }

    /**
     * 客户销售统计
     * @param asOrderStock
     * @return
     */
    @Override
    public List<AsOrderStockDTO> mechantSaleCalc(AsOrderStockDTO asOrderStock)
    {
        if(StringUtils.isEmpty(asOrderStock.getMerchantId())) {
            return new ArrayList<>();
        }

        Date start = new Date();
        if(StringUtils.isNotBlank(asOrderStock.getQryDate())) {
            start = DateUtils.parseDate(asOrderStock.getQryDate());
        }
        if(StringUtils.isNotBlank(asOrderStock.getStartDate())) {
            start = DateUtils.parseDate(asOrderStock.getStartDate());
        }
        Date end = DateUtils.addDays(start, 1);
        if(StringUtils.isNotBlank(asOrderStock.getEndDate())) {
            end = DateUtils.parseDate(asOrderStock.getEndDate());
        }
        // 商户每天下单金额
        asOrderStock.setStartDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, start).concat(" 09:00:00"));
        asOrderStock.setEndDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, end).concat(" 09:00:00"));
        List<AsOrderStockDTO> dayList = asOrderStockMapper.selectMechantOrderStat(asOrderStock);

        // 商户每月下单金额
        asOrderStock.setQryDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM, start));
        initMonthStartAndEnd(asOrderStock);
        List<AsOrderStockDTO> monthList = asOrderStockMapper.selectMechantOrderStat(asOrderStock);

        // 商户累计下单金额
        asOrderStock.setStartDate(null);
        asOrderStock.setEndDate(null);
        List<AsOrderStockDTO> allList = asOrderStockMapper.selectMechantOrderStat(asOrderStock);

        // 获取所有商品
        List<StoreWarehouseInventoryItem>  spuList = storeWarehouseInventoryItemMapper.selectAllInventorySpus(null);
        Map<String, StoreWarehouseInventoryItem> spuMap = spuList.stream().collect(Collectors.toMap(key->key.getSpuNo(), obj->obj));


        // 将日，月，所有金额的商品合并
        Set<String> spuNoList = new HashSet<>();
        dayList.forEach(o->spuNoList.add(o.getSpuNo()));
        monthList.forEach(o->spuNoList.add(o.getSpuNo()));
        allList.forEach(o->spuNoList.add(o.getSpuNo()));

        StoreMember member = storeMemberMapper.selectStoreMemberById(Long.parseLong(asOrderStock.getMerchantId()));

        List<AsOrderStockDTO> rsltList = new ArrayList<>();
        spuNoList.forEach(spu->{
            AsOrderStockDTO dto = new AsOrderStockDTO();
            dto.setSpuNo(spu);
            dto.setSpuName(spuMap.get(spu).getSpuName());
            dto.setMerchantName(member != null ? member.getNickname() : "");
            dto.setOrderTotalPrice(0.0);
            dto.setMonthOrderTotalPrice(0.0);
            dto.setAllOrderTotalPrice(0.0);
            dayList.forEach(o->{
                if (o.getSpuNo().equals(spu)) {
                    dto.setOrderTotalPrice(o.getOrderTotalPrice());
                }
            });
            monthList.forEach(o->{
                if (o.getSpuNo().equals(spu)) {
                    dto.setMonthOrderTotalPrice(o.getOrderTotalPrice());
                }
            });
            allList.forEach(o->{
                if (o.getSpuNo().equals(spu)) {
                    dto.setAllOrderTotalPrice(o.getOrderTotalPrice());
                }
            });
            rsltList.add(dto);
        });

        return rsltList;
    }

    /**
     * 已支付、未支付金额统计
     * @param asOrderStock
     * @return
     */
    public AsGoodsQuotation orderPayStat(AsOrderStockDTO asOrderStock){
        initMonthStartAndEnd(asOrderStock);
        AsGoodsQuotation quotation = asOrderStockMapper.selectOrderPayStat(asOrderStock);
        return  quotation;
    }

    /**
     * 订单状态统计
     * @param asOrderStock
     * @return
     */
    public AsGoodsQuotation orderStatusStat(AsOrderStockDTO asOrderStock){
        initMonthStartAndEnd(asOrderStock);
        AsGoodsQuotation quotation = asOrderStockMapper.selectOrderStatusStat(asOrderStock);
        return  quotation;
    }

    private void initMonthStartAndEnd(AsOrderStockDTO asOrderStock) {
        if(StringUtils.isNotBlank(asOrderStock.getQryDate())) {
            String startDate = asOrderStock.getQryDate().concat("-01");
            Date firstDay = DateUtils.parseDate(startDate);
            Calendar c = Calendar.getInstance();
            c.setTime(firstDay);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String endDate = format.format(c.getTime());
            asOrderStock.setStartDate(startDate);
            asOrderStock.setEndDate(endDate);
        }
    }


}
