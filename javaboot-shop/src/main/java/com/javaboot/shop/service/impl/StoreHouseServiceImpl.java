package com.javaboot.shop.service.impl;

import com.javaboot.common.core.text.Convert;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.mapper.StoreHouseMapper;
import com.javaboot.shop.service.IStoreHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 仓库信息Service业务层处理
 * 
 * @author javaboot
 * @date 2021-05-25
 */
@Service
public class StoreHouseServiceImpl implements IStoreHouseService {
    @Autowired
    private StoreHouseMapper storeHouseMapper;

    /**
     * 查询仓库信息
     * 
     * @param storeId 仓库信息ID
     * @return 仓库信息
     */
    @Override
    public StoreHouse selectStoreHouseById(Long storeId) {
        return storeHouseMapper.selectStoreHouseById(storeId);
    }

    /**
     * 查询仓库信息列表
     * 
     * @param storeHouse 仓库信息
     * @return 仓库信息
     */
    @Override
    public List<StoreHouse> selectStoreHouseList(StoreHouse storeHouse) {
        return storeHouseMapper.selectStoreHouseList(storeHouse);
    }
    /**
     * 查询仓库信息列表
     * @param idList
     * @return
     */
    @Override
    public List<StoreHouse> selectStoreHouseListByIds( List<Long> idList){
        return storeHouseMapper.selectStoreHouseListByIds(idList);
    }
    /**
     * 新增仓库信息
     * 
     * @param storeHouse 仓库信息
     * @return 结果
     */
    @Override
    public int insertStoreHouse(StoreHouse storeHouse) {
        storeHouse.setCreateTime(DateUtils.getNowDate());
        return storeHouseMapper.insertStoreHouse(storeHouse);
    }

    /**
     * 修改仓库信息
     * 
     * @param storeHouse 仓库信息
     * @return 结果
     */
    @Override
    public int updateStoreHouse(StoreHouse storeHouse) {
        return storeHouseMapper.updateStoreHouse(storeHouse);
    }

    /**
     * 删除仓库信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreHouseByIds(String ids) {
        return storeHouseMapper.deleteStoreHouseByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除仓库信息信息
     * 
     * @param storeId 仓库信息ID
     * @return 结果
     */
    @Override
    public int deleteStoreHouseById(Long storeId) {
        return storeHouseMapper.deleteStoreHouseById(storeId);
    }
}
