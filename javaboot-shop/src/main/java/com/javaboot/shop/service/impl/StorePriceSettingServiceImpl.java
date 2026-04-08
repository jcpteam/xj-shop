package com.javaboot.shop.service.impl;

import java.beans.Transient;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StorePriceSetting;
import com.javaboot.shop.mapper.StorePriceSettingMapper;
import com.javaboot.shop.service.IStorePriceSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 控价Service业务层处理
 * 
 * @author javaboot
 * @date 2021-06-24
 */
@Service
public class StorePriceSettingServiceImpl implements IStorePriceSettingService {
    @Autowired
    private StorePriceSettingMapper storePriceSettingMapper;

    /**
     * 查询控价
     * 
     * @param settingId 控价ID
     * @return 控价
     */
    @Override
    public StorePriceSetting selectStorePriceSettingById(Long settingId) {
        return storePriceSettingMapper.selectStorePriceSettingById(settingId);
    }

    /**
     * 查询控价列表
     * 
     * @param storePriceSetting 控价
     * @return 控价
     */
    @Override
    public List<StorePriceSetting> selectStorePriceSettingList(StorePriceSetting storePriceSetting) {
        return storePriceSettingMapper.selectStorePriceSettingList(storePriceSetting);
    }

    @Override
    public StorePriceSetting selectStorePriceSettingByDeptId(String deptId) {
        return storePriceSettingMapper.selectStorePriceSettingByDeptId(deptId);
    }

    /**
     * 新增控价
     * 
     * @param storePriceSetting 控价
     * @return 结果
     */
    @Override
    public int insertStorePriceSetting(StorePriceSetting storePriceSetting) {
        return storePriceSettingMapper.insertStorePriceSetting(storePriceSetting);
    }

    /**
     * 修改控价
     * 
     * @param storePriceSetting 控价
     * @return 结果
     */
    @Override
    public int updateStorePriceSetting(StorePriceSetting storePriceSetting) {
        return storePriceSettingMapper.updateStorePriceSetting(storePriceSetting);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateStorePriceSettingBatch(String settings) {
        if(StringUtils.isEmpty(settings)){
            return 0;
        }
        JSONObject jsonObject = JSON.parseObject(settings);

        JSONArray jsonArray = jsonObject.getJSONArray("priceSettings");

        for(int i=0;i<jsonArray.size();i++){
            JSONObject json  = jsonArray.getJSONObject(i);
            StorePriceSetting storePriceSettingOld = new StorePriceSetting();
            storePriceSettingOld.setDeptId(json.getString("deptId"));
            storePriceSettingOld.setStatus("0");
            storePriceSettingMapper.updateStorePriceSetting(storePriceSettingOld);
            StorePriceSetting storePriceSetting =  new StorePriceSetting();
            storePriceSetting.setSettingId(json.getLong("settingId"));
            storePriceSetting.setStatus("2");
            storePriceSettingMapper.updateStorePriceSetting(storePriceSetting);
        }
        return jsonArray.size();
    }

    /**
     * 删除控价对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStorePriceSettingByIds(String ids) {
        return storePriceSettingMapper.deleteStorePriceSettingByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除控价信息
     * 
     * @param settingId 控价ID
     * @return 结果
     */
    @Override
    public int deleteStorePriceSettingById(Long settingId) {
        return storePriceSettingMapper.deleteStorePriceSettingById(settingId);
    }
}
