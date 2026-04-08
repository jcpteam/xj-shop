package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreOrderPasswordMapper;
import com.javaboot.shop.domain.StoreOrderPassword;
import com.javaboot.shop.service.IStoreOrderPasswordService;
import com.javaboot.common.core.text.Convert;

/**
 * 订单密码Service业务层处理
 * 
 * @author javaboot
 * @date 2021-08-07
 */
@Service
public class StoreOrderPasswordServiceImpl implements IStoreOrderPasswordService {
    @Autowired
    private StoreOrderPasswordMapper storeOrderPasswordMapper;

    /**
     * 查询订单密码
     * 
     * @param id 订单密码ID
     * @return 订单密码
     */
    @Override
    public StoreOrderPassword selectStoreOrderPasswordById(Long id) {
        return storeOrderPasswordMapper.selectStoreOrderPasswordById(id);
    }

    /**
     * 查询订单密码列表
     * 
     * @param storeOrderPassword 订单密码
     * @return 订单密码
     */
    @Override
    public List<StoreOrderPassword> selectStoreOrderPasswordList(StoreOrderPassword storeOrderPassword) {
        return storeOrderPasswordMapper.selectStoreOrderPasswordList(storeOrderPassword);
    }

    /**
     * 新增订单密码
     * 
     * @param storeOrderPassword 订单密码
     * @return 结果
     */
    @Override
    public int insertStoreOrderPassword(StoreOrderPassword storeOrderPassword) {
        storeOrderPassword.setCreateTime(DateUtils.getNowDate());
        return storeOrderPasswordMapper.insertStoreOrderPassword(storeOrderPassword);
    }

    /**
     * 修改订单密码
     * 
     * @param storeOrderPassword 订单密码
     * @return 结果
     */
    @Override
    public int updateStoreOrderPassword(StoreOrderPassword storeOrderPassword) {
        return storeOrderPasswordMapper.updateStoreOrderPassword(storeOrderPassword);
    }

    /**
     * 删除订单密码对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreOrderPasswordByIds(String ids) {
        return storeOrderPasswordMapper.deleteStoreOrderPasswordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除订单密码信息
     * 
     * @param id 订单密码ID
     * @return 结果
     */
    @Override
    public int deleteStoreOrderPasswordById(Long id) {
        return storeOrderPasswordMapper.deleteStoreOrderPasswordById(id);
    }
}
