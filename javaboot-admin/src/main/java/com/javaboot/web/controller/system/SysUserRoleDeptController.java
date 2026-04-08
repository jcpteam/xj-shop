package com.javaboot.web.controller.system;

import java.util.List;

import com.javaboot.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.system.domain.SysUserRoleDept;
import com.javaboot.system.service.ISysUserRoleDeptService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 用户和角色和部门关联Controller
 * 
 * @author javaboot
 * @date 2021-05-30
 */
@Controller
@RequestMapping("/system/userRoleDept")
public class SysUserRoleDeptController extends BaseController {
    private String prefix = "system/userRoleDept";

    @Autowired
    private ISysUserRoleDeptService sysUserRoleDeptService;

    @Autowired
    private ISysUserService sysUserService;

    @RequiresPermissions("system:userRoleDept:view")
    @GetMapping()
    public String userRoleDept() {
        return prefix + "/userRoleDept";
    }

    /**
     * 查询用户角色部门详细
     */
    @RequiresPermissions("system:userRoleDept:view")
    @GetMapping("/detail/{userId}")
    public String detail(@PathVariable("userId") String userId, ModelMap mmap) {
        //查询用户信息、及用户角色部门信息列表返回
        mmap.put("user" , sysUserService.selectUserById(userId));
        SysUserRoleDept where = new SysUserRoleDept();
        where.setUserId(userId);
        mmap.put("userRoleDeptList" , sysUserRoleDeptService.selectSysUserRoleDeptList(where));
        return prefix + "/userRoleDept";
    }

    /**
     * 查询用户和角色和部门关联列表
     */
    @RequiresPermissions("system:userRoleDept:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUserRoleDept sysUserRoleDept) {
        startPage();
        List<SysUserRoleDept> list = sysUserRoleDeptService.selectSysUserRoleDeptList(sysUserRoleDept);
        return getDataTable(list);
    }

    /**
     * 查询用户和角色和部门关联列表
     */
    @RequiresPermissions("system:userRoleDept:list")
    @PostMapping("/list/{userId}")
    @ResponseBody
    public TableDataInfo listWithUserId(@PathVariable("userId") String userId) {
        startPage();
        SysUserRoleDept where = new SysUserRoleDept();
        where.setUserId(userId);
        List<SysUserRoleDept> list = sysUserRoleDeptService.selectSysUserRoleDeptList(where);
        return getDataTable(list);
    }

    /**
     * 导出用户和角色和部门关联列表
     */
    @RequiresPermissions("system:userRoleDept:export")
    @Log(title = "用户和角色和部门关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUserRoleDept sysUserRoleDept) {
        List<SysUserRoleDept> list = sysUserRoleDeptService.selectSysUserRoleDeptList(sysUserRoleDept);
        ExcelUtil<SysUserRoleDept> util = new ExcelUtil<SysUserRoleDept>(SysUserRoleDept.class);
        return util.exportExcel(list, "userRoleDept");
    }

    /**
     * 新增用户和角色和部门关联
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 查询用户角色部门详细
     */
    @GetMapping("/add/{userId}")
    public String addWithUserId(@PathVariable("userId") String userId, ModelMap mmap) {
        //查询用户信息、及用户角色部门信息列表返回
        mmap.put("user" , sysUserService.selectUserById(userId));
        return prefix + "/add";
    }

    /**
     * 新增保存用户和角色和部门关联
     */
    @RequiresPermissions("system:userRoleDept:add")
    @Log(title = "用户和角色和部门关联", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysUserRoleDept sysUserRoleDept) {
        return toAjax(sysUserRoleDeptService.insertSysUserRoleDept(sysUserRoleDept));
    }

    /**
     * 修改用户和角色和部门关联
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        SysUserRoleDept sysUserRoleDept = sysUserRoleDeptService.selectSysUserRoleDeptById(id);
        mmap.put("sysUserRoleDept", sysUserRoleDept);
        return prefix + "/edit";
    }

    /**
     * 修改保存用户和角色和部门关联
     */
    @RequiresPermissions("system:userRoleDept:edit")
    @Log(title = "用户和角色和部门关联", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysUserRoleDept sysUserRoleDept) {
        return toAjax(sysUserRoleDeptService.updateSysUserRoleDept(sysUserRoleDept));
    }

    /**
     * 删除用户和角色和部门关联
     */
    @RequiresPermissions("system:userRoleDept:remove")
    @Log(title = "用户和角色和部门关联", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysUserRoleDeptService.deleteSysUserRoleDeptByIds(ids));
    }
}
