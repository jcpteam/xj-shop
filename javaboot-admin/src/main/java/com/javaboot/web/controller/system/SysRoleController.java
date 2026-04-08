package com.javaboot.web.controller.system;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.config.Global;
import com.javaboot.common.constant.UserConstants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.system.domain.SysRole;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.domain.SysUserRole;
import com.javaboot.system.service.ISysRoleService;
import com.javaboot.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 角色信息
 *
 * @author javaboot
 */
@Controller
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    private String prefix = "system/role" ;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("system:role:view")
    @GetMapping()
    public String role() {
        return prefix + "/role" ;
    }

    @RequiresPermissions("system:role:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @Log(title = "导出角色数据" , businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:role:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.exportExcel(list, "角色数据");
    }

    @Log(title = "下载角色数据导入模板" , businessType = BusinessType.OTHER)
    @RequestMapping("/template")
    @ResponseBody
    public AjaxResult importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.importTemplateExcel("角色数据导入模板");
    }


    @Log(title = "导入角色数据" , businessType = BusinessType.OTHER)
    @RequestMapping("/import")
    @ResponseBody
    public AjaxResult importExcel(MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }

        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        List<SysRole> roles = util.importExcel(file.getInputStream());
        if(roles != null && roles.size() > 0) {
            for (SysRole r : roles) {
                r.setStatus("0");
                r.setDelFlag("0");
                roleService.insertRole(r);
            }
        }

        return AjaxResult.success("导入成功!");
    }

    /**
     * 新增角色
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add" ;
    }

    /**
     * 新增保存角色
     */
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理" , businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysRole role) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(ShiroUtils.getLoginName());
        ShiroUtils.clearCachedAuthorizationInfo();
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改角色
     */
    @GetMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        mmap.put("role" , roleService.selectRoleById(roleId));
        return prefix + "/edit" ;
    }

    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理" , businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysRole role) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        roleService.checkRoleAllowed(role);
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(ShiroUtils.getLoginName());
        ShiroUtils.clearCachedAuthorizationInfo();
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 角色分配数据权限
     */
    @GetMapping("/authDataScope/{roleId}")
    public String authDataScope(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        mmap.put("role" , roleService.selectRoleById(roleId));
        return prefix + "/dataScope" ;
    }

    /**
     * 保存角色分配数据权限
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理" , businessType = BusinessType.UPDATE)
    @PostMapping("/authDataScope")
    @ResponseBody
    public AjaxResult authDataScopeSave(SysRole role) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(ShiroUtils.getLoginName());
        if (roleService.authDataScope(role) > 0) {
            ShiroUtils.setSysUser(userService.selectUserById(ShiroUtils.getSysUser().getUserId()));
            return success();
        }
        return error();
    }

    @RequiresPermissions("system:role:remove")
    @Log(title = "角色管理" , businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        try {
            return toAjax(roleService.deleteRoleByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkRoleNameUnique")
    @ResponseBody
    public String checkRoleNameUnique(SysRole role) {
        return roleService.checkRoleNameUnique(role);
    }

    /**
     * 校验角色权限
     */
    @PostMapping("/checkRoleKeyUnique")
    @ResponseBody
    public String checkRoleKeyUnique(SysRole role) {
        return roleService.checkRoleKeyUnique(role);
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree() {
        return prefix + "/tree" ;
    }

    /**
     * 角色状态修改
     */
    @Log(title = "角色管理" , businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:role:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysRole role) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.changeStatus(role));
    }

    /**
     * 分配用户
     */
    @RequiresPermissions("system:role:edit")
    @GetMapping("/authUser/{roleId}")
    public String authUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        mmap.put("role" , roleService.selectRoleById(roleId));
        return prefix + "/authUser" ;
    }

    /**
     * 查询已分配用户角色列表
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/authUser/allocatedList")
    @ResponseBody
    public TableDataInfo allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权
     */
    @Log(title = "角色管理" , businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancel")
    @ResponseBody
    public AjaxResult cancelAuthUser(SysUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权
     */
    @Log(title = "角色管理" , businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancelAll")
    @ResponseBody
    public AjaxResult cancelAuthUserAll(Long roleId, String userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 选择用户
     */
    @GetMapping("/authUser/selectUser/{roleId}")
    public String selectUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        mmap.put("role" , roleService.selectRoleById(roleId));
        return prefix + "/selectUser" ;
    }

    /**
     * 查询未分配用户角色列表
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/authUser/unallocatedList")
    @ResponseBody
    public TableDataInfo unallocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 批量选择用户授权
     */
    @Log(title = "角色管理" , businessType = BusinessType.GRANT)
    @PostMapping("/authUser/selectAll")
    @ResponseBody
    public AjaxResult selectAuthUserAll(Long roleId, String userIds) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }



}