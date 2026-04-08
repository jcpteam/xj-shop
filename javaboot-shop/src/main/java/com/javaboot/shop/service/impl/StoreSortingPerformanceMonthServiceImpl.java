package com.javaboot.shop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.service.*;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreSortingPerformanceMonthMapper;
import com.javaboot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

import static com.javaboot.common.core.domain.AjaxResult.success;

/**
 * 分拣绩效月份Service业务层处理
 *
 * @author javaboot
 * @date 2021-07-20
 */
@Service
public class StoreSortingPerformanceMonthServiceImpl implements IStoreSortingPerformanceMonthService {
    @Autowired
    private StoreSortingPerformanceMonthMapper storeSortingPerformanceMonthMapper;

    @Autowired
    private IStoreSortingPerformanceDayService storeSortingPerformanceDayService;

    @Autowired
    private IStoreSortingDailyWorkService storeSortingDailyWorkService;

    @Autowired
    private IStoreSortingDetailService storeSortingDetailService;

    @Autowired
    private IStoreWarehouseStockItemService storeWarehouseStockItemService;
    @Autowired
    private IStoreGoodsWarehouseAdjustItemService storeGoodsWarehouseAdjustItemService;

    /**
     * 查询分拣绩效月份
     *
     * @param id 分拣绩效月份ID
     * @return 分拣绩效月份
     */
    @Override
    public StoreSortingPerformanceMonth selectStoreSortingPerformanceMonthById(Long id) {
        return storeSortingPerformanceMonthMapper.selectStoreSortingPerformanceMonthById(id);
    }

    /**
     * 查询分拣绩效月份列表
     *
     * @param storeSortingPerformanceMonth 分拣绩效月份
     * @return 分拣绩效月份
     */
    @Override
    public List<StoreSortingPerformanceMonth> selectStoreSortingPerformanceMonthList(StoreSortingPerformanceMonth storeSortingPerformanceMonth) {
        return storeSortingPerformanceMonthMapper.selectStoreSortingPerformanceMonthList(storeSortingPerformanceMonth);
    }

    /**
     * 新增分拣绩效月份
     *
     * @param storeSortingPerformanceMonth 分拣绩效月份
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreSortingPerformanceMonth(StoreSortingPerformanceMonth storeSortingPerformanceMonth) {
        storeSortingPerformanceMonth.setCreateTime(DateUtils.getNowDate());
        return storeSortingPerformanceMonthMapper.insertStoreSortingPerformanceMonth(storeSortingPerformanceMonth);
    }

    /**
     * 修改分拣绩效月份
     *
     * @param storeSortingPerformanceMonth 分拣绩效月份
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreSortingPerformanceMonth(StoreSortingPerformanceMonth storeSortingPerformanceMonth) {
        return storeSortingPerformanceMonthMapper.updateStoreSortingPerformanceMonth(storeSortingPerformanceMonth);
    }

    /**
     * 删除分拣绩效月份对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreSortingPerformanceMonthByIds(String ids) {
        return storeSortingPerformanceMonthMapper.deleteStoreSortingPerformanceMonthByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除分拣绩效月份信息
     *
     * @param id 分拣绩效月份ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreSortingPerformanceMonthById(Long id) {
        return storeSortingPerformanceMonthMapper.deleteStoreSortingPerformanceMonthById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult calculatePerformanceMonth(StoreSortingPerformanceMonth storeSortingPerformanceMonth) {
        //1、先查询当前区域和月份是否已经有核算记录
        StoreSortingPerformanceMonth where = new StoreSortingPerformanceMonth();
        where.setDeptId(storeSortingPerformanceMonth.getDeptId());
        where.setSortingMonth(storeSortingPerformanceMonth.getSortingMonth());
        where.setStatus("1");
        List<StoreSortingPerformanceMonth> existList = selectStoreSortingPerformanceMonthList(where);
        if (CollectionUtils.isNotEmpty(existList)) {
            for (StoreSortingPerformanceMonth update : existList) {
                update.setStatus("0");
                updateStoreSortingPerformanceMonth(update);
            }
        }

        //2、对当前区域月份绩效进行核算
        //查询该区域月份的考勤分拣人员
        StoreSortingDailyWork where1 = new StoreSortingDailyWork();
        where1.setDeptId(storeSortingPerformanceMonth.getDeptId());
        where1.setWorkDayStartDate(storeSortingPerformanceMonth.getSortingMonth() + "-01");
        where1.setWorkDayEndDate(storeSortingPerformanceMonth.getSortingMonth() + "-31");
        where1.setStatus("1");
        List<StoreSortingDailyWork> sortingDailyWorkUserList = storeSortingDailyWorkService.selectStoreSortingDailyWorkUserList(where1);
        if (CollectionUtils.isEmpty(sortingDailyWorkUserList)) {
            return success();
        }
        //遍历分拣考勤列表，拿到考勤人员名单
        List<String> userIds = new ArrayList<>();
        //用户id对应的当月的考勤记录
        Map<String, List<StoreSortingDailyWork>> userId2WorkListMap = new HashMap<>(16);
        //每日日期对应的用户的考勤记录
        Map<String, List<StoreSortingDailyWork>> workDay2WorkListMap = new HashMap<>(16);
        for (StoreSortingDailyWork workUser : sortingDailyWorkUserList) {
            if (!userIds.contains(workUser.getSortingUserId())) {
                userIds.add(workUser.getSortingUserId());
            }

            if (userId2WorkListMap.get(workUser.getSortingUserId()) == null) {
                List<StoreSortingDailyWork> l = new ArrayList<StoreSortingDailyWork>();
                userId2WorkListMap.put(workUser.getSortingUserId(), l);
            }
            userId2WorkListMap.get(workUser.getSortingUserId()).add(workUser);

            if (workDay2WorkListMap.get(workUser.getWorkDay()) == null) {
                List<StoreSortingDailyWork> l = new ArrayList<StoreSortingDailyWork>();
                workDay2WorkListMap.put(workUser.getWorkDay(), l);
            }
            workDay2WorkListMap.get(workUser.getWorkDay()).add(workUser);
        }

        //查询考勤人员名单的分拣数据
        StoreSortingDetail where2 = new StoreSortingDetail();
        where2.setSortingUserIds(userIds);
        where2.setDeptId(storeSortingPerformanceMonth.getDeptId());
        where2.setWorkDayStartDate(storeSortingPerformanceMonth.getSortingMonth() + "-01");
        where2.setWorkDayEndDate(storeSortingPerformanceMonth.getSortingMonth() + "-31");
        where2.setStatus("1");
        List<StoreSortingDetail> sortingDetailList = storeSortingDetailService.selectStoreSortingDetailListForDeptMonth(where2);
        //每日对应的所有分拣员结果值综合
        Map<String, Double> userDay2TotalResultMap = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(sortingDetailList)) {
            for (StoreSortingDetail detail : sortingDetailList) {
                if(userDay2TotalResultMap.get(detail.getSortingDay()) == null){
                    userDay2TotalResultMap.put(detail.getSortingDay(), (detail.getPerformanceResult() == null ? 0 : detail.getPerformanceResult()));
                }else{
                    double existV = userDay2TotalResultMap.get(detail.getSortingDay());
                    double curV = (detail.getPerformanceResult() == null ? 0 : detail.getPerformanceResult());
                    userDay2TotalResultMap.put(detail.getSortingDay(), (existV + curV));
                }
            }
        }

        //查询该区域月份的所有采购 和 调拨数据
        StoreWarehouseStockItem storeWarehouseStockItem = new StoreWarehouseStockItem();
        storeWarehouseStockItem.setDeptId(storeSortingPerformanceMonth.getDeptId());
        storeWarehouseStockItem.setStartDate(storeSortingPerformanceMonth.getSortingMonth() + "-01");
        storeWarehouseStockItem.setEndDate(storeSortingPerformanceMonth.getSortingMonth() + "-31");
        storeWarehouseStockItem.setStatus("1");
        List<StoreWarehouseStockItem> storeWarehouseStockItemList = storeWarehouseStockItemService.selectStockItemListForSorting(storeWarehouseStockItem);
        //计算每次的采购数据和调拨数据总和
        Map<String,Double> day2StockTotalMap = new HashMap<>();
        Map<String,Double> day2AdjustTotalMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(storeWarehouseStockItemList)){
            for (StoreWarehouseStockItem info : storeWarehouseStockItemList ){
                if(info.getCreateDay() != null && info.getCategory() != null){
                    if("4".equals(info.getCategory())){
                        //采购
                        if(day2StockTotalMap.get(info.getCreateDay()) == null){
                            day2StockTotalMap.put(info.getCreateDay(),0d);
                        }
                        day2StockTotalMap.put(info.getCreateDay(), day2StockTotalMap.get(info.getCreateDay()) + (info.getQuantity() == null ? 0d : info.getQuantity()));
                    }else if("5".equals(info.getCategory())){
                        //调拨
                        if(day2AdjustTotalMap.get(info.getCreateDay()) == null){
                            day2AdjustTotalMap.put(info.getCreateDay(),0d);
                        }
                        day2AdjustTotalMap.put(info.getCreateDay(), day2AdjustTotalMap.get(info.getCreateDay()) + (info.getQuantity() == null ? 0d : info.getQuantity()));
                    }
                }
            }
        }

        //分拣员每日对应的分拣结果平均值
        Map<String, Double> day2AvgResultMap = new HashMap<>(16);
        Map<String, Double> day2AvgStockMap = new HashMap<>(16);
        Map<String, Double> day2AvgAdjustMap = new HashMap<>(16);
        if (!workDay2WorkListMap.isEmpty()) {
            for (String workDay : workDay2WorkListMap.keySet()) {
                //获取当天的分拣员数据
                List<StoreSortingDailyWork> dailyWorkList = workDay2WorkListMap.get(workDay);
                if (dailyWorkList != null && !dailyWorkList.isEmpty()) {
                    Double totalV = userDay2TotalResultMap.get(workDay);
                    if (totalV == null) {
                        totalV = 0d;
                    }
                    day2AvgResultMap.put(workDay, 1.0f * totalV / dailyWorkList.size());

                    //计算采购和调拨平均值
                    if (day2StockTotalMap.get(workDay) == null) {
                        day2AvgStockMap.put(workDay,0d);
                    } else {
                        day2AvgStockMap.put(workDay,1.0f * 0.01f * day2StockTotalMap.get(workDay) / dailyWorkList.size());
                    }

                    if (day2AdjustTotalMap.get(workDay) == null) {
                        day2AvgAdjustMap.put(workDay,0d);
                    } else {
                        day2AvgAdjustMap.put(workDay,1.0f * 0.01f * day2AdjustTotalMap.get(workDay) / dailyWorkList.size());
                    }
                }
            }
        }

        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //再遍历分拣考勤用户，生成分拣绩效数据
        for (String key : userId2WorkListMap.keySet()) {
            List<StoreSortingDailyWork> userWorkList = userId2WorkListMap.get(key);
            if (CollectionUtils.isNotEmpty(userWorkList)) {
                List<StoreSortingPerformanceDay> performanceDaysInsert = new ArrayList<>();
                StoreSortingPerformanceMonth month = new StoreSortingPerformanceMonth();
                month.setDeptId(storeSortingPerformanceMonth.getDeptId());
                month.setSortingMonth(storeSortingPerformanceMonth.getSortingMonth());
                month.setOperatorId(user.getUserId());
                month.setUserId(key);
                month.setResult(0d);
                month.setStockResult(0d);
                month.setAdjustResult(0d);
                for (StoreSortingDailyWork work : userWorkList) {
                    StoreSortingPerformanceDay day = new StoreSortingPerformanceDay();
                    day.setDeptId(storeSortingPerformanceMonth.getDeptId());
                    day.setUserId(work.getSortingUserId());
                    day.setSortingDay(work.getWorkDay());
                    day.setOperatorId(user.getUserId());
                    day.setResult((day2AvgResultMap.get(work.getWorkDay()) == null ? 0d : day2AvgResultMap.get(work.getWorkDay())));
                    day.setStockResult((day2AvgStockMap.get(work.getWorkDay()) == null ? 0d : day2AvgStockMap.get(work.getWorkDay())));
                    day.setAdjustResult((day2AvgAdjustMap.get(work.getWorkDay()) == null ? 0d : day2AvgAdjustMap.get(work.getWorkDay())));
                    performanceDaysInsert.add(day);
                    month.setResult(month.getResult() + day.getResult());
                    month.setStockResult(month.getStockResult() + day.getStockResult());
                    month.setAdjustResult(month.getAdjustResult() + day.getAdjustResult());
                }
                //保存单个人的月份数据
                int ret = insertStoreSortingPerformanceMonth(month);
                if (ret > 0) {
                    StoreSortingPerformanceMonth month1 = new StoreSortingPerformanceMonth();
                    month1.setUserId(key);
                    month1.setSortingMonth(storeSortingPerformanceMonth.getSortingMonth());
                    month1.setStatus("1");
                    List<StoreSortingPerformanceMonth> news = selectStoreSortingPerformanceMonthList(month1);
                    if (CollectionUtils.isNotEmpty(news)) {
                        for (StoreSortingPerformanceDay day : performanceDaysInsert) {
                            day.setMonthId(news.get(0).getId());
                            storeSortingPerformanceDayService.insertStoreSortingPerformanceDay(day);
                        }
                    }
                }
            }
        }
        return success();
    }
}
