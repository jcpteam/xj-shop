package com.javaboot.shop.service;

import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.shop.domain.StoreSortingDailyWork;
import java.util.List;

/**
 * 分拣每日考勤Service接口
 * 
 * @author javaboot
 * @date 2021-06-11
 */
public interface IStoreSortingDailyWorkService {
    /**
     * 查询分拣每日考勤
     * 
     * @param id 分拣每日考勤ID
     * @return 分拣每日考勤
     */
    public StoreSortingDailyWork selectStoreSortingDailyWorkById(Long id);

    /**
     * 查询分拣每日考勤列表
     * 
     * @param storeSortingDailyWork 分拣每日考勤
     * @return 分拣每日考勤集合
     */
    public List<StoreSortingDailyWork> selectStoreSortingDailyWorkList(StoreSortingDailyWork storeSortingDailyWork);
    /**
     * 查询分拣每日考勤列表
     *
     * @param storeSortingDailyWork 分拣每日考勤
     * @return 分拣每日考勤集合
     */
    public List<StoreSortingDailyWork> selectStoreSortingDailyWorkUserList(StoreSortingDailyWork storeSortingDailyWork);

    /**
     * 新增分拣每日考勤
     * 
     * @param storeSortingDailyWork 分拣每日考勤
     * @return 结果
     */
    public int insertStoreSortingDailyWork(StoreSortingDailyWork storeSortingDailyWork);

    /**
     * 修改分拣每日考勤
     * 
     * @param storeSortingDailyWork 分拣每日考勤
     * @return 结果
     */
    public int updateStoreSortingDailyWork(StoreSortingDailyWork storeSortingDailyWork);

    /**
     * 批量删除分拣每日考勤
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSortingDailyWorkByIds(String ids);

    /**
     * 删除分拣每日考勤信息
     * 
     * @param id 分拣每日考勤ID
     * @return 结果
     */
    public int deleteStoreSortingDailyWorkById(Long id);

    public AjaxResult saveStoreSortingDailyWork(StoreSortingDailyWork storeSortingDailyWork);
}
