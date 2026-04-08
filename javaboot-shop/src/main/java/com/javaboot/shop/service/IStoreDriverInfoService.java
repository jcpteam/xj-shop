package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreDriverInfo;
import java.util.List;

/**
 * 司机信息Service接口
 * 
 * @author javaboot
 * @date 2021-05-25
 */
public interface IStoreDriverInfoService {
    /**
     * 查询司机信息
     * 
     * @param driverId 司机信息ID
     * @return 司机信息
     */
    public StoreDriverInfo selectStoreDriverInfoById(Long driverId);

    /**
     * 查询司机信息列表
     * 
     * @param storeDriverInfo 司机信息
     * @return 司机信息集合
     */
    public List<StoreDriverInfo> selectStoreDriverInfoList(StoreDriverInfo storeDriverInfo);

    /**
     * 新增司机信息
     * 
     * @param storeDriverInfo 司机信息
     * @return 结果
     */
    public int insertStoreDriverInfo(StoreDriverInfo storeDriverInfo);

    /**
     * 修改司机信息
     * 
     * @param storeDriverInfo 司机信息
     * @return 结果
     */
    public int updateStoreDriverInfo(StoreDriverInfo storeDriverInfo);

    /**
     * 批量删除司机信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreDriverInfoByIds(String ids);

    /**
     * 删除司机信息信息
     * 
     * @param driverId 司机信息ID
     * @return 结果
     */
    public int deleteStoreDriverInfoById(Long driverId);
}
