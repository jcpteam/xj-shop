package com.javaboot.shop.mapper;

import com.javaboot.common.core.dao.BaseMapper;
import com.javaboot.shop.domain.StoreSaleSettingLog;
import java.util.List;

/**
 * 商品上数日志Mapper接口
 * 
 * @author lqh
 * @date 2021-06-01
 */
public interface StoreSaleSettingLogMapper extends BaseMapper<StoreSaleSettingLog>
{
    /**
     * 查询商品上数日志
     * 
     * @param logId 商品上数日志ID
     * @return 商品上数日志
     */
    public StoreSaleSettingLog selectStoreSaleSettingLogById(Long logId);

    /**
     * 查询商品上数日志列表
     * 
     * @param storeSaleSettingLog 商品上数日志
     * @return 商品上数日志集合
     */
    public List<StoreSaleSettingLog> selectStoreSaleSettingLogList(StoreSaleSettingLog storeSaleSettingLog);

    /**
     * 新增商品上数日志
     * 
     * @param storeSaleSettingLog 商品上数日志
     * @return 结果
     */
    public int insertStoreSaleSettingLog(StoreSaleSettingLog storeSaleSettingLog);

    /**
     * 修改商品上数日志
     * 
     * @param storeSaleSettingLog 商品上数日志
     * @return 结果
     */
    public int updateStoreSaleSettingLog(StoreSaleSettingLog storeSaleSettingLog);

    /**
     * 删除商品上数日志
     * 
     * @param logId 商品上数日志ID
     * @return 结果
     */
    public int deleteStoreSaleSettingLogById(Long logId);

    /**
     * 批量删除商品上数日志
     * 
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSaleSettingLogByIds(String[] logIds);
}
