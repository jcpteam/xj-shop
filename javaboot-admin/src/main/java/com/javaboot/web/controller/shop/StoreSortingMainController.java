package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreSortingDailyWork;
import com.javaboot.shop.service.IStoreSortingDailyWorkService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分拣主页Controller
 * 
 * @author javaboot
 * @date 2021-06-11
 */
@Controller
@RequestMapping("/shop/sortingMain")
public class StoreSortingMainController extends BaseController {
    private String prefix = "shop/sortingMain";

    @Autowired
    private IStoreSortingDailyWorkService storeSortingDailyWorkService;

    @GetMapping()
    public String sortingMain() {
        return prefix + "/sortingMain";
    }
}
