package com.javaboot.shop.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreSaleSetting;
import com.javaboot.shop.mapper.StoreSaleSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreSaleSettingLogMapper;
import com.javaboot.shop.domain.StoreSaleSettingLog;
import com.javaboot.shop.service.IStoreSaleSettingLogService;
import com.javaboot.common.core.text.Convert;

/**
 * 商品上数日志Service业务层处理
 * 
 * @author lqh
 * @date 2021-06-01
 */
@Service
public class StoreSaleSettingLogServiceImpl extends ServiceImpl<StoreSaleSettingLogMapper, StoreSaleSettingLog> implements IStoreSaleSettingLogService {
    @Autowired
    private StoreSaleSettingLogMapper storeSaleSettingLogMapper;

    /**
     * 查询商品上数日志
     * 
     * @param logId 商品上数日志ID
     * @return 商品上数日志
     */
    @Override
    public StoreSaleSettingLog selectStoreSaleSettingLogById(Long logId) {
        return storeSaleSettingLogMapper.selectStoreSaleSettingLogById(logId);
    }

    /**
     * 查询商品上数日志列表
     * 
     * @param storeSaleSettingLog 商品上数日志
     * @return 商品上数日志
     */
    @Override
    public List<StoreSaleSettingLog> selectStoreSaleSettingLogList(StoreSaleSettingLog storeSaleSettingLog) {
        return storeSaleSettingLogMapper.selectStoreSaleSettingLogList(storeSaleSettingLog);
    }

    /**
     * 新增商品上数日志
     * 
     * @param storeSaleSettingLog 商品上数日志
     * @return 结果
     */
    @Override
    public int insertStoreSaleSettingLog(StoreSaleSettingLog storeSaleSettingLog) {
        storeSaleSettingLog.setCreateTime(DateUtils.getNowDate());
        return storeSaleSettingLogMapper.insertStoreSaleSettingLog(storeSaleSettingLog);
    }

    /**
     * 修改商品上数日志
     * 
     * @param storeSaleSettingLog 商品上数日志
     * @return 结果
     */
    @Override
    public int updateStoreSaleSettingLog(StoreSaleSettingLog storeSaleSettingLog) {
        return storeSaleSettingLogMapper.updateStoreSaleSettingLog(storeSaleSettingLog);
    }

    /**
     * 删除商品上数日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreSaleSettingLogByIds(String ids) {
        return storeSaleSettingLogMapper.deleteStoreSaleSettingLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品上数日志信息
     * 
     * @param logId 商品上数日志ID
     * @return 结果
     */
    @Override
    public int deleteStoreSaleSettingLogById(Long logId) {
        return storeSaleSettingLogMapper.deleteStoreSaleSettingLogById(logId);
    }
}
