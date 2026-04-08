package com.javaboot.shop.mapper;

import com.javaboot.common.core.dao.BaseMapper;
import com.javaboot.shop.domain.StoreSaleSetting;
import com.javaboot.shop.dto.StoreSaleSettingExportDTO;

import java.util.List;

/**
 * 商品上数设置Mapper接口
 * 
 * @author lqh
 * @date 2021-05-19
 */
public interface StoreSaleSettingMapper extends BaseMapper<StoreSaleSetting>
{
    /**
     * 查询商品上数设置
     * 
     * @param settingid 商品上数设置ID
     * @return 商品上数设置
     */
    public StoreSaleSetting selectStoreSaleSettingById(Long settingid);

    /**
     * 查询商品上数设置列表
     * 
     * @param storeSaleSetting 商品上数设置
     * @return 商品上数设置集合
     */
    public List<StoreSaleSetting> selectStoreSaleSettingList(StoreSaleSetting storeSaleSetting);

    /**
     * 商品上数设置导出
     *
     * @param storeSaleSetting 商品上数设置
     * @return 商品上数设置导出
     */
     List<StoreSaleSettingExportDTO> selectExportSaleSettingList(StoreSaleSetting storeSaleSetting);

    /**
     * 查询商品上数设置列表
     *
     * @param storeSaleSetting 商品上数设置
     * @return 商品上数设置集合
     */
    public List<StoreSaleSetting> selectStoreSaleSettingListForApp(StoreSaleSetting storeSaleSetting);

    /**
     * 新增商品上数设置
     * 
     * @param storeSaleSetting 商品上数设置
     * @return 结果
     */
    public int insertStoreSaleSetting(StoreSaleSetting storeSaleSetting);

    /**
     * 修改商品上数设置
     * 
     * @param storeSaleSetting 商品上数设置
     * @return 结果
     */
    public int updateStoreSaleSetting(StoreSaleSetting storeSaleSetting);

    /**
     * 删除商品上数设置
     * 
     * @param settingid 商品上数设置ID
     * @return 结果
     */
    public int deleteStoreSaleSettingById(Long settingid);

    /**
     * 批量删除商品上数设置
     * 
     * @param settingids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSaleSettingByIds(String[] settingids);

    /**
     * 获取报价单商品对应的商品SPU（去重）
     * @param deptId
     * @return
     */
    public List<String> selectStoreSaleSpuNoList(String deptId);
}
