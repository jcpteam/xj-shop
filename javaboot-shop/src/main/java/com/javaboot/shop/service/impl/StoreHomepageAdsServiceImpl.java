package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreHomepageAdsMapper;
import com.javaboot.shop.domain.StoreHomepageAds;
import com.javaboot.shop.service.IStoreHomepageAdsService;
import com.javaboot.common.core.text.Convert;

/**
 * 商城首页广告信息Service业务层处理
 * 
 * @author javaboot
 * @date 2021-06-05
 */
@Service
public class StoreHomepageAdsServiceImpl implements IStoreHomepageAdsService {
    @Autowired
    private StoreHomepageAdsMapper storeHomepageAdsMapper;

    /**
     * 查询商城首页广告信息
     * 
     * @param id 商城首页广告信息ID
     * @return 商城首页广告信息
     */
    @Override
    public StoreHomepageAds selectStoreHomepageAdsById(Long id) {
        return storeHomepageAdsMapper.selectStoreHomepageAdsById(id);
    }

    /**
     * 查询商城首页广告信息列表
     * 
     * @param storeHomepageAds 商城首页广告信息
     * @return 商城首页广告信息
     */
    @Override
    public List<StoreHomepageAds> selectStoreHomepageAdsList(StoreHomepageAds storeHomepageAds) {
        return storeHomepageAdsMapper.selectStoreHomepageAdsList(storeHomepageAds);
    }

    /**
     * 新增商城首页广告信息
     * 
     * @param storeHomepageAds 商城首页广告信息
     * @return 结果
     */
    @Override
    public int insertStoreHomepageAds(StoreHomepageAds storeHomepageAds) {
        storeHomepageAds.setCreateTime(DateUtils.getNowDate());
        return storeHomepageAdsMapper.insertStoreHomepageAds(storeHomepageAds);
    }

    /**
     * 修改商城首页广告信息
     * 
     * @param storeHomepageAds 商城首页广告信息
     * @return 结果
     */
    @Override
    public int updateStoreHomepageAds(StoreHomepageAds storeHomepageAds) {
        return storeHomepageAdsMapper.updateStoreHomepageAds(storeHomepageAds);
    }

    /**
     * 删除商城首页广告信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreHomepageAdsByIds(String ids) {
        return storeHomepageAdsMapper.deleteStoreHomepageAdsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商城首页广告信息信息
     * 
     * @param id 商城首页广告信息ID
     * @return 结果
     */
    @Override
    public int deleteStoreHomepageAdsById(Long id) {
        return storeHomepageAdsMapper.deleteStoreHomepageAdsById(id);
    }
}
