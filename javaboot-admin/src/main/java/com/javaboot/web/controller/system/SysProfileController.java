package com.javaboot.web.controller.system;

import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.system.domain.SysMenu;
import com.javaboot.system.domain.SysUserRoleDept;
import com.javaboot.system.service.ISysMenuService;
import com.javaboot.system.service.ISysUserRoleDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.config.Global;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.common.utils.file.FileUploadUtils;
import com.javaboot.framework.shiro.service.SysPasswordService;
import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysUserService;

import java.util.List;

/**
 * 个人信息
 *
 * @author javaboot
 */
@Controller
@RequestMapping("/system/user/profile")
@Api("个人信息")
public class SysProfileController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(SysProfileController.class);

    private String prefix = "system/user/profile" ;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysUserRoleDeptService sysUserRoleDeptService;

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 个人信息
     */
    @GetMapping()
    public String profile(ModelMap mmap) {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user" , user);
        mmap.put("roleGroup" , userService.selectUserRoleGroup(user.getUserId()));
        mmap.put("postGroup" , userService.selectUserPostGroup(user.getUserId()));
        return prefix + "/profile" ;
    }


    /**
     * 个人信息-供钉钉小程序使用
     */
    @PostMapping("/{userId}")
    @ResponseBody
    @ApiOperation("个人信息")
    public SysUser getUserInfo(@PathVariable("userId") String userId) {
        userService.selectUserRoleGroup(userId);
        return ShiroUtils.getSysUser();
    }

    /**
     * 个人权限菜单-供钉钉小程序使用
     */
    @PostMapping("/menu/{userId}")
    @ResponseBody
    @ApiOperation("个人权限菜单")
    public List<SysMenu> getRoleMenu(@PathVariable("userId") String userId) {
        return sysMenuService.selectMenuList(new SysMenu(), userId);
    }

    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password) {
        SysUser user = ShiroUtils.getSysUser();
        if (passwordService.matches(user, password)) {
            return true;
        }
        return false;
    }

    @GetMapping("/resetPwd")
    public String resetPwd(ModelMap mmap) {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user" , userService.selectUserById(user.getUserId()));
        return prefix + "/resetPwd" ;
    }

    @Log(title = "重置密码" , businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwd(String oldPassword, String newPassword) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        SysUser user = ShiroUtils.getSysUser();
        if (StringUtils.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword)) {
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
            if (userService.resetUserPwd(user) > 0) {
                ShiroUtils.setSysUser(userService.selectUserById(user.getUserId()));
                return success();
            }
            return error();
        } else {
            return error("修改密码失败，旧密码错误");
        }
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit")
    public String edit(ModelMap mmap) {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user" , userService.selectUserById(user.getUserId()));
        return prefix + "/edit" ;
    }

    /**
     * 修改头像
     */
    @GetMapping("/avatar")
    public String avatar(ModelMap mmap) {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user" , userService.selectUserById(user.getUserId()));
        return prefix + "/avatar" ;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息" , businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxResult update(SysUser user) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        SysUser currentUser = ShiroUtils.getSysUser();
        currentUser.setUserName(user.getUserName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (userService.updateUserInfo(currentUser) > 0) {
            ShiroUtils.setSysUser(userService.selectUserById(currentUser.getUserId()));
            return success();
        }
        return error();
    }

    /**
     * 保存头像
     */
    @Log(title = "个人信息" , businessType = BusinessType.UPDATE)
    @PostMapping("/updateAvatar")
    @ResponseBody
    public AjaxResult updateAvatar(@RequestParam("avatarfile") MultipartFile file) {
        SysUser currentUser = ShiroUtils.getSysUser();
        try {
            if (!file.isEmpty()) {
                String avatar = FileUploadUtils.upload(Global.getAvatarPath(), file);
                currentUser.setAvatar(avatar);
                if (userService.updateUserInfo(currentUser) > 0) {
                    ShiroUtils.setSysUser(userService.selectUserById(currentUser.getUserId()));
                    return success();
                }
            }
            return error();
        } catch (Exception e) {
            log.error("修改头像失败！" , e);
            return error(e.getMessage());
        }
    }

    @GetMapping("/changeRoleAuth")
    public String changeRoleAuth(ModelMap mmap) {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user" , userService.selectUserById(user.getUserId()));
        //查询当前用户授权的角色列表
        SysUserRoleDept where = new SysUserRoleDept();
        where.setUserId(user.getUserId());
        mmap.put("userRoleDeptList",sysUserRoleDeptService.selectSysUserRoleDeptList(where));
        return prefix + "/changeRoleAuth" ;
    }

    @GetMapping("/changeRoleAuthForApp")
    @ResponseBody
    public TableDataInfo changeRoleAuthForApp() {
        SysUser user = ShiroUtils.getSysUser();
        //查询当前用户授权的角色列表
        SysUserRoleDept where = new SysUserRoleDept();
        where.setUserId(user.getUserId());
        return getDataTable(sysUserRoleDeptService.selectSysUserRoleDeptList(where)) ;
    }
}
