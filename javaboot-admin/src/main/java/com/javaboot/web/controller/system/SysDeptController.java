package com.javaboot.web.controller.system;

import java.util.List;
import java.util.stream.Collectors;

import com.javaboot.common.config.Global;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.vo.SysDeptSelectVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.constant.UserConstants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.domain.Ztree;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.system.domain.SysDept;
import com.javaboot.system.domain.SysRole;
import com.javaboot.system.service.ISysDeptService;

/**
 * 部门信息
 *
 * @author javaboot
 */
@Controller
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
    private String prefix = "system/dept";

    @Autowired
    private ISysDeptService deptService;

    @RequiresPermissions("system:dept:view")
    @GetMapping()
    public String dept() {
        return prefix + "/dept";
    }

    @RequiresPermissions("system:dept:list")
    @PostMapping("/list")
    @ResponseBody
    public List<SysDept> list(SysDept dept) {
        List<SysDept> deptList = deptService.selectDeptList(dept);
        return deptList;
    }

    @RequiresPermissions("system:dept:list")
    @GetMapping("/listTree")
    @ResponseBody
    public AjaxResult listTree(SysDept dept) {
        List<SysDept> menuList = deptService.selectDeptList(dept);
        AjaxResult result = new AjaxResult(AjaxResult.Type.SUCCESS, "success", menuList);
        result.put("count", menuList.size());
        return result;
    }

    /**
     * 新增部门
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") String parentId, ModelMap mmap) {
        SysDept dept = null;
        if (!"0".equals(parentId)) {
            dept = deptService.selectDeptById(parentId);
        } else {
            dept = new SysDept();
            dept.setDeptId("0");
            dept.setDeptName("主目录");
        }
        mmap.put("dept", dept);
        return prefix + "/add";
    }

    /**
     * 新增保存部门
     */
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dept:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysDept dept) {
        if (Global.isDemoEnabled()) {
            return AjaxResult.error("演示模式，不允许操作!");
        }
        if (UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        if (UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptIdUnique(dept))) {
            return error("新增部门'" + dept.getDeptId() + "'失败，部门编号已存在");
        }
        dept.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{deptId}")
    public String edit(@PathVariable("deptId") String deptId, ModelMap mmap) {
        SysDept dept = deptService.selectDeptById(deptId);
        if (StringUtils.isNotNull(dept) && "100".equals(deptId) ) {
            dept.setParentName("无");
        }
        mmap.put("dept", dept);
        return prefix + "/edit";
    }

    /**
     * 保存
     */
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dept:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysDept dept) {
        if (Global.isDemoEnabled()) {
            return AjaxResult.error("演示模式，不允许操作!");
        }
        if (UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId())) {
            return error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        dept.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除
     */
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dept:remove")
    @GetMapping("/remove/{deptId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("deptId") String deptId) {
        if (Global.isDemoEnabled()) {
            return AjaxResult.error("演示模式，不允许操作!");
        }
        if (deptService.selectDeptCount(deptId) > 0) {
            return AjaxResult.warn("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.warn("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 校验部门名称
     */
    @PostMapping("/checkDeptNameUnique")
    @ResponseBody
    public String checkDeptNameUnique(SysDept dept) {
        return deptService.checkDeptNameUnique(dept);
    }

    /**
     * 选择部门树
     */
    @GetMapping("/selectDeptTree/{deptId}")
    public String selectDeptTree(@PathVariable("deptId") String deptId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        return prefix + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> ztrees = deptService.selectDeptTree(new SysDept());
        return ztrees;
    }

    /**
     * 加载角色部门（数据权限）列表树
     */
    @GetMapping("/roleDeptTreeData")
    @ResponseBody
    public List<Ztree> deptTreeData(SysRole role) {
        List<Ztree> ztrees = deptService.roleDeptTreeData(role);
        return ztrees;
    }

    /**
     * 查询登陆用户部门
     *
     * @return
     */
    @GetMapping("/getLoginUserDept")
    @ResponseBody
    public AjaxResult getLoginUserDept() {
        try {
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            List<SysDeptSelectVO> deptList = deptService.selectDeptListById(user.isAdmin()?null:user.getDeptId());
            List<SysDeptSelectVO> ret = deptList;
            if(user.isAdmin() && !CollectionUtils.isEmpty(deptList)){
                ret = deptList.stream()
                        .filter((SysDeptSelectVO dept) -> !(dept.getValue().equals("0401") || dept.getValue().equals("0402") || dept.getValue().equals("0403")) )
                        .collect(Collectors.toList());
            }
            return   AjaxResult.success(ret);
        } catch (Exception e) {
            return AjaxResult.warn("未查询到登陆用户信息");
        }

    }
}
