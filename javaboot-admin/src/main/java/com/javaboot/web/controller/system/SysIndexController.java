package com.javaboot.web.controller.system;

import com.javaboot.common.config.Global;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.system.domain.SysMenu;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysConfigService;
import com.javaboot.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 首页 业务处理
 *
 * @author javaboot
 */
@Controller
public class SysIndexController extends BaseController {
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysConfigService configService;

    @RequestMapping("/admin")
    public String admin() {
        return "forward:/index" ;
    }

    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap) {
        // 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus" , menus);
        mmap.put("user" , user);
        mmap.put("copyrightYear" , Global.getCopyrightYear());
        mmap.put("demoEnabled" , Global.isDemoEnabled());

        return "index";
    }

    // 系统首页顶部菜单
    @PostMapping("/index/getMenu")
    @ResponseBody
    public AjaxResult getMenu(HttpServletRequest request) {
        // 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        return AjaxResult.success(menus);
    }


    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap) {
        mmap.put("version" , Global.getVersion());
        return "main" ;
    }

    /**
     * 锁屏
     */
    @GetMapping("/lockScreen")
    public String lockScreen() {
        return "tpl-lock-screen" ;
    }

    /**
     * 消息
     */
    @GetMapping("/message")
    public String message() {
        return "tpl-message" ;
    }

    /**
     * 便签
     */
    @GetMapping("/note")
    public String note() {
        return "tpl-note" ;
    }

    /**
     * 主题
     */
    @GetMapping("/theme")
    public String theme() {
        return "tpl-theme" ;
    }

    /**
     * 主题
     */
    @GetMapping("/themeGen")
    public String themeGen() {
        return "theme" ;
    }

}
