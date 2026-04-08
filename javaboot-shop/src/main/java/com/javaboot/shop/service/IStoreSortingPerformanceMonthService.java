package com.javaboot.shop.service;

import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.shop.domain.StoreSortingPerformanceMonth;
import java.util.List;

/**
 * 分拣绩效月份Service接口
 * 
 * @author javaboot
 * @date 2021-07-20
 */
public interface IStoreSortingPerformanceMonthService {
    /**
     * 查询分拣绩效月份
     * 
     * @param id 分拣绩效月份ID
     * @return 分拣绩效月份
     */
    public StoreSortingPerformanceMonth selectStoreSortingPerformanceMonthById(Long id);

    /**
     * 查询分拣绩效月份列表
     * 
     * @param storeSortingPerformanceMonth 分拣绩效月份
     * @return 分拣绩效月份集合
     */
    public List<StoreSortingPerformanceMonth> selectStoreSortingPerformanceMonthList(StoreSortingPerformanceMonth storeSortingPerformanceMonth);

    /**
     * 新增分拣绩效月份
     * 
     * @param storeSortingPerformanceMonth 分拣绩效月份
     * @return 结果
     */
    public int insertStoreSortingPerformanceMonth(StoreSortingPerformanceMonth storeSortingPerformanceMonth);

    /**
     * 修改分拣绩效月份
     * 
     * @param storeSortingPerformanceMonth 分拣绩效月份
     * @return 结果
     */
    public int updateStoreSortingPerformanceMonth(StoreSortingPerformanceMonth storeSortingPerformanceMonth);

    /**
     * 批量删除分拣绩效月份
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSortingPerformanceMonthByIds(String ids);

    /**
     * 删除分拣绩效月份信息
     * 
     * @param id 分拣绩效月份ID
     * @return 结果
     */
    public int deleteStoreSortingPerformanceMonthById(Long id);

    public AjaxResult calculatePerformanceMonth(StoreSortingPerformanceMonth storeSortingPerformanceMonth);
}
