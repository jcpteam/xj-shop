package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreHouse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓库信息Mapper接口
 * 
 * @author javaboot
 * @date 2021-05-25
 */
public interface StoreHouseMapper {
    /**
     * 查询仓库信息
     * 
     * @param storeId 仓库信息ID
     * @return 仓库信息
     */
    public StoreHouse selectStoreHouseById(Long storeId);

    /**
     * 查询仓库信息列表
     * 
     * @param storeHouse 仓库信息
     * @return 仓库信息集合
     */
    public List<StoreHouse> selectStoreHouseList(StoreHouse storeHouse);

    /**
     * 查询仓库信息列表
     * @param idList
     * @return
     */
      List<StoreHouse> selectStoreHouseListByIds(@Param("idList") List<Long> idList);

    /**
     * 新增仓库信息
     * 
     * @param storeHouse 仓库信息
     * @return 结果
     */
    public int insertStoreHouse(StoreHouse storeHouse);

    /**
     * 修改仓库信息
     * 
     * @param storeHouse 仓库信息
     * @return 结果
     */
    public int updateStoreHouse(StoreHouse storeHouse);

    /**
     * 删除仓库信息
     * 
     * @param storeId 仓库信息ID
     * @return 结果
     */
    public int deleteStoreHouseById(Long storeId);

    /**
     * 批量删除仓库信息
     * 
     * @param storeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreHouseByIds(String[] storeIds);
}
