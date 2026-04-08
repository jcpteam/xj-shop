package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreDriverInfoMapper;
import com.javaboot.shop.domain.StoreDriverInfo;
import com.javaboot.shop.service.IStoreDriverInfoService;
import com.javaboot.common.core.text.Convert;

/**
 * 司机信息Service业务层处理
 * 
 * @author javaboot
 * @date 2021-05-25
 */
@Service
public class StoreDriverInfoServiceImpl implements IStoreDriverInfoService {
    @Autowired
    private StoreDriverInfoMapper storeDriverInfoMapper;

    /**
     * 查询司机信息
     * 
     * @param driverId 司机信息ID
     * @return 司机信息
     */
    @Override
    public StoreDriverInfo selectStoreDriverInfoById(Long driverId) {
        return storeDriverInfoMapper.selectStoreDriverInfoById(driverId);
    }

    /**
     * 查询司机信息列表
     * 
     * @param storeDriverInfo 司机信息
     * @return 司机信息
     */
    @Override
    public List<StoreDriverInfo> selectStoreDriverInfoList(StoreDriverInfo storeDriverInfo) {
        return storeDriverInfoMapper.selectStoreDriverInfoList(storeDriverInfo);
    }

    /**
     * 新增司机信息
     * 
     * @param storeDriverInfo 司机信息
     * @return 结果
     */
    @Override
    public int insertStoreDriverInfo(StoreDriverInfo storeDriverInfo) {
        storeDriverInfo.setCreateTime(DateUtils.getNowDate());
        return storeDriverInfoMapper.insertStoreDriverInfo(storeDriverInfo);
    }

    /**
     * 修改司机信息
     * 
     * @param storeDriverInfo 司机信息
     * @return 结果
     */
    @Override
    public int updateStoreDriverInfo(StoreDriverInfo storeDriverInfo) {
        return storeDriverInfoMapper.updateStoreDriverInfo(storeDriverInfo);
    }

    /**
     * 删除司机信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreDriverInfoByIds(String ids) {
        return storeDriverInfoMapper.deleteStoreDriverInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除司机信息信息
     * 
     * @param driverId 司机信息ID
     * @return 结果
     */
    @Override
    public int deleteStoreDriverInfoById(Long driverId) {
        return storeDriverInfoMapper.deleteStoreDriverInfoById(driverId);
    }
}
