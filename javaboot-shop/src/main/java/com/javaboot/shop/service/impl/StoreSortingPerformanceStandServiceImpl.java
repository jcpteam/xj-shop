package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreSortingPerformanceStandMapper;
import com.javaboot.shop.domain.StoreSortingPerformanceStand;
import com.javaboot.shop.service.IStoreSortingPerformanceStandService;
import com.javaboot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分拣绩效标准信息Service业务层处理
 * 
 * @author javaboot
 * @date 2021-07-01
 */
@Service
public class StoreSortingPerformanceStandServiceImpl implements IStoreSortingPerformanceStandService {
    @Autowired
    private StoreSortingPerformanceStandMapper storeSortingPerformanceStandMapper;

    /**
     * 查询分拣绩效标准信息
     * 
     * @param id 分拣绩效标准信息ID
     * @return 分拣绩效标准信息
     */
    @Override
    public StoreSortingPerformanceStand selectStoreSortingPerformanceStandById(Long id) {
        return storeSortingPerformanceStandMapper.selectStoreSortingPerformanceStandById(id);
    }

    /**
     * 查询分拣绩效标准信息列表
     * 
     * @param storeSortingPerformanceStand 分拣绩效标准信息
     * @return 分拣绩效标准信息
     */
    @Override
    public List<StoreSortingPerformanceStand> selectStoreSortingPerformanceStandList(StoreSortingPerformanceStand storeSortingPerformanceStand) {
        return storeSortingPerformanceStandMapper.selectStoreSortingPerformanceStandList(storeSortingPerformanceStand);
    }

    /**
     * 新增分拣绩效标准信息
     * 
     * @param storeSortingPerformanceStand 分拣绩效标准信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreSortingPerformanceStand(StoreSortingPerformanceStand storeSortingPerformanceStand) {
        storeSortingPerformanceStand.setCreateTime(DateUtils.getNowDate());
        return storeSortingPerformanceStandMapper.insertStoreSortingPerformanceStand(storeSortingPerformanceStand);
    }

    /**
     * 修改分拣绩效标准信息
     * 
     * @param storeSortingPerformanceStand 分拣绩效标准信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreSortingPerformanceStand(StoreSortingPerformanceStand storeSortingPerformanceStand) {
        return storeSortingPerformanceStandMapper.updateStoreSortingPerformanceStand(storeSortingPerformanceStand);
    }

    /**
     * 删除分拣绩效标准信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreSortingPerformanceStandByIds(String ids) {
        return storeSortingPerformanceStandMapper.deleteStoreSortingPerformanceStandByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除分拣绩效标准信息信息
     * 
     * @param id 分拣绩效标准信息ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreSortingPerformanceStandById(Long id) {
        return storeSortingPerformanceStandMapper.deleteStoreSortingPerformanceStandById(id);
    }

    /**
     * 查询分拣绩效标准信息列表
     *
     * @param storeSortingPerformanceStand 分拣绩效标准信息
     * @return 分拣绩效标准信息
     */
    @Override
    public List<StoreSortingPerformanceStand> selectStoreSortingPerformanceStandWithOrderItemId(StoreSortingPerformanceStand storeSortingPerformanceStand) {
        return storeSortingPerformanceStandMapper.selectStoreSortingPerformanceStandWithOrderItemId(storeSortingPerformanceStand);
    }
}
