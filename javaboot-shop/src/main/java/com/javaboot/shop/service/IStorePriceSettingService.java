package com.javaboot.shop.service;

import com.javaboot.shop.domain.StorePriceSetting;

import java.util.List;

/**
 * 控价Service接口
 * 
 * @author javaboot
 * @date 2021-06-24
 */
public interface IStorePriceSettingService {
    /**
     * 查询控价
     * 
     * @param settingId 控价ID
     * @return 控价
     */
    public StorePriceSetting selectStorePriceSettingById(Long settingId);

    /**
     * 查询控价列表
     * 
     * @param storePriceSetting 控价
     * @return 控价集合
     */
    public List<StorePriceSetting> selectStorePriceSettingList(StorePriceSetting storePriceSetting);


    /**
     * 查询控价列表
     *
     * @param deptId 控价
     * @return 控价集合
     */
    public StorePriceSetting selectStorePriceSettingByDeptId(String  deptId);


    /**
     * 新增控价
     * 
     * @param storePriceSetting 控价
     * @return 结果
     */
    public int insertStorePriceSetting(StorePriceSetting storePriceSetting);

    /**
     * 修改控价
     * 
     * @param storePriceSetting 控价
     * @return 结果
     */
    public int updateStorePriceSetting(StorePriceSetting storePriceSetting);

    public int updateStorePriceSettingBatch(String settings);

    /**
     * 批量删除控价
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public  int deleteStorePriceSettingByIds(String ids);
    /**
     * 删除控价信息
     * 
     * @param settingId 控价ID
     * @return 结果
     */
    public int deleteStorePriceSettingById(Long settingId);
}
