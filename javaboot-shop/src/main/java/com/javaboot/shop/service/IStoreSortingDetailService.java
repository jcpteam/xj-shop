package com.javaboot.shop.service;

import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.shop.domain.StoreSortingDetail;
import java.util.List;

/**
 * 分拣记录详情Service接口
 * 
 * @author javaboot
 * @date 2021-07-05
 */
public interface IStoreSortingDetailService {
    /**
     * 查询分拣记录详情
     * 
     * @param id 分拣记录详情ID
     * @return 分拣记录详情
     */
    public StoreSortingDetail selectStoreSortingDetailById(Long id);

    /**
     * 查询分拣记录详情列表
     * 
     * @param storeSortingDetail 分拣记录详情
     * @return 分拣记录详情集合
     */
    public List<StoreSortingDetail> selectStoreSortingDetailList(StoreSortingDetail storeSortingDetail);
    public List<StoreSortingDetail> selectStoreSortingDetailListForDeptMonth(StoreSortingDetail storeSortingDetail);

    /**
     * 新增分拣记录详情
     * 
     * @param storeSortingDetail 分拣记录详情
     * @return 结果
     */
    public int insertStoreSortingDetail(StoreSortingDetail storeSortingDetail);

    /**
     * 修改分拣记录详情
     * 
     * @param storeSortingDetail 分拣记录详情
     * @return 结果
     */
    public int updateStoreSortingDetail(StoreSortingDetail storeSortingDetail);

    /**
     * 批量删除分拣记录详情
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSortingDetailByIds(String ids);

    /**
     * 删除分拣记录详情信息
     * 
     * @param id 分拣记录详情ID
     * @return 结果
     */
    public int deleteStoreSortingDetailById(Long id);

    public AjaxResult saveStoreSortingDetail(StoreSortingDetail storeSortingDetail);

    void updateSortDetailForItem(StoreSortingDetail storeSortingDetail);
}
