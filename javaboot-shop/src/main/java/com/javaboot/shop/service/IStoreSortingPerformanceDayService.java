package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreSortingPerformanceDay;
import java.util.List;

/**
 * 分拣绩效每日Service接口
 * 
 * @author javaboot
 * @date 2021-07-20
 */
public interface IStoreSortingPerformanceDayService {
    /**
     * 查询分拣绩效每日
     * 
     * @param id 分拣绩效每日ID
     * @return 分拣绩效每日
     */
    public StoreSortingPerformanceDay selectStoreSortingPerformanceDayById(Long id);

    /**
     * 查询分拣绩效每日列表
     * 
     * @param storeSortingPerformanceDay 分拣绩效每日
     * @return 分拣绩效每日集合
     */
    public List<StoreSortingPerformanceDay> selectStoreSortingPerformanceDayList(StoreSortingPerformanceDay storeSortingPerformanceDay);

    /**
     * 新增分拣绩效每日
     * 
     * @param storeSortingPerformanceDay 分拣绩效每日
     * @return 结果
     */
    public int insertStoreSortingPerformanceDay(StoreSortingPerformanceDay storeSortingPerformanceDay);

    /**
     * 修改分拣绩效每日
     * 
     * @param storeSortingPerformanceDay 分拣绩效每日
     * @return 结果
     */
    public int updateStoreSortingPerformanceDay(StoreSortingPerformanceDay storeSortingPerformanceDay);

    /**
     * 批量删除分拣绩效每日
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSortingPerformanceDayByIds(String ids);

    /**
     * 删除分拣绩效每日信息
     * 
     * @param id 分拣绩效每日ID
     * @return 结果
     */
    public int deleteStoreSortingPerformanceDayById(Long id);
}
