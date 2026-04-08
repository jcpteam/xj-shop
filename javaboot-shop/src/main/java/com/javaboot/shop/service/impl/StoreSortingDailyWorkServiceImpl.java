package com.javaboot.shop.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreSortingDailyUser;
import com.javaboot.shop.service.IStoreSortingDailyUserService;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreSortingDailyWorkMapper;
import com.javaboot.shop.domain.StoreSortingDailyWork;
import com.javaboot.shop.service.IStoreSortingDailyWorkService;
import com.javaboot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分拣每日考勤Service业务层处理
 * 
 * @author javaboot
 * @date 2021-06-11
 */
@Service
public class StoreSortingDailyWorkServiceImpl implements IStoreSortingDailyWorkService {
    @Autowired
    private StoreSortingDailyWorkMapper storeSortingDailyWorkMapper;

    @Autowired
    private IStoreSortingDailyUserService sortingDailyUserService;

    /**
     * 查询分拣每日考勤
     * 
     * @param id 分拣每日考勤ID
     * @return 分拣每日考勤
     */
    @Override
    public StoreSortingDailyWork selectStoreSortingDailyWorkById(Long id) {
        return storeSortingDailyWorkMapper.selectStoreSortingDailyWorkById(id);
    }

    /**
     * 查询分拣每日考勤列表
     * 
     * @param storeSortingDailyWork 分拣每日考勤
     * @return 分拣每日考勤
     */
    @Override
    public List<StoreSortingDailyWork> selectStoreSortingDailyWorkList(StoreSortingDailyWork storeSortingDailyWork) {
        String loginUserDeptId = null;
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if (!user.isAdmin()) {
            loginUserDeptId = user.getDept().getDeptId();
        }
        storeSortingDailyWork.setLoginUserDeptId(loginUserDeptId);
        return storeSortingDailyWorkMapper.selectStoreSortingDailyWorkList(storeSortingDailyWork);
    }

    /**
     * 查询分拣每日考勤列表
     *
     * @param storeSortingDailyWork 分拣每日考勤
     * @return 分拣每日考勤
     */
    @Override
    public List<StoreSortingDailyWork> selectStoreSortingDailyWorkUserList(StoreSortingDailyWork storeSortingDailyWork) {
        return storeSortingDailyWorkMapper.selectStoreSortingDailyWorkUserList(storeSortingDailyWork);
    }

    /**
     * 新增分拣每日考勤
     * 
     * @param storeSortingDailyWork 分拣每日考勤
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreSortingDailyWork(StoreSortingDailyWork storeSortingDailyWork) {
        storeSortingDailyWork.setCreateTime(DateUtils.getNowDate());
        return storeSortingDailyWorkMapper.insertStoreSortingDailyWork(storeSortingDailyWork);
    }

    /**
     * 修改分拣每日考勤
     * 
     * @param storeSortingDailyWork 分拣每日考勤
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreSortingDailyWork(StoreSortingDailyWork storeSortingDailyWork) {
        return storeSortingDailyWorkMapper.updateStoreSortingDailyWork(storeSortingDailyWork);
    }

    /**
     * 删除分拣每日考勤对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreSortingDailyWorkByIds(String ids) {
        return storeSortingDailyWorkMapper.deleteStoreSortingDailyWorkByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除分拣每日考勤信息
     * 
     * @param id 分拣每日考勤ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreSortingDailyWorkById(Long id) {
        return storeSortingDailyWorkMapper.deleteStoreSortingDailyWorkById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult saveStoreSortingDailyWork(StoreSortingDailyWork storeSortingDailyWork) {
        getDateLit(storeSortingDailyWork.getWorkDayStartDate(),storeSortingDailyWork.getWorkDayEndDate());
        //判断当前区域是否已经存在当前日期考勤
        StoreSortingDailyWork where = new StoreSortingDailyWork();
        where.setDeptId(storeSortingDailyWork.getDeptId());
//        where.setWorkDay(storeSortingDailyWork.getWorkDay());
        List<String> dayList = getDateLit(storeSortingDailyWork.getWorkDayStartDate(),storeSortingDailyWork.getWorkDayEndDate());
        where.setWorkDayList(dayList);
        List<StoreSortingDailyWork> existList = selectStoreSortingDailyWorkList(where);
        if(CollectionUtils.isNotEmpty(existList)){
            return AjaxResult.error("已经存在该区域相同的考勤日期");
        }

        String[] userIds = null;
        if(StringUtils.isNotEmpty(storeSortingDailyWork.getUserIds())){
            userIds = storeSortingDailyWork.getUserIds().split(",");
        }

        int result = 0;
        for (String day : dayList){
            StoreSortingDailyWork insert = new StoreSortingDailyWork();
            insert.setDeptId(storeSortingDailyWork.getDeptId());
            insert.setWorkDay(day);
            result += insertStoreSortingDailyWork(insert);

            //新增用户关联
            if(userIds != null){
                List<StoreSortingDailyWork> temps = selectStoreSortingDailyWorkList(insert);
                if(CollectionUtils.isNotEmpty(temps)){
                    for (String userId : userIds){
                        StoreSortingDailyUser userInsert = new StoreSortingDailyUser();
                        userInsert.setWorkId(temps.get(0).getId());
                        userInsert.setUserId(userId);
                        sortingDailyUserService.insertStoreSortingDailyUser(userInsert);
                    }
                }
            }
        }
        if(result > 0){
            return AjaxResult.success();
        }else{
            return AjaxResult.error();
        }
    }

    /**
     * 获取日期列表
     * @param start
     * @param end
     * @return
     */
    private List<String> getDateLit(String start,String end){
        List<String> retList = new ArrayList<>();
        if(start.equals(end)){
            retList.add(start);
            return retList;
        }
        try {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            long startTime = date.parse(start).getTime();
            long endTime = date.parse(end).getTime();
            long day = 1000 * 60 * 60 * 24;
            for (long i = startTime; i <= endTime; i += day) {
                retList.add(date.format(new Date(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
            retList.add(start);
            retList.add(end);
        }
        return retList;
    }
}
