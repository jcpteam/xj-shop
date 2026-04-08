package com.javaboot.shop.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javaboot.common.constant.Constants;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreEarlyWarningMapper;
import com.javaboot.shop.domain.StoreEarlyWarning;
import com.javaboot.shop.service.IStoreEarlyWarningService;
import com.javaboot.common.core.text.Convert;

/**
 * 预警设置Service业务层处理
 * 
 * @author javaboot
 * @date 2021-11-15
 */
@Service
public class StoreEarlyWarningServiceImpl implements IStoreEarlyWarningService {
    @Autowired
    private StoreEarlyWarningMapper storeEarlyWarningMapper;

    /**
     * 查询预警设置
     * 
     * @param id 预警设置ID
     * @return 预警设置
     */
    @Override
    public StoreEarlyWarning selectStoreEarlyWarningById(Long id) {
        return storeEarlyWarningMapper.selectStoreEarlyWarningById(id);
    }

    /**
     * 查询预警设置列表
     * 
     * @param storeEarlyWarning 预警设置
     * @return 预警设置
     */
    @Override
    public List<StoreEarlyWarning> selectStoreEarlyWarningList(StoreEarlyWarning storeEarlyWarning) {
        return storeEarlyWarningMapper.selectStoreEarlyWarningList(storeEarlyWarning);
    }

    /**
     * 新增预警设置
     * 
     * @param storeEarlyWarning 预警设置
     * @return 结果
     */
    @Override
    public int insertStoreEarlyWarning(StoreEarlyWarning storeEarlyWarning) {
        storeEarlyWarning.setCreateTime(DateUtils.getNowDate());
        return storeEarlyWarningMapper.insertStoreEarlyWarning(storeEarlyWarning);
    }

    /**
     * 修改预警设置
     * 
     * @param storeEarlyWarning 预警设置
     * @return 结果
     */
    @Override
    public int updateStoreEarlyWarning(StoreEarlyWarning storeEarlyWarning) {
        return storeEarlyWarningMapper.updateStoreEarlyWarning(storeEarlyWarning);
    }

    /**
     * 删除预警设置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreEarlyWarningByIds(String ids) {
        return storeEarlyWarningMapper.deleteStoreEarlyWarningByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除预警设置信息
     * 
     * @param id 预警设置ID
     * @return 结果
     */
    @Override
    public int deleteStoreEarlyWarningById(Long id) {
        return storeEarlyWarningMapper.deleteStoreEarlyWarningById(id);
    }

    @Override
    public List<StoreMember> getNoOrderCustomerListForWarn(String beforeYestoday, String yestoday) {
        Map<String,Object> params=new HashMap<>();
        params.put("beforeYestodayStart",beforeYestoday + " 00:00:00");
        params.put("beforeYestodayEnd", beforeYestoday + " 59:59:59");
        params.put("yestodayStart",yestoday + " 00:00:00");
        params.put("yestodayEnd", yestoday + " 59:59:59");

        return storeEarlyWarningMapper.getNoOrderCustomerListForWarn(params);
    }
}
