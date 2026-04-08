package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreSortingPerformanceStand;
import java.util.List;

/**
 * 分拣绩效标准信息Mapper接口
 * 
 * @author javaboot
 * @date 2021-07-01
 */
public interface StoreSortingPerformanceStandMapper {
    /**
     * 查询分拣绩效标准信息
     * 
     * @param id 分拣绩效标准信息ID
     * @return 分拣绩效标准信息
     */
    public StoreSortingPerformanceStand selectStoreSortingPerformanceStandById(Long id);

    /**
     * 查询分拣绩效标准信息列表
     * 
     * @param storeSortingPerformanceStand 分拣绩效标准信息
     * @return 分拣绩效标准信息集合
     */
    public List<StoreSortingPerformanceStand> selectStoreSortingPerformanceStandList(StoreSortingPerformanceStand storeSortingPerformanceStand);

    /**
     * 新增分拣绩效标准信息
     * 
     * @param storeSortingPerformanceStand 分拣绩效标准信息
     * @return 结果
     */
    public int insertStoreSortingPerformanceStand(StoreSortingPerformanceStand storeSortingPerformanceStand);

    /**
     * 修改分拣绩效标准信息
     * 
     * @param storeSortingPerformanceStand 分拣绩效标准信息
     * @return 结果
     */
    public int updateStoreSortingPerformanceStand(StoreSortingPerformanceStand storeSortingPerformanceStand);

    /**
     * 删除分拣绩效标准信息
     * 
     * @param id 分拣绩效标准信息ID
     * @return 结果
     */
    public int deleteStoreSortingPerformanceStandById(Long id);

    /**
     * 批量删除分拣绩效标准信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSortingPerformanceStandByIds(String[] ids);

    /**
     * 查询分拣绩效标准信息列表
     *
     * @param storeSortingPerformanceStand 分拣绩效标准信息
     * @return 分拣绩效标准信息集合
     */
    public List<StoreSortingPerformanceStand> selectStoreSortingPerformanceStandWithOrderItemId(StoreSortingPerformanceStand storeSortingPerformanceStand);
}
