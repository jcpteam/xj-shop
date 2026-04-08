package com.javaboot.shop.service;

import com.javaboot.shop.domain.StorePriceSettingDetail;

import java.util.List;

/**
 * 控价详情Service接口
 * 
 * @author javaboot
 * @date 2021-07-04
 */
public interface IStorePriceSettingDetailService {
    /**
     * 查询控价详情
     * 
     * @param settingId 控价详情ID
     * @return 控价详情
     */
    public StorePriceSettingDetail selectStorePriceSettingDetailById(Integer settingId);

    /**
     * 查询控价详情列表
     * 
     * @param storePriceSettingDetail 控价详情
     * @return 控价详情集合
     */
    public List<StorePriceSettingDetail> selectStorePriceSettingDetailList(StorePriceSettingDetail storePriceSettingDetail);



    /**
     * 查询控价详情列表
     *
     * @param priceId 控价单ID
     * @return 控价详情集合
     */
    public List<StorePriceSettingDetail> selectStorePriceSettingDetailsByPriceId(StorePriceSettingDetail storePriceSettingDetail);
    public List<StorePriceSettingDetail> selectStorePriceSettingDetailsByPriceIdForParams(StorePriceSettingDetail storePriceSettingDetail);


    /**
     * 查询控价详情列表
     *
     * @param deptId 控价单ID
     * @return 控价详情集合
     */
    public List<StorePriceSettingDetail> selectStorePriceSettingDetailsByDeptId(String  deptId);


    /**
     * 新增控价详情
     * 
     * @param storePriceSettingDetail 控价详情
     * @return 结果
     */
    public int insertStorePriceSettingDetail(StorePriceSettingDetail storePriceSettingDetail);

    public int  insertStorePriceSettingDetailBatch(List<StorePriceSettingDetail> list);

    public int insertStorePriceSettingDetailByDeptId(String deptId,String optDate);

    /**
     * 修改控价详情
     * 
     * @param storePriceSettingDetail 控价详情
     * @return 结果
     */
    public int updateStorePriceSettingDetail(StorePriceSettingDetail storePriceSettingDetail);


    public int updateStorePriceSettingDetailByDeptId(String deptId,String status);

    /**
     * 批量删除控价详情
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStorePriceSettingDetailByIds(String ids);

    /**
     * 删除控价详情信息
     * 
     * @param settingId 控价详情ID
     * @return 结果
     */
    public int deleteStorePriceSettingDetailById(Integer settingId);


    public int updateStorePriceSettingDetailBatch(List<StorePriceSettingDetail> storePriceSettingDetails);
}
