package com.javaboot.framework.shiro.service;

import com.javaboot.system.domain.SysUserRoleDept;
import com.javaboot.system.service.ISysUserRoleDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.constant.ShiroConstants;
import com.javaboot.common.constant.UserConstants;
import com.javaboot.common.enums.UserStatus;
import com.javaboot.common.exception.user.CaptchaException;
import com.javaboot.common.exception.user.UserBlockedException;
import com.javaboot.common.exception.user.UserDeleteException;
import com.javaboot.common.exception.user.UserNotExistsException;
import com.javaboot.common.exception.user.UserPasswordNotMatchException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.MessageUtils;
import com.javaboot.common.utils.ServletUtils;
import com.javaboot.framework.manager.AsyncManager;
import com.javaboot.framework.manager.factory.AsyncFactory;
import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysUserService;

import java.util.List;

/**
 * 登录校验方法
 *
 * @author javaboot
 */
@Component
public class SysLoginService {
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysUserRoleDeptService userRoleDeptService;

    /**
     * 登录
     */
    public SysUser login(String username, String password) {
        // 验证码校验
        if (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // 查询用户信息
        SysUser user = userService.selectUserByLoginName(username);

//        if (user == null && maybeMobilePhoneNumber(username)) {
//            user = userService.selectUserByPhoneNumber(username);
//        }

        if (user == null && maybeEmail(username)) {
            user = userService.selectUserByEmail(username);
        }

        if (user == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
            throw new UserNotExistsException();
        }

        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete")));
            throw new UserDeleteException();
        }

        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked" , user.getRemark())));
            throw new UserBlockedException();
        }

        passwordService.validate(user, password);

        //检测是否需要更新用户授权角色id
        user = checkUpdateUserRoleAuthId(user,username);

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        recordLoginInfo(user);
        return user;
    }

    private boolean maybeEmail(String username) {
        if (!username.matches(UserConstants.EMAIL_PATTERN)) {
            return false;
        }
        return true;
    }

    private boolean maybeMobilePhoneNumber(String username) {
        if (!username.matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN)) {
            return false;
        }
        return true;
    }

    /**
     * 检测是否需要更新用户角色授权id
     * @param user
     * @param loginName
     * @return
     */
    private SysUser checkUpdateUserRoleAuthId(SysUser user, String loginName) {
        //判断是否需要重新查询用户授权角色更新用户信息
        boolean needUpdate = false;
        if (user.getRoleAuthId() == null || user.getRoleAuthId() == -1) {
            //如果当前用户信息中没有角色授权id，则需要查询并更新
            needUpdate = true;
        } else {
            //如果已经存在角色授权id，判断角色id是否已删除，已删除则需要重新查询并更新
            SysUserRoleDept existInfo = userRoleDeptService.selectSysUserRoleDeptById(user.getRoleAuthId());
            if (existInfo == null) {
                needUpdate = true;
            }
        }

        SysUser ret = user;
        if (needUpdate) {
            SysUserRoleDept where = new SysUserRoleDept();
            where.setUserId(user.getUserId());
            List<SysUserRoleDept> userRoleDeptList = userRoleDeptService.selectSysUserRoleDeptList(where);
            if (!CollectionUtils.isEmpty(userRoleDeptList)) {
                //默认取第一个，查询已按照是否默认排序
                user.setRoleAuthId(userRoleDeptList.get(0).getId());
                userService.updateUserInfo(user);
                //重新查询一把用户信息
                ret = userService.selectUserByLoginName(loginName);
            }
        }
        return ret;
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(SysUser user) {
        user.setLoginIp(ShiroUtils.getIp());
        user.setLoginDate(DateUtils.getNowDate());
        userService.updateUserInfo(user);
    }

}
