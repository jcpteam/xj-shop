package com.javaboot.web.controller.tool;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.javaboot.common.core.controller.BaseController;

/**
 * build 表单构建
 *
 * @author javaboot
 */
@Controller
@RequestMapping("/tool")
public class BuildController extends BaseController {
    private String prefix = "tool" ;

    @RequiresPermissions("tool:build:view")
    @GetMapping("build")
    public String build() {
        return prefix + "/build" ;
    }

    @RequiresPermissions("tool:build:view")
    @GetMapping("preview")
    public String preview() {
        return prefix + "/preview" ;
    }

}
