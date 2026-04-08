package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreSortingBox;
import java.util.List;

/**
 * 分拣框信息Service接口
 * 
 * @author javaboot
 * @date 2021-06-11
 */
public interface IStoreSortingBoxService {
    /**
     * 查询分拣框信息
     * 
     * @param id 分拣框信息ID
     * @return 分拣框信息
     */
    public StoreSortingBox selectStoreSortingBoxById(Long id);

    /**
     * 查询分拣框信息列表
     * 
     * @param storeSortingBox 分拣框信息
     * @return 分拣框信息集合
     */
    public List<StoreSortingBox> selectStoreSortingBoxList(StoreSortingBox storeSortingBox);

    /**
     * 新增分拣框信息
     * 
     * @param storeSortingBox 分拣框信息
     * @return 结果
     */
    public int insertStoreSortingBox(StoreSortingBox storeSortingBox);

    /**
     * 修改分拣框信息
     * 
     * @param storeSortingBox 分拣框信息
     * @return 结果
     */
    public int updateStoreSortingBox(StoreSortingBox storeSortingBox);

    /**
     * 批量删除分拣框信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSortingBoxByIds(String ids);

    /**
     * 删除分拣框信息信息
     * 
     * @param id 分拣框信息ID
     * @return 结果
     */
    public int deleteStoreSortingBoxById(Long id);
}
