package com.javaboot.system.mapper;

import com.javaboot.system.domain.SysUserRoleDept;
import java.util.List;

/**
 * 用户和角色和部门关联Mapper接口
 * 
 * @author javaboot
 * @date 2021-05-30
 */
public interface SysUserRoleDeptMapper {
    /**
     * 查询用户和角色和部门关联
     * 
     * @param id 用户和角色和部门关联ID
     * @return 用户和角色和部门关联
     */
    public SysUserRoleDept selectSysUserRoleDeptById(Long id);

    /**
     * 查询用户和角色和部门关联列表
     * 
     * @param sysUserRoleDept 用户和角色和部门关联
     * @return 用户和角色和部门关联集合
     */
    public List<SysUserRoleDept> selectSysUserRoleDeptList(SysUserRoleDept sysUserRoleDept);

    /**
     * 新增用户和角色和部门关联
     * 
     * @param sysUserRoleDept 用户和角色和部门关联
     * @return 结果
     */
    public int insertSysUserRoleDept(SysUserRoleDept sysUserRoleDept);

    /**
     * 修改用户和角色和部门关联
     * 
     * @param sysUserRoleDept 用户和角色和部门关联
     * @return 结果
     */
    public int updateSysUserRoleDept(SysUserRoleDept sysUserRoleDept);

    /**
     * 删除用户和角色和部门关联
     * 
     * @param id 用户和角色和部门关联ID
     * @return 结果
     */
    public int deleteSysUserRoleDeptById(Long id);

    /**
     * 批量删除用户和角色和部门关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysUserRoleDeptByIds(String[] ids);
}
