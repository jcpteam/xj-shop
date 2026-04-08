package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreOpmanagerPerformanceDayMapper;
import com.javaboot.shop.domain.StoreOpmanagerPerformanceDay;
import com.javaboot.shop.service.IStoreOpmanagerPerformanceDayService;
import com.javaboot.common.core.text.Convert;

/**
 * 业务员每日绩效Service业务层处理
 * 
 * @author javaboot
 * @date 2021-12-21
 */
@Service
public class StoreOpmanagerPerformanceDayServiceImpl implements IStoreOpmanagerPerformanceDayService {
    @Autowired
    private StoreOpmanagerPerformanceDayMapper storeOpmanagerPerformanceDayMapper;

    /**
     * 查询业务员每日绩效
     * 
     * @param id 业务员每日绩效ID
     * @return 业务员每日绩效
     */
    @Override
    public StoreOpmanagerPerformanceDay selectStoreOpmanagerPerformanceDayById(Long id) {
        return storeOpmanagerPerformanceDayMapper.selectStoreOpmanagerPerformanceDayById(id);
    }

    /**
     * 查询业务员每日绩效列表
     * 
     * @param storeOpmanagerPerformanceDay 业务员每日绩效
     * @return 业务员每日绩效
     */
    @Override
    public List<StoreOpmanagerPerformanceDay> selectStoreOpmanagerPerformanceDayList(StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay) {
        return storeOpmanagerPerformanceDayMapper.selectStoreOpmanagerPerformanceDayList(storeOpmanagerPerformanceDay);
    }

    /**
     * 新增业务员每日绩效
     * 
     * @param storeOpmanagerPerformanceDay 业务员每日绩效
     * @return 结果
     */
    @Override
    public int insertStoreOpmanagerPerformanceDay(StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay) {
        storeOpmanagerPerformanceDay.setCreateTime(DateUtils.getNowDate());
        return storeOpmanagerPerformanceDayMapper.insertStoreOpmanagerPerformanceDay(storeOpmanagerPerformanceDay);
    }

    /**
     * 修改业务员每日绩效
     * 
     * @param storeOpmanagerPerformanceDay 业务员每日绩效
     * @return 结果
     */
    @Override
    public int updateStoreOpmanagerPerformanceDay(StoreOpmanagerPerformanceDay storeOpmanagerPerformanceDay) {
        return storeOpmanagerPerformanceDayMapper.updateStoreOpmanagerPerformanceDay(storeOpmanagerPerformanceDay);
    }

    /**
     * 删除业务员每日绩效对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreOpmanagerPerformanceDayByIds(String ids) {
        return storeOpmanagerPerformanceDayMapper.deleteStoreOpmanagerPerformanceDayByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除业务员每日绩效信息
     * 
     * @param id 业务员每日绩效ID
     * @return 结果
     */
    @Override
    public int deleteStoreOpmanagerPerformanceDayById(Long id) {
        return storeOpmanagerPerformanceDayMapper.deleteStoreOpmanagerPerformanceDayById(id);
    }
}
