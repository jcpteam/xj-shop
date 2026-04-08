package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StorePriceSettingDetail;
import com.javaboot.shop.mapper.StorePriceSettingDetailMapper;
import com.javaboot.shop.service.IStorePriceSettingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.common.core.text.Convert;

/**
 * 控价详情Service业务层处理
 * 
 * @author javaboot
 * @date 2021-07-04
 */
@Service
public class StorePriceSettingDetailServiceImpl implements IStorePriceSettingDetailService {
    @Autowired
    private StorePriceSettingDetailMapper storePriceSettingDetailMapper;

    /**
     * 查询控价详情
     * 
     * @param settingId 控价详情ID
     * @return 控价详情
     */
    @Override
    public StorePriceSettingDetail selectStorePriceSettingDetailById(Integer settingId) {
        return storePriceSettingDetailMapper.selectStorePriceSettingDetailById(settingId);
    }

    /**
     * 查询控价详情列表
     * 
     * @param storePriceSettingDetail 控价详情
     * @return 控价详情
     */
    @Override
    public List<StorePriceSettingDetail> selectStorePriceSettingDetailList(StorePriceSettingDetail storePriceSettingDetail) {
        return storePriceSettingDetailMapper.selectStorePriceSettingDetailList(storePriceSettingDetail);
    }

    @Override
    public List<StorePriceSettingDetail> selectStorePriceSettingDetailsByPriceId(StorePriceSettingDetail storePriceSettingDetail) {
        return storePriceSettingDetailMapper.selectStorePriceSettingDetailsByPriceId(storePriceSettingDetail);
    }
    @Override
    public List<StorePriceSettingDetail> selectStorePriceSettingDetailsByPriceIdForParams(StorePriceSettingDetail storePriceSettingDetail) {
        return storePriceSettingDetailMapper.selectStorePriceSettingDetailsByPriceIdForParams(storePriceSettingDetail);
    }

    @Override
    public List<StorePriceSettingDetail> selectStorePriceSettingDetailsByDeptId(String deptId) {
        return storePriceSettingDetailMapper.selectStorePriceSettingDetailListByDeptId(deptId);
    }

    /**
     * 新增控价详情
     * 
     * @param storePriceSettingDetail 控价详情
     * @return 结果
     */
    @Override
    public int insertStorePriceSettingDetail(StorePriceSettingDetail storePriceSettingDetail) {
        storePriceSettingDetail.setCreateTime(DateUtils.getNowDate());
        return storePriceSettingDetailMapper.insertStorePriceSettingDetail(storePriceSettingDetail);
    }

    @Override
    public int insertStorePriceSettingDetailBatch(List<StorePriceSettingDetail> list) {
        return storePriceSettingDetailMapper.insertStorePriceSettingDetailBatch(list);
    }

    @Override
    public int insertStorePriceSettingDetailByDeptId(String deptId,String optDate) {
        return storePriceSettingDetailMapper.insertStorePriceSettingDetailByDeptId(deptId,optDate);
    }

    /**
     * 修改控价详情
     * 
     * @param storePriceSettingDetail 控价详情
     * @return 结果
     */
    @Override
    public int updateStorePriceSettingDetail(StorePriceSettingDetail storePriceSettingDetail) {
        return storePriceSettingDetailMapper.updateStorePriceSettingDetail(storePriceSettingDetail);
    }

    @Override
    public int updateStorePriceSettingDetailByDeptId(String deptId, String status) {
        return storePriceSettingDetailMapper.updateStorePriceSettingDetailByDeptId(deptId,status);
    }

    /**
     * 删除控价详情对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStorePriceSettingDetailByIds(String ids) {
        return storePriceSettingDetailMapper.deleteStorePriceSettingDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除控价详情信息
     * 
     * @param settingId 控价详情ID
     * @return 结果
     */
    @Override
    public int deleteStorePriceSettingDetailById(Integer settingId) {
        return storePriceSettingDetailMapper.deleteStorePriceSettingDetailById(settingId);
    }

    @Override
    public int updateStorePriceSettingDetailBatch(List<StorePriceSettingDetail> storePriceSettingDetails) {
        return storePriceSettingDetailMapper.updateStorePriceSettingDetailBatch(storePriceSettingDetails);
    }
}
