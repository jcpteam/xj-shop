package com.javaboot.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboot.shop.domain.StoreGoodsQuotationGoods;
import com.javaboot.shop.domain.StoreSaleSettingLog;
import java.util.List;

/**
 * 商品上数日志Service接口
 * 
 * @author lqh
 * @date 2021-06-01
 */
public interface IStoreSaleSettingLogService extends IService<StoreSaleSettingLog>
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
     * 批量删除商品上数日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSaleSettingLogByIds(String ids);

    /**
     * 删除商品上数日志信息
     * 
     * @param logId 商品上数日志ID
     * @return 结果
     */
    public int deleteStoreSaleSettingLogById(Long logId);
}
