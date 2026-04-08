package com.javaboot.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.system.mapper.SysUserRoleDeptMapper;
import com.javaboot.system.domain.SysUserRoleDept;
import com.javaboot.system.service.ISysUserRoleDeptService;
import com.javaboot.common.core.text.Convert;

/**
 * 用户和角色和部门关联Service业务层处理
 * 
 * @author javaboot
 * @date 2021-05-30
 */
@Service
public class SysUserRoleDeptServiceImpl implements ISysUserRoleDeptService {
    @Autowired
    private SysUserRoleDeptMapper sysUserRoleDeptMapper;

    /**
     * 查询用户和角色和部门关联
     * 
     * @param id 用户和角色和部门关联ID
     * @return 用户和角色和部门关联
     */
    @Override
    public SysUserRoleDept selectSysUserRoleDeptById(Long id) {
        return sysUserRoleDeptMapper.selectSysUserRoleDeptById(id);
    }

    /**
     * 查询用户和角色和部门关联列表
     * 
     * @param sysUserRoleDept 用户和角色和部门关联
     * @return 用户和角色和部门关联
     */
    @Override
    public List<SysUserRoleDept> selectSysUserRoleDeptList(SysUserRoleDept sysUserRoleDept) {
        return sysUserRoleDeptMapper.selectSysUserRoleDeptList(sysUserRoleDept);
    }

    /**
     * 新增用户和角色和部门关联
     * 
     * @param sysUserRoleDept 用户和角色和部门关联
     * @return 结果
     */
    @Override
    public int insertSysUserRoleDept(SysUserRoleDept sysUserRoleDept) {
        return sysUserRoleDeptMapper.insertSysUserRoleDept(sysUserRoleDept);
    }

    /**
     * 修改用户和角色和部门关联
     * 
     * @param sysUserRoleDept 用户和角色和部门关联
     * @return 结果
     */
    @Override
    public int updateSysUserRoleDept(SysUserRoleDept sysUserRoleDept) {
        return sysUserRoleDeptMapper.updateSysUserRoleDept(sysUserRoleDept);
    }

    /**
     * 删除用户和角色和部门关联对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysUserRoleDeptByIds(String ids) {
        return sysUserRoleDeptMapper.deleteSysUserRoleDeptByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用户和角色和部门关联信息
     * 
     * @param id 用户和角色和部门关联ID
     * @return 结果
     */
    @Override
    public int deleteSysUserRoleDeptById(Long id) {
        return sysUserRoleDeptMapper.deleteSysUserRoleDeptById(id);
    }
}
