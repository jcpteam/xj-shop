package com.javaboot.shop.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreSortingDailyUserMapper;
import com.javaboot.shop.domain.StoreSortingDailyUser;
import com.javaboot.shop.service.IStoreSortingDailyUserService;
import com.javaboot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分拣每日考勤员工关联Service业务层处理
 * 
 * @author javaboot
 * @date 2021-06-12
 */
@Service
public class StoreSortingDailyUserServiceImpl implements IStoreSortingDailyUserService {
    @Autowired
    private StoreSortingDailyUserMapper storeSortingDailyUserMapper;

    /**
     * 查询分拣每日考勤员工关联
     * 
     * @param id 分拣每日考勤员工关联ID
     * @return 分拣每日考勤员工关联
     */
    @Override
    public StoreSortingDailyUser selectStoreSortingDailyUserById(Long id) {
        return storeSortingDailyUserMapper.selectStoreSortingDailyUserById(id);
    }

    /**
     * 查询分拣每日考勤员工关联列表
     * 
     * @param storeSortingDailyUser 分拣每日考勤员工关联
     * @return 分拣每日考勤员工关联
     */
    @Override
    public List<StoreSortingDailyUser> selectStoreSortingDailyUserList(StoreSortingDailyUser storeSortingDailyUser) {
        return storeSortingDailyUserMapper.selectStoreSortingDailyUserList(storeSortingDailyUser);
    }

    /**
     * 新增分拣每日考勤员工关联
     * 
     * @param storeSortingDailyUser 分拣每日考勤员工关联
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreSortingDailyUser(StoreSortingDailyUser storeSortingDailyUser) {
        return storeSortingDailyUserMapper.insertStoreSortingDailyUser(storeSortingDailyUser);
    }

    /**
     * 修改分拣每日考勤员工关联
     * 
     * @param storeSortingDailyUser 分拣每日考勤员工关联
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreSortingDailyUser(StoreSortingDailyUser storeSortingDailyUser) {
        return storeSortingDailyUserMapper.updateStoreSortingDailyUser(storeSortingDailyUser);
    }

    /**
     * 删除分拣每日考勤员工关联对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreSortingDailyUserByIds(String ids) {
        return storeSortingDailyUserMapper.deleteStoreSortingDailyUserByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除分拣每日考勤员工关联信息
     * 
     * @param id 分拣每日考勤员工关联ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreSortingDailyUserById(Long id) {
        return storeSortingDailyUserMapper.deleteStoreSortingDailyUserById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addSortingUserList(Long workId, String userIds){
        String[] users = Convert.toStrArray(userIds);
        // 新增用户与角色管理
        int row = 0;
        for (String userId : users) {
            StoreSortingDailyUser ur = new StoreSortingDailyUser();
            ur.setUserId(userId);
            ur.setWorkId(workId);
            row += insertStoreSortingDailyUser(ur);
        }
        return row;
    }
}
