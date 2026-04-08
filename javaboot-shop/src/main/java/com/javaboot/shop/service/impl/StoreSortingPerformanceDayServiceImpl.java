package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreSortingPerformanceDayMapper;
import com.javaboot.shop.domain.StoreSortingPerformanceDay;
import com.javaboot.shop.service.IStoreSortingPerformanceDayService;
import com.javaboot.common.core.text.Convert;

/**
 * 分拣绩效每日Service业务层处理
 * 
 * @author javaboot
 * @date 2021-07-20
 */
@Service
public class StoreSortingPerformanceDayServiceImpl implements IStoreSortingPerformanceDayService {
    @Autowired
    private StoreSortingPerformanceDayMapper storeSortingPerformanceDayMapper;

    /**
     * 查询分拣绩效每日
     * 
     * @param id 分拣绩效每日ID
     * @return 分拣绩效每日
     */
    @Override
    public StoreSortingPerformanceDay selectStoreSortingPerformanceDayById(Long id) {
        return storeSortingPerformanceDayMapper.selectStoreSortingPerformanceDayById(id);
    }

    /**
     * 查询分拣绩效每日列表
     * 
     * @param storeSortingPerformanceDay 分拣绩效每日
     * @return 分拣绩效每日
     */
    @Override
    public List<StoreSortingPerformanceDay> selectStoreSortingPerformanceDayList(StoreSortingPerformanceDay storeSortingPerformanceDay) {
        return storeSortingPerformanceDayMapper.selectStoreSortingPerformanceDayList(storeSortingPerformanceDay);
    }

    /**
     * 新增分拣绩效每日
     * 
     * @param storeSortingPerformanceDay 分拣绩效每日
     * @return 结果
     */
    @Override
    public int insertStoreSortingPerformanceDay(StoreSortingPerformanceDay storeSortingPerformanceDay) {
        storeSortingPerformanceDay.setCreateTime(DateUtils.getNowDate());
        return storeSortingPerformanceDayMapper.insertStoreSortingPerformanceDay(storeSortingPerformanceDay);
    }

    /**
     * 修改分拣绩效每日
     * 
     * @param storeSortingPerformanceDay 分拣绩效每日
     * @return 结果
     */
    @Override
    public int updateStoreSortingPerformanceDay(StoreSortingPerformanceDay storeSortingPerformanceDay) {
        return storeSortingPerformanceDayMapper.updateStoreSortingPerformanceDay(storeSortingPerformanceDay);
    }

    /**
     * 删除分拣绩效每日对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreSortingPerformanceDayByIds(String ids) {
        return storeSortingPerformanceDayMapper.deleteStoreSortingPerformanceDayByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除分拣绩效每日信息
     * 
     * @param id 分拣绩效每日ID
     * @return 结果
     */
    @Override
    public int deleteStoreSortingPerformanceDayById(Long id) {
        return storeSortingPerformanceDayMapper.deleteStoreSortingPerformanceDayById(id);
    }
}
