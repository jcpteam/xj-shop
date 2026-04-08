package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StorePriceSettingDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 控价详情Mapper接口
 * 
 * @author javaboot
 * @date 2021-07-04
 */
public interface StorePriceSettingDetailMapper {
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
     * store_price_setting， store_price_setting_detail两个表查询
     * @param storePriceSettingDetail
     * @return
     */
    public List<StorePriceSettingDetail> selectSettingDetailList(StorePriceSettingDetail storePriceSettingDetail);

    /**
     * 查询控价详情列表
     *
     * @param deptId 控价详情
     * @return 控价详情集合
     */
    public List<StorePriceSettingDetail> selectStorePriceSettingDetailListByDeptId(@Param("deptId") String deptId);


    /**
     * 查询控价详情列表
     *
     * @param priceId 控价详情
     * @return 控价详情集合
     */
    public List<StorePriceSettingDetail> selectStorePriceSettingDetailsByPriceId(StorePriceSettingDetail storePriceSettingDetail);
    public List<StorePriceSettingDetail> selectStorePriceSettingDetailsByPriceIdForParams(StorePriceSettingDetail storePriceSettingDetail);


    /**
     * 新增控价详情
     * 
     * @param storePriceSettingDetail 控价详情
     * @return 结果
     */
    public int insertStorePriceSettingDetail(StorePriceSettingDetail storePriceSettingDetail);

    public int insertStorePriceSettingDetailBatch(List<StorePriceSettingDetail> list);

    public int insertStorePriceSettingDetailByDeptId(@Param("deptId") String deptId,@Param("optDate") String optDate);

    /**
     * 修改控价详情
     * 
     * @param storePriceSettingDetail 控价详情
     * @return 结果
     */
    public int updateStorePriceSettingDetail(StorePriceSettingDetail storePriceSettingDetail);


    /**
     *
     * @param deptId
     * @param status
     * @return
     */
    public int updateStorePriceSettingDetailByDeptId(@Param("deptId") String deptId, @Param("status") String status);


    public int updateStorePriceSettingDetailBatch(List<StorePriceSettingDetail> storePriceSettingDetails);

    /**
     * 删除控价详情
     * 
     * @param settingId 控价详情ID
     * @return 结果
     */
    public int deleteStorePriceSettingDetailById(Integer settingId);

    /**
     * 批量删除控价详情
     * 
     * @param settingIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStorePriceSettingDetailByIds(String[] settingIds);
}
