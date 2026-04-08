package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreEarlyWarning;
import com.javaboot.shop.domain.StoreMember;

import java.util.List;

/**
 * 预警设置Service接口
 * 
 * @author javaboot
 * @date 2021-11-15
 */
public interface IStoreEarlyWarningService {
    /**
     * 查询预警设置
     * 
     * @param id 预警设置ID
     * @return 预警设置
     */
    public StoreEarlyWarning selectStoreEarlyWarningById(Long id);

    /**
     * 查询预警设置列表
     * 
     * @param storeEarlyWarning 预警设置
     * @return 预警设置集合
     */
    public List<StoreEarlyWarning> selectStoreEarlyWarningList(StoreEarlyWarning storeEarlyWarning);

    /**
     * 新增预警设置
     * 
     * @param storeEarlyWarning 预警设置
     * @return 结果
     */
    public int insertStoreEarlyWarning(StoreEarlyWarning storeEarlyWarning);

    /**
     * 修改预警设置
     * 
     * @param storeEarlyWarning 预警设置
     * @return 结果
     */
    public int updateStoreEarlyWarning(StoreEarlyWarning storeEarlyWarning);

    /**
     * 批量删除预警设置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreEarlyWarningByIds(String ids);

    /**
     * 删除预警设置信息
     * 
     * @param id 预警设置ID
     * @return 结果
     */
    public int deleteStoreEarlyWarningById(Long id);

    /**
     * 查询未下单预警的客户信息
     * @param beforeYestoday  前天
     * @param yestoday 昨天
     * @return
     */
    public List<StoreMember> getNoOrderCustomerListForWarn(String beforeYestoday,String yestoday);
}
