package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreHomepageAds;
import java.util.List;

/**
 * 商城首页广告信息Service接口
 * 
 * @author javaboot
 * @date 2021-06-05
 */
public interface IStoreHomepageAdsService {
    /**
     * 查询商城首页广告信息
     * 
     * @param id 商城首页广告信息ID
     * @return 商城首页广告信息
     */
    public StoreHomepageAds selectStoreHomepageAdsById(Long id);

    /**
     * 查询商城首页广告信息列表
     * 
     * @param storeHomepageAds 商城首页广告信息
     * @return 商城首页广告信息集合
     */
    public List<StoreHomepageAds> selectStoreHomepageAdsList(StoreHomepageAds storeHomepageAds);

    /**
     * 新增商城首页广告信息
     * 
     * @param storeHomepageAds 商城首页广告信息
     * @return 结果
     */
    public int insertStoreHomepageAds(StoreHomepageAds storeHomepageAds);

    /**
     * 修改商城首页广告信息
     * 
     * @param storeHomepageAds 商城首页广告信息
     * @return 结果
     */
    public int updateStoreHomepageAds(StoreHomepageAds storeHomepageAds);

    /**
     * 批量删除商城首页广告信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreHomepageAdsByIds(String ids);

    /**
     * 删除商城首页广告信息信息
     * 
     * @param id 商城首页广告信息ID
     * @return 结果
     */
    public int deleteStoreHomepageAdsById(Long id);
}
