package com.javaboot.web.controller.system;

import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.ServletUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.common.utils.cookie.CookieUtils;
import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysConfigService;
import com.javaboot.system.service.ISysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录验证
 *
 * @author javaboot
 */
@Controller
public class SysLoginController extends BaseController {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService userService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        SysUser user = ShiroUtils.getSysUser();
        if (user != null) {
            return "redirect:/index" ;
        }
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request)) {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }

        //是否记住我
        String admin_rememberMe = CookieUtils.getCookie(request, Constants.ADMIN_COOKIE_REMEMBERME);
        model.addAttribute("rememberMe", "1".equals(admin_rememberMe) ? true : false);

        return "login" ;

    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            ServletUtils.setLoginCookie(username, password, rememberMe);
            logger.info("=======登录的sessionid==========" + ServletUtils.getSession().getId());
            return success(ServletUtils.getSession().getId());
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误" ;
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @PostMapping("/appLogin")
    @ResponseBody
    public AjaxResult appLogin(String username, String password, Boolean rememberMe) {
        //如果app登录成功，则查询用户用户信息返回
        SysUser userInfo = userService.selectUserByLoginNameForApp(username);
        if(userInfo == null || userInfo.getMemberId() == null){
            return AjaxResult.error("账号不存在");
        }

        AjaxResult ret = ajaxLogin(username,password,rememberMe);
        if(ret.isSuccess()){
            //如果app登录成功，则查询用户用户信息返回
            userInfo = userService.selectUserByLoginNameForApp(username);
            if(userInfo != null){
                ret.put(AjaxResult.DATA_TAG, userInfo);
            }
        }
        return ret;
    }

    @GetMapping("/unauth")
    public String unauth() {
        return "error/unauth" ;
    }

    @GetMapping("/reg")
    public String reg() {
        return "reg" ;
    }

    @GetMapping("/forget")
    public String forget() {
        return "forget" ;
    }
}
