package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreOpmanagerPerformanceDay;
import java.util.List;

/**
 * 业务员每日绩效Service接口
 * 
 * @author javaboot
 * @date 2021-12-21
 */
public interface IStoreOpmanagerPerformanceDayService {
    /**
     * 查询业务员每日绩效
     * 
     * @param id 业务员每日绩效ID
     * @return 业务员每日绩效
     */
    public StoreOpmanagerPerformanceDay selectStoreOpmanagerPerformanceDayById(Long id);

    /**
     * 查询业务员每日绩效列表
     * 
     * @param storeOpmanagerPerformanceDay 业务员每日绩效
     * @return 业务员每日绩效集合
     */
    public List<StoreOpmanagerPerformanceDay> selectStoreOpmanagerPerformanceDayList(StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay);

    /**
     * 新增业务员每日绩效
     * 
     * @param storeOpmanagerPerformanceDay 业务员每日绩效
     * @return 结果
     */
    public int insertStoreOpmanagerPerformanceDay(StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay);

    /**
     * 修改业务员每日绩效
     * 
     * @param storeOpmanagerPerformanceDay 业务员每日绩效
     * @return 结果
     */
    public int updateStoreOpmanagerPerformanceDay(StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay);

    /**
     * 批量删除业务员每日绩效
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreOpmanagerPerformanceDayByIds(String ids);

    /**
     * 删除业务员每日绩效信息
     * 
     * @param id 业务员每日绩效ID
     * @return 结果
     */
    public int deleteStoreOpmanagerPerformanceDayById(Long id);
}
