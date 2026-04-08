package com.javaboot.web.controller.system;

import java.util.List;
import java.util.UUID;

import com.javaboot.common.config.Global;
import com.javaboot.common.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.constant.UserConstants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.framework.shiro.service.SysPasswordService;
import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysPostService;
import com.javaboot.system.service.ISysRoleService;
import com.javaboot.system.service.ISysUserService;

/**
 * 用户信息
 *
 * @author javaboot
 */
@Controller
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    private String prefix = "system/user" ;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private SysPasswordService passwordService;

    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user() {
        return prefix + "/user" ;
    }

    @RequiresPermissions("system:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理" , businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理" , businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:user:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = ShiroUtils.getSysUser().getLoginName();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @RequiresPermissions("system:user:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 新增用户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("roles" , roleService.selectRoleAll());
        mmap.put("posts" , postService.selectPostAll());
        return prefix + "/add" ;
    }

    /**
     * 新增保存用户
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理" , businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysUser user) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }

        if(StringUtils.isEmpty(user.getUserId())){
            user.setUserId("" + System.currentTimeMillis() + (int)(Math.round(Math.random() + 1) * 1000));
        }else{
            SysUser existUser = userService.selectUserById(user.getUserId());
            if(existUser != null){
                return error("新增用户编号'" + user.getUserId() + "'失败，编号已存在");
            }
        }
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName()))) {
            return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
        }
//        if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
//            return error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
//        }
        if(user.getMemberId() == null) {
            if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
                return error("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
            }
        } else {
            user.setPassword(UserConstants.DEFAULT_PASSWORD);
        }

        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") String userId, ModelMap mmap) {
        mmap.put("user" , userService.selectUserById(userId));
        mmap.put("roles" , roleService.selectRolesByUserId(userId));
        mmap.put("posts" , postService.selectPostsByUserId(userId));
        return prefix + "/edit" ;
    }

    /**
     * 修改保存用户
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理" , businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysUser user) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        userService.checkUserAllowed(user);
        if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return error("修改用户'" + user.getLoginName() + "'失败，手机号码已存在");
        } else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return error("修改用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(userService.updateUser(user));
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码" , businessType = BusinessType.UPDATE)
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") String userId, ModelMap mmap) {
        mmap.put("user" , userService.selectUserById(userId));
        return prefix + "/resetPwd" ;
    }


    @GetMapping("/resetPwdAllByLittleT")
    public String resetPwdAll() {
//        List<SysUser> users = userService.selectSubUserList();
//        for(SysUser user0: users){
//            user0.setSalt(ShiroUtils.randomSalt());
//            user0.setPassword(passwordService.encryptPassword(user0.getLoginName(), "123456", user0.getSalt()));
//            userService.resetUserPwd(user0);
//        }

        return "" ;
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码" , businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwdSave(SysUser user) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        userService.checkUserAllowed(user);
        //单独处理用户密码
//        SysUser userr = new SysUser();
//        userr.setStatus("0");
//        List<SysUser> users = userService.selectUserList(userr);
//        for(SysUser user0: users){
//            user0.setSalt(ShiroUtils.randomSalt());
//            user0.setPassword(passwordService.encryptPassword(user0.getLoginName(), "123456", user0.getSalt()));
//            userService.resetUserPwd(user0);
//        }
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        if (userService.resetUserPwd(user) > 0) {
            if (ShiroUtils.getUserId() == user.getUserId() || ShiroUtils.getUserId().equals(user.getUserId())) {
                ShiroUtils.setSysUser(userService.selectUserById(user.getUserId()));
            }
            return success();
        }
        return error();
    }

    @Log(title = "重置密码" , businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwdForApp")
    @ResponseBody
    public AjaxResult resetPwdForApp(SysUser user) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        userService.checkUserAllowed(user);
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        if (userService.resetUserPwd(user) > 0) {
            return success();
        }
        return error();
    }

    @Log(title = "更新用户角色" , businessType = BusinessType.UPDATE)
    @PostMapping("/changeUserRoleAuth")
    @ResponseBody
    public AjaxResult changeUserRoleAuth(SysUser user) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        userService.checkUserAllowed(user);
        if (userService.updateUser(user) > 0) {
            if (ShiroUtils.getUserId() == user.getUserId() || ShiroUtils.getUserId().equals(user.getUserId())) {
                ShiroUtils.setSysUser(userService.selectUserById(user.getUserId()));
            }
            return success();
        }
        return error();
    }

    @RequiresPermissions("system:user:remove")
    @Log(title = "用户管理" , businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        try {
            return toAjax(userService.deleteUserByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 校验用户名
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(SysUser user) {
        return userService.checkLoginNameUnique(user.getLoginName());
    }

    /**
     * 校验手机号码
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(SysUser user) {
        return userService.checkPhoneUnique(user);
    }

    /**
     * 校验email邮箱
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(SysUser user) {
        return userService.checkEmailUnique(user);
    }

    /**
     * 用户状态修改
     */
    @Log(title = "用户管理" , businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:user:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysUser user) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        userService.checkUserAllowed(user);
        return toAjax(userService.changeStatus(user));
    }

    /**
     * 校验email邮箱
     */
    @PostMapping("/getOpmanagerList")
    @ResponseBody
    public List<SysUser> getOpmanagerList(SysUser user) {
        return userService.selectOpmanagerList(user.getDeptId());
    }

}