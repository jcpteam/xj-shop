package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreSortingDailyUser;
import java.util.List;

/**
 * 分拣每日考勤员工关联Service接口
 * 
 * @author javaboot
 * @date 2021-06-12
 */
public interface IStoreSortingDailyUserService {
    /**
     * 查询分拣每日考勤员工关联
     * 
     * @param id 分拣每日考勤员工关联ID
     * @return 分拣每日考勤员工关联
     */
    public StoreSortingDailyUser selectStoreSortingDailyUserById(Long id);

    /**
     * 查询分拣每日考勤员工关联列表
     * 
     * @param storeSortingDailyUser 分拣每日考勤员工关联
     * @return 分拣每日考勤员工关联集合
     */
    public List<StoreSortingDailyUser> selectStoreSortingDailyUserList(StoreSortingDailyUser storeSortingDailyUser);

    /**
     * 新增分拣每日考勤员工关联
     * 
     * @param storeSortingDailyUser 分拣每日考勤员工关联
     * @return 结果
     */
    public int insertStoreSortingDailyUser(StoreSortingDailyUser storeSortingDailyUser);

    /**
     * 修改分拣每日考勤员工关联
     * 
     * @param storeSortingDailyUser 分拣每日考勤员工关联
     * @return 结果
     */
    public int updateStoreSortingDailyUser(StoreSortingDailyUser storeSortingDailyUser);

    /**
     * 批量删除分拣每日考勤员工关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSortingDailyUserByIds(String ids);

    /**
     * 删除分拣每日考勤员工关联信息
     * 
     * @param id 分拣每日考勤员工关联ID
     * @return 结果
     */
    public int deleteStoreSortingDailyUserById(Long id);

    public int addSortingUserList(Long workId, String userIds);
}
